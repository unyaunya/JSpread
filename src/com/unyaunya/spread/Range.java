package com.unyaunya.spread;

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
}
