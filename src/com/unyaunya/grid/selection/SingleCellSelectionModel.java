package com.unyaunya.grid.selection;

import com.unyaunya.swing.JGrid;

/**
 *�@�P��Z���I���݂̂��T�|�[�g����A�Z���N�V�����N���X
 * @author wata
 *
 */
public class SingleCellSelectionModel extends AbstractSelectionModel {
	private int focusedRow = -1;
	private int focusedColumn = -1;
	
	public SingleCellSelectionModel(JGrid grid) {
		super(grid);
	}

	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	@Override
	public void clear() {
		focusedRow = -1;
		focusedColumn = -1;
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
		return (focusedColumn == column);
	}

	@Override
	public boolean isRowSelected(int row) {
		return (focusedRow == row);
	}

	@Override
	public int getFocusedColumn() {
		return focusedColumn;
	}

	@Override
	public int getFocusedRow() {
		return focusedRow;
	}

	@Override
	protected void focus(int row, int column) {
		focusedRow = row;
		focusedColumn = column;
	}
}
