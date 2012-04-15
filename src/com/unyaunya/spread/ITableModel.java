/**
 * 
 */
package com.unyaunya.spread;

import java.util.Vector;

import javax.swing.table.TableModel;

/**
 * @author wata
 *
 */
public interface ITableModel extends TableModel {
	public void insertRow(int row, Object[] rowData);
	public void insertColumn(int row, Object[] columnData);
	public void removeColumn(int column);
}
