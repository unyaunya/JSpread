package com.unyaunya.spread;

import java.util.ArrayList;

public class RangeDescriptor {
	public static final int SINGLE_CELL = 0;
	public static final int SINGLE_RANGE = 1;
	public static final int MULTI_RANGE = 2;
	
	private ArrayList<Range> selectedRangeList = new ArrayList<Range>();

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
		Range r = selectedRangeList.get(0);
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

	public void add(Range range) {
		selectedRangeList = Range.merge(getSelectedRangeList(), range);
	}

	public void sub(Range range) {
		selectedRangeList = Range.sub(getSelectedRangeList(), range);
	}

	public ArrayList<Range> getSelectedRangeList() {
		return selectedRangeList;		
	}

	public void clear() {
		getSelectedRangeList().clear();
	}
}
