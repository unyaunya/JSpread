package com.unyaunya.spread;

public class SpreadSheetModel {
	private SpreadModel tableModel;
	public SpreadSheetModel() {
		tableModel = new SpreadModel();
	}
	
	public SpreadModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(SpreadModel model) {
		tableModel = model;
	}
}
