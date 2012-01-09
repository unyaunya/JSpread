/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


import com.unyaunya.spread.ISpreadCellEditor;
import com.unyaunya.swing.JSpread;

class TextField extends JTextField {
	public TextField() {
		super();
	}

	public boolean _processKeyBinding(KeyStroke ks,
            KeyEvent e,
            int condition,
            boolean pressed){
		return super.processKeyBinding(ks, e, condition, pressed);
	}
}

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
		super(new TextField());
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
