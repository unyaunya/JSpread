package com.unyaunya.grid.selection;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.swing.JGrid;

/**
 * �X�v���b�h�V�[�g�p�̃Z���N�V�������戵�����f��
 *
 * SHIFT�L�[�������ꂽ��Ԃ̃N���b�N�A�J�[�\���ړ��́A���݂̑I��͈͂�ύX����B
 * CTRL�L�[��������Ă��Ȃ��ꍇ�́A�I��͈̓��X�g���N���A������łɐV���Ȕ͈͂�ǉ�����B
 * CTRL�L�[����������Ă��Ȃ���΁A�I��͈̓��X�g���N���A�����V���Ȕ͈͂�ǉ�����B
 * 
 * @author wata
 *
 */
public class DefaultSelectionModel extends AbstractSelectionModel {
    @SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(DefaultSelectionModel.class.getName());
	
    /**
     * �}�E�X�A�L�[�{�[�h�ɂ��A���ڑ��삳���Z���͈́B
     * �Z���͈͂̌��E�́A���leadCell��anchorCell���܂ނ悤�ɐݒ肳���B
     */
	private CellRange currentRange;

	/**
	 * currentRange�̊p�ł���AleadCell�̑Ίp�ƂȂ�Z���ʒu
	 */
	private CellPosition tailCell;
	
	/**
	 * �R���X�g���N�^
	 */
	public DefaultSelectionModel(JGrid grid) {
		super(grid);
		this.tailCell = new CellPosition(getMinimumFocusedRow(), getMinimumFocusedColumn());
		clear();
	}

	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	@Override
	public void clear(){
		getSelectedRangeList().clear();
		pushDownCurrentRange();
		adjustCurrentRange();
	}

	/**
	 * �w�肵���Z�����e�[���Z���ɂ���B���[�h�Z���͈ړ����Ȃ��B
	 */
	private void setTailCell(int row, int column) {
		row = Math.max(getMinimumFocusedRow(), row);
		column = Math.max(getMinimumFocusedColumn(), column);
		tailCell.set(row, column);
		adjustCurrentRange();
	}

	private void pushDownCurrentRange() {
		currentRange = new CellRange(0, 0);
		getSelectedRangeList().add(currentRange);
	}
	
	/**
	 * focusCell, tailCell�̒l�ɉ����āAcurentRange�͈̔͂𒲐�����B
	 */
	private void adjustCurrentRange() {
		int top = Math.min(getFocusedRow(), tailCell.getRow());
		int bottom = Math.max(getFocusedRow(), tailCell.getRow());
		int left = Math.min(getFocusedColumn(), tailCell.getColumn());
		int right = Math.max(getFocusedColumn(), tailCell.getColumn());
		int t = top;
		int b = bottom;
		int l = left;
		int r = right;
		for(int i = top; i <= bottom; i++) {
			for(int j = left; j<= right; j++) {
				IRange range = getGrid().getGridModel().getCellAt(i, j).getRange();
				t = Math.min(t, range.getTop());
				b = Math.max(b, range.getBottom());
				l = Math.min(l, range.getLeft());
				r = Math.max(r, range.getRight());
			}
		}
		currentRange.set(t, l, b, r);
	}
	
	/**
	 * �t�H�[�J�X�Z�����ړ������ꍇ�̏������s���B
	 * 
	 * �ESHIFT�L�[����������Ă���ꍇ�A�������Ȃ��B
	 * 		Tail�Z�������̂܂܂ŁA�t�H�[�J�X�Z���i��Lead�Z���j�̈ʒu���ύX�ɂȂ�̂ŁA
	 * 		�I��͈͂��ω�����B
	 * �ESHIFT�L�[����������Ă��Ȃ��ꍇ�A
	 * 		(1)CTRL�L�[����������Ă���΁A�J�����g�ȊO�̑I��͈̓��X�g���N���A����B�A
	 * 		(2)Tail�Z�����A�t�H�[�J�X�Z���̈ʒu�Ɉړ�����B
	 */
	public void onFocusMoved(int row, int column) {
		boolean shft = isShiftDown();
		boolean ctrl = isControlDown();
		ArrayList<IRange> rangeList = getSelectedRangeList();

		if(ctrl) {
			//CTRL=ON:�I�����X�g�̓N���A���Ȃ��B
			pushDownCurrentRange();
			adjustCurrentRange();
		}
		else {
			//CTRL=OFF:�I�����X�g�̓N���A����B
			rangeList.clear();
			rangeList.add(currentRange);
		}

		if(shft) {
			//SHIFT=ON:	�J�����g�͈͂��t�H�[�J�X�Z���݂̂Ɍ��肵�Ȃ��B
		}
		else {
			//SHIFT=OFF:�J�����g�͈͂��t�H�[�J�X�Z���݂̂Ɍ��肷��B
			setTailCell(row, column);
		}
	}

	@Override
	public void focus(int row, int column) {
		super.focus(row, column);
		adjustCurrentRange();
	}

	@Override
	public void setMinimumFocusedRow(int row) {
		super.setMinimumFocusedRow(row);
		tailCell.setRow(Math.max(tailCell.getRow(), getMinimumFocusedRow()));
	}

	@Override
	public void setMinimumFocusedColumn(int column) {
		super.setMinimumFocusedColumn(column);
		tailCell.setColumn(Math.max(tailCell.getColumn(), getMinimumFocusedColumn()));
	}
	
}
