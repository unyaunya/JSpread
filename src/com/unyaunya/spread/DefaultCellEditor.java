/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
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

	private JSpread spread; 
	/**
	 * 
	 */
	public DefaultCellEditor(JSpread spread) {
		super(new TextField());
		this.spread = spread;
		Action cancelAction = new AbstractAction("cancel") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				fireEditingCanceled();
			}
		};
		JComponent c = (JComponent)getComponent();
		Object key = cancelAction.getValue(Action.NAME);
		c.getActionMap().put(key, cancelAction);
		c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0), key);
	}

	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellEditor#getCellEditorComponent(com.unyaunya.swing.JSpread, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getCellEditorComponent(JSpread spread, Object value,
			boolean isSelected, int row, int column) {
		//delegate.setValue(value);
		delegate.setValue(null); //ï“èWäJénéûÇÕãÛîí
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
