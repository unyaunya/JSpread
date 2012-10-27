package com.unyaunya.grid.editor;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.swing.JGrid;

public class EditorHandler implements CellEditorListener  {
    private static final Logger LOG = Logger.getLogger(EditorHandler.class.getName());

	transient private JGrid grid;
	transient private IGridCellEditor defaultCellEditor;
	transient private IGridCellEditor cellEditor = null;

	transient private int editingColumn;
	transient private int editingRow;
	transient private Component editorComponent;

	transient private boolean isProcessingKeyboardEvent;
	
	public EditorHandler(JGrid grid) {
		this.grid = grid;
    	this.defaultCellEditor = new DefaultCellEditor(grid);
	}

	private int getEditingRow() {
		return editingRow;
	}
	private int getEditingColumn() {
		return editingColumn;
	}
	private void setEditingRow(int row) {
		editingRow = row;
	}
	private void setEditingColumn(int column) {
		editingColumn = column;
	}

	private Component getEditorComponent() {
		return editorComponent;
	}

	private void setEditorComponent(Component c) {
		this.editorComponent = c;
	}

	private IGridCellEditor getDefaultCellEditor() {
		return defaultCellEditor;
	}

	/**
	 * Returns the active cell editor 
	 * @return
	 */
	private IGridCellEditor getCellEditor() {
		return cellEditor;
	}

	public void setCellEditor(IGridCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	private IGridCellEditor getCellEditor(int row, int col) {
		IGridCellEditor editor = getCellEditor();
		if(editor == null) {
			editor = getDefaultCellEditor();
		}
		return editor;
	}

	private Component prepareEditor(IGridCellEditor editor, int row, int column) {
		Object value = grid.getGridModel().getValueAt(row, column);
		boolean hasFocus = grid.getGridSelectionModel().hasFocus(row, column);
		Component comp = editor.getCellEditorComponent(grid, value, hasFocus, row, column);
		/*
		if (comp instanceof JComponent) {
			JComponent jComp = (JComponent)comp;
			if (jComp.getNextFocusableComponent() == null) {
				jComp.setNextFocusableComponent(this);			}
		}
		*/
		return comp;
	}

	private void removeEditor() {
		IGridCellEditor editor = getCellEditor();
		if(editor != null) {
			editor.removeCellEditorListener(this);
		}
		if (getEditorComponent() != null) {
			grid.remove(getEditorComponent());
			//Rectangle cellRect = getCellRect(editingRow, editingColumn, false);
			setCellEditor(null);
			setEditingColumn(-1);
			setEditingRow(-1);
			setEditorComponent(null);
			//repaint(cellRect);
			grid.repaint();
		}
	}

	public boolean editCellAt(int row, int column){
		if (row < 0 || row >= grid.getRows().getCount() || column < 0 || column >= grid.getColumns().getCount()) {
			return false;
		}
		//セルが結合されている場合、riw,columnから、結合範囲の左上隅のセルを取得して描画する必要がある。
		//CellPosition cell = getEffectiveCell(row, column);
		IRange range = grid.getCellRange(row, column);
		if(range == null) {
			range = new CellRange(row, column);
		}
		int top = range.getTop();
		int left = range.getLeft();
		if(!grid.getGridModel().getTableModel().isCellEditable(top, left)) {
			return false;
		}
		IGridCellEditor editor = getCellEditor(top, left);
		setEditorComponent(prepareEditor(editor, top, left));
		getEditorComponent().setBounds(grid.getRangeRect(range));
		grid.add(getEditorComponent());
		getEditorComponent().validate();
		
		setCellEditor(editor);
		editor.addCellEditorListener(this);
		setEditingRow(top);
		setEditingColumn(left);
		LOG.info("editCellAt("+row+","+column+")");
		return true;
	}

	public void stopEditing() {
		int row = getEditingRow();
		int col = getEditingColumn();
		IGridCellEditor editor = getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			LOG.info(grid.getRangeRect(grid.getCellRange(row, col)).toString());
			grid.getGridModel().setValueAt(value, getEditingRow(), getEditingColumn());
			LOG.info(grid.getRangeRect(grid.getCellRange(row, col)).toString());
			removeEditor();
			LOG.info("editingStopped(" + value +  ")");
		}
		else {
			LOG.info("editingStopped():editor = null");
		}
	}
	
	public boolean onProcessKeyBinding(
			KeyStroke ks,
            KeyEvent e,
            boolean pressed){
		LOG.info("onProcessKeyBinding():start");
		if(isProcessingKeyboardEvent) {
			LOG.info("isProcessingKeyboardEvent=true");
			return false;
		}

		Component editorComponent = getEditorComponent();
		//install editorComponent
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
			int row = grid.getGridSelectionModel().getFocusedRow();
			int col = grid.getGridSelectionModel().getFocusedColumn();
			//TODO:editorCellAtでコンポーネントが表示される位置がおかしい
			if (!editCellAt(row, col)) {
				return false;
			}
			editorComponent = getEditorComponent();
			if (editorComponent == null) {
				return false;
			}
		}
		if ((editorComponent instanceof JComponent) && ((e.getModifiers() & InputEvent.ALT_MASK) == 0)) {
			boolean retValue = true;
			//retValue = ((JComponent)editorComponent).processKeyBinding(ks, e, WHEN_FOCUSED, pressed);
			KeyEvent ke = new KeyEvent(editorComponent, e.getID(), e.getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation());
			isProcessingKeyboardEvent = true;
			LOG.info("editorComponent.dispatchEvent(ke):start");
			editorComponent.dispatchEvent(ke);
			LOG.info("editorComponent.dispatchEvent(ke):end");
			isProcessingKeyboardEvent = false;
			//if (getSurrendersFocusOnKeystroke()) {
			editorComponent.requestFocus();
			//}
		}
		return false;
	}

	//
	//implementation of CellEditorListener
	//
	
	@Override
	public void editingStopped(ChangeEvent e) {
		LOG.info("editingStopped");
		stopEditing();
	}

	@Override
	public void editingCanceled(ChangeEvent arg0) {
		LOG.info("editingCanceled");
		removeEditor();
	}
}
