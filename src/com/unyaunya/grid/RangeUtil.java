package com.unyaunya.grid;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;

public class RangeUtil {
	/**
	 * rangeListの先頭から、rowIndex, columnIndex で指定したセルを含む、
	 * セル範囲を検索して返す。含むものがなければnullを返す
	 * 
	 * @param rangeList
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public static IRange getRange(ArrayList<IRange> rangeList, int rowIndex, int columnIndex) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange range = rangeList.get(i);
			assert(range != null);
			if(range.contains(rowIndex, columnIndex)) {
				return range;
			}
		}
		return null;
	}
	
	/**
	 * rowIndex, columnIndex で指定したセルが、rangeListに含まれる
	 * いずれかのセル範囲にふくまれていれば、trueを返す。
	 * 
	 * @param rangeList
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	public static boolean contains(ArrayList<IRange> rangeList, int rowIndex, int columnIndex) {
		return getRange(rangeList, rowIndex, columnIndex) != null;
	}

	/**
	 * rangeListに含まれるいずれかのセル範囲が、rowIndex で指定した行を
	 * 含んでいれば、trueを返す。
	 * 
	 * @param rangeList
	 * @param rowIndex
	 * @return
	 */
	public static boolean containsRow(ArrayList<IRange> rangeList, int rowIndex) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange r = rangeList.get(i); 
			if(r.containsRow(rowIndex)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * rangeListに含まれるいずれかのセル範囲が、columnIndex で指定した列を
	 * 含んでいれば、trueを返す。
	 * 
	 * @param rangeList
	 * @param columnIndex
	 * @return
	 */
	public static boolean containsColumn(ArrayList<IRange> rangeList, int columnIndex) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange r = rangeList.get(i); 
			if(r.containsColumn(columnIndex)) {
				return true;
			}
		}
		return false;
	}

	public static void insertRow(ArrayList<IRange> rangeList, int row, int length) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange r = rangeList.get(i); 
			if((row+length-1) < r.getTop()) {
				r.setTop(r.getTop()+length);
				r.setBottom(r.getBottom()+length);
			}
			else if(r.containsRow(row)) {
				r.setBottom(r.getBottom() + length);
			}
		}
	}

	public static void insertColumn(ArrayList<IRange> rangeList, int col, int length) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange r = rangeList.get(i); 
			if((col+length-1) < r.getLeft()) {
				r.setLeft(r.getLeft()+length);
				r.setRight(r.getRight()+length);
			}
			else if(r.containsColumn(col)) {
				r.setRight(r.getRight() + length);
			}
		}
	}

	public static void removeRow(ArrayList<IRange> rangeList, int row, int length) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange r = rangeList.get(i);
			if((row+length-1) < r.getTop()) {
				r.setTop(r.getTop()-length);
				r.setBottom(r.getBottom()-length);
			}
			else {
				int s = Math.max(row, r.getTop());
				int e = Math.min(row + length - 1, r.getBottom());
				if(s < e) {
					r.setBottom(r.getBottom() - (e - s));
				}
			}
		}
	}

	public static void removeColumn(ArrayList<IRange> rangeList, int col, int length) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange r = rangeList.get(i);
			if((col+length-1) < r.getLeft()) {
				r.setLeft(r.getLeft()-length);
				r.setRight(r.getRight()-length);
			}
			else {
				int s = Math.max(col, r.getLeft());
				int e = Math.min(col + length - 1, r.getRight());
				if(s < e) {
					r.setRight(r.getRight() - (e - s));
				}
			}
		}
	}

	public static void tableChanged(ArrayList<IRange> list, TableModelEvent e) {
		if(e.getFirstRow() == 0 && e.getLastRow() == Integer.MAX_VALUE && e.getColumn() == TableModelEvent.ALL_COLUMNS) {
			list.clear();
		}
		else {
			switch(e.getType()) {
			case TableModelEvent.INSERT:
				if(e.getColumn() == TableModelEvent.ALL_COLUMNS) {
					RangeUtil.insertRow(list, e.getFirstRow(), e.getLastRow() - e.getFirstRow() + 1);
				}
				else {
					RangeUtil.insertColumn(list, e.getColumn(), 1);
				}
				break;
			case TableModelEvent.DELETE:
				if(e.getColumn() == TableModelEvent.ALL_COLUMNS) {
					RangeUtil.removeRow(list, e.getFirstRow(), e.getLastRow() - e.getFirstRow() + 1);
				}
				else {
					RangeUtil.removeColumn(list, e.getColumn(), 1);
				}
				break;
			case TableModelEvent.UPDATE:
				break;
			default:
				break;
			}
		}
	}

}
