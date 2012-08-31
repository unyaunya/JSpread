package com.unyaunya.grid;

public interface IRange {
	public int getTop();
	public int getLeft();
	public int getBottom();
	public int getRight();
	
	/**
	 * ・当該セル範囲が、位置(0,0)を含んでいれば、containsは常にtrueを返す。
	 *　・当該セル範囲が、位置(rowIndex,0)を含んでいれば、contains(rowIndex, any)は常にtrueを返す。
	 *　・当該セル範囲が、位置(columnIndex,0)を含んでいれば、contains(any, columnIndex)は常にtrueを返す。
	 *　・その他の場合は、次の条件を満たす場合のみ、contains(rowIndex, columnIndex)は常にtrueを返す。
	 *   (getTop() <= rowIndex && rowIndex <= getBottom()) && (getLeft() <= columnIndex && columnIndex <= getRight())
	 * @param rowIndex
	 * @param columnIndex
	 * @return　rowIndex, columnIndexで示されるセル位置を含んでいればtrue
	 */
	public boolean contains(int rowIndex, int columnIndex);

	public boolean containsRow(int rowIndex);
	public boolean containsColumn(int columnIndex);
	
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
