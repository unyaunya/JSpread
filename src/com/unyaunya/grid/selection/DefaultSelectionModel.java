package com.unyaunya.grid.selection;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
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
	
    /**
     * マウス、キーボードにより、直接操作されるセル範囲。
     * セル範囲の限界は、常にleadCellとanchorCellを含むように設定される。
     */
	private CellRange currentRange;

	/**
	 * currentRangeの角であり、leadCellの対角となるセル位置
	 */
	private CellPosition tailCell;
	
	/**
	 * コンストラクタ
	 */
	public DefaultSelectionModel(JGrid grid) {
		super(grid);
		this.tailCell = new CellPosition();
		clear();
	}

	/**
	 * 選択をリセットして、デフォルト状態に戻す。
	 */
	@Override
	public void clear(){
		getSelectedRangeList().clear();
		pushDownCurrentRange();
		adjustCurrentRange();
	}

	/**
	 * 指定したセルをテールセルにする。リードセルは移動しない。
	 */
	private void setTailCell(int row, int column) {
		tailCell.set(row, column);
		adjustCurrentRange();
	}

	private void pushDownCurrentRange() {
		currentRange = new CellRange(0, 0);
		getSelectedRangeList().add(currentRange);
	}
	
	/**
	 * focusCell, tailCellの値に応じて、curentRangeの範囲を調整する。
	 */
	private void adjustCurrentRange() {
		int top = Math.min(getFocusCell().getRow(), tailCell.getRow());
		int bottom = Math.max(getFocusCell().getRow(), tailCell.getRow());
		int left = Math.min(getFocusCell().getColumn(), tailCell.getColumn());
		int right = Math.max(getFocusCell().getColumn(), tailCell.getColumn());
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
			pushDownCurrentRange();
			adjustCurrentRange();
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

	@Override
	protected void focus(int row, int column) {
		LOG.info("focus("+row+","+column+")");
		getFocusCell().set(row, column);
		adjustCurrentRange();
	}
}
