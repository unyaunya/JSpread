package com.unyaunya.grid.selection;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.unyaunya.grid.IRange;

public interface IGridSelectionModel extends KeyListener, TableModelListener {
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
	public void onMousePressed(MouseEvent e);
	public void onMouseDragged(MouseEvent e);

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
	
	//�e�[�u���ύX�ւ̑Ή�
	@Override
	public void tableChanged(TableModelEvent e);
}
