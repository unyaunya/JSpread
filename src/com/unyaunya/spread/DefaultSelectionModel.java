package com.unyaunya.spread;

import java.util.logging.Logger;

public class DefaultSelectionModel implements ISpreadSelectionModel {
    private static final Logger LOG = Logger.getLogger(DefaultSelectionModel.class.getName());
	
	private RangeDescriptor selectedRangeList = new RangeDescriptor();
	private CellPosition anchorCell = new CellPosition();
	private CellPosition leadCell = new CellPosition();
	private CellRange currentRange = new CellRange();
	
	public DefaultSelectionModel() {
		reset();
	}

	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	public void reset() {
		selectedRangeList.clear();
		selectedRangeList.add(currentRange);
		select(1,1,true);
	}

	private void setAnchorCell(int row, int column) {
		anchorCell.set(row, column);
		currentRange.set(leadCell, anchorCell);
	}

	/**
	 * �w�肵���Z�������[�h�Z���ɂ���B�A���J�[�Z���͈ړ����Ȃ��B
	 */
	public void setLeadCell(int row, int column) {
		LOG.info("setLeadCell("+row+","+column+")");
		leadCell.set(row, column);
		currentRange.set(leadCell, anchorCell);
	}

	/**
	 * �w�肵���Z����I������B(���[�h�Z���A�A���J�[�Z���͓����Z�����w���B)
	 * (SHIFT�L�[���������ɃN���b�N�A�J�[�\���ړ��������̓���)
	 * 
	 * clear�t���O��true�Ȃ�΁A�����ɂ��̑��̑I�����N���A����B
	 * (CTRL�L�[�������ăN���b�N�������̓���)
	 */
	public void select(int row, int column, boolean clear) {
		LOG.info("select("+row+","+column+","+clear+")");
		if(clear) {
			selectedRangeList.clear();
		}
		currentRange = new CellRange();
		selectedRangeList.getSelectedRangeList().add(currentRange);
		setLeadCell(row, column);
		setAnchorCell(row, column);
	}

	/**
	 * �S�Z����I������B
	 */
	@Override
	public void selectAll() {
		reset();
		setAnchorCell(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * �w�肵���s�S�̂�I������B
	 */
	@Override
	public void selectRow(int row, boolean clear) {
		select(row, Integer.MAX_VALUE, clear);
		setLeadCell(row, 1);
	}

	/**
	 * �w�肵����S�̂�I������B
	 */
	@Override
	public void selectColumn(int column, boolean clear) {
		select(Integer.MAX_VALUE, column, clear);
		setLeadCell(1, column);
	}
	
	private CellRange getCurrentRange() {
		return currentRange;
	}
	
	@Override
	public boolean isCellSelected(int rowIndex, int columnIndex) {
		if(rowIndex == 0  && columnIndex == 0) {
			return anchorCell.getRow() == Integer.MAX_VALUE && anchorCell.getColumn() == Integer.MAX_VALUE; 
		}
		return selectedRangeList.contains(rowIndex, columnIndex);
	}

	@Override
	public boolean isRowSelected(int rowIndex) {
		return selectedRangeList.isRowSelected(rowIndex);
	}

	@Override
	public boolean isColumnSelected(int columnIndex) {
		return selectedRangeList.isColumnSelected(columnIndex);
	}

	@Override
	public boolean isLeadCell(int rowIndex, int columnIndex) {
		return (rowIndex == getLeadSelectionRow() && columnIndex == getLeadSelectionColumn());
	}

	@Override
	public CellPosition getLeadCell() {
		return leadCell;
	}

	@Override
	public int getLeadSelectionRow() {
		return leadCell.getRow();
	}

	@Override
	public int getLeadSelectionColumn() {
		return leadCell.getColumn();
	}
}
