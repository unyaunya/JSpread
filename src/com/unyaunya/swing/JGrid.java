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
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.Columns;
import com.unyaunya.grid.DefaultCellRenderer;
import com.unyaunya.grid.Handler;
import com.unyaunya.grid.ICellRange;
import com.unyaunya.grid.IGridCellRenderer;
import com.unyaunya.grid.IGridModel;
import com.unyaunya.grid.IGridSelectionModel;
import com.unyaunya.grid.Rows;
import com.unyaunya.grid.ScrollModel;
import com.unyaunya.grid.SingleCellSelectionModel;
import com.unyaunya.spread.Actions;
import com.unyaunya.spread.SpreadBorder;
import com.unyaunya.swing.plaf.GridUI;

class GridModelAdapter implements IGridModel {
	TableModel tableModel;

	GridModelAdapter(TableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	@Override
	public Color getBackgroundColor(int row, int col) {
		return Color.WHITE; 
	}

	@Override
	public Border getBorder(int row, int col) {
		return null;
	}

	@Override
	public Color getForegroundColor(int row, int col) {
		return Color.BLACK; 
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
	public ICellRange getCellRange(int row, int col) {
		return null;
	}
}

@SuppressWarnings("serial")
public class JGrid extends JComponent {
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
		this.scrollModel = new ScrollModel(this);
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
		setGridSelectionModel(new SingleCellSelectionModel(this));
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

	public void setGridSelectionModel(IGridSelectionModel selectionModel) {
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

	public ICellRange getCellRange(int row, int column) {
   		return null;
    }

	public Rectangle getGridRect(int rowIndex, int colIndex) {
		return getScrollModel().getGridRect(rowIndex, colIndex);
	}

	public Rectangle getCellRect(int rowIndex, int colIndex) {
		ICellRange r = getCellRange(rowIndex, colIndex);
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
		Object s = m.getValueAt(row, col);
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
		renderer.setForeground(m.getForegroundColor(row, col));
		renderer.setBackground(this.getCellBackground(isSelected, hasFocus, row, col));
		renderer.setHorizontalAlignment(m.getHorizontalAlignment(row, col));
		Component c = renderer.getGridCellRendererComponent(this, s, isSelected, hasFocus, row, col);
		return c;
	}
	
	/**
	 *
	 * 

	abstract public Component prepareRenderer(IGridCellRenderer renderer, int row, int col) {
		SpreadSheetModel m = getSpreadSheetModel();
		Object s = m.getValueAt(row, col);
		boolean hasFocus = this.hasFocus(row, col);
		boolean isSelected = false;
		if(!hasFocus) {
			if(row == 0 && col == 0) {
				isSelected = this.getSelectionModel().isCellSelected(row, col);
			}
			else if(row == 0) {
				isSelected = this.getSelectionModel().isColumnSelected(col);
			}
			else if(col == 0) {
				isSelected = this.getSelectionModel().isRowSelected(row);
			}
			else {
				isSelected = this.getSelectionModel().isCellSelected(row, col);
			}
		}
		Border border = getCellBorder(hasFocus, row, col);
		renderer.setBorder(border);
		renderer.setForeground(this.getCellForeground(row, col));
		renderer.setBackground(this.getCellBackground(isSelected, hasFocus, row, col));
		renderer.setHorizontalAlignment(this.getHorizontalAlignment(row, col));
		Component c = renderer.getGridCellRendererComponent(this, s, isSelected, hasFocus, row, col);
		return c;
	}
	*/

	/*
	 * methods delegating to ScrollModel
	 */
	public CellPosition getCellPositionAtPoint(Point pt) {
		return new CellPosition(getScrollModel().rowAtPoint(pt), getScrollModel().columnAtPoint(pt));
	}

	public CellPosition getGridPositionAtPoint(Point pt) {
		return new CellPosition(getScrollModel().rowAtPoint(pt), getScrollModel().columnAtPoint(pt));
	}

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
	    		return this.getGridModel().getBackgroundColor(row, column);
	    	}
		}
    }

	/*
	 * methods related to UI appearance
	 */
	public Border getFocusBorder() {
    	return SpreadBorder.DEFAULT_FOCUS_BORDER;
    }

    public Border getNoFocusBorder() {
    	return SpreadBorder.defaultBorder;
    }

    public Border getCellBorder(boolean hasFocus, int row, int column) {
		if(hasFocus) {
			return this.getFocusBorder();
		}
		else {
			return this.getNoFocusBorder();
		}
    }

	public void _select(int newRowIndex, int newColumnIndex, boolean shft,
			boolean ctrl) {
		if(shft) {
			getGridSelectionModel().focus(newRowIndex, newColumnIndex);
		}
		else {
			getGridSelectionModel().select(newRowIndex, newColumnIndex, !ctrl);
			repaint();
		}
	}

	public void select(int rowIndex, int columnIndex, boolean shft, boolean ctrl) {
		int orig_row = getGridSelectionModel().getFocusedRow();
		int orig_col = getGridSelectionModel().getFocusedColumn();
		int newRowIndex = _rowIndex(rowIndex);
		int newColumnIndex = _columnIndex(columnIndex);
		if(orig_row != newRowIndex || orig_col != newColumnIndex) {
			stopEditing();
			scrollToVisible(newRowIndex, newColumnIndex);
			//getSpread().repaintCell(orig_row, orig_col);
			//getSpread().repaintCell(this.rowIndex, this.columnIndex);
		}
		_select(newRowIndex, newColumnIndex, shft, ctrl);
	}

	private int _rowIndex(int rowIndex) {
		if(rowIndex < 1) {
			rowIndex = 1;
		}
		if(getRows().getCount() <= rowIndex) {
			rowIndex = getRows().getCount() - 1; 
		}
		return rowIndex;
	}

	private int _columnIndex(int columnIndex) {
		if(columnIndex < 1) {
			columnIndex = 1;
		}
		if(getColumns().getCount() <= columnIndex) {
			columnIndex = getColumns().getCount() - 1; 
		}
		return columnIndex;
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
}
