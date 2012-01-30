package com.unyaunya.spread;

public interface ISpreadSelectionModel {
	public boolean isCellSelected(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public void selectCell(int row, int column);
	public void clearSelection();
	public Range getLeadCell();
	public int getLeadSelectionRow();
	public int getLeadSelectionColumn();
	public boolean isLeadCell(int rowIndex, int columnIndex);
}
