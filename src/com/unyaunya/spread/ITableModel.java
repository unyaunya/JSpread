/**
 * 
 */
package com.unyaunya.spread;

import javax.swing.table.TableModel;

/**
 * @author wata
 *
 */
public interface ITableModel extends TableModel {
	public void insertRow(int row, Object[] rowData);
	public void removeRow(int row);
	public void insertColumn(int row, Object[] columnData);
	public void removeColumn(int column);
}
