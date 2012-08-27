package com.unyaunya.grid.selection;

public interface IGridSelectionModel {
	public void focus(int row, int column);
	public void select(int row, int column, boolean clear);

	public int getFocusedRow();
	public int getFocusedColumn();
	public boolean isSelected(int row, int column);
	public boolean hasFocus(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
}
