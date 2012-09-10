package com.unyaunya.grid.selection;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.IGridModel;
import com.unyaunya.swing.JGrid;

/**
 * スプレッドシート用のセレクションを取扱うモデル
 *
 * SHIFTキーが押された状態のクリック、カーソル移動は、現在の選択範囲を変更する。
 * CTRLキーが押されていない場合は、選択範囲リストをクリアした上でに新たな範囲を追加する。
 * CTRLキーが押下されていなければ、選択範囲リストをクリアせず新たな範囲を追加する。
 * 
 * @author wata
 *
 */
public class DefaultSelectionModel extends AbstractSelectionModel {
    private static final Logger LOG = Logger.getLogger(DefaultSelectionModel.class.getName());
	
    private IGridModel gridModel;
    
    /**
     * マウス、キーボードにより、直接操作されるセル範囲。
     * セル範囲の限界は、常にleadCellとanchorCellを含むように設定される。
     */
	private CellRange currentRange;

	/**
	 * currentRangeの角であり、anchorCellの対角となるセル位置。
	 * 入力コンポーネントが配置されるセル位置でもある。 
	 */
	private CellPosition focusCell;

	/**
	 * currentRangeの角であり、leadCellの対角となるセル位置
	 */
	private CellPosition tailCell;
	
	/**
	 * コンストラクタ
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
	 * 選択をリセットして、デフォルト状態に戻す。
	 */
	@Override
	public void clear(){
		getSelectedRangeList().clear();
		currentRange = new CellRange(0, 0);
		getSelectedRangeList().add(currentRange);
	}

	/**
	 * 指定したセルをテールセルにする。リードセルは移動しない。
	 */
	private void setTailCell(int row, int column) {
		tailCell.set(row, column);
		currentRange.set(focusCell, tailCell);
	}

	/**
	 * フォーカスセルが移動した場合の処理を行う。
	 * 
	 * ・SHIFTキーが押下されている場合、何もしない。
	 * 		Tailセルがそのままで、フォーカスセル（＝Leadセル）の位置が変更になるので、
	 * 		選択範囲が変化する。
	 * ・SHIFTキーが押下されていない場合、
	 * 		(1)CTRLキーが押下されていれば、カレント以外の選択範囲リストをクリアする。、
	 * 		(2)Tailセルを、フォーカスセルの位置に移動する。
	 */
	public void onFocusMoved(int row, int column) {
		boolean shft = isShiftDown();
		boolean ctrl = isControlDown();
		ArrayList<IRange> rangeList = getSelectedRangeList();

		if(ctrl) {
			//CTRL=ON:選択リストはクリアしない。
			currentRange = new CellRange();
			rangeList.add(currentRange);
			currentRange.set(focusCell, tailCell);
		}
		else {
			//CTRL=OFF:選択リストはクリアする。
			rangeList.clear();
			rangeList.add(currentRange);
		}

		if(shft) {
			//SHIFT=ON:	カレント範囲をフォーカスセルのみに限定しない。
		}
		else {
			//SHIFT=OFF:カレント範囲をフォーカスセルのみに限定する。
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
