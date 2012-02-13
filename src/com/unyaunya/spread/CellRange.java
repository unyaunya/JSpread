package com.unyaunya.spread;

import java.util.ArrayList;

public class CellRange implements ICellRange {
	private int top;
	private int left;
	private int bottom;
	private int right;
	
	static CellRange NULL = new CellRange(null);
	
	public CellRange(int top, int left, int bottom, int right) {
		set(top, left, bottom, right);
	}

	public CellRange(int top, int left) {
		this(top, left, top, left);
	}
	
	public CellRange() {
		this(0, 0);
	}

	public CellRange(CellRange src) {
		set(src);
	}

	public void set(CellRange src) {
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

	public void set(CellPosition p) {
		set(p, p);
	}
	
	public void set(CellPosition p1, CellPosition p2) {
		set(p1.getRow(), p1.getColumn(), p2.getRow(), p2.getColumn());
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
	
	@Override
	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		set(top, this.left, this.bottom, this.right);
	}

	@Override
	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		set(this.top, left, this.bottom, this.right);
	}

	@Override
	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		set(this.top, this.left, bottom, this.right);
	}

	@Override
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

	/**
	 * ・当該セル範囲が、位置(0,0)を含んでいれば、containsは常にtrueを返す。
	 *　・当該セル範囲が、位置(rowIndex,0)を含んでいれば、contains(rowIndex, any)は常にtrueを返す。
	 *　・当該セル範囲が、位置(columnIndex,0)を含んでいれば、contains(any, columnIndex)は常にtrueを返す。
	 *　・その他の場合は、次の条件を満たす場合のみ、contains(rowIndex, columnIndex)は常にtrueを返す。
	 *   (getTop() <= rowIndex && rowIndex <= getBottom()) && (getLeft() <= columnIndex && columnIndex <= getRight())
	 * @param rowIndex
	 * @param columnIndex
	 * @return　rowIndex, columnIndexで示されるセル位置を含んでいればtrue
	 */
	@Override
	public boolean contains(int rowIndex, int columnIndex) {
		if(_contains(0,0)) {
			return true;
		}
		if(_contains(rowIndex, 0)) {
			return true;
		}
		if(_contains(0,columnIndex)) {
			return true;
		}
		return _contains(rowIndex, columnIndex);
	}

	private boolean _contains(int rowIndex, int columnIndex) {
		return (getTop() <= rowIndex && rowIndex <= getBottom()) && (getLeft() <= columnIndex && columnIndex <= getRight());
	}


	public boolean containsRow(int rowIndex) {
		if(getTop() == 0) {
			return true;
		}
		return (getTop() <= rowIndex && rowIndex <= getBottom());
	}
	
	public boolean containsColumn(int columnIndex) {
		if(getLeft() == 0) {
			return true;
		}
		return (getLeft() <= columnIndex && columnIndex <= getRight());
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
	 * ２つのRangeオブジェクトを結合した結果を返す。
	 * 結合するRangeオブジェクトは、縦または横の範囲が同じで、隣接あるいは重なっていなければならない。
	 */
	public ICellRange merge(ICellRange r) {
		return merge(this, r);
	}

	public static ICellRange merge(ICellRange r1, ICellRange r2) {
		assert(r1 != null);
		assert(r2 != null);
		if(r1.getTop() == r2.getTop() && r1.getBottom() == r2.getBottom()) {
			if(isOverlappedOrAdjacent(r1.getLeft(), r1.getRight(), r2.getLeft(), r2.getRight())) {
				return new CellRange(r1.getTop(),    Math.min(r1.getLeft(), r2.getLeft()),
								 r1.getBottom(), Math.max(r1.getRight(), r2.getRight()));
			}
		}
		if(r1.getLeft() == r2.getLeft() && r1.getRight() == r2.getRight()) {
			if(isOverlappedOrAdjacent(r1.getTop(), r1.getBottom(), r2.getTop(), r2.getBottom())) {
				return new CellRange(r1.getLeft(),  Math.min(r1.getTop(), r2.getTop()),
								 r1.getRight(), Math.max(r1.getBottom(), r2.getBottom()));
			}
		}
		return null;
	}

	public static ArrayList<ICellRange> merge(ArrayList<ICellRange> rangeList, ICellRange r) {
		if(rangeList == null) {
			rangeList = new ArrayList<ICellRange>();
		}
		if(r != null) {
			if(rangeList.size() == 0) {
				rangeList.add(r);
			}
			else {
				ICellRange new_r = null;
				int i;
				for(i = 0; i < rangeList.size(); i++) {
					new_r = merge(rangeList.get(i), r);
					if(new_r != null) {
						break;
					}
				}
				if(new_r != null) {
					rangeList.remove(i);
					return merge(rangeList, new_r); 
				}
			}
		}
		return rangeList;
	}

	/*
	 * 指定したRangeオブジェクトの範囲を削除した結果（Rangeオブジェクトのリスト）を返す。
	 * 削除される領域次第で、結合するRangeオブジェクトは、縦または横の範囲が同じで、隣接あるいは重なっていなければならない。
	 */
	public ArrayList<ICellRange> sub(ICellRange r) {
		return sub(this, r);
	}
	
	public static ArrayList<ICellRange> sub(ICellRange r1, ICellRange r2) {
		assert(r1 != null);
		assert(r2 != null);
		ArrayList<ICellRange> rslt = new ArrayList<ICellRange>();
		if(!isOverlapped(r1.getTop(), r1.getBottom(), r2.getTop(), r2.getBottom())) {
			rslt.add(r1);
			return rslt;
		}
		if(!isOverlapped(r1.getLeft(), r1.getRight(), r2.getLeft(), r2.getRight())) {
			rslt.add(r1);
			return rslt;
		}

		int[] x = new int[6];
		int[] y = new int[6];
		x[0] = r1.getLeft();
		x[1] = r2.getLeft()-1;
		x[2] = r2.getLeft();
		x[3] = r2.getRight();
		x[4] = r2.getRight()+1;
		x[5] = r1.getRight();
		y[0] = r1.getTop();
		y[1] = r2.getTop()-1;
		y[2] = r2.getTop();
		y[3] = r2.getBottom();
		y[4] = r2.getBottom()+1;
		y[5] = r1.getBottom();
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(i != 1 || j != 1) {
					int left   = x[i*2];
					int right  = x[i*2+1];
					int top    = y[i*2];
					int bottom = y[i*2+1];
					if(left <= right && top <= bottom) {
						merge(rslt, new CellRange(top, left, right, bottom));
					}
				}
			}
		}
		return rslt;
	}

	public static ArrayList<ICellRange> sub(ArrayList<ICellRange> rangeList, ICellRange r) {
		ArrayList<ICellRange> rslt = new ArrayList<ICellRange>();
		ArrayList<ICellRange> newList;
		int i;
		for(i = 0; i < rangeList.size(); i++) {
			newList = sub(rangeList.get(i), r);
			for(int j = 0; j < newList.size(); j++) {
				rslt = merge(rslt, newList.get(j));
			}
		}
		return rslt;
	}
}
