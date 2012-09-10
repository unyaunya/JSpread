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
@SuppressWarnings("serial")
class SpreadModel extends AbstractTableModel implements ITableModel {
	/**
	 * 
	 */
	private SpreadTableModel tableModel = new SpreadTableModel();
	/**
	 * 
	 */
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
		return tableModel.getColumnCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return tableModel.getRowCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableModel.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		tableModel.setValueAt(aValue, rowIndex, columnIndex);
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
		tableModel.removeColumn(column);
		this.fireTableStructureChanged();
	}

	@Override
	public void removeRow(int row) {
		tableModel.removeRow(row);
		this.fireTableStructureChanged();
	}
}
