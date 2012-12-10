package com.unyaunya.grid;

import java.awt.Point;

import com.unyaunya.swing.JGrid;


public class Rows extends RowOrColumn {
	public Rows(JGrid grid, ScrollRangeModel rangeModel) {
		super(grid, rangeModel);
	}
	
	public int getDefaultHeight() {
		return rangeModel.getDefaultSize();
	}
	
	public void setDefaultHeight(int height) {
		super.setDefaultSize(height);
	}

	public void setHeight(int rowIndex, int height) {
		super.setSize(rowIndex, height);
	}

	public int getHeight(int rowIndex) {
		return rangeModel.getDisplaySize(rowIndex);
	}

	public int rowAtPoint(Point pt) {
		return rangeModel.getIndex(pt.y);
	}

	public int rowAtViewPoint(Point pt) {
		return rangeModel.getIndex(rangeModel.viewToModel(pt.y));
	}
	
	public boolean isLeaf(int index) {
		return rangeModel.isLeaf(index);
	}

	public boolean isExpanded(int index) {
		return rangeModel.isExpanded(index);
	}
}
