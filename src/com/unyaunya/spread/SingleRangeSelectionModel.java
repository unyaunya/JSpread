package com.unyaunya.spread;

public class SingleRangeSelectionModel implements ISpreadSelectionModel {
	private Range selectedRange = new Range();
	
	public SingleRangeSelectionModel() {
		clearSelection();
	}

	@Override
	public void clearSelection() {
		selectedRange = Range.NULL;
	}

	@Override
	public void selectCell(int rowIndex, int columnIndex) {
		selectRange(new Range(rowIndex, columnIndex));
	}

	public void selectRange(Range range) {
		selectedRange = range;
	}
	
	@Override
	public boolean isCellSelected(int rowIndex, int columnIndex) {
		if(this.selectedRange.getTop() != rowIndex) {
			return false;
		}
		if(this.selectedRange.getLeft() != columnIndex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isRowSelected(int row) {
		return (selectedRange.getTop() == row);
	}

	@Override
	public boolean isColumnSelected(int column) {
		return (selectedRange.getLeft() == column);
	}

	@Override
	public boolean isLeadCell(int rowIndex, int columnIndex) {
		return (rowIndex == getLeadSelectionRow() && columnIndex == getLeadSelectionColumn());
	}

	@Override
	public Range getLeadCell() {
		return new Range(selectedRange);
	}

	@Override
	public int getLeadSelectionRow() {
		return selectedRange.getTop();
	}

	@Override
	public int getLeadSelectionColumn() {
		return selectedRange.getLeft();
	}
}
