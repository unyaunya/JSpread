package com.unyaunya.spread;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.ICellRange;
import com.unyaunya.grid.IGridModel;

public class SpreadSheetModel implements IGridModel, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SpreadModel tableModel;
	private CellSpanModel cellSpanModel;
	private CellFormatModel cellFormatModel;
	
	public SpreadSheetModel() {
		_setTableModel(null);
	}
	
	private void _setTableModel(TableModel model) {
		tableModel = new SpreadModel(model);
		this.cellSpanModel = new CellSpanModel();
		this.cellFormatModel = new CellFormatModel();
	}

	public void setTableModel(TableModel model) {
		tableModel.copyValuesFrom(model);
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
		tableModel.removeRow(row);
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

	
    /**
     * セルのフォーマットを取得する。
     * @param row
     * @param column
     * @return セルのフォーマット。設定されていない場合はnull
     */
    private CellFormat getCellFormat(int row, int column) {
        return this.getCellFormatModel().get(row, column);
    }


	@Override
	public Color getBackgroundColor(int row, int col) {
		CellFormat format = this.getCellFormat(row,  col);
		Color color = null; 
		if(format != null) {
			color = format.getBackgroundColor();
		}
		if(color == null) {
    		color = Color.WHITE; 
		}
		return color;
	}

	@Override
	public Border getBorder(int row, int col) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getForegroundColor(int row, int col) {
		CellFormat format = this.getCellFormat(row,  col);
		Color color = null; 
		if(format != null) {
			color = format.getForegroundColor();
		}
		if(color == null) {
    		color = Color.BLACK; 
		}
		return color;
	}
	
	@Override
	public int getHorizontalAlignment(int row, int col) {
    	if(row <= 0 || col <= 0) {
    		return SwingConstants.CENTER;
    	}
    	else {
    		return SwingConstants.LEFT;
    	}
	}
}
