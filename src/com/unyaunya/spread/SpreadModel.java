/**
 * 
 */
package com.unyaunya.spread;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @author wata
 *
 */
public class SpreadModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TableModel tableModel = new DefaultTableModel();
	/**
	 * 
	 */
	public SpreadModel() {}

	public SpreadModel(TableModel newModel) {
		if(newModel == null) {
			newModel = new DefaultTableModel();
		}
		tableModel = newModel;
	}

	public TableModel getTableModel() {
		return this.tableModel;
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
}
