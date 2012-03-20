package com.unyaunya.spread;

public class SpreadSheetModel {
	private SpreadModel tableModel;
	private ScrollModel scrollModel;
	private CellSpanModel cellSpanModel;
	
	public SpreadSheetModel() {
		this.tableModel = new SpreadModel();
		this.scrollModel = new ScrollModel();
		this.cellSpanModel = new CellSpanModel();
	}
	
	public SpreadModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(SpreadModel model) {
		tableModel = model;
	}

	public ScrollModel getScrollModel() {
		return scrollModel;
	}

	public CellSpanModel getCellSpanModel() {
		return cellSpanModel;
	}

    public ICellRange getCellRange(int row, int column) {
   		return getCellSpanModel().getCellRange(row, column);
    }
    
	public void coupleCells(CellRange range) {
   		getCellSpanModel().coupleCells(range);
	}
    
}
