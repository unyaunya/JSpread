package com.unyaunya.spread;

public class CellPosition {
	private int row;
	private int column;
	
	public CellPosition() {
		this(0,0);
	}
	public CellPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public void set(int row, int column) {
		this.row = row;
		this.column = column;
	}
}
