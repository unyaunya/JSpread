package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.border.Border;

public interface ICell {
	public int getRow();
	public int getColumn();
	public ICellRange getCellRange();
	public Object getValue();
	public String getText();
	public Color getForegroundColor();
	public Color getBackgroundColor();
	public Border getBorder();
	public int getHorizontalAlignment();
}
