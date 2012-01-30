package com.unyaunya.spread;

public class Range {
	private int top;
	private int left;
	private int bottom;
	private int right;
	
	static Range NULL = new Range(null);
	
	public Range(int top, int left, int bottom, int right) {
		if(top < 0) {
			throw new IllegalArgumentException();
		}
		if(left < 0) {
			throw new IllegalArgumentException();
		}
		if(bottom < top) {
			throw new IllegalArgumentException();
		}
		if(right < left) {
			throw new IllegalArgumentException();
		}
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}

	public Range(int top, int left) {
		this(top, left, top, left);
	}
	
	public Range() {
		this(0, 0);
	}

	public Range(Range src) {
		if(src == null) {
			this.top = -1;
			this.left = -1;
			this.bottom = -1;
			this.right = -1;
		}
		else {
			this.top = src.top;
			this.left = src.left;
			this.bottom = src.bottom;
			this.right = src.right;
		}
	}

	public int getTop() {
		return top;
	}

	public int getLeft() {
		return left;
	}

	public int getBottom() {
		return bottom;
	}

	public int getRight() {
		return right;
	}
	
	public int getRows() {
		return bottom - top + 1;
	}

	public int getColumns() {
		return right - left + 1;
	}
}
