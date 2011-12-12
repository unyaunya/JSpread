package com.unyaunya.spread;

import javax.swing.table.AbstractTableModel;

public class CsvTable extends AbstractTableModel {
	private CSVReader csv;
	
	public CsvTable(CSVReader csv) {
		this.csv = csv;
		System.out.println("CSVTable:row=" + getRowCount());
		System.out.println("CSVTable:col=" + getColumnCount());
	}

	@Override
	public int getColumnCount() {
		return csv.getTitleArrayList().size();
	}

	@Override
	public int getRowCount() {
		return csv.getRowArrayList().size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		return csv.getRowArrayList().get(row).get(col);
	}
}
