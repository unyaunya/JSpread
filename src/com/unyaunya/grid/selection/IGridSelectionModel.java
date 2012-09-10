package com.unyaunya.grid.selection;

import java.awt.event.KeyListener;
import java.util.ArrayList;

import com.unyaunya.grid.IRange;

public interface IGridSelectionModel extends KeyListener {
	//
	//�Z���N�V�������̎擾
	//
	public int getFocusedRow();
	public int getFocusedColumn();
	public boolean isSelected(int row, int column);
	public boolean hasFocus(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public ArrayList<IRange> getSelectedRangeList();

	//
	//�Z���N�V��������
	//

	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	public void clear();

	/**
	 * �w�肵���Z����I������B(���[�h�Z���A�e�[���Z���͓����Z�����w���B)
	 * (SHIFT�L�[���������ɃN���b�N�A�J�[�\���ړ��������̓���)
	 * 
	 * clear�t���O��true�Ȃ�΁A�����ɂ��̑��̑I�����N���A����B
	 * (CTRL�L�[�������ăN���b�N�������̓���)
	 */
	public void onFocusMoved(int row, int column);

	//
	//�}�E�X���͂̎�舵��
	//
	public void onMousePressed(int row, int column, boolean shft, boolean ctrl);
	public void onMouseDragged(int row, int column, boolean shft, boolean ctrl);

	//
	//�L�[�{�[�h���͂̎�舵��
	//
	public void onKeyLeft();
	public void onKeyRight();
	public void onKeyUp();
	public void onKeyDown();
	public void onKeyPageLeft();
	public void onKeyPageRight();
	public void onKeyPageUp();
	public void onKeyPageDown();
}
