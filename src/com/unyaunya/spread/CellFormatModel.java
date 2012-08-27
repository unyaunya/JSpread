package com.unyaunya.spread;

import java.io.Serializable;
import java.util.HashMap;

/**
 * セル書式データを格納するクラス
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class CellFormatModel implements Serializable {
	private HashMap<Integer, HashMap<Integer, CellFormat>> formatData;

	public CellFormatModel() {
		 formatData = new HashMap<Integer, HashMap<Integer, CellFormat>>();
	}

	public void add(int row, int col, CellFormat format) {
		Integer rowKey = Integer.valueOf(row);
		HashMap<Integer, CellFormat> rowData = formatData.get(rowKey);
		if(rowData == null) {
			rowData = new HashMap<Integer, CellFormat>();
		}
		rowData.put(Integer.valueOf(col), format);
		formatData.put(rowKey, rowData);
	}

	public CellFormat get(int row, int col) {
		HashMap<Integer, CellFormat> rowData = formatData.get(Integer.valueOf(row));
		if(rowData == null) {
			return null;
		}
		return rowData.get(Integer.valueOf(col));
	}
}
