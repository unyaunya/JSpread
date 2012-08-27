/**
 * 
 */
package com.unyaunya.grid;

import java.awt.Component;

import javax.swing.JLabel;

import com.unyaunya.swing.JGrid;
import com.unyaunya.swing.JSpread;

/**
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class DefaultCellRenderer extends JLabel implements IGridCellRenderer {
	public DefaultCellRenderer() {
    	super();
    	setOpaque(true);
    }

	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellRenderer#getCellRendererComponent()
	 */
	@Override
	public Component getGridCellRendererComponent(JGrid grid, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JSpread spread = (JSpread)grid;
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
