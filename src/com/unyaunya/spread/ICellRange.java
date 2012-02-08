package com.unyaunya.spread;

public interface ICellRange {
	public int getTop();
	public int getLeft();
	public int getBottom();
	public int getRight();
	public boolean contains(int rowIndex, int columnIndex);

	/*
	public void setTop(int top) {
		set(top, this.left, this.bottom, this.right);
	}
	public void setLeft(int left) {
		set(this.top, left, this.bottom, this.right);
	}
	public void setBottom(int bottom) {
		set(this.top, this.left, bottom, this.right);
	}
	*/
}
