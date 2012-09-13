package com.unyaunya.swing;

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
import com.unyaunya.grid.IGridModel;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.editor.DefaultCellEditor;
import com.unyaunya.grid.editor.EditorHandler;
import com.unyaunya.grid.editor.IGridCellEditor;

@SuppressWarnings("serial")
public class JEditableGrid extends JGrid implements CellEditorListener {
    private static final Logger LOG = Logger.getLogger(JEditableGrid.class.getName());

	private IGridCellEditor defaultCellEditor;
	private IGridCellEditor cellEditor = null;
	private boolean isProcessingKeyboardEvent;
	private EditorHandler editorHandler = new EditorHandler();

	public JEditableGrid(IGridModel model) {
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

	public boolean editCellAt(int row, int column) {
		return editCellAt(row, column, null);
	}

	public boolean editCellAt(int row, int column, EventObject e){
		if (row < 0 || row >= getRows().getCount() || column < 0 || column >= getColumns().getCount()) {
			return false;
		}
		IGridCellEditor editor = getCellEditor(row, column);
		editorHandler.setEditorComponent(prepareEditor(editor, row, column));
		editorHandler.getEditorComponent().setBounds(this.getCellRect(row, column));
		add(editorHandler.getEditorComponent());
		editorHandler.getEditorComponent().validate();
		
		setCellEditor(editor);
		editor.addCellEditorListener(this);
		editorHandler.setEditingRow(row);
		editorHandler.setEditingColumn(column);
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
			getGridModel().setValueAt(value, editorHandler.getEditingRow(), editorHandler.getEditingColumn());
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
		if (editorHandler.getEditorComponent() != null) {
			remove(editorHandler.getEditorComponent());
			//Rectangle cellRect = getCellRect(editingRow, editingColumn, false);
			setCellEditor(null);
			editorHandler.setEditingColumn(-1);
			editorHandler.setEditingRow(-1);
			editorHandler.setEditorComponent(null);
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
		if (retValue) {
			return true;
		}
		if (condition != WHEN_ANCESTOR_OF_FOCUSED_COMPONENT || !isFocusOwner()) {
			return false;
		}
		{
			Component editorComponent = editorHandler.getEditorComponent();
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
				editorComponent = editorHandler.getEditorComponent();
				if (editorComponent == null) {
					return false;
				}
			}
			if ((editorComponent instanceof JComponent) && ((e.getModifiers() & InputEvent.ALT_MASK) != 0)) {
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

	/**
	 * 実効セル(連結した場合は、左上隅)を取得する。
	 * @param row
	 * @param col
	 * @return
	 */
	private CellPosition getEffectiveCell(int row, int col) {
		IRange range = getCellRange(row, col);
		if(range == null) {
			return new CellPosition(row, col);
		}
		else {
			return new CellPosition(range.getTop(), range.getLeft());
		}
	}
}
