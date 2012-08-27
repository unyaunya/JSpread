package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.border.Border;
import javax.swing.table.TableModel;

public interface IGridModel extends TableModel {
	public Color getForegroundColor(int row, int col);
	public Color getBackgroundColor(int row, int col);
	public Border getBorder(int row, int col);
	public int getHorizontalAlignment(int row, int col);
	public ICellRange getCellRange(int row, int col);
}
