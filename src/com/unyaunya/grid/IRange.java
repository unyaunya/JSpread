package com.unyaunya.grid;

public interface IRange {
	public int getTop();
	public int getLeft();
	public int getBottom();
	public int getRight();
	
	/**
	 * �E���Y�Z���͈͂��A�ʒu(0,0)���܂�ł���΁Acontains�͏��true��Ԃ��B
	 *�@�E���Y�Z���͈͂��A�ʒu(rowIndex,0)���܂�ł���΁Acontains(rowIndex, any)�͏��true��Ԃ��B
	 *�@�E���Y�Z���͈͂��A�ʒu(columnIndex,0)���܂�ł���΁Acontains(any, columnIndex)�͏��true��Ԃ��B
	 *�@�E���̑��̏ꍇ�́A���̏����𖞂����ꍇ�̂݁Acontains(rowIndex, columnIndex)�͏��true��Ԃ��B
	 *   (getTop() <= rowIndex && rowIndex <= getBottom()) && (getLeft() <= columnIndex && columnIndex <= getRight())
	 * @param rowIndex
	 * @param columnIndex
	 * @return�@rowIndex, columnIndex�Ŏ������Z���ʒu���܂�ł����true
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
