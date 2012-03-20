/**
 * 
 */
package com.unyaunya.swing;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.EventObject;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.unyaunya.spread.CellRange;
import com.unyaunya.spread.CellSpanModel;
import com.unyaunya.spread.Config;
import com.unyaunya.spread.DefaultCellEditor;
import com.unyaunya.spread.DefaultCellRenderer;
import com.unyaunya.spread.Actions;
import com.unyaunya.spread.CellPosition;
import com.unyaunya.spread.ICellRange;
import com.unyaunya.spread.ISpreadCellEditor;
import com.unyaunya.spread.ISpreadCellRenderer;
import com.unyaunya.spread.ISpreadSelectionModel;
import com.unyaunya.spread.DefaultSelectionModel;
import com.unyaunya.spread.RangeDescriptor;
import com.unyaunya.spread.RangeModel;
import com.unyaunya.spread.ScrollModel;
import com.unyaunya.spread.SpreadBorder;
import com.unyaunya.spread.SpreadModel;
import com.unyaunya.spread.SpreadSheetModel;
import com.unyaunya.swing.plaf.SpreadUI;

/**
 * @author wata
 *
 */
public class JSpread extends JComponent implements CellEditorListener {
    private static final Logger LOG = Logger.getLogger(JSpread.class.getName());
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String uiClassID = "SpreadUI";

	public static final Color DEFAULT_HEADER_BACKGROUND_COLOR = new Color(0xf0,0xf0,0xf0);
	public static final Color DEFAULT_SELECTION_BACKGROUND_COLOR = new Color(0xe0,0xe0,0xff);
	public static final Color DEFAULT_FOREGROUND_COLOR = new Color(0x00,0x00,0x00);

	protected Color selectionBackground = DEFAULT_SELECTION_BACKGROUND_COLOR;
	protected Color selectionForeground = DEFAULT_FOREGROUND_COLOR;
	protected static ISpreadCellRenderer defaultCellRenderer = new DefaultCellRenderer();

	private Config config;
	private SpreadSheetModel spreadSheetModel;
	private ISpreadSelectionModel selectionModel;
	private CellSpanModel cellSpanModel;
	private Actions actions;
	private Handler handler;
	
	transient ISpreadCellEditor defaultCellEditor;
	transient ISpreadCellEditor cellEditor = null;
	transient private Component editorComponent;
	transient protected int editingColumn;
	transient protected int editingRow;
	transient protected boolean isProcessingKeyboardEvent;

	/**
	 * constructor
	 */
	public JSpread() {
		this(new Config());
	}

