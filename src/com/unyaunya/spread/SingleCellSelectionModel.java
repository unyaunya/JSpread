package com.unyaunya.spread;

public class SingleCellSelectionModel implements ICellSelectionModel {
	private Range selectedCell = new Range();
	
	public SingleCellSelectionModel() {
		clearSelection();
	}

	@Override
	public void clearSelection() {
		selectedCell = Range.NULL;
	}

	@Override
	public void selectCell(int rowIndex, int columnIndex) {
		selectedCell = new Range(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellSelected(int rowIndex, int columnIndex) {
		if(this.selectedCell.getTop() != rowIndex) {
			return false;
		}
		if(this.selectedCell.getLeft() != columnIndex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isRowSelected(int row) {
		return (selectedCell.getTop() == row);
	}

	@Override
	public boolean isColumnSelected(int column) {
		return (selectedCell.getLeft() == column);
	}

	@Override
	public boolean isLeadCell(int rowIndex, int columnIndex) {
		return (rowIndex == getLeadSelectionRow() && columnIndex == getLeadSelectionColumn());
	}

	@Override
	public Range getLeadCell() {
		return new Range(selectedCell);
	}

	@Override
	public int getLeadSelectionRow() {
		return selectedCell.getTop();
	}

	@Override
	public int getLeadSelectionColumn() {
		return selectedCell.getLeft();
	}
}
