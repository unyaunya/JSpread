package com.unyaunya.grid.selection;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.IGridModel;
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
    private static final Logger LOG = Logger.getLogger(DefaultSelectionModel.class.getName());
	
    private IGridModel gridModel;
    
    /**
     * �}�E�X�A�L�[�{�[�h�ɂ��A���ڑ��삳���Z���͈́B
     * �Z���͈͂̌��E�́A���leadCell��anchorCell���܂ނ悤�ɐݒ肳���B
     */
	private CellRange currentRange;

	/**
	 * currentRange�̊p�ł���AanchorCell�̑Ίp�ƂȂ�Z���ʒu�B
	 * ���̓R���|�[�l���g���z�u�����Z���ʒu�ł�����B 
	 */
	private CellPosition focusCell;

	/**
	 * currentRange�̊p�ł���AleadCell�̑Ίp�ƂȂ�Z���ʒu
	 */
	private CellPosition tailCell;
	
	/**
	 * �R���X�g���N�^
	 */
	public DefaultSelectionModel(JGrid grid) {
		super(grid);
		this.gridModel = getGrid().getGridModel();
		this.currentRange = new CellRange();
		this.focusCell = new CellPosition();
		this.tailCell = new CellPosition();
		//
		getSelectedRangeList().add(currentRange);
		focus(0, 0);
		setTailCell(0, 0);
	}

	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	@Override
	public void clear(){
		getSelectedRangeList().clear();
		currentRange = new CellRange(0, 0);
		getSelectedRangeList().add(currentRange);
	}

	/**
	 * �w�肵���Z�����e�[���Z���ɂ���B���[�h�Z���͈ړ����Ȃ��B
	 */
	private void setTailCell(int row, int column) {
		tailCell.set(row, column);
		currentRange.set(focusCell, tailCell);
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
			currentRange = new CellRange();
			rangeList.add(currentRange);
			currentRange.set(focusCell, tailCell);
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

	private boolean isLeadCell(int rowIndex, int columnIndex) {
		return (rowIndex == getFocusedRow() && columnIndex == getFocusedColumn());
	}

	private int getRowOfLeadCell() {
		return Math.max(1, focusCell.getRow());
	}

	private int getColumnOfLeadCell() {
		return Math.max(1, focusCell.getColumn());
	}

	@Override
	public boolean hasFocus(int row, int col) {
		boolean rslt = false;
		IRange range = gridModel.getCellAt(row, col).getRange();
		if(range == null) {
			rslt = this.isLeadCell(row, col);
		}
		else {
			int rowLeadCell = this.getFocusedRow();
			int colLeadCell = this.getFocusedColumn();
			if(range.contains(rowLeadCell, colLeadCell)) {
				rslt = true;
			}
		}
		return rslt;
	}

	@Override
	public int getFocusedColumn() {
		return getColumnOfLeadCell();
	}

	@Override
	public int getFocusedRow() {
		return getRowOfLeadCell();
	}

	@Override
	protected void focus(int row, int column) {
		LOG.info("focus("+row+","+column+")");
		focusCell.set(row, column);
		currentRange.set(focusCell, tailCell);
	}
}
