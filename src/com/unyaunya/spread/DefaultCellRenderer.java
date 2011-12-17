/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

	public DefaultCellRenderer() {
    	super();
    	setOpaque(true);
    	setBorder(getNoFocusBorder());
    }

    private static Border getNoFocusBorder() {
    	if (System.getSecurityManager() != null) {
    		return SAFE_NO_FOCUS_BORDER;
    	} else {
    		return noFocusBorder;
    	}
    }

	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellRenderer#getCellRendererComponent()
	 */
	@Override
	public Component getCellRendererComponent(JSpread spread, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Rectangle r = spread.getCellRect(row, column);
		setBounds(r);
		setText(value.toString());
		return this;
	}
}
