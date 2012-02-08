package com.unyaunya.spread;

import java.util.ArrayList;
import java.util.logging.Logger;

public class DefaultRangeSelectionModel implements ISpreadSelectionModel {
    private static final Logger LOG = Logger.getLogger(DefaultRangeSelectionModel.class.getName());
	
	private RangeDescriptor selectedRangeList = new RangeDescriptor();
	private CellPosition anchorCell = new CellPosition();
	private CellPosition leadCell = new CellPosition();
	private CellRange currentRange = new CellRange();
	
	public DefaultRangeSelectionModel() {
		reset();
	}

	/**
	 * 選択をリセットして、デフォルト状態に戻す。
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
	 * 指定したセルをリードセルにする。アンカーセルは移動しない。
	 */
	public void setLeadCell(int row, int column) {
		LOG.info("setLeadCell("+row+","+column+")");
		leadCell.set(row, column);
		currentRange.set(leadCell, anchorCell);
	}

	/**
	 * 指定したセルを選択する。(リードセル、アンカーセルは同じセルを指す。)
	 * (SHIFTキーを押さずにクリック、カーソル移動した時の動作)
	 * 
	 * clearフラグがtrueならば、同時にその他の選択をクリアする。
	 * (CTRLキーを押してクリックした時の動作)
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
	
	private CellRange getCurrentRange() {
		return currentRange;
	}
	
	@Override
	public boolean isCellSelected(int rowIndex, int columnIndex) {
		return selectedRangeList.contains(rowIndex, columnIndex);
	}

	@Override
	public boolean isRowSelected(int rowIndex) {
		if(rowIndex < this.getCurrentRange().getTop() || this.getCurrentRange().getBottom() < rowIndex) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isColumnSelected(int columnIndex) {
		if(columnIndex < this.getCurrentRange().getLeft() || this.getCurrentRange().getRight() < columnIndex) {
			return false;
		}
		return true;
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
