package com.unyaunya.grid.editor;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import com.unyaunya.grid.CellPosition;
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

	public int getEditingRow() {
		return editingRow;
	}
	public int getEditingColumn() {
		return editingColumn;
	}
	public void setEditingRow(int row) {
		editingRow = row;
	}
	public void setEditingColumn(int column) {
		editingColumn = column;
	}

	public Component getEditorComponent() {
		return editorComponent;
	}

	public void setEditorComponent(Component c) {
		this.editorComponent = c;
	}

	public IGridCellEditor getDefaultCellEditor() {
		return defaultCellEditor;
	}

	/**
	 * Returns the active cell editor 
	 * @return
	 */
	public IGridCellEditor getCellEditor() {
		return cellEditor;
	}

	public void setCellEditor(IGridCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public IGridCellEditor getCellEditor(int row, int col) {
		IGridCellEditor editor = getCellEditor();
		if(editor == null) {
			editor = getDefaultCellEditor();
		}
		return editor;
	}

	public Component prepareEditor(IGridCellEditor editor, int row, int column) {
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

	public void removeEditor() {
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

	private boolean editCellAt(int row, int column, EventObject e){
		if (row < 0 || row >= grid.getRows().getCount() || column < 0 || column >= grid.getColumns().getCount()) {
			return false;
		}
		IGridCellEditor editor = getCellEditor(row, column);
		setEditorComponent(prepareEditor(editor, row, column));
		getEditorComponent().setBounds(grid.getCellRect(row, column));
		grid.add(getEditorComponent());
		getEditorComponent().validate();
		
		setCellEditor(editor);
		editor.addCellEditorListener(this);
		setEditingRow(row);
		setEditingColumn(column);
		LOG.info("editCellAt("+row+","+column+")");
		return true;
	}

	public void stopEditing() {
		IGridCellEditor editor = getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			grid.getGridModel().setValueAt(value, getEditingRow(), getEditingColumn());
			removeEditor();
			LOG.info("editingStopped(" + value +  ")");
		}
		else {
			LOG.info("editingStopped():editor = null");
		}
	}

	/**
	 * 実効セル(連結した場合は、左上隅)を取得する。
	 * @param row
	 * @param col
	 * @return
	 */
	private CellPosition getEffectiveCell(int row, int col) {
		IRange range = grid.getCellRange(row, col);
		if(range == null) {
			return new CellPosition(row, col);
		}
		else {
			return new CellPosition(range.getTop(), range.getLeft());
		}
	}
	
	private boolean editCellAt(int row, int column) {
		return editCellAt(row, column, null);
	}

	public boolean onProcessKeyBinding(
			boolean retValue,
			KeyStroke ks,
            KeyEvent e,
            int condition,
            boolean pressed){
		if(isProcessingKeyboardEvent) {
			return false;
		}
		if (retValue) {
			return true;
		}
		if (condition != JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT || !grid.isFocusOwner()) {
			return false;
		}
		{
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
				int row = grid.getGridSelectionModel().getFocusedRow();
				int col = grid.getGridSelectionModel().getFocusedColumn();
				CellPosition cell = getEffectiveCell(row, col);
				if (!editCellAt(cell.getRow(), cell.getColumn())) {
					return false;
				}
				editorComponent = getEditorComponent();
				if (editorComponent == null) {
					return false;
				}
			}
			if ((editorComponent instanceof JComponent) && ((e.getModifiers() & InputEvent.ALT_MASK) == 0)) {
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
