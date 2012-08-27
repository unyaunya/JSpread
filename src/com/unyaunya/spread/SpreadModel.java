/**
 * 
 */
package com.unyaunya.spread;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * TableModelをとって、スプレッドシート用の表頭、表側をつけたTableに変換するアダプタ
 * @author wata
 *
 */
class SpreadModel extends AbstractTableModel implements ITableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpreadTableModel tableModel = new SpreadTableModel();
	/**
	 * 
	 */
	public SpreadModel() {}

	public SpreadModel(TableModel newModel) {
		if(newModel == null) {
			newModel = new SpreadTableModel();
		}
		if(newModel instanceof SpreadTableModel) {
			tableModel = (SpreadTableModel)newModel;
		}
		else {
			tableModel = new SpreadTableModel(newModel);
		}
	}

	public void copyValuesFrom(TableModel model) {
		tableModel.copyValuesFrom(model);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return tableModel.getColumnCount()+1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return tableModel.getRowCount()+1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex == 0) {
			if(columnIndex == 0) {
				return "";
			}
			return Integer.valueOf(columnIndex);
		}
		if(columnIndex == 0) {
			return Integer.valueOf(rowIndex);
		}
		return tableModel.getValueAt(rowIndex-1, columnIndex-1);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if(rowIndex == 0 || columnIndex == 0) {
			return;
		}
		tableModel.setValueAt(aValue, rowIndex-1, columnIndex-1);
	}

	@Override
	public void insertRow(int row, Object[] rowData) {
		tableModel.insertRow(row, rowData);
		this.fireTableStructureChanged();
	}

	@Override
	public void insertColumn(int col, Object[] columnData) {
		tableModel.insertColumn(col, columnData);
		this.fireTableStructureChanged();
	}

	@Override
	public void removeColumn(int column) {
		tableModel.removeColumn(column-1);
		this.fireTableStructureChanged();
	}

	@Override
	public void removeRow(int row) {
		tableModel.removeRow(row-1);
		this.fireTableStructureChanged();
	}
}
