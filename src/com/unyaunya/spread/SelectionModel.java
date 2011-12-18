package com.unyaunya.spread;

public class SelectionModel {
	private int selectedRowIndex = 0;
	private int selectedColumnIndex = 0;
	
	public SelectionModel() {
	}

	public void setSelectedRowIndex(int selectedRowIndex) {
		this.selectedRowIndex = selectedRowIndex;
	}

	public int getSelectedRowIndex() {
		return selectedRowIndex;
	}

	public void setSelectedColumnIndex(int selectedColumnIndex) {
		this.selectedColumnIndex = selectedColumnIndex;
	}

	public int getSelectedColumnIndex() {
		return selectedColumnIndex;
	}

	public void select(int rowIndex, int columnIndex) {
		setSelectedRowIndex(rowIndex);
		setSelectedColumnIndex(columnIndex);
	}

	public boolean isSelected(int rowIndex, int columnIndex) {
		if(this.getSelectedRowIndex() != rowIndex) {
			return false;
		}
		if(this.getSelectedColumnIndex() != columnIndex) {
			return false;
		}
		return true;
	}

}
