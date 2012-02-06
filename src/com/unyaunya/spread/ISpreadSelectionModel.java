package com.unyaunya.spread;

import java.awt.event.InputEvent;

public interface ISpreadSelectionModel {
	public void clearSelection();
	public void selectCell(int row, int column);
	public void selectCell(int row, int column, InputEvent e);
	public void selectRange(Range range);
	public void selectRange(int top, int left, int bottom, int right);

	public boolean isCellSelected(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public Range getLeadCell();
	public int getLeadSelectionRow();
	public int getLeadSelectionColumn();
	public boolean isLeadCell(int rowIndex, int columnIndex);
}
