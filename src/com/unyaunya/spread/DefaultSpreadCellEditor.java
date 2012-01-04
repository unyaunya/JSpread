/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;

import com.unyaunya.swing.JSpread;

import com.unyaunya.spread.ISpreadCellEditor;

/**
 * @author wata
 *
 */
public class DefaultSpreadCellEditor extends AbstractCellEditor  implements
		ISpreadCellEditor {
	transient private JTextField textField = new JTextField(); 
	
	/**
	 * 
	 */
	public DefaultSpreadCellEditor() {
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
	public Component getCellEditorComponent(JSpread spread, Object value,
			boolean isSelected, int row, int column) {
		if(value == null) {
			value = "";
		}
		textField.setText(value.toString());
		return textField;
	}
}
