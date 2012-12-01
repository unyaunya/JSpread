package com.unyaunya.grid;

import java.util.logging.Logger;

import javax.swing.table.TableModel;

import com.unyaunya.grid.format.CellFormatModel;
import com.unyaunya.grid.format.CellSpanModel;
import com.unyaunya.grid.table.IEditableTableModel;

public class GridModel implements IGridModel {
	private static final Logger LOG = Logger.getLogger(GridModel.class.getName());
	
	private TableModel tableModel;
	private CellFormatModel cellFormatModel;
	private CellSpanModel cellSpanModel = new CellSpanModel();

	public GridModel(TableModel tableModel) {
		setTableModel(tableModel);
	}

	public void setTableModel(TableModel model) {
		assert(model != null);
		this.tableModel = model;
		this.cellFormatModel = new CellFormatModel();
		this.cellSpanModel = new CellSpanModel();
		tableModel.addTableModelListener(cellSpanModel);
		tableModel.addTableModelListener(cellFormatModel);
	}

	public TableModel getTableModel() {
		return tableModel;
	}

	protected IEditableTableModel getITableModel() {
		return (tableModel instanceof IEditableTableModel) ? (IEditableTableModel)tableModel : null;
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


	//@Override
	public Class<?> getColumnClass(int columnIndex) {
		return tableModel.getColumnClass(columnIndex);
	}

	//@Override
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

	//@Override
	public int getRowCount() {
		return tableModel.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableModel.getValueAt(rowIndex, columnIndex);
	}

	//@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return tableModel.isCellEditable(rowIndex, columnIndex);
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		tableModel.setValueAt(value, rowIndex, columnIndex);
	}

	/*
	 * セルスパンﾓﾃﾞﾙ関連
	 */
	public IRange getCellRange(int row, int column) {
   		return getCellSpanModel().getCellRangeOld(row, column);
    }
    
	public void coupleCells(CellRange range) {
   		getCellSpanModel().coupleCells(range);
	}
    
	/*
	 * 
	 */
	protected void copyValuesFrom(TableModel model) {
		IEditableTableModel m = getITableModel();
		if(m == null) {
			return;
		}
		int rowCount = model.getRowCount();
		int colCount = model.getColumnCount();
		m.setRowCount(rowCount);
		m.setColumnCount(colCount);
		for(int row = 0; row < rowCount; row++) {
			for(int col = 0; col < colCount; col++) {
				m.setValueAt(model.getValueAt(row, col), row, col);
			}
		}
	}
	
	public void insertRow(int row, Object[] rowData) {
		IEditableTableModel m = getITableModel();
		if(m == null) {
			LOG.info("can't execute insertRow().");
			return;
		}
		m.insertRow(row, rowData);
		//this.fireTableStructureChanged();
	}

	@Override
	public void insertRow(int row) {
		IEditableTableModel m = getITableModel();
		if(m != null) {
			m.insertRow(row, (Object[])null);
		}
	}

	public void insertColumn(int column) {
		IEditableTableModel m = getITableModel();
		if(m != null) {
			m.insertColumn(column, (Object[])null);
		}
	}

	public void removeRow(int row) {
		IEditableTableModel m = getITableModel();
		if(m != null) {
			m.removeRow(row);
		}
	}

	@Override
	public int getLevel(int row) {
		//派生クラスでオーバライドする。
		return 0;
	}

	/**
	 * ヘッダとして扱う行の数を返す。
	 * 派生クラスでオーバライドする。
	 * @return
	 */
	public int getHeaderRowCount() {
		return 0;
	}
}
