package com.unyaunya.spread;

import java.util.List;

import javax.swing.table.AbstractTableModel;


public class CsvTable extends AbstractTableModel {
	private List<String[]> csv;
	
	public CsvTable(List<String[]> csv) {
		this.csv = csv;
		System.out.println("CSVTable:row=" + getRowCount());
		System.out.println("CSVTable:col=" + getColumnCount());
	}

	@Override
	public int getColumnCount() {
		if(csv.isEmpty()) {
			return 0;
		}
		return csv.get(0).length;
	}

	@Override
	public int getRowCount() {
		return csv.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		return csv.get(row)[col];
	}
}
