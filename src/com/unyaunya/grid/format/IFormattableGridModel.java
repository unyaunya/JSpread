package com.unyaunya.grid.format;

import java.awt.Color;

import com.unyaunya.grid.IGridModel;

public interface IFormattableGridModel extends IGridModel {
	public void setBackgroundColor(Color color, int row, int col);
	public void setForegroundColor(Color color, int row, int col);
}
