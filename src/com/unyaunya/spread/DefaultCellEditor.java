/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


import com.unyaunya.spread.ISpreadCellEditor;
import com.unyaunya.swing.JSpread;

class TextField extends JTextField {
	private static final long serialVersionUID = 1L;
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

	//private final JSpread spread; 
	/**
	 * 
	 */
	public DefaultCellEditor(JSpread spread) {
		super(new TextField());
		//this.spread = spread;
		JComponent c = (JComponent)getComponent();
		String key;

		//ESCキーで編集キャンセル
		key = "edit-cancel";
		Action editCancelAction = new AbstractAction(key) {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent ae) {
				fireEditingCanceled();
			}
		};
		registerKeyAction(c,KeyEvent.VK_ESCAPE, editCancelAction);

		//セル移動のアクションを、登録する。
		ActionMap actionMap = spread.getActions().getActionMap();
		for(Object i : actionMap.keys()) {
			c.getActionMap().put(i, actionMap.get(i));
		}
		InputMap inputMap = spread.getActions().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		for(KeyStroke i : inputMap.keys()) {
			c.getInputMap().put(i, inputMap.get(i));
			//VK_ENTERは、VK_DOWNと同じアクションを実行する。
			if(i.getKeyCode() == KeyEvent.VK_DOWN) {
				c.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), inputMap.get(i));
			}
		}
	}

	private void registerKeyAction(JComponent c, int key, Action action) {
		Object actionMapKey = action.getValue(Action.NAME);
		c.getActionMap().put(actionMapKey, action);
		c.getInputMap().put(KeyStroke.getKeyStroke(key, 0), actionMapKey);
	}

	
	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellEditor#getCellEditorComponent(com.unyaunya.swing.JSpread, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getCellEditorComponent(JSpread spread, Object value,
			boolean isSelected, int row, int column) {
		//delegate.setValue(value);
		delegate.setValue(null); //編集開始時は空白
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
