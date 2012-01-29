/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;

import javax.swing.JLabel;

import com.unyaunya.spread.ISpreadCellRenderer;
import com.unyaunya.swing.JSpread;

/**
 * @author wata
 *
 */
public class DefaultCellRenderer extends JLabel implements ISpreadCellRenderer {
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
	public Component getSpreadCellRendererComponent(JSpread spread, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
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
