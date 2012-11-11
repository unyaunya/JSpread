/**
 * 
 */
package com.unyaunya.grid;

import java.awt.Component;
import java.text.Format;

import javax.swing.JLabel;

import com.unyaunya.swing.JGrid;

/**
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class DefaultCellRenderer extends JLabel implements IGridCellRenderer {
	private Format format;

	public DefaultCellRenderer() {
    	super();
    	setOpaque(true);
    }

	protected String getValueString(JGrid grid, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return value.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellRenderer#getCellRendererComponent()
	 */
	@Override
	public Component getGridCellRendererComponent(JGrid grid, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(value == null) {
			value = "";
		}
		setText(getValueString(grid, value, isSelected, hasFocus, row, column));
		if(isSelected) {
			super.setForeground(grid.getSelectionForeground());
		}
		else {
			
		}
		return this;
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}
	
	@Override
	public Format getFormat() {
		return format;
	}
}