	/**
	 * constructor
	 */
	public JSpread(Config config) {
		this.setFocusable(true);

		this.config = config;
		this.spreadSheetModel = new SpreadSheetModel();
		this.getScrollModel().getRangeModel(Adjustable.VERTICAL).addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				repaint();
			}
		});
		this.getScrollModel().getRangeModel(Adjustable.HORIZONTAL).addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				repaint();
			}
		});

		this.selectionModel = new DefaultSelectionModel();
        this.actions = new Actions(this);
    	this.defaultCellEditor = new DefaultCellEditor(this);
    	this.cellSpanModel = new CellSpanModel();

        /*
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusForwardTraversalKeys());
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusBackwardTraversalKeys());
		*/
		setUI(new SpreadUI());
		this.addMouseListener(getHandler());
		this.addMouseMotionListener(getHandler());
		this.addKeyListener(getHandler());
	}

	public Config getConfig() {
		return config;
	}
	
	//
	// Operation of the SpreadSheet
	//
	public void insertRow() {
		insertRow(getSelectionModel().getRowOfLeadCell()-1, true);
	}

	public void insertRow(int row) {
		insertRow(row, true);
	}
	
	public void insertRow(int row, boolean paint) {
		if(!getConfig().isRowInsertionSuppoorted()) {
			throw new UnsupportedOperationException();
		}
		DefaultTableModel m = (DefaultTableModel)this.getModel().getTableModel();
		m.insertRow(row,  (Object[])null);
		if(paint) {
			repaint();
		}
	}

	public void removeRow() {
		removeRow(getSelectionModel().getRowOfLeadCell()-1, true);
	}
	public void removeRow(int row) {
		removeRow(row, true);
	}
	public void removeRow(int row, boolean paint) {
		if(!getConfig().isRowInsertionSuppoorted()) {
			throw new UnsupportedOperationException();
		}
		DefaultTableModel m = (DefaultTableModel)this.getModel().getTableModel();
		m.removeRow(row);
		if(paint) {
			repaint();
		}
	}
	
	//
	// Managing TableUI
	//

	/**
	 * Returns the L&F object that renders this component.
	 *
	 * @return the <code>SpreadUI</code> object that renders this component
	 */
	public SpreadUI getUI() {
	    return (SpreadUI)ui;
	}

	/**
	 * Sets the L&F object that renders this component and repaints.
	 *
	 * @param ui  the TableUI L&F object
	 * @see UIDefaults#getUI
	 * @beaninfo
	 *        bound: true
	 *       hidden: true
	 *    attribute: visualUpdate true
	 *  description: The UI object that implements the Component's LookAndFeel.
	 */
	public void setUI(SpreadUI ui) {
	    if (this.ui != ui) {
	        super.setUI(ui);
	        repaint();
	    }
	}

	 /**
     * Returns the suffix used to construct the name of the L&F class used to
     * render this component.
     *
     * @return the string "SpreadUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    public String getUIClassID() {
        return uiClassID;
    }

	/*
	 * methods set/get various models.
	 */
	public void setModel(TableModel model) {
		if(model == null) {
			model = new SpreadModel();
		}
		if(!(model instanceof SpreadModel)) {
			model = new SpreadModel(model);
		}
		this.spreadSheetModel.setTableModel((SpreadModel)model);
		this.getSelectionModel().reset();
		getScrollModel().setTableModel(this.getModel());
		this.repaint(this.getBounds());
	}
	
	public SpreadModel getModel() {
		return this.spreadSheetModel.getTableModel();
	}

	public ScrollModel getScrollModel() {
		return this.spreadSheetModel.getScrollModel();
	}

    public Actions getActions() {
    	return this.actions;
    }
	
	/**
	 * @return the rangeModel
	 */
	public RangeModel getRangeModel(int direction) {
		return getScrollModel().getRangeModel(direction);
	}

	public ISpreadSelectionModel getSelectionModel() {
		return this.selectionModel;
	}

	/*
	 * methods delegating to TableModel
	 */
	public int getRowCount() {
		return getModel().getRowCount();
	}
	
	public int getColumnCount() {
		return getModel().getColumnCount();
	}

	/*
	 * methods delegating to ScrollModel
	 */
	
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
	
	public void freezePanes() {
		getScrollModel().freezePanes(selectionModel.getRowOfLeadCell(), selectionModel.getColumnOfLeadCell());
		repaint();
	}
	
	public void unfreezePanes() {
		getScrollModel().unfreezePanes();
		repaint();
	}

	public void coupleCells(RangeDescriptor desc) {
		if(desc.isMultiRange()) {
			return;
		}
		CellRange range = new CellRange((CellRange)desc.getSelectedRangeList().get(0));
		cellSpanModel.coupleCells(range);
		repaint();
		LOG.info(range.toString());
	}
	
	public void coupleCells() {
		coupleCells(this.getSelectionModel().getRangeDescriptor());
	}

	public void setColumnWidth(int colIndex, int width) {
		getScrollModel().setColumnWidth(colIndex, width);
	}
	public void setRowHeight(int rowIndex, int height) {
		getScrollModel().setRowHeight(rowIndex, height);
	}
	
	/*
	 * methods delegating to SelectionModel
	 */
	private int _rowIndex(int rowIndex) {
		if(rowIndex < 1) {
			rowIndex = 1;
		}
		if(getRowCount() <= rowIndex) {
			rowIndex = getRowCount() - 1; 
		}
		return rowIndex;
	}

	private int _columnIndex(int columnIndex) {
		if(columnIndex < 1) {
			columnIndex = 1;
		}
		if(getColumnCount() <= columnIndex) {
			columnIndex = getColumnCount() - 1; 
		}
		return columnIndex;
	}

	public void select(int rowIndex, int columnIndex, boolean shft, boolean ctrl) {
		int orig_row = getSelectionModel().getRowOfLeadCell();
		int orig_col = getSelectionModel().getColumnOfLeadCell();
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

	private void _select(int newRowIndex, int newColumnIndex, boolean shft,
			boolean ctrl) {
		if(shft) {
			selectionModel.setLeadCell(newRowIndex, newColumnIndex);
		}
		else {
			selectionModel.select(newRowIndex, newColumnIndex, !ctrl);
		}
		repaint();
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
    
    protected Color getCellBackground(boolean isSelected, boolean hasFocus, int row, int column) {
		if(isSelected) {
			return this.getSelectionBackground();
		}
		else {
	    	if(row <= 0 || column <= 0) {
	    		return DEFAULT_HEADER_BACKGROUND_COLOR;
	    	}
	    	else {
	    		return Color.WHITE;
	    	}
		}
    }

    protected int getHorizontalAlignment(int row, int column) {
    	if(row <= 0 || column <= 0) {
    		return SwingConstants.CENTER;
    	}
    	else {
    		return SwingConstants.LEFT;
    	}
    }

    public ICellRange getCellRange(int row, int column) {
   		return cellSpanModel.getCellRange(row, column);
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
	 * methods implementing to paint
	 */
	//public void repaintCell(int row, int column) {
		//repaint(getCellRect(row, column));
	//}

	public ISpreadCellRenderer getCellRenderer(int row, int column) {
		return defaultCellRenderer;
    }

	public Component prepareRenderer(ISpreadCellRenderer renderer, int row, int col) {
		SpreadModel m = getModel();
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
		renderer.setBackground(this.getCellBackground(isSelected, hasFocus, row, col));
		renderer.setHorizontalAlignment(this.getHorizontalAlignment(row, col));
		Component c = renderer.getSpreadCellRendererComponent(this, s, isSelected, hasFocus, row, col);
		return c;
	}

	private boolean hasFocus(int row, int col) {
		boolean rslt = false;
		ICellRange range = this.getCellRange(row, col);
		if(range == null) {
			rslt = this.getSelectionModel().isLeadCell(row, col);
		}
		else {
			int rowLeadCell = this.getSelectionModel().getRowOfLeadCell();
			int colLeadCell = this.getSelectionModel().getColumnOfLeadCell();
			if(range.contains(rowLeadCell, colLeadCell)) {
				rslt = true;
			}
		}
		return rslt;
	}

	/*
	 * methods related to cell editing 
	 */
	public ISpreadCellEditor getCellEditor(int row, int col) {
		ISpreadCellEditor editor = getCellEditor();
		if(editor == null) {
			editor = getDefaultCellEditor();
		}
		return editor;
	}

	/**
	 * Returns the active cell editor 
	 * @return
	 */
	public ISpreadCellEditor getCellEditor() {
		return cellEditor;
	}

	public void setCellEditor(ISpreadCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public ISpreadCellEditor getDefaultCellEditor() {
		return defaultCellEditor;
	}

	private Component getEditorComponent() {
		return editorComponent;
	}

	public int getEditingRow() {
		return editingRow;
	}
	public int getEditingColumn() {
		return editingColumn;
	}
	private void setEditingRow(int row) {
		editingRow = row;
	}
	private void setEditingColumn(int column) {
		editingColumn = column;
	}
	
	public boolean editCellAt(int row, int column) {
		return editCellAt(row, column, null);
	}

	public boolean editCellAt(int row, int column, EventObject e){
		if (row < 0 || row >= getRowCount() || column < 0 || column >= getColumnCount()) {
			return false;
		}
		ISpreadCellEditor editor = getCellEditor(row, column);
		editorComponent = prepareEditor(editor, row, column);
		editorComponent.setBounds(this.getCellRect(row, column));
		add(editorComponent);
		editorComponent.validate();
		
		setCellEditor(editor);
		editor.addCellEditorListener(this);
		setEditingRow(row);
		setEditingColumn(column);
		LOG.info("editCellAt("+row+","+column+")");
		return true;
	}

	public Component prepareEditor(ISpreadCellEditor editor, int row, int column) {
		Object value = getModel().getValueAt(row, column);
		boolean hasFocus = getSelectionModel().isLeadCell(row, column);
		Component comp = editor.getCellEditorComponent(this, value, hasFocus, row, column);
		/*
		if (comp instanceof JComponent) {
			JComponent jComp = (JComponent)comp;
			if (jComp.getNextFocusableComponent() == null) {
				jComp.setNextFocusableComponent(this);			}
		}
		*/
		return comp;
	}

	public void removeEditor() {
		ISpreadCellEditor editor = getCellEditor();
		if(editor != null) {
			editor.removeCellEditorListener(this);
		}
		if (editorComponent != null) {
			remove(editorComponent);
			//Rectangle cellRect = getCellRect(editingRow, editingColumn, false);
			setCellEditor(null);
			setEditingColumn(-1);
			setEditingRow(-1);
			editorComponent = null;
			//repaint(cellRect);
			repaint();
		}
	}
	
	private CellPosition getEffectiveCell(int row, int col) {
		ICellRange range = getCellRange(row, col);
		if(range == null) {
			return new CellPosition(row, col);
		}
		else {
			return new CellPosition(range.getTop(), range.getLeft());
		}
	}
	
	@Override
	protected boolean processKeyBinding(KeyStroke ks,
            KeyEvent e,
            int condition,
            boolean pressed){
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
		if(isProcessingKeyboardEvent) {
			return false;
		}
		if (!retValue && condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT &&
				isFocusOwner()) {
			Component editorComponent = getEditorComponent();
			if(editorComponent == null) {
				// Only attempt to install the editor on a KEY_PRESSED,
				if (e == null || e.getID() != KeyEvent.KEY_PRESSED) {
					return false;
				}
				// Don't start when just a modifier is pressed
				int code = e.getKeyCode();
				if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL || code == KeyEvent.VK_ALT) {
					return false;
				}
				// Try to install the editor
				
				//int leadRow = getSelectionModel().getLeadSelectionIndex();
				//int leadColumn = getColumnModel().getSelectionModel().getLeadSelectionIndex();
				//if (leadRow != -1 && leadColumn != -1 && !isEditing()) {
				int row = getSelectionModel().getRowOfLeadCell();
				int col = getSelectionModel().getColumnOfLeadCell();
				CellPosition cell = getEffectiveCell(row, col);
				if (!editCellAt(cell.getRow(), cell.getColumn())) {
					return false;
				}
				editorComponent = getEditorComponent();
				if (editorComponent == null) {
					return false;
				}
			}
			if (editorComponent instanceof JComponent) {
				//retValue = ((JComponent)editorComponent).processKeyBinding(ks, e, WHEN_FOCUSED, pressed);
				KeyEvent ke = new KeyEvent(editorComponent, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation());
				isProcessingKeyboardEvent = true;
				editorComponent.dispatchEvent(ke);
				isProcessingKeyboardEvent = false;
				retValue = true;
				//if (getSurrendersFocusOnKeystroke()) {
				editorComponent.requestFocus();
				//}
			}
		}
		return retValue;
	}

	@Override
	public void editingCanceled(ChangeEvent arg0) {
		removeEditor();
		LOG.info("editingCanceled");
	}

	@Override
	public void editingStopped(ChangeEvent arg0) {
		stopEditing();
	}

	public void stopEditing() {
		ISpreadCellEditor editor = getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			getModel().setValueAt(value, editingRow, editingColumn);
			removeEditor();
			LOG.info("editingStopped(" + value +  ")");
		}
		else {
			LOG.info("editingStopped():editor = null");
		}
	}

	/*
	 * 
	 */
	public Handler getHandler() {
		if(handler == null) {
			handler = new Handler();
		}
		return handler;
	}
	
	public class Handler extends MouseInputAdapter implements KeyListener {
		static final int RESIZE_ZONE_WIDTH = 3;
		private final Cursor ROW_SELECT_CURSOR = createCursor("icons/arrow_right.gif", new Point(15, 15));
		private final Cursor COLUMN_SELECT_CURSOR = createCursor("icons/arrow_down.gif", new Point(15, 15));
		private Cursor COLUMN_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
		private Cursor ROW_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
		private Cursor currentCursor = null;
		private int resizeBorderIndex = 0;
		private boolean shiftDown;
		private boolean controlDown;
		
		private Cursor createCursor(String name, Point hotSpot) {
			Toolkit kit = Toolkit.getDefaultToolkit();
			URL url = getClass().getClassLoader().getResource(name);
			Image img = kit.createImage(url); 
			Cursor cursor = kit.createCustomCursor(img, hotSpot, name);
			return cursor;
		}
		
		private boolean isShiftDown() {
			return shiftDown;
		}
		private void setShiftDown(boolean value) {
			shiftDown = value;
		}
		private boolean isControlDown() {
			return controlDown;
		}
		private void setControlDown(boolean value) {
			controlDown = value;
		}
		
		private int getNearbyResizeColumnBorderIndex(Point pt, int row, int col) {
			if(row != 0) {
				return 0;
			}
			if(col == 0) {
				return 0;
			}
			int left = getScrollModel().getColumnPosition(col);
			if((col != 1) && ((pt.x - left) < RESIZE_ZONE_WIDTH)) {
				return col;
			}
			int right = getScrollModel().getColumnPosition(col+1);
			if((right - pt.x) < RESIZE_ZONE_WIDTH) {
				return col + 1;
			}
			return 0;
		}

		private int getNearbyResizeRowBorderIndex(Point pt, int row, int col) {
			if(col != 0) {
				return 0;
			}
			if(row == 0) {
				return 0;
			}
			int top = getScrollModel().getRowPosition(row);
			if((row != 1) && ((pt.y - top) < RESIZE_ZONE_WIDTH)) {
				return row;
			}
			int bottom = getScrollModel().getRowPosition(row+1);
			if((bottom - pt.y) < RESIZE_ZONE_WIDTH) {
				return row+1;
			}
			return 0;
		}

		/*
		private Cursor getNextCursor(Point pt, int row, int col) {
			int colIndex = getNearbyResizeColumnBorderIndex(pt, row, col);
			if(colIndex != 0) {
				return COLUMN_RESIZE_CURSOR;
			}
			int rowIndex = getNearbyResizeRowBorderIndex(pt, row, col);
			if(rowIndex != 0) {
				return ROW_RESIZE_CURSOR;
			}
			return null;
		}
		*/
		
		@Override
		public void mouseMoved(MouseEvent e) {
			Point pt = e.getPoint();
			int row = rowAtPoint(pt);
			int col = columnAtPoint(pt);
			Cursor nextCursor = null;
			resizeBorderIndex = 0;
			int colIndex = getNearbyResizeColumnBorderIndex(pt, row, col);
			int rowIndex = getNearbyResizeRowBorderIndex(pt, row, col);
			if(colIndex != 0) {
				nextCursor = COLUMN_RESIZE_CURSOR;
				resizeBorderIndex = colIndex;
			}
			else if(rowIndex != 0) {
				nextCursor = ROW_RESIZE_CURSOR;
				resizeBorderIndex = rowIndex;
			}
			else if(row == 0 && col == 0) {
				nextCursor = null;
			}
			else if(row == 0) {
				nextCursor = COLUMN_SELECT_CURSOR;
			}
			else if(col == 0) {
				nextCursor = ROW_SELECT_CURSOR;
			}

			if(currentCursor != nextCursor) {
				if(nextCursor != null) {
					setCursor(nextCursor);
				}
				else {
					setCursor(null);
					//LOG.info("("+row+","+col+")");
				}
				currentCursor = nextCursor;
			}
		}

		@Override
		/*
		 * (non-Javadoc)
		 * リードセル　　　　　：　キー入力対象セル。
		 * 修飾キーなし　　　：　セレクションをクリアしてから、クリックされたセルを選択する。
		 *					クリックされたセルはリードかつアンカーになる。
		 * SHIFTキー押下時：　現在のRangeオブジェクトを、アンカーとクリックされたセルを含めるものに変更する。
		 *					リードセルは、クリックされたセル。
		 * CTRLキー押下時：  　現在のRangeオブジェクトを、アンカーとクリックされたセルを含めるものに変更する。
		 *					リードセルは、クリックされたセル。
		 * 
		 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			LOG.info("mousePressed");
			if(currentCursor == ROW_RESIZE_CURSOR) {
				LOG.info("行リサイズ開始");
			}
			else if(currentCursor == COLUMN_RESIZE_CURSOR) {
				LOG.info("列リサイズ開始");
			}
			else {
				Point pt = e.getPoint();
				int row = rowAtPoint(pt);
				int col = columnAtPoint(pt);
				_select(row, col, e.isShiftDown(), e.isControlDown());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			LOG.info("mouseReleased");
			if(currentCursor == ROW_RESIZE_CURSOR) {
				LOG.info("行リサイズ終了");
			}
			else if(currentCursor == COLUMN_RESIZE_CURSOR) {
				LOG.info("列リサイズ終了");
			}
			else if(currentCursor == ROW_SELECT_CURSOR) {
				LOG.info("行選択終了");
			}
			else if(currentCursor == COLUMN_SELECT_CURSOR) {
				LOG.info("列選択終了");
			}
		}
		
		/*
		 * (non-Javadoc)
		 * リードセル　　　　　：　キー入力対象セル。
		 * 修飾キーなし　　　：　セレクションをクリアしてから、クリックされたセルを選択する。
		 *					クリックされたセルはリードかつアンカーになる。
		 * SHIFTキー押下時：　現在のRangeオブジェクトを、アンカーとクリックされたセルを含めるものに変更する。
		 *					リードセルは、クリックされたセル。
		 * マウスドラッグ時　　：　現在のRangeオブジェクトを変更する。
		 * 
		 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			LOG.info("mouseDragged");
			if(currentCursor == ROW_RESIZE_CURSOR) {
				int width = e.getPoint().y - getScrollModel().getRowPosition(resizeBorderIndex-1);
				LOG.info("行リサイズ:"+resizeBorderIndex+"=>"+width);
				setRowHeight(resizeBorderIndex-1, width);
				repaint();
			}
			else if(currentCursor == COLUMN_RESIZE_CURSOR) {
				int height = e.getPoint().x - getScrollModel().getColumnPosition(resizeBorderIndex-1);
				LOG.info("列リサイズ:"+resizeBorderIndex+"=>"+height);
				setColumnWidth(resizeBorderIndex-1, height);
				repaint();
			}
			else {
				Point pt = e.getPoint();
				int row = rowAtPoint(pt);
				int col = columnAtPoint(pt);
				if(currentCursor == COLUMN_SELECT_CURSOR) {
					row = 0;
					if(col == 0) {
						col = 1;
					}
				}
				else if(currentCursor == ROW_SELECT_CURSOR) {
					col = 0;
					if(row == 0) {
						row = 1;
					}
				}
				else {
					//if(row != 0 && col != 0) {
					//}
				}
				getSelectionModel().setLeadCell(row, col);
				repaint();
			}
		}

		private void move(int deltaRow, int deltaColumn) {
			LOG.info("SHIFT="+isShiftDown()+",CTRL="+isControlDown());
			int currentRow = getSelectionModel().getRowOfLeadCell();
			int currentCol = getSelectionModel().getColumnOfLeadCell();
			ICellRange range = JSpread.this.getCellRange(currentRow, currentCol);
			int row = currentRow+deltaRow;
			int col = currentCol+deltaColumn;
			if(range != null) {
				if(deltaRow > 0) {
					if(row <= range.getBottom()) {
						row = range.getBottom() + 1;
					}
				}
				else if(deltaRow < 0) {
					if(row >= range.getTop()) {
						row = range.getTop() - 1;
					}
				}
				if(deltaColumn > 0) {
					if(col <= range.getRight()) {
						col = range.getRight() + 1;
					}
				}
				else if(deltaColumn < 0) {
					if(col >= range.getLeft()) {
						col = range.getLeft() - 1;
					}
				}
			}
			JSpread.this.select(row, col, isShiftDown(), isControlDown());
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
				setShiftDown(true);
			}
			if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
				setControlDown(true);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
				setShiftDown(false);
			}
			if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
				setControlDown(false);
			}
		}
		@Override
		public void keyTyped(KeyEvent e) {
		}

		public void left() {
			move(0, -1);
		}
		public void right() {
			move(0, +1);
		}
		public void up() {
			move(-1, 0);
		}
		public void down() {
			move(+1, 0);
		}
		public void pageLeft() {
			move(0, - getRangeModel(Adjustable.HORIZONTAL).getExtent());
		}
		public void pageRight() {
			move(0, + getRangeModel(Adjustable.HORIZONTAL).getExtent());
		}
		public void pageUp() {
			move(- getRangeModel(Adjustable.VERTICAL).getExtent(), 0);
		}
		public void pageDown() {
			move(+ getRangeModel(Adjustable.VERTICAL).getExtent(), 0);
		}
	}
}
