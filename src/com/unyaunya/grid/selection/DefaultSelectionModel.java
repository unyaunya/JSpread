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
     * �I��͈͑S�̂�ێ����郊�X�g�B�����̃Z���͈͂�ێ�����B
     */
    private RangeDescriptor selectedRangeList;
 
    /**
     * �}�E�X�A�L�[�{�[�h�ɂ��A���ڑ��삳���Z���͈́B
     * �Z���͈͂̌��E�́A���leadCell��anchorCell���܂ނ悤�ɐݒ肳���B
     */
	private CellRange currentRange;

	/**
	 * currentRange�̊p�ł���AanchorCell�̑Ίp�ƂȂ�Z���ʒu�B
	 * ���̓R���|�[�l���g���z�u�����Z���ʒu�ł�����B 
	 */
	private CellPosition leadCell;

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
	}

	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	public void reset() {
	    if(selectedRangeList == null) {
	    	selectedRangeList = new RangeDescriptor();
	    	currentRange = new CellRange();
	    	leadCell = new CellPosition();
	    	tailCell = new CellPosition();
	    }
	    else {
			selectedRangeList.clear();
	    }
		selectedRangeList.getSelectedRangeList().add(currentRange);
		select(1,1,true);
	}

	/**
	 * �w�肵���Z�����e�[���Z���ɂ���B���[�h�Z���͈ړ����Ȃ��B
	 */
	private void setTailCell(int row, int column) {
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

	private boolean isLeadCell(int rowIndex, int columnIndex) {
		return (rowIndex == getFocusedRow() && columnIndex == getFocusedColumn());
	}

	private int getRowOfLeadCell() {
		return Math.max(1, leadCell.getRow());
	}

	private int getColumnOfLeadCell() {
		return Math.max(1, leadCell.getColumn());
	}

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
	public void focus(int row, int column) {
		setLeadCell(row, column);
		repaint();
	}

	@Override
	public ArrayList<IRange> getSelectedRangeList() {
		return getRangeDescriptor().getSelectedRangeList();
	}
}
