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
		Range m = null;
		int i = 0;
		for(i = 0; i < selectedRangeList.size(); i++) {
			Range r = selectedRangeList.get(i);
			m = range.merge(r);
			if(m != null) {
				selectedRangeList.set(i, m);
				break;
			}
		}
		if(m != null) {
			selectedRangeList.remove(i);
			add(m);
		}
	}

	public void sub(Range range) {
	}

	public ArrayList<Range> getSelectedRangeList() {
		return selectedRangeList;		
	}
}
