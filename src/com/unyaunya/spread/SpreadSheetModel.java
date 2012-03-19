package com.unyaunya.spread;

public class SpreadSheetModel {
	private SpreadModel tableModel;
	private ScrollModel scrollModel;
	
	public SpreadSheetModel() {
		this.tableModel = new SpreadModel();
		this.scrollModel = new ScrollModel();
	}
	
	public SpreadModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(SpreadModel model) {
		tableModel = model;
	}

	public ScrollModel getScrollModel() {
		return scrollModel;
	}
}
