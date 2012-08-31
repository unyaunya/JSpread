package com.unyaunya.spread;

import java.awt.Color;

import javax.swing.border.Border;

import com.unyaunya.grid.ICell;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.format.IFormattableGridModel;

class SpreadSheetCell implements ICell {
	private SpreadSheetModel model;
	private int row;
	private int column;

	SpreadSheetCell(SpreadSheetModel model, int row, int column) {
		this.model = model;
		this.row = row;
		this.column = column;
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
	public String getText() {
		Object value = getValue();
		return value == null ? null : value.toString();
	}

	@Override
	public Object getValue() {
		return model.getValueAt(row, column);
	}

	@Override
	public IRange getRange() {
		return model.getCellRange(row, column);
	}

	@Override
	public Color getBackgroundColor() {
		return model.getCellFormatModel().getBackgroundColor(row, column);
	}

	@Override
	public Color getForegroundColor() {
		return model.getCellFormatModel().getForegroundColor(row, column);
	}

	/**
	 * ボーダーを取得する。
	 * @return
	 */
	@Override
	public Border getBorder() {
		return model.getCellFormatModel().getBorder(row, column);
	}

	/**
	 * 水平方向のアライメントを取得する。
	 * @return
	 */
	@Override
	public int getHorizontalAlignment() {
		return model.getCellFormatModel().getHorizontalAlignment(row, column);
	}

	/**
	 * 垂直方向のアライメントを取得する。
	 * @return
	 */
	@Override
	public int getVerticalAlignment() {
		return model.getCellFormatModel().getVerticalAlignment(row, column);
	}
}
