package com.unyaunya.grid.selection;

import java.util.ArrayList;

import com.unyaunya.grid.IRange;

public interface IGridSelectionModel {
	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	public void reset();

	public void focus(int rowIndex, int columnIndex, boolean shft, boolean ctrl);

	public void focus(int row, int column);
	
	/**
	 * �w�肵���Z����I������B(���[�h�Z���A�e�[���Z���͓����Z�����w���B)
	 * (SHIFT�L�[���������ɃN���b�N�A�J�[�\���ړ��������̓���)
	 * 
	 * clear�t���O��true�Ȃ�΁A�����ɂ��̑��̑I�����N���A����B
	 * (CTRL�L�[�������ăN���b�N�������̓���)
	 */
	public void select(int row, int column, boolean clear);

	public int getFocusedRow();
	public int getFocusedColumn();
	public boolean isSelected(int row, int column);
	public boolean hasFocus(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public ArrayList<IRange> getSelectedRangeList();

	public void onMousePressed(int row, int column, boolean shft, boolean ctrl);
}
