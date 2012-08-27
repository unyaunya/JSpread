package com.unyaunya.grid;


/**
 * 列操作を行うためのクラス
 * 
 * 内部的には、ScrollModelへのプロキシクラスとなっている。
 * 
 * @author wata
 *
 */
public class Columns extends RowOrColumn {
	transient ScrollModel scrollModel;
	
	public Columns(ScrollModel model) {
		scrollModel = model;
	}
	
	public void setWidth(int columnIndex, int width) {
		scrollModel.setColumnWidth(columnIndex, width);
	}

	/**
	 * 列数を取得する
	 * @return
	 */
	public int getCount() {
		return scrollModel.getColumnCount();
	}
}
