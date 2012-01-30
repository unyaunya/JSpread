package com.unyaunya.spread;

import java.awt.Adjustable;

import com.unyaunya.swing.JSpread;



public class FocusModel {
	private JSpread spread;
	
	public FocusModel(JSpread spread) {
		assert(spread != null);
		this.spread = spread;
	}
	
	public JSpread getSpread() {
		return this.spread;
	}

	private int getRowIndex() {
		return spread.getSelectionModel().getLeadCell().getTop();
	}

	private int getColumnIndex() {
		return spread.getSelectionModel().getLeadCell().getLeft();
	}

	private int _rowIndex(int rowIndex) {
		if(rowIndex < 1) {
			rowIndex = 1;
		}
		if(getSpread().getRowCount() <= rowIndex) {
			rowIndex = getSpread().getRowCount() - 1; 
		}
		return rowIndex;
	}

	private int _columnIndex(int columnIndex) {
		if(columnIndex < 1) {
			columnIndex = 1;
		}
		if(getSpread().getColumnCount() <= columnIndex) {
			columnIndex = getSpread().getColumnCount() - 1; 
		}
		return columnIndex;
	}

	public void setFocus(int rowIndex, int columnIndex) {
		int orig_row = getRowIndex();
		int orig_col = getColumnIndex();
		int newRowIndex = _rowIndex(rowIndex);
		int newColumnIndex = _columnIndex(columnIndex);
		if(orig_row != newRowIndex || orig_col != newColumnIndex) {
			spread.stopEditing();
			getSpread().scrollToVisible(newRowIndex, newColumnIndex);
			//getSpread().repaintCell(orig_row, orig_col);
			//getSpread().repaintCell(this.rowIndex, this.columnIndex);
		}
		spread.select(rowIndex, columnIndex);
	}
	
	public void left() {
		setFocus(getRowIndex(), getColumnIndex()-1);
	}
	public void right() {
		setFocus(getRowIndex(), getColumnIndex()+1);
	}
	public void up() {
		setFocus(getRowIndex()-1, getColumnIndex());
	}
	public void down() {
		setFocus(getRowIndex()+1, getColumnIndex());
	}
	public void pageLeft() {
		setFocus(getRowIndex(), getColumnIndex()-spread.getRangeModel(Adjustable.HORIZONTAL).getExtent());
	}
	public void pageRight() {
		setFocus(getRowIndex(), getColumnIndex()+spread.getRangeModel(Adjustable.HORIZONTAL).getExtent());
	}
	public void pageUp() {
		setFocus(getRowIndex()-spread.getRangeModel(Adjustable.VERTICAL).getExtent(), getColumnIndex());
	}
	public void pageDown() {
		setFocus(getRowIndex()+spread.getRangeModel(Adjustable.VERTICAL).getExtent(), getColumnIndex());
	}
}
