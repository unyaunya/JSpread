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
 * スプレッドシート用のセレクションを取扱うモデル
 * 
 * @author wata
 *
 */
public class DefaultSelectionModel implements ISpreadSelectionModel {
    private static final Logger LOG = Logger.getLogger(DefaultSelectionModel.class.getName());
	
    private JGrid grid;
    private IGridModel gridModel;
    
    /**
     * 選択範囲全体を保持するリスト。複数のセル範囲を保持する。
     */
    private RangeDescriptor selectedRangeList = new RangeDescriptor();
    /**
     * マウス、キーボードにより、直接操作されるセル範囲。
     * セル範囲の限界は、常にleadCellとanchorCellを含むように設定される。
     */
	private CellRange currentRange = new CellRange();
	/**
	 * currentRangeの角であり、anchorCellの対角となるセル位置。
	 * 入力コンポーネントが配置されるセル位置でもある。 
	 */
	private CellPosition leadCell = new CellPosition();
	/**
	 * currentRangeの角であり、leadCellの対角となるセル位置
	 */
	private CellPosition tailCell = new CellPosition();
	
	/**
	 * コンストラクタ
	 */
	public DefaultSelectionModel(JGrid grid) {
		this.grid = grid;
		this.gridModel = grid.getGridModel();
		reset();
	}

	/**
	 * 選択をリセットして、デフォルト状態に戻す。
	 */
	public void reset() {
		selectedRangeList.clear();
		selectedRangeList.getSelectedRangeList().add(currentRange);
		select(1,1,true);
	}

	/**
	 * 指定したセルをテールセルにする。リードセルは移動しない。
	 */
	public void setTailCell(int row, int column) {
		tailCell.set(row, column);
		currentRange.set(leadCell, tailCell);
	}

	/**
	 * 指定したセルをリードセルにする。テールセルは移動しない。
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
	 * 指定したセルを選択する。(リードセル、テールセルは同じセルを指す。)
	 * (SHIFTキーを押さずにクリック、カーソル移動した時の動作)
	 * 
	 * clearフラグがtrueならば、同時にその他の選択をクリアする。
	 * (CTRLキーを押してクリックした時の動作)
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
	 * 全セルを選択する。
	 */
	@Override
	public void selectAll() {
		reset();
		setTailCell(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * 指定した行全体を選択する。
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
	 * 指定した列全体を選択する。
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
