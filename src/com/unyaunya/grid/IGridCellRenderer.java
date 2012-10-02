package com.unyaunya.grid;

import java.awt.Color;
import java.awt.Component;

import javax.swing.border.Border;

import com.unyaunya.swing.JGrid;

public interface IGridCellRenderer {
	public Component getGridCellRendererComponent(JGrid grid, Object value, boolean isSelected, boolean hasFocus, int row, int column);
	public void setBorder(Border border);
	public Border getBorder();
	public void setForeground(Color color);
	public void setBackground(Color color);
	public void setHorizontalAlignment(int alignment);
	public void setVerticalAlignment(int verticalAlignment);
	public void setDateFormat(String pattern);
}
