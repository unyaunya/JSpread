package com.unyaunya.grid.table;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


/**
 * スプレッドシート内のテーブルデータを取り扱うコンポーネント
 * （とりあえず、DefaultTableModelをベースにしているが、
 * 大容量データを取り扱う場合には、実装の変更が必要）
 * 
 * 列入替えの機能あり。
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class GridTableModel extends DefaultTableModel implements IEditableTableModel {
	/**
	 * 
	 */
	private Vector<Integer> columnIndexes = new Vector<Integer>();

	/**
	* Creates an empty table with zero rows and zero columns.
	*/
	public GridTableModel() {
		this(256, 256);
	}

	public GridTableModel(int numRows, int numColumns) {
		super(numRows, numColumns);
		_setColumnIndexesSize(numColumns);
	}

	public GridTableModel(TableModel model) {
		super();
		copyValuesFrom(model);
	}
	
	/*
	@Override
	public void setColumnIdentifiers(Vector columnIdentifiers) {
		super.setColumnIdentifiers(columnIdentifiers);
		columnIndexes = new Vector<Integer>();
		int columnCount = this.getColumnCount();
		for(int i = 0; i < columnCount; i++) {
			columnIndexes.add(Integer.valueOf(i));
		}
	}
	*/

	@SuppressWarnings("unchecked")
	@Override
	public void setColumnCount(int columnCount) {
		Vector<Object> newRow = new Vector<Object>();
		newRow.setSize(columnCount);
		for (int i = 0; i < getDataVector().size(); ++i) {
			Vector<Object> vector = (Vector<Object>)getDataVector().get(i);
			Vector<Object> currentRow = vector;
			for (int j = 0; j < columnCount; ++j) {
				newRow.set(j, _getValueFromRow(currentRow, j));
			}
			getDataVector().set(i, newRow);
			newRow = currentRow;
			newRow.setSize(columnCount);
		}
		if (columnIdentifiers != null)  
			columnIdentifiers.setSize(columnCount);
		if(columnIndexes == null) {
			columnIndexes = new Vector<Integer>();
		}
		_setColumnIndexesSize(columnCount);
		fireTableStructureChanged();
	}

	private void _setColumnIndexesSize(int columnCount) {
		columnIndexes.setSize(columnCount);
		for(int i = 0; i < columnCount; i++) {
			columnIndexes.set(i, Integer.valueOf(i));
		}
	}
	
	public Vector<Integer> getColumnIndexes() {
		return columnIndexes;
	}
	
	/*
	public void setColumnIndexes(Vector<Integer> columnIndexes) {
		columIndexes = columnIndexes;
	}
	*/

	public void copyValuesFrom(TableModel model) {
		int rowCount = model.getRowCount();
		int colCount = model.getColumnCount();
		this.setRowCount(rowCount);
		this.setColumnCount(colCount);
		for(int row = 0; row < rowCount; row++) {
			for(int col = 0; col < colCount; col++) {
				this.setValueAt(model.getValueAt(row, col), row, col);
			}
		}
	}

	//@Override
	public void insertColumn(int row, Vector<Object> columnData) {
		insertColumn(row, columnData.toArray());
	}

	@Override
	public void insertColumn(int row, Object[] columnData) {
		columnIndexes.add(row, Integer.valueOf(this.getColumnCount()));
		super.addColumn(super.getColumnName(this.getColumnCount()), columnData);
	}

	@Override
	public void addColumn(Object columnName, Object[] columnData) {
		columnIndexes.add(Integer.valueOf(this.getColumnCount()));
		super.addColumn(columnName, columnData);
	}

	@Override
	public String getColumnName(int column) {
		return super.getColumnName(_columnIndex(column));
	}

	@Override
	public Object getValueAt(int row, int column) {
		return super.getValueAt(row, _columnIndex(column));
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		super.setValueAt(aValue, row, _columnIndex(column));
	}
	
	@Override
	public void removeColumn(int column) {
		columnIndexes.remove(column);
	}

	private Object _getValueFromRow(Vector<Object> row, int column) {
		int col = _columnIndex(column);
		if(col < 0 || col >= row.size()) {
			return null;
		}
		return row.get(col);
	}

	private int _columnIndex(int column) {
		if(column < 0 || column >= this.getColumnCount()) {
			return column;
		}
		Integer n = columnIndexes.get(column);
		assert(n != null);
		return n.intValue();
	}
}
