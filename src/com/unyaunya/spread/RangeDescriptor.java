package com.unyaunya.spread;

import java.util.ArrayList;

public class RangeDescriptor {
	public static final int SINGLE_CELL = 0;
	public static final int SINGLE_RANGE = 1;
	public static final int MULTI_RANGE = 2;
	
	private ArrayList<ICellRange> selectedRangeList = new ArrayList<ICellRange>();

	public RangeDescriptor() {
	}

	public int getRangeType() {
		if(isSingleCell()) {
			return SINGLE_CELL;
		}
		if(isSingleRange()) {
			return SINGLE_RANGE;
		}
		return MULTI_RANGE;
	}

	public boolean isSingleCell() {
		if(selectedRangeList.size() != 1) {
			return false;
		}
		ICellRange r = selectedRangeList.get(0);
		return (r.getTop() == r.getBottom() && r.getLeft() == r.getRight());
	}

	public boolean isSingleRange() {
		if(isSingleCell()) {
			return false;
		}
		return selectedRangeList.size() == 1;
	}

	public boolean isMultiRange() {
		return selectedRangeList.size() > 1;
	}

	public boolean contains(int rowIndex, int columnIndex) {
		for(int i = 0; i < selectedRangeList.size(); i++) {
			if(selectedRangeList.get(i).contains(rowIndex, columnIndex)) {
				return true;
			}
		}
		return false;
	}

	public boolean isRowSelected(int rowIndex) {
		for(int i = 0; i < selectedRangeList.size(); i++) {
			ICellRange r = selectedRangeList.get(i); 
			if(r.getTop() <= rowIndex && rowIndex <= r.getBottom()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isColumnSelected(int columnIndex) {
		for(int i = 0; i < selectedRangeList.size(); i++) {
			ICellRange r = selectedRangeList.get(i); 
			if(r.getLeft() <= columnIndex && columnIndex <= r.getRight()) {
				return true;
			}
		}
		return false;
	}

	public void add(ICellRange range) {
		selectedRangeList = CellRange.merge(getSelectedRangeList(), range);
	}

	public void sub(ICellRange range) {
		selectedRangeList = CellRange.sub(getSelectedRangeList(), range);
	}

	public ArrayList<ICellRange> getSelectedRangeList() {
		return selectedRangeList;		
	}

	public void clear() {
		getSelectedRangeList().clear();
	}
}
