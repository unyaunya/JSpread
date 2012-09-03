package com.unyaunya.grid.selection;

import com.unyaunya.swing.JGrid;

abstract public class AbstractSelectionModel implements IGridSelectionModel {
	private JGrid grid;
	
	/**
	 * コンストラクタ
	 */
	public AbstractSelectionModel(JGrid grid) {
		assert(grid != null);
		this.grid = grid;
		reset();
	}

	protected JGrid getGrid() {
		return grid;
	}

	protected void repaint() {
		getGrid().repaint();
	}


	public void focus(int rowIndex, int columnIndex, boolean shft, boolean ctrl) {
		int orig_row = getFocusedRow();
		int orig_col = getFocusedColumn();
		int newRowIndex = _rowIndex(rowIndex);
		int newColumnIndex = _columnIndex(columnIndex);
		if(orig_row != newRowIndex || orig_col != newColumnIndex) {
			getGrid().stopEditing();
			getGrid().scrollToVisible(newRowIndex, newColumnIndex);
		}
		if(shft) {
			focus(newRowIndex, newColumnIndex);
		}
		else {
			select(newRowIndex, newColumnIndex, !ctrl);
			repaint();
		}
	}

	private int _rowIndex(int rowIndex) {
		if(rowIndex < 1) {
			rowIndex = 1;
		}
		int count = getGrid().getRows().getCount();
		if(count <= rowIndex) {
			rowIndex = count - 1; 
		}
		return rowIndex;
	}

	private int _columnIndex(int columnIndex) {
		if(columnIndex < 1) {
			columnIndex = 1;
		}
		int count = getGrid().getColumns().getCount();
		if(count <= columnIndex) {
			columnIndex = count - 1; 
		}
		return columnIndex;
	}

	@Override
	public void onMousePressed(int row, int column, boolean shft, boolean ctrl) {
		if(shft) {
			focus(row, column);
		}
		else {
			select(row, column, !ctrl);
			repaint();
		}
	}


}
