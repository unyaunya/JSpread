package com.unyaunya.spread;

import com.unyaunya.swing.JSpread;

public class FocusModel {
	private JSpread spread;
	private int rowIndex = 1;
	private int columnIndex = 1;
	
	public FocusModel(JSpread spread) {
		assert(spread != null);
		this.spread = spread;
	}
	
	public JSpread getSpread() {
		return this.spread;
	}

	public void setRowIndex(int rowIndex) {
		this.setFocus(rowIndex, getColumnIndex());
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.setFocus(getRowIndex(), columnIndex);
	}

	public int getColumnIndex() {
		return columnIndex;
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
		int orig_row = this.rowIndex;
		int orig_col = this.columnIndex;
		this.rowIndex = _rowIndex(rowIndex);
		this.columnIndex = _columnIndex(columnIndex);
		if(orig_row != this.rowIndex || orig_col != this.columnIndex) {
			getSpread().repaintCell(orig_row, orig_col);
			getSpread().repaintCell(this.rowIndex, this.columnIndex);
		}
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

	public boolean hasFocus(int rowIndex, int columnIndex) {
		if(this.getRowIndex() != rowIndex) {
			return false;
		}
		if(this.getColumnIndex() != columnIndex) {
			return false;
		}
		return true;
	}
}
