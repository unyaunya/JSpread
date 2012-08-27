package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Cell implements ICell {
	private Object value;
	private int row;
	private int column;
	private Color foregroundColor;
	private Color backgroundColor;
	private Border border;
	private int horizontalAlignment;
	
	public Cell(int row, int column, Object value) {
		this(row, column, value, Color.BLACK, Color.WHITE);
	}

	public Cell(int row, int column, Object value, Color foregroundColor, Color backgroundColor) {
		this(row, column, value, foregroundColor, backgroundColor, null);
	}

	public Cell(int row, int column, Object value, Color foregroundColor, Color backgroundColor, Border border) {
		this.row = row;
		this.column = column;
		this.value = value;
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.border = border;
		horizontalAlignment = SwingConstants.LEFT;
	}
	
	@Override
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	@Override
	public Border getBorder() {
		return border;
	}

	@Override
	public Color getForegroundColor() {
		return foregroundColor;
	}

	@Override
	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	@Override
	public String getText() {
		if(getValue() != null) {
			return getValue().toString();
		}
		return null;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public ICellRange getCellRange() {
		return new CellRange(row, column);
	}
}
