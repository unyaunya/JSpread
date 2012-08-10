package com.unyaunya.spread;

import java.io.Serializable;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class SpreadSheetModel implements TableModel, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SpreadModel tableModel;
	private CellSpanModel cellSpanModel;
	private CellFormatModel cellFormatModel;
	
	public SpreadSheetModel() {
		setTableModel(new SpreadModel());
	}
	
	public void setTableModel(SpreadModel model) {
		tableModel = model;
		this.cellSpanModel = new CellSpanModel();
		this.cellFormatModel = new CellFormatModel();
	}

	public CellSpanModel getCellSpanModel() {
		return cellSpanModel;
	}

	public CellFormatModel getCellFormatModel() {
		return cellFormatModel;
	}

	public ICellRange getCellRange(int row, int column) {
   		return getCellSpanModel().getCellRange(row, column);
    }
    
	public void coupleCells(CellRange range) {
   		getCellSpanModel().coupleCells(range);
	}
    
	public void insertRow(int row) {
		tableModel.insertRow(row,  (Object[])null);
	}

	public void insertColumn(int column) {
		tableModel.insertColumn(column, (Object[])null);
	}

	public void removeRow(int row) {
		DefaultTableModel m = (DefaultTableModel)tableModel.getTableModel();
		m.removeRow(row);
	}

	/*
	 * implementation of TableModel interface
	 */

	@Override
	public int getColumnCount() {
		return tableModel.getColumnCount();
	}

	@Override
	public int getRowCount() {
		return tableModel.getRowCount();
	}
	
	@Override
	public Object getValueAt(int row, int column) {
		return tableModel.getValueAt(row, column);
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		tableModel.setValueAt(value, row, column);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		tableModel.addTableModelListener(l);
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return tableModel.getColumnClass(col);
	}

	@Override
	public String getColumnName(int col) {
		return tableModel.getColumnName(col);
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return tableModel.isCellEditable(row, col);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		tableModel.removeTableModelListener(l);
	}
}
