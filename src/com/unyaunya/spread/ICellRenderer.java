package com.unyaunya.spread;

import java.awt.Color;
import java.awt.Component;

import javax.swing.border.Border;

public interface ICellRenderer {
	public Component getCellRendererComponent(JSpread spread, Object value, boolean isSelected, boolean hasFocus, int row, int column);
	public void setBorder(Border border);
	public Border getBorder();
	public void setBackground(Color color);
	public void setHorizontalAlignment(int alignment);
}
