package com.unyaunya.spread;

import java.util.ArrayList;

public class Range {
	private int top;
	private int left;
	private int bottom;
	private int right;
	
	static Range NULL = new Range(null);
	
	public Range(int top, int left, int bottom, int right) {
		set(top, left, bottom, right);
	}

	public Range(int top, int left) {
		this(top, left, top, left);
	}
	
	public Range() {
		this(0, 0);
	}

	public Range(Range src) {
		set(src);
	}

	public void set(Range src) {
		if(src == null) {
			this.top = -1;
			this.left = -1;
			this.bottom = -1;
			this.right = -1;
		}
		else {
			set(src.top, src.left, src.bottom, src.right);
		}
	}

	public void set(int top, int left, int bottom, int right) {
		if(top < 0) {
			throw new IllegalArgumentException();
		}
		if(left < 0) {
			throw new IllegalArgumentException();
		}
		if(top < bottom) {
			this.top = top;
			this.bottom = bottom;
		}
		else {
			this.top = bottom;
			this.bottom = top;
		}
		if(left < right) {
			this.left = left;
			this.right = right;
		}
		else {
			this.left = right;
			this.right = left;
		}
	}
	
	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		set(top, this.left, this.bottom, this.right);
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		set(this.top, left, this.bottom, this.right);
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		set(this.top, this.left, bottom, this.right);
	}

	public int getRight() {
		return right;
	}
	
	public void setRight(int right) {
		set(this.top, this.left, this.bottom, right);
	}

	public int getRows() {
		return bottom - top + 1;
	}

	public int getColumns() {
		return right - left + 1;
	}

	private static boolean isOverlappedOrAdjacent(int s1, int e1, int s2, int e2) {
		if(s1 < s2) {
			return (e1 >= (s2-1));
		}
		else if(s2 < s1) {
			return (e2 >= (s1-1));
		}
		else {
			return true;
		}
	}
	private static boolean isOverlapped(int s1, int e1, int s2, int e2) {
		if(s1 < s2) {
			return (e1 >= s2);
		}
		else if(s2 < s1) {
			return (e2 >= s1);
		}
		else {
			return true;
		}
	}

	/*
	public boolean isMergeable(Range r) {
		if(getTop() == r.getTop() && getBottom() == r.getBottom()) {
			if(isIntersected(getLeft(), getRight(), r.getLeft(), r.getRight())) {
				return true;
			}
		}
		if(getLeft() == r.getLeft() && getRight() == r.getRight()) {
			if(isIntersected(getTop(), getBottom(), r.getTop(), r.getBottom())) {
				return true;
			}
		}
		return false;
	}
	*/
	
	public Range merge(Range r) {
		if(getTop() == r.getTop() && getBottom() == r.getBottom()) {
			if(isOverlappedOrAdjacent(getLeft(), getRight(), r.getLeft(), r.getRight())) {
				return new Range(getTop(),    Math.min(getLeft(), r.getLeft()),
								 getBottom(), Math.max(getRight(), r.getRight()));
			}
		}
		if(getLeft() == r.getLeft() && getRight() == r.getRight()) {
			if(isOverlappedOrAdjacent(getTop(), getBottom(), r.getTop(), r.getBottom())) {
				return new Range(getLeft(),  Math.min(getTop(), r.getTop()),
						         getRight(), Math.max(getBottom(), r.getBottom()));
			}
		}
		return null;
	}

	public ArrayList<Range> sub(Range r) {
		ArrayList<Range> rslt = new ArrayList<Range>();
		if(!isOverlapped(getTop(), getBottom(), r.getTop(), r.getBottom())) {
			rslt.add(this);
			return rslt;
		}
		if(!isOverlapped(getLeft(), getRight(), r.getLeft(), r.getRight())) {
			rslt.add(this);
			return rslt;
		}

		int[][] lrtb = new int[3][4];
		lrtb[0][0] = getLeft();
		lrtb[0][1] = r.getLeft()-1;
		lrtb[1][0] = r.getLeft();
		lrtb[1][1] = r.getRight();
		lrtb[2][0] = r.getRight()+1;
		lrtb[2][1] = getRight();
		lrtb[0][2] = getTop();
		lrtb[0][3] = r.getTop()-1;
		lrtb[1][2] = r.getTop();
		lrtb[1][3] = r.getBottom();
		lrtb[2][2] = r.getBottom()+1;
		lrtb[2][3] = getBottom();
		for(int i = 0; i < 3; i++) {
			int left   = lrtb[i][0];
			int right  = lrtb[i][1];
			int top    = lrtb[i][2];
			int bottom = lrtb[i][3];
			if(left <= right && top <= bottom) {
				rslt.add(new Range(top, left, right, bottom));
			}
		}
		return rslt;
	}
}
