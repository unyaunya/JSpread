package com.unyaunya.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.text.Format;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.unyaunya.grid.Columns;
import com.unyaunya.grid.DefaultCellRenderer;
import com.unyaunya.grid.Handler;
import com.unyaunya.grid.ICell;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.IGridCellRenderer;
import com.unyaunya.grid.IGridModel;
import com.unyaunya.grid.Rows;
import com.unyaunya.grid.ScrollModel;
import com.unyaunya.grid.action.Actions;
import com.unyaunya.grid.editor.EditorHandler;
import com.unyaunya.grid.editor.IGridCellEditor;
import com.unyaunya.grid.format.GridBorder;
import com.unyaunya.grid.selection.DefaultSelectionModel;
import com.unyaunya.grid.selection.IGridSelectionModel;
import com.unyaunya.swing.plaf.GridUI;

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
	private static final Logger LOG = Logger.getLogger(JGrid.class.getName());
	public static final Color DEFAULT_HEADER_BACKGROUND_COLOR = new Color(0xF0,0xD0,0xD0);
	public static final Color DEFAULT_SELECTION_BACKGROUND_COLOR = new Color(0xe0,0xe0,0xff);
	public static final Color DEFAULT_FOREGROUND_COLOR = new Color(0x00,0x00,0x00);
 
	protected Color selectionBackground = DEFAULT_SELECTION_BACKGROUND_COLOR;
	protected Color selectionForeground = DEFAULT_FOREGROUND_COLOR;

	private IGridCellRenderer cellRenderer;
	private EditorHandler editorHandler;
	
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

	public JGrid(IGridModel model) {
		this.setFocusable(true);
		this.scrollModel = createScrollModel();
		this.columns = new Columns(getScrollModel());
		this.rows = new Rows(getScrollModel());
        this.actions = new Actions(this);
    	this.editorHandler = new EditorHandler(this);
    	setCellRenderer(new DefaultCellRenderer());
		setUI(new GridUI());
		init(model);
	}

	private void init(IGridModel model) {
		setGridModel(model);
		setGridSelectionModel(createSelectionModel());

		/*
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusForwardTraversalKeys());
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusBackwardTraversalKeys());
		*/
		this.addMouseListener(getHandler());
		this.addMouseMotionListener(getHandler());
	}

	protected void setCellRenderer(IGridCellRenderer cellRenderer) {
		this.cellRenderer = cellRenderer; 
	}

	public EditorHandler getEditorHandler() {
		return editorHandler;
	}

	/**
	 * スクロールモデルを作成する。
	 * スクロールモデルの機能拡張を行う場合、派生クラスでこのメソッドをオーバライドし、
	 * スクロールモデルの派生クラスのインスタンスを返すようにする。
	 * @return
	 */
	protected ScrollModel createScrollModel() {
		return new ScrollModel(this, getColumnHeaderHeight(), getRowHeaderWidth());
	}

	public void setCellEditor(IGridCellEditor cellEditor) {
		getEditorHandler().setCellEditor(cellEditor);
	}

	/**
	 * セレクションモデルを作成する。
	 * セレクションモデルの機能拡張を行う場合、派生クラスでこのメソッドをオーバライドし、
	 * セレクションモデルの派生クラスのインスタンスを返すようにする。
	 * 
	 * @return
	 */
	protected IGridSelectionModel createSelectionModel() {
		return new DefaultSelectionModel(this);
	}
	
	public Actions getActions() {
    	return this.actions;
    }
	
	public void setGridModel(IGridModel model) {
		gridModel = model;
		getScrollModel().setTableModel(gridModel.getTableModel());
		if(getGridSelectionModel() != null) {
			getGridSelectionModel().clear();
		}
		this.repaint(this.getBounds());
	}

	private void setGridSelectionModel(IGridSelectionModel selectionModel) {
		assert(selectionModel != null);
		this.selectionModel = selectionModel;
		getGridModel().getTableModel().addTableModelListener(selectionModel);
		this.addKeyListener(getGridSelectionModel());
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

	public Rectangle getRangeRect(IRange r) {
		return getScrollModel().getRangeRect(r);
	}

	/*
	 * methods implementing to paint
	 */
	public IGridCellRenderer getCellRenderer(int row, int column) {
		return cellRenderer;
    }

	private String getRowName(int row) {
		return Integer.toString(row+1);
	}

	private String getColumnName(int col) {
		String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String name = "";
		int pos = col % 26;
		name = ABC.substring(pos, pos+1);
		col = col / 26;
		while(col > 0) {
			col = col - 1;
			pos = col % 26;
			name = ABC.substring(pos, pos+1) + name;
			col = col / 26;
		}
		return name;
	}

	public Component prepareRenderer(IGridCellRenderer renderer, int row, int col) {
		Object value = null;
		boolean hasFocus = false;
		boolean isSelected = false;
		Border border = GridBorder.DEFAULT;
		Color foregroundColor = Color.BLACK;
		Color backgroundColor = DEFAULT_HEADER_BACKGROUND_COLOR;
;
		int horizontalAlignment = SwingConstants.CENTER;
		int verticalAlignment = SwingConstants.CENTER;
		
		if(row < 0 && col < 0) {
			isSelected = this.getGridSelectionModel().isSelected(row, col);
		}
		else if(row < 0) {
			isSelected = this.getGridSelectionModel().isColumnSelected(col);
			value = getColumnName(col);
		}
		else if(col < 0) {
			isSelected = this.getGridSelectionModel().isRowSelected(row);
			value = getRowName(row);
		}
		else {
			ICell cell = getGridModel().getCellAt(row, col);
			hasFocus = getGridSelectionModel().hasFocus(row, col);
			isSelected = this.getGridSelectionModel().isSelected(row, col);
			if(hasFocus) {
		    	border = GridBorder.DEFAULT_FOCUS_BORDER;
			}
			backgroundColor = cell.getBackgroundColor();
			foregroundColor = cell.getForegroundColor();
			horizontalAlignment = cell.getHorizontalAlignment();
			verticalAlignment = cell.getVerticalAlignment();
			value = cell.getValue();
			if(value != null) {
				Format f = getGridModel().getCellFormatModel().getFormat(row, col);
				if(f != null) {
					value = f.format(value);
				}
			}
		}
		if(isSelected) {
			backgroundColor = this.getSelectionBackground();
		}
		renderer.setBorder(border);
		renderer.setForeground(foregroundColor);
		renderer.setBackground(backgroundColor);
		renderer.setHorizontalAlignment(horizontalAlignment);
		renderer.setVerticalAlignment(verticalAlignment);
		return renderer.getGridCellRendererComponent(this, value, isSelected, hasFocus, row, col);
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

	/*
	 * methods related to UI appearance
	 */

	/**
	 * 行ヘッダの幅を取得する。
	 * 
	 */
	public int getRowHeaderWidth() {
		return 40;
	}
	
	/**
	 * 列ヘッダの幅を取得する。
	 * 
	 */
	public int getColumnHeaderHeight() {
		return 23;
	}
	
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

	@Override
	protected boolean processKeyBinding(KeyStroke ks,
            KeyEvent e,
            int condition,
            boolean pressed){
		LOG.info("super.processKeyBinding():start");
		LOG.info(ks.toString());
		LOG.info(e.toString());
		LOG.info("condition:WHEN_FOCUSED=0, WHEN_ANCESTOR_OF_FOCUSED_COMPONENT=1, WHEN_IN_FOCUSED_WINDOW=2");
		LOG.info("condition="+condition+", pressed="+pressed);
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
		/*
		{
		    retValue = false;
			InputMap map = this.getInputMap(condition);
			LOG.info(map==null ? "map=null" : map.toString());
			ActionMap am = getActionMap();
			LOG.info(am==null ? "am=null" : am.toString());
		    if(map != null && am != null && isEnabled()) {
		    	Object binding = map.get(ks);
				LOG.info(binding==null ? "binding=null" : binding.toString());
		    	Action action = (binding == null) ? null : am.get(binding);
				LOG.info(action==null ? "action=null" : action.toString());
		    	if (action != null) {
		    		retValue = SwingUtilities.notifyAction(action, ks, e, this, e.getModifiers());
		    	}
		    }
		}
		*/
		LOG.info("super.processKeyBinding():end=" + retValue);
		if(retValue) {
			return true;
		}
		LOG.info("editorHandler.onProcessKeyBinding():start");
		retValue = editorHandler.onProcessKeyBinding(
				retValue,
				ks,
	            e,
	            condition,
	            pressed);
		LOG.info("editorHandler.onProcessKeyBinding():end=" + retValue);
		return retValue;
	}
}
