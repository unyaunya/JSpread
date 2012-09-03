package com.unyaunya.grid.selection;

import java.util.ArrayList;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;

public class RangeDescriptor {
	public static final int SINGLE_CELL = 0;
	public static final int SINGLE_RANGE = 1;
	public static final int MULTI_RANGE = 2;
	
	private ArrayList<IRange> selectedRangeList = new ArrayList<IRange>();

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
		IRange r = selectedRangeList.get(0);
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
			IRange r = selectedRangeList.get(i); 
			if(r.containsRow(rowIndex)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isColumnSelected(int columnIndex) {
		for(int i = 0; i < selectedRangeList.size(); i++) {
			IRange r = selectedRangeList.get(i); 
			if(r.containsColumn(columnIndex)) {
				return true;
			}
		}
		return false;
	}

	public void add(IRange range) {
		selectedRangeList = CellRange.merge(getSelectedRangeList(), range);
	}

	public void sub(IRange range) {
		selectedRangeList = CellRange.sub(getSelectedRangeList(), range);
	}

	public ArrayList<IRange> getSelectedRangeList() {
		return selectedRangeList;		
	}

	public void clear() {
		getSelectedRangeList().clear();
	}
}
