package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Cell implements ICell {
	private int row;
	private int column;
	private Object value;
	
	public Cell(int row, int column) {
		this(row, column, null);
	}

	public Cell(int row, int column, Object value) {
		this.row = row;
		this.column = column;
		this.value = value;
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
	public IRange getRange() {
		return new CellRange(row, column);
	}

	@Override
	public Color getBackgroundColor() {
		return Color.WHITE;
	}

	@Override
	public Color getForegroundColor() {
		return Color.BLACK;
	}

	/**
	 * ボーダーを取得する。
	 * @return
	 */
	@Override
	public Border getBorder() {
		return null;
	}

	/**
	 * 水平方向のアライメントを取得する。
	 * @return
	 */
	@Override
	public int getHorizontalAlignment() {
		return SwingConstants.LEFT;
	}

	/**
	 * 垂直方向のアライメントを取得する。
	 * @return
	 */
	@Override
	public int getVerticalAlignment() {
		return SwingConstants.TOP;
	}
}
