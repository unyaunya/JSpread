package com.unyaunya.spread;

import java.awt.event.InputEvent;

public class MultiRangeSelectionModel implements ISpreadSelectionModel {
	private RangeDescriptor selectedRangeList = new RangeDescriptor();
	private Range selectedRange = new Range();
	private Range anchorCell = new Range();
	private Range lastSelectedCell = new Range();
	
	public MultiRangeSelectionModel() {
		clearSelection();
	}

	@Override
	public void clearSelection() {
		selectedRangeList.clear();
		selectedRangeList.add(new Range());
	}

	@Override
	public void selectCell(int rowIndex, int columnIndex) {
		selectCell(rowIndex, columnIndex, null);
	}

	@Override
	public void selectCell(int rowIndex, int columnIndex, InputEvent e) {
		if(e != null && e.isShiftDown()) {
			selectRange(new Range(rowIndex, columnIndex, anchorCell.getTop(), anchorCell.getLeft()));
		}
		else {
			anchorCell = new Range(rowIndex, columnIndex);
			selectRange(new Range(rowIndex, columnIndex));
		}
		lastSelectedCell = new Range(rowIndex, columnIndex);
	}
	
	@Override
	public void selectRange(Range range) {
		selectedRange = range;
	}

	@Override
	public void selectRange(int top, int left, int bottom, int right) {
		selectedRange = new Range(top, left, bottom, right);
	}

	@Override
	public boolean isCellSelected(int rowIndex, int columnIndex) {
		if(selectedRangeList.contains(rowIndex, columnIndex)) {
			return true;
		}
		return selectedRange.contains(rowIndex, columnIndex);
	}

	@Override
	public boolean isRowSelected(int rowIndex) {
		if(rowIndex < this.selectedRange.getTop() || this.selectedRange.getBottom() < rowIndex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isColumnSelected(int columnIndex) {
		if(columnIndex < this.selectedRange.getLeft() || this.selectedRange.getRight() < columnIndex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isLeadCell(int rowIndex, int columnIndex) {
		return (rowIndex == getLeadSelectionRow() && columnIndex == getLeadSelectionColumn());
	}

	@Override
	public Range getLeadCell() {
		return lastSelectedCell;
	}

	@Override
	public int getLeadSelectionRow() {
		return lastSelectedCell.getTop();
	}

	@Override
	public int getLeadSelectionColumn() {
		return lastSelectedCell.getLeft();
	}
}
