/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;


import com.unyaunya.spread.IGridCellEditor;
import com.unyaunya.swing.JEditableGrid;

/**
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class DefaultGridCellEditor extends AbstractCellEditor  implements
		IGridCellEditor {
	transient private JTextField textField = new JTextField(); 
	
	/**
	 * 
	 */
	public DefaultGridCellEditor() {
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return textField.getText();
	}
	
	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellEditor#getCellEditorComponent(com.unyaunya.swing.JSpread, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getCellEditorComponent(JEditableGrid spread, Object value,
			boolean isSelected, int row, int column) {
		if(value == null) {
			value = "";
		}
		textField.setText(value.toString());
		return textField;
	}
}
