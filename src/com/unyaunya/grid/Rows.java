package com.unyaunya.grid;


public class Rows extends RowOrColumn {
	transient ScrollModel scrollModel;
	
	public Rows(ScrollModel model) {
		scrollModel = model;
	}

	public void setHeight(int rowIndex, int height) {
		scrollModel.setRowHeight(rowIndex, height);
	}

	/**
	 * 行数を取得する
	 * @return
	 */
	public int getCount() {
		return scrollModel.getRowCount();
	}

}
