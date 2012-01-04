/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.unyaunya.swing.JSpread;

import com.unyaunya.spread.ISpreadCellEditor;

/**
 * @author wata
 *
 */
public class DefaultCellEditor extends javax.swing.DefaultCellEditor  implements
		ISpreadCellEditor {
	
	/**
	 * 
	 */
	public DefaultCellEditor() {
		super(new JTextField());
	}

	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellEditor#getCellEditorComponent(com.unyaunya.swing.JSpread, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getCellEditorComponent(JSpread spread, Object value,
			boolean isSelected, int row, int column) {
		delegate.setValue(value);
        if (editorComponent instanceof JCheckBox) {
        	ISpreadCellRenderer renderer = spread.getCellRenderer(row, column);
        	Component c = renderer.getSpreadCellRendererComponent(spread, value, isSelected, true, row, column);
        	if (c != null) {
        		editorComponent.setOpaque(true);
        		editorComponent.setBackground(c.getBackground());
        		if (c instanceof JComponent) {
        				editorComponent.setBorder(((JComponent)c).getBorder());
        		}
        	}
        	else {
        		editorComponent.setOpaque(false);
        	}
        }
        return editorComponent;	
	}
}
