/**
 * 
 */
package com.unyaunya.swing;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.unyaunya.spread.CellPosition;
import com.unyaunya.spread.Config;
import com.unyaunya.spread.DefaultCellEditor;
import com.unyaunya.spread.DefaultCellRenderer;
import com.unyaunya.spread.Actions;
import com.unyaunya.spread.ISpreadCellEditor;
import com.unyaunya.spread.ISpreadCellRenderer;
import com.unyaunya.spread.ISpreadSelectionModel;
import com.unyaunya.spread.DefaultSelectionModel;
import com.unyaunya.spread.RangeModel;
import com.unyaunya.spread.ScrollModel;
import com.unyaunya.spread.SpreadBorder;
import com.unyaunya.spread.SpreadModel;
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
	private SpreadModel model;
	private ScrollModel scrollModel;
	private ISpreadSelectionModel selectionModel;
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
		this.model = new SpreadModel();
		this.scrollModel = new ScrollModel(this);
		this.selectionModel = new DefaultSelectionModel();
        this.actions = new Actions(this);
    	this.defaultCellEditor = new DefaultCellEditor(this);

        /*
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusForwardTraversalKeys());
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusBackwardTraversalKeys());
		*/
		setUI(new SpreadUI());
		this.addMouseListener(new Handler());
		this.addMouseMotionListener(new Handler());
	}

	public Config getConfig() {
		return config;
	}
	
	//
	// Operation of the SpreadSheet
	//
	public void insertRow() {
		insertRow(getSelectionModel().getLeadSelectionRow()-1, true);
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
		removeRow(getSelectionModel().getLeadSelectionRow()-1, true);
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
		this.model = (SpreadModel)model;
		this.getSelectionModel().reset();
		scrollModel.setTableModel(model);
		this.repaint(this.getBounds());
	}
	
	public SpreadModel getModel() {
		return model;
	}

	public ScrollModel getScrollModel() {
		return scrollModel;
	}

    public Actions getActions() {
    	return this.actions;
    }
	
	/**
	 * @return the rangeModel
	 */
	public RangeModel getRangeModel(int direction) {
		return scrollModel.getRangeModel(direction);
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
	
	public Rectangle getCellRect(int rowIndex, int colIndex) {
		return scrollModel.getCellRect(rowIndex, colIndex);
	}

	public int rowAtPoint(Point pt) {
		return scrollModel.rowAtPoint(pt);
	}
	
	public int columnAtPoint(Point pt) {
		return scrollModel.columnAtPoint(pt);
	}
	
	public void scrollToVisible(int rowIndex, int columnIndex) {
		scrollModel.scrollToVisible(rowIndex, columnIndex);
	}

	public boolean arePanesFreezed() {
		return scrollModel.arePanesFreezed();
	}
	
	public void freezePanes() {
		//scrollModel.freezePanes(focusModel.getRowIndex()-1, focusModel.getColumnIndex()-1);
		CellPosition leadCell = selectionModel.getLeadCell();
		scrollModel.freezePanes(leadCell.getRow()-1, leadCell.getColumn()-1);
	}
	public void unfreezePanes() {
		scrollModel.unfreezePanes();
	}

	public void setColumnWidth(int colIndex, int width) {
		scrollModel.setColumnWidth(colIndex, width);
	}
	public void setRowHeight(int rowIndex, int height) {
		scrollModel.setRowHeight(rowIndex, height);
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

	public void setFocus(int rowIndex, int columnIndex, InputEvent modifiers) {
		boolean ctrl = false;
		boolean shft = false;
		if(modifiers != null) {
			ctrl = modifiers.isControlDown();
			shft = modifiers.isShiftDown();
		}
		select(rowIndex, columnIndex, shft, ctrl);
	}
	
	public void select(int rowIndex, int columnIndex) {
		select(rowIndex, columnIndex, false, false);
	}

	public void select(int rowIndex, int columnIndex, boolean shft, boolean ctrl) {
		int orig_row = getSelectionModel().getLeadCell().getRow();
		int orig_col = getSelectionModel().getLeadCell().getColumn();
		int newRowIndex = _rowIndex(rowIndex);
		int newColumnIndex = _columnIndex(columnIndex);
		if(orig_row != newRowIndex || orig_col != newColumnIndex) {
			stopEditing();
			scrollToVisible(newRowIndex, newColumnIndex);
			//getSpread().repaintCell(orig_row, orig_col);
			//getSpread().repaintCell(this.rowIndex, this.columnIndex);
		}
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

    public Border getCellBorder(boolean isSelected, boolean hasFocus, int row, int column) {
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
		boolean isSelected;
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
		boolean hasFocus = this.getSelectionModel().isLeadCell(row, col);
		Border border = getCellBorder(isSelected, hasFocus, row, col);
		renderer.setBorder(border);
		renderer.setBackground(this.getCellBackground(isSelected, hasFocus, row, col));
		renderer.setHorizontalAlignment(this.getHorizontalAlignment(row, col));
		Component c = renderer.getSpreadCellRendererComponent(this, s, isSelected, hasFocus, row, col);
		return c;
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
		editorComponent.setBounds(getCellRect(row, column));
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
				int row = getSelectionModel().getLeadSelectionRow();
				int col = getSelectionModel().getLeadSelectionColumn();
				if (!editCellAt(row, col)) {
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
	
	public class Handler extends MouseInputAdapter {
		static final int RESIZE_ZONE_WIDTH = 3;
		private Cursor COLUMN_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
		private Cursor ROW_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
		private Cursor currentCursor = null;
		private int resizeBorderIndex = 0;
		
		private int getNearbyResizeColumnBorderIndex(Point pt, int row, int col) {
			if(row != 0) {
				return 0;
			}
			if(col == 0) {
				return 0;
			}
			int left = scrollModel.getColumnPosition(col);
			if((col != 1) && ((pt.x - left) < RESIZE_ZONE_WIDTH)) {
				return col;
			}
			int right = scrollModel.getColumnPosition(col+1);
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
			int top = scrollModel.getRowPosition(row);
			if((row != 1) && ((pt.y - top) < RESIZE_ZONE_WIDTH)) {
				return row;
			}
			int bottom = scrollModel.getRowPosition(row+1);
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
			if(colIndex != 0) {
				nextCursor = COLUMN_RESIZE_CURSOR;
				resizeBorderIndex = colIndex;
			}
			int rowIndex = getNearbyResizeRowBorderIndex(pt, row, col);
			if(rowIndex != 0) {
				nextCursor = ROW_RESIZE_CURSOR;
				resizeBorderIndex = rowIndex;
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
				if(row == 0 && col == 0) {
					getSelectionModel().selectAll();
					repaint();
				}
				else if(row == 0) {
					getSelectionModel().selectColumn(col, !e.isControlDown());
					repaint();
				}
				else if(col == 0) {
					getSelectionModel().selectRow(row, !e.isControlDown());
					repaint();
				}
				else if(row > 0 && col > 0) {
					/*
					if(!e.isShiftDown() && !e.isControlDown()) {
						getSelectionModel().reset();
					}
					setFocus(row, col, e);
					*/
					getSelectionModel().select(row, col, !e.isShiftDown() && !e.isControlDown());
					repaint();
				}
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
				int width = e.getPoint().y - scrollModel.getRowPosition(resizeBorderIndex-1);
				LOG.info("行リサイズ:"+resizeBorderIndex+"=>"+width);
				setRowHeight(resizeBorderIndex-1, width);
				repaint();
			}
			else if(currentCursor == COLUMN_RESIZE_CURSOR) {
				int height = e.getPoint().x - scrollModel.getColumnPosition(resizeBorderIndex-1);
				LOG.info("列リサイズ:"+resizeBorderIndex+"=>"+height);
				setColumnWidth(resizeBorderIndex-1, height);
				repaint();
			}
			else {
				Point pt = e.getPoint();
				int row = rowAtPoint(pt);
				int col = columnAtPoint(pt);
				if(row != 0 && col != 0) {
					setFocus(row, col, e);
					repaint();
				}
			}
		}

		private int _getRowIndex() {
			return getSelectionModel().getLeadCell().getRow();
		}

		private int _getColumnIndex() {
			return getSelectionModel().getLeadCell().getColumn();
		}

		public void left() {
			select(_getRowIndex(), _getColumnIndex()-1);
		}
		public void right() {
			select(_getRowIndex(), _getColumnIndex()+1);
		}
		public void up() {
			select(_getRowIndex()-1, _getColumnIndex());
		}
		public void down() {
			select(_getRowIndex()+1, _getColumnIndex());
		}
		public void pageLeft() {
			select(_getRowIndex(), _getColumnIndex() - getRangeModel(Adjustable.HORIZONTAL).getExtent());
		}
		public void pageRight() {
			select(_getRowIndex(), _getColumnIndex() + getRangeModel(Adjustable.HORIZONTAL).getExtent());
		}
		public void pageUp() {
			select(_getRowIndex() - getRangeModel(Adjustable.VERTICAL).getExtent(), _getColumnIndex());
		}
		public void pageDown() {
			select(_getRowIndex() + getRangeModel(Adjustable.VERTICAL).getExtent(), _getColumnIndex());
		}
	}
}
