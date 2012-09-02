package com.unyaunya.grid.selection;

import java.util.logging.Logger;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.IGridModel;
import com.unyaunya.spread.ISpreadSelectionModel;
import com.unyaunya.spread.RangeDescriptor;
import com.unyaunya.swing.JGrid;

/**
 * �X�v���b�h�V�[�g�p�̃Z���N�V�������戵�����f��
 * 
 * @author wata
 *
 */
public class DefaultSelectionModel implements ISpreadSelectionModel {
    private static final Logger LOG = Logger.getLogger(DefaultSelectionModel.class.getName());
	
    private JGrid grid;
    private IGridModel gridModel;
    
    /**
     * �I��͈͑S�̂�ێ����郊�X�g�B�����̃Z���͈͂�ێ�����B
     */
    private RangeDescriptor selectedRangeList = new RangeDescriptor();
    /**
     * �}�E�X�A�L�[�{�[�h�ɂ��A���ڑ��삳���Z���͈́B
     * �Z���͈͂̌��E�́A���leadCell��anchorCell���܂ނ悤�ɐݒ肳���B
     */
	private CellRange currentRange = new CellRange();
	/**
	 * currentRange�̊p�ł���AanchorCell�̑Ίp�ƂȂ�Z���ʒu�B
	 * ���̓R���|�[�l���g���z�u�����Z���ʒu�ł�����B 
	 */
	private CellPosition leadCell = new CellPosition();
	/**
	 * currentRange�̊p�ł���AleadCell�̑Ίp�ƂȂ�Z���ʒu
	 */
	private CellPosition tailCell = new CellPosition();
	
	/**
	 * �R���X�g���N�^
	 */
	public DefaultSelectionModel(JGrid grid) {
		this.grid = grid;
		this.gridModel = grid.getGridModel();
		reset();
	}

	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	public void reset() {
		selectedRangeList.clear();
		selectedRangeList.getSelectedRangeList().add(currentRange);
		select(1,1,true);
	}

	/**
	 * �w�肵���Z�����e�[���Z���ɂ���B���[�h�Z���͈ړ����Ȃ��B
	 */
	public void setTailCell(int row, int column) {
		tailCell.set(row, column);
		currentRange.set(leadCell, tailCell);
	}

	/**
	 * �w�肵���Z�������[�h�Z���ɂ���B�e�[���Z���͈ړ����Ȃ��B
	 */
	private void setLeadCell(int row, int column) {
		LOG.info("setLeadCell("+row+","+column+")");
		leadCell.set(row, column);
		currentRange.set(leadCell, tailCell);
	}

	private void addNewRange(boolean clear) {
		if(clear) {
			selectedRangeList.clear();
		}
		currentRange = new CellRange();
		selectedRangeList.getSelectedRangeList().add(currentRange);
	}

	/**
	 * �w�肵���Z����I������B(���[�h�Z���A�e�[���Z���͓����Z�����w���B)
	 * (SHIFT�L�[���������ɃN���b�N�A�J�[�\���ړ��������̓���)
	 * 
	 * clear�t���O��true�Ȃ�΁A�����ɂ��̑��̑I�����N���A����B
	 * (CTRL�L�[�������ăN���b�N�������̓���)
	 */
	public void select(int row, int column, boolean clear) {
		LOG.info("select("+row+","+column+","+clear+")");
		addNewRange(clear);
		if(row == 0 && column == 0) {
			//selectAll();
		}
		else if(row == 0) {
			
		}
		else if(column == 0) {
			
		}
		else {
		}
		setLeadCell(row, column);
		setTailCell(row, column);
	}

	/**
	 * �S�Z����I������B
	 */
	@Override
	public void selectAll() {
		reset();
		setTailCell(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * �w�肵���s�S�̂�I������B
	 */
	/*
	@Override
	public void selectRow(int row, boolean clear) {
		addNewRange(clear);
		setLeadCell(row, 1);
		setTailCell(row, Integer.MAX_VALUE);
	}
	*/

	/**
	 * �w�肵����S�̂�I������B
	 */
	/*
	@Override
	public void selectColumn(int column, boolean clear) {
		addNewRange(clear);
		setLeadCell(1, column);
		setTailCell(Integer.MAX_VALUE, column);
	}
	*/
	
	@Override
	public boolean isSelected(int rowIndex, int columnIndex) {
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
		return (rowIndex == getRowOfLeadCell() && columnIndex == getColumnOfLeadCell());
	}

	@Override
	public int getRowOfLeadCell() {
		return Math.max(1, leadCell.getRow());
	}

	@Override
	public int getColumnOfLeadCell() {
		return Math.max(1, leadCell.getColumn());
	}

	@Override
	public RangeDescriptor getRangeDescriptor() {
		return this.selectedRangeList;
	}

	@Override
	public boolean hasFocus(int row, int col) {
		boolean rslt = false;
		IRange range = gridModel.getCellAt(row, col).getRange();
		if(range == null) {
			rslt = this.isLeadCell(row, col);
		}
		else {
			int rowLeadCell = this.getRowOfLeadCell();
			int colLeadCell = this.getColumnOfLeadCell();
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
	public void focus(int row, int column) {
		setLeadCell(row, column);
		grid.repaint();
	}
}
