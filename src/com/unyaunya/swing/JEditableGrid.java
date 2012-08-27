package com.unyaunya.swing;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.ICellRange;
import com.unyaunya.grid.editor.DefaultCellEditor;
import com.unyaunya.grid.editor.IGridCellEditor;

@SuppressWarnings("serial")
public class JEditableGrid extends JGrid implements CellEditorListener {
    private static final Logger LOG = Logger.getLogger(JEditableGrid.class.getName());

	protected IGridCellEditor defaultCellEditor;
	protected IGridCellEditor cellEditor = null;
	protected Component editorComponent;
	protected int editingColumn;
	protected int editingRow;
	protected boolean isProcessingKeyboardEvent;

	public JEditableGrid(TableModel model) {
		super(model);
    	this.defaultCellEditor = new DefaultCellEditor(this);
	}

	public IGridCellEditor getCellEditor(int row, int col) {
		IGridCellEditor editor = getCellEditor();
		if(editor == null) {
			editor = getDefaultCellEditor();
		}
		return editor;
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

	public IGridCellEditor getDefaultCellEditor() {
		return defaultCellEditor;
	}

	protected Component getEditorComponent() {
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
		if (row < 0 || row >= getRows().getCount() || column < 0 || column >= getColumns().getCount()) {
			return false;
		}
		IGridCellEditor editor = getCellEditor(row, column);
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

	@Override
	public void editingStopped(ChangeEvent e) {
		stopEditing();
	}

	@Override
	public void editingCanceled(ChangeEvent arg0) {
		removeEditor();
		LOG.info("editingCanceled");
	}

	public void stopEditing() {
		IGridCellEditor editor = getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			getGridModel().setValueAt(value, editingRow, editingColumn);
			removeEditor();
			LOG.info("editingStopped(" + value +  ")");
		}
		else {
			LOG.info("editingStopped():editor = null");
		}
	}
	
	public Component prepareEditor(IGridCellEditor editor, int row, int column) {
		Object value = getGridModel().getValueAt(row, column);
		boolean hasFocus = getGridSelectionModel().hasFocus(row, column);
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
		IGridCellEditor editor = getCellEditor();
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
				int row = getGridSelectionModel().getFocusedRow();
				int col = getGridSelectionModel().getFocusedColumn();
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

	/**
	 * 実効セル(連結した場合は、左上隅)を取得する。
	 * @param row
	 * @param col
	 * @return
	 */
	private CellPosition getEffectiveCell(int row, int col) {
		ICellRange range = getCellRange(row, col);
		if(range == null) {
			return new CellPosition(row, col);
		}
		else {
			return new CellPosition(range.getTop(), range.getLeft());
		}
	}
}
