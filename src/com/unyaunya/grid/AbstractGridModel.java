package com.unyaunya.grid;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.unyaunya.grid.format.CellFormatModel;
import com.unyaunya.grid.CellSpanModel;
import com.unyaunya.spread.ITableModel;

public class AbstractGridModel implements IGridModel {
	private TableModel tableModel;
	private CellFormatModel cellFormatModel;
	private CellSpanModel cellSpanModel = new CellSpanModel();

	public AbstractGridModel(TableModel tableModel) {
		this.tableModel = tableModel;
		this.cellSpanModel = new CellSpanModel();
		this.cellFormatModel = new CellFormatModel();
	}

	public void setTableModel(TableModel model) {
		this.cellFormatModel = new CellFormatModel();
		this.cellSpanModel = new CellSpanModel();
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	protected ITableModel getITableModel() {
		return (tableModel instanceof ITableModel) ? (ITableModel)tableModel : null;
	}
	
	@Override
	public CellFormatModel getCellFormatModel() {
		return cellFormatModel;
	}

	@Override
	public CellSpanModel getCellSpanModel() {
		return cellSpanModel;
	}
	
	/**
	 * 指定された行のヘッダーに表示する値を取得する
	 * @param row
	 * @return
	 */
	public String getRowName(int row) {
		return Integer.valueOf(row+1).toString(); 
	}

	@Override
	public Range getRange(int top, int left, int bottom, int right) {
		return new Range(this, top, left, bottom, right);
	}

	@Override
	public ICell getCellAt(int row, int col) {
		return new Cell(this, row, col);
	}

	/*
	 * implementation of TableModel interface
	 */

	@Override
	public void addTableModelListener(TableModelListener l) {
		tableModel.addTableModelListener(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return tableModel.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return tableModel.getColumnCount();
	}

	/**
	 * 指定されたカラムのヘッダーに表示する値を取得する
	 * @param row
	 * @return
	 */
	@Override
	public String getColumnName(int columnIndex) {
		String name = tableModel.getColumnName(columnIndex);
		if(name == null || name.equals("")) {
			name = Integer.valueOf(columnIndex).toString(); 
		}
		return name; 
	}

	@Override
	public int getRowCount() {
		return tableModel.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableModel.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return tableModel.isCellEditable(rowIndex, columnIndex);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		tableModel.removeTableModelListener(l);
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		tableModel.setValueAt(value, rowIndex, columnIndex);
	}


	/*
	 * セルスパンﾓﾃﾞﾙ関連
	 */
	public IRange getCellRange(int row, int column) {
   		return getCellSpanModel().getCellRange(row, column);
    }
    
	public void coupleCells(CellRange range) {
   		getCellSpanModel().coupleCells(range);
	}
    
	/*
	 * 
	 */
	public void insertRow(int row, Object[] rowData) {
		ITableModel m = getITableModel();
		if(m == null) {
			return;
		}
		m.insertRow(row, rowData);
		//this.fireTableStructureChanged();
	}

	@Override
	public void insertRow(int row) {
		// TODO Auto-generated method stub
		
	}

}
