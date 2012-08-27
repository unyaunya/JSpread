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
	 * s”‚ğæ“¾‚·‚é
	 * @return
	 */
	public int getCount() {
		return scrollModel.getRowCount();
	}

}
