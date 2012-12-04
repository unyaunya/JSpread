package com.unyaunya.grid.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import com.unyaunya.grid.table.IEditableTableModel;

@SuppressWarnings("serial")
public class CsvTable extends DefaultTableModel implements IEditableTableModel {
	public CsvTable(List<String[]> data) {
		setCsvData(data);
	}

	private void setCsvData(List<String[]> data) {
		int colNum = 0;
		Vector<Vector<String>> rows = new Vector<Vector<String>>();
		Vector<String> colIds = new Vector<String>();
		for(int i = 0; i < data.size(); i++) {
			String[] rowData = data.get(i);
			if(colNum < rowData.length) {
				colNum = rowData.length;
			}
			Vector<String> row = new Vector<String>();
			for(int j = 0; j < rowData.length; j++) {
				row.add(rowData[j]);
			}
			rows.add(row);
		}
		for(int i = 0; i < colNum; i++) {
			colIds.add(Integer.valueOf(i).toString());
		}
		setDataVector(rows, colIds);
	}

	public List<String[]> getData() {
		List<String[]> data = new ArrayList<String[]>();
		for(int i = 0; i < this.getRowCount(); i++) {
			String[] row = new String[this.getColumnCount()];
			for(int j = 0; j < this.getColumnCount(); j++) {
				Object value = this.getValueAt(i, j);
				if(value != null) {
					row[j] = value.toString();
				}
				else {
					row[j] = null;
				}
			}
			data.add(row);
		}
		return data;
	}

	@Override
	public void insertColumn(int row, Object[] columnData) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void removeColumn(int column) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}
