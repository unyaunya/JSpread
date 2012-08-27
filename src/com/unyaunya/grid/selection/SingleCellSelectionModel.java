package com.unyaunya.grid.selection;

import com.unyaunya.swing.JGrid;

public class SingleCellSelectionModel implements IGridSelectionModel {
	private JGrid grid;
	private int selectedRow = -1;
	private int selectedColumn = -1;
	
	public SingleCellSelectionModel(JGrid grid) {
		this.grid = grid;
	}

	@Override
	public boolean isSelected(int row, int column) {
		return isRowSelected(row) && isColumnSelected(column);
	}

	@Override
	public boolean hasFocus(int row, int column) {
		return isSelected(row, column);
	}

	@Override
	public boolean isColumnSelected(int column) {
		return (selectedColumn == column);
	}

	@Override
	public boolean isRowSelected(int row) {
		return (selectedRow == row);
	}

	@Override
	public int getFocusedColumn() {
		return selectedColumn;
	}

	@Override
	public int getFocusedRow() {
		return selectedRow;
	}

	@Override
	public void focus(int row, int column) {
		selectedRow = row;
		selectedColumn = column;
		grid.repaint();
	}

	@Override
	public void select(int row, int column, boolean clear) {
		focus(row, column);
	}
}
