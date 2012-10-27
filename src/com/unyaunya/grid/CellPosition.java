package com.unyaunya.grid;

import java.io.Serializable;

/**
 * �Z���ʒu��\������N���X
 * 
 * row,col�̃����o�����B
 * 
 */
@SuppressWarnings("serial")
public class CellPosition implements Serializable {
	private int row;    //�s
	private int column; //��
	
	/**
	 * �R���X�g���N�^
	 */
	public CellPosition() {
		this(0,0);
	}

	/**
	 * �R���X�g���N�^
	 */
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

	@Override
	public boolean equals(Object value) {
		if(value == null) {
			return false;
		}
		//System.out.println(String.format("equals(%s, %s)", this, value));
		if(value instanceof CellPosition) {
			CellPosition val = (CellPosition)value;
			if(this.row == val.row && this.column == val.column) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("CallPosition[row=%d,column=%d]", this.row, this.column);
	}

	@Override
	public int hashCode(){
		return row*column;
	}
}