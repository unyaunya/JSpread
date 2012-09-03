package com.unyaunya.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.unyaunya.grid.Actions;
import com.unyaunya.grid.Cell;
import com.unyaunya.grid.Columns;
import com.unyaunya.grid.DefaultCellRenderer;
import com.unyaunya.grid.Handler;
import com.unyaunya.grid.ICell;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.IGridCellRenderer;
import com.unyaunya.grid.IGridModel;
import com.unyaunya.grid.Rows;
import com.unyaunya.grid.ScrollModel;
import com.unyaunya.grid.format.CellFormatModel;
import com.unyaunya.grid.format.GridBorder;
import com.unyaunya.grid.selection.IGridSelectionModel;
import com.unyaunya.grid.selection.SingleCellSelectionModel;
import com.unyaunya.swing.plaf.GridUI;

class GridModelAdapter implements IGridModel {
	TableModel tableModel;
	CellFormatModel formatModel = new CellFormatModel();

	GridModelAdapter(TableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	public int getHorizontalAlignment(int row, int col) {
		return SwingConstants.LEFT;
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		tableModel.addTableModelListener(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return tableModel.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return tableModel.getColumnCount();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return tableModel.getColumnName(columnIndex);
	}

	@Override
	public int getRowCount() {
		return tableModel.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableModel.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return tableModel.isCellEditable(rowIndex, columnIndex);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		tableModel.removeTableModelListener(l);
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		tableModel.setValueAt(value, rowIndex, columnIndex);
	}

	@Override
	public ICell getCellAt(int row, int col) {
		return new Cell(row, col, getValueAt(row, col));
	}

	@Override
	public CellFormatModel getCellFormatModel() {
		return formatModel;
	}
}

/**
 * JGridは、各機能を担当する複数のサブ・コンポーネントから構成される(ようにしたい)。
 * Xxxxサブ・コンポーネントを作成するメソッドは、createXxxxメソッドという名称にしている。
 * サブ・コンポーネントの機能を拡張する場合は、JGridのサブクラスで、createXxxxメソッドが
 * サブ・コンポーネントの派生クラスのインスタンスを返すようにする。
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class JGrid extends JComponent implements TableModelListener {
    @SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(JGrid.class.getName());
	public static final Color DEFAULT_HEADER_BACKGROUND_COLOR = new Color(0xf0,0xf0,0xf0);
	public static final Color DEFAULT_SELECTION_BACKGROUND_COLOR = new Color(0xe0,0xe0,0xff);
	public static final Color DEFAULT_FOREGROUND_COLOR = new Color(0x00,0x00,0x00);
 
	protected Color selectionBackground = DEFAULT_SELECTION_BACKGROUND_COLOR;
	protected Color selectionForeground = DEFAULT_FOREGROUND_COLOR;

	protected static IGridCellRenderer defaultCellRenderer = new DefaultCellRenderer();
	
	private IGridModel gridModel;
	private IGridSelectionModel selectionModel;
	private Handler handler;
	
	transient private ScrollModel scrollModel;
	transient private Columns columns;
	transient private Rows rows;
	private Actions actions;

	//
	// Managing GridUI
	//
	/**
     * @see #getUIClassID
     * @see #readObject
     */
	private static final String uiClassID = "GridUI";

	/**
	 * Returns the L&F object that renders this component.
	 *
	 * @return the <code>SpreadUI</code> object that renders this component
	 */
	public GridUI getUI() {
	    return (GridUI)ui;
	}

	/**
	 * Sets the L&F object that renders this component and repaints.
	 *
	 * @param ui  the GridUI L&F object
	 * @see UIDefaults#getUI
	 * @beaninfo
	 *        bound: true
	 *       hidden: true
	 *    attribute: visualUpdate true
	 *  description: The UI object that implements the Component's LookAndFeel.
	 */
	public void setUI(GridUI ui) {
	    if (this.ui != ui) {
	        super.setUI(ui);
	        repaint();
	    }
	}

	 /**
     * Returns the suffix used to construct the name of the L&F class used to
     * render this component.
     *
     * @return the string "GridUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    public String getUIClassID() {
        return uiClassID;
    }

    /**
	 * default constructor
	 */
	public JGrid() {
		this(null);
	}

	public JGrid(TableModel model) {
		setUI(new GridUI());
		this.setFocusable(true);
		this.scrollModel = createScrollModel();
		this.columns = new Columns(getScrollModel());
		this.rows = new Rows(getScrollModel());
        this.actions = new Actions(this);
		init(model);
	}

	private void init(TableModel model) {
		if(model == null) {
			model = new DefaultTableModel();
		}
		setTableModel(model);
		setGridSelectionModel(createSelectionModel());

		/*
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusForwardTraversalKeys());
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusBackwardTraversalKeys());
		*/
		this.addMouseListener(getHandler());
		this.addMouseMotionListener(getHandler());
		this.addKeyListener(getHandler());
	}

	/**
	 * スクロールモデルを作成する。
	 * スクロールモデルの機能拡張を行う場合、派生クラスでこのメソッドをオーバライドし、
	 * スクロールモデルの派生クラスのインスタンスを返すようにする。
	 * @return
	 */
	protected ScrollModel createScrollModel() {
		return new ScrollModel(this);
	}

	/**
	 * セレクションモデルを作成する。
	 * セレクションモデルの機能拡張を行う場合、派生クラスでこのメソッドをオーバライドし、
	 * セレクションモデルの派生クラスのインスタンスを返すようにする。
	 * 
	 * @return
	 */
	protected IGridSelectionModel createSelectionModel() {
		return new SingleCellSelectionModel(this);
	}
	
	public Actions getActions() {
    	return this.actions;
    }
	
	public void setTableModel(TableModel model) {
		if(model instanceof IGridModel) {
			gridModel = (IGridModel)model;
		}
		else {
			gridModel = new GridModelAdapter(model);
		}
		getScrollModel().setTableModel(gridModel);
		this.repaint(this.getBounds());
	}

	private void setGridSelectionModel(IGridSelectionModel selectionModel) {
		assert(selectionModel != null);
		this.selectionModel = selectionModel;
		if(isVisible()) {
			repaint();
		}
	}

	public IGridSelectionModel getGridSelectionModel() {
		return this.selectionModel;
	}
		
	public IGridModel getGridModel() {
		return gridModel;
	}
	
	/**
	 * スクロールモデルを取得する
	 * @return
	 */
	public ScrollModel getScrollModel() {
		return this.scrollModel;
	}

	public Columns getColumns() {
		return columns;
	}

	public Rows getRows() {
		return rows;
	}

	public IRange getCellRange(int row, int column) {
   		return getGridModel().getCellAt(row, column).getRange();
    }

	public Rectangle getGridRect(int rowIndex, int colIndex) {
		return getScrollModel().getGridRect(rowIndex, colIndex);
	}

	public Rectangle getCellRect(int rowIndex, int colIndex) {
		IRange r = getCellRange(rowIndex, colIndex);
		if(r == null) {
			return getGridRect(rowIndex, colIndex);
		}
		if(r.getTop() == rowIndex && r.getLeft() == colIndex) {
			int top = r.getTop();
			int left = r.getLeft();
			int bottom = r.getBottom();
			int right = r.getRight();
			ScrollModel scrollModel = getScrollModel();
			Rectangle cellRect = new Rectangle();
			cellRect.y = scrollModel.getRowPosition(top);
			cellRect.height = scrollModel.getRowPosition(bottom+1) - cellRect.y;
			cellRect.x = scrollModel.getColumnPosition(left);
			cellRect.width = scrollModel.getColumnPosition(right+1) - cellRect.x;
			return cellRect;
		}
		else {
			return null;
		}
	}

	/*
	 * methods implementing to paint
	 */
	public IGridCellRenderer getCellRenderer(int row, int column) {
		return defaultCellRenderer;
    }

	/*
	 * methods implementing to paint
	 */

	public Component prepareRenderer(IGridCellRenderer renderer, int row, int col) {
		IGridModel m = getGridModel();
		ICell cell = m.getCellAt(row, col);
		boolean hasFocus = getGridSelectionModel().hasFocus(row, col);
		boolean isSelected = false;
		if(!hasFocus) {
			if(row == 0 && col == 0) {
				isSelected = this.getGridSelectionModel().isSelected(row, col);
			}
			else if(row == 0) {
				isSelected = this.getGridSelectionModel().isColumnSelected(col);
			}
			else if(col == 0) {
				isSelected = this.getGridSelectionModel().isRowSelected(row);
			}
			else {
				isSelected = this.getGridSelectionModel().isSelected(row, col);
			}
		}
		Border border = getCellBorder(hasFocus, row, col);
		renderer.setBorder(border);
		renderer.setForeground(cell.getForegroundColor());
		renderer.setBackground(this.getCellBackground(isSelected, hasFocus, row, col));
		renderer.setHorizontalAlignment(cell.getHorizontalAlignment());
		Component c = renderer.getGridCellRendererComponent(this, cell.getValue(), isSelected, hasFocus, row, col);
		return c;
	}

	/*
	 * methods delegating to ScrollModel
	 */
	public int rowAtPoint(Point pt) {
		return getScrollModel().rowAtPoint(pt);
	}
	
	public int columnAtPoint(Point pt) {
		return getScrollModel().columnAtPoint(pt);
	}
	
	public void scrollToVisible(int rowIndex, int columnIndex) {
		getScrollModel().scrollToVisible(rowIndex, columnIndex);
	}

	public boolean arePanesFreezed() {
		return getScrollModel().arePanesFreezed();
	}

	public void freezePanes() {
		getScrollModel().freezePanes(getGridSelectionModel().getFocusedRow(), getGridSelectionModel().getFocusedColumn());
		repaint();
	}

	public void unfreezePanes() {
		getScrollModel().unfreezePanes();
		repaint();
	}

	/**
	 * スクロールバーを設定する。
	 */
	public void setScrollBar(JScrollBar horizontalBar, JScrollBar verticalBar) {
		getScrollModel().setScrollBar(horizontalBar, verticalBar);
	}

    public Color getSelectionBackground() {
		return this.selectionBackground;
	}
	public void setSelectionBackground(Color color) {
		this.selectionBackground = color;
		repaint();
	}
	public Color getSelectionForeground() {
		return this.selectionForeground;
		
	}
	public void setSelectionForeground(Color color) {
		this.selectionForeground = color;
		repaint();
	}

	protected Color getCellBackground(boolean isSelected, boolean hasFocus, int row, int column) {
		if(isSelected) {
			return this.getSelectionBackground();
		}
		else {
	    	if(row <= 0 || column <= 0) {
	    		return DEFAULT_HEADER_BACKGROUND_COLOR;
	    	}
	    	else {
	    		return this.getGridModel().getCellAt(row, column).getBackgroundColor();
	    	}
		}
    }

	/*
	 * methods related to UI appearance
	 */
	public Border getFocusBorder() {
    	return GridBorder.DEFAULT_FOCUS_BORDER;
    }

    public Border getNoFocusBorder() {
    	return GridBorder.defaultBorder;
    }

    public Border getCellBorder(boolean hasFocus, int row, int column) {
		if(hasFocus) {
			return this.getFocusBorder();
		}
		else {
			return this.getNoFocusBorder();
		}
    }

	public void stopEditing() {}

	/*
	 * 
	 */
	public Handler getHandler() {
		if(handler == null) {
			handler = new Handler(this);
		}
		return handler;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		repaint();
	}
}
