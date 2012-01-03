/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JLabel;

import com.unyaunya.spread.ICellRenderer;
import com.unyaunya.swing.JSpread;

/**
 * @author wata
 *
 */
public class DefaultCellRenderer extends JLabel implements ICellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DefaultCellRenderer() {
    	super();
    	setOpaque(true);
    }

	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellRenderer#getCellRendererComponent()
	 */
	@Override
	public Component getCellRendererComponent(JSpread spread, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(value == null) {
			value = "";
		}
		setText(value.toString());
		if(isSelected) {
			super.setForeground(spread.getSelectionForeground());
		}
		else {
			
		}
		return this;
	}
}
