package com.unyaunya.spread;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class ScrollModel {
	private SizeModel colSizeModel;
	private SizeModel rowSizeModel;
	private RangeModel colRangeModel;
	private RangeModel rowRangeModel;

	public ScrollModel() {
		this.rowSizeModel = new SizeModel();
		this.colSizeModel = new SizeModel();
		this.rowRangeModel = new RangeModel(rowSizeModel);
		this.colRangeModel = new RangeModel(colSizeModel);
	}

	/**
	 * @return the rangeModel
	 */
	public RangeModel getRangeModel(int direction) {
		switch(direction) {
		case JSpread.HORIZONTAL:
			return colRangeModel;
		case JSpread.VERTICAL:
			return rowRangeModel;
		default:
			throw new RuntimeException("illegal direction value.");
		}
	}

	/**
	 * @return the sizeModel
	 */
	public SizeModel getSizeModel(int direction) {
		switch(direction) {
		case JSpread.HORIZONTAL:
			return colSizeModel;
		case JSpread.VERTICAL:
			return rowSizeModel;
		default:
			throw new RuntimeException("illegal direction value.");
		}
	}

	public Rectangle getCellRect(int rowIndex, int colIndex) {
		return new Rectangle(
					colSizeModel.getPosition(colIndex),
					rowSizeModel.getPosition(rowIndex),
					colSizeModel.getSize(colIndex),
					rowSizeModel.getSize(rowIndex));
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(
				colSizeModel.getPreferredSize(),
				rowSizeModel.getPreferredSize());
	}

	public int getRowPosition(int rowIndex) {
		return rowRangeModel.untranslate(rowSizeModel.getPosition(rowIndex));
	}
	public int getRowHeight(int rowIndex) {
		return rowSizeModel.getSize(rowIndex);
	}
	public int getColumnPosition(int columnIndex) {
		return colRangeModel.untranslate(colSizeModel.getPosition(columnIndex));
	}
	public int getColumnWidth(int columnIndex) {
		return colSizeModel.getSize(columnIndex);
	}
	
	public int rowAtPoint(Point pt) {
		return rowSizeModel.getIndex(rowRangeModel.translate(pt.y));
	}
	
	public int columnAtPoint(Point pt) {
		return colSizeModel.getIndex(colRangeModel.translate(pt.x));
	}
}
