package com.unyaunya.grid.selection;

import java.util.ArrayList;

import com.unyaunya.grid.IRange;
import com.unyaunya.swing.JGrid;

/**
 *�@�P��Z���I���݂̂��T�|�[�g����A�Z���N�V�����N���X
 * @author wata
 *
 */
public class SingleCellSelectionModel extends AbstractSelectionModel {
	private int selectedRow = -1;
	private int selectedColumn = -1;
	
	public SingleCellSelectionModel(JGrid grid) {
		super(grid);
	}

	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	@Override
	public void reset() {
		selectedRow = -1;
		selectedColumn = -1;
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
		repaint();
	}

	@Override
	public void select(int row, int column, boolean clear) {
		focus(row, column);
	}

	@Override
	public ArrayList<IRange> getSelectedRangeList() {
		return new ArrayList<IRange>();		
	}
}
