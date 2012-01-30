package com.unyaunya.spread;

public interface ICellSelectionModel {
	public boolean isCellSelected(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public void selectCell(int row, int column);
	public void clearSelection();
	public Range getLeadCell();
}
