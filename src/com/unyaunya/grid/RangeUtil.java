package com.unyaunya.grid;

import java.util.ArrayList;

public class RangeUtil {
	public static boolean contains(ArrayList<IRange> rangeList, int rowIndex, int columnIndex) {
		for(int i = 0; i < rangeList.size(); i++) {
			if(rangeList.get(i).contains(rowIndex, columnIndex)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsRow(ArrayList<IRange> rangeList, int rowIndex) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange r = rangeList.get(i); 
			if(r.containsRow(rowIndex)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsColumn(ArrayList<IRange> rangeList, int columnIndex) {
		for(int i = 0; i < rangeList.size(); i++) {
			IRange r = rangeList.get(i); 
			if(r.containsColumn(columnIndex)) {
				return true;
			}
		}
		return false;
	}


}
