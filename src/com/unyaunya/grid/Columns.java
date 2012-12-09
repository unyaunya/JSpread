package com.unyaunya.grid;

import java.awt.Point;

import com.unyaunya.swing.JGrid;


/**
 * 列操作を行うためのクラス
 * 
 * 内部的には、ScrollModelへのプロキシクラスとなっている。
 * 
 * @author wata
 *
 */
public class Columns extends RowOrColumn {
	public Columns(JGrid grid, ScrollRangeModel rangeModel) {
		super(grid, rangeModel);
	}
	
	public int getDefaultWidth() {
		return rangeModel.getDefaultSize();
	}
	public void setDefaultWidth(int width) {
		super.setDefaultSize(width);
	}
	
	public void setWidth(int columnIndex, int width) {
		super.setSize(columnIndex, width);
	}

	public int getWidth(int columnIndex) {
		return rangeModel.getDisplaySize(columnIndex);
	}

	public int columnAtPoint(Point pt) {
		return rangeModel.getIndex(pt.x);
	}
	public int columnAtViewPoint(Point pt) {
		return rangeModel.getIndex(rangeModel.viewToModel(pt.x));
	}
}
