/**
 * 
 */
package com.unyaunya.grid.table;

import javax.swing.table.TableModel;

/**
 * @author wata
 *
 */
public interface IEditableTableModel extends TableModel {
	public void setRowCount(int rowCount);
	public void setColumnCount(int colCount);

	public void insertRow(int row, Object[] rowData);
	public void removeRow(int row);
	public void insertColumn(int row, Object[] columnData);
	public void removeColumn(int column);
}
