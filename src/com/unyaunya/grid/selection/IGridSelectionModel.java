package com.unyaunya.grid.selection;

import java.awt.event.KeyListener;
import java.util.ArrayList;

import com.unyaunya.grid.IRange;

public interface IGridSelectionModel extends KeyListener {
	//
	//セレクション情報の取得
	//
	public int getFocusedRow();
	public int getFocusedColumn();
	public boolean isSelected(int row, int column);
	public boolean hasFocus(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public ArrayList<IRange> getSelectedRangeList();

	//
	//セレクション操作
	//

	/**
	 * 選択をリセットして、デフォルト状態に戻す。
	 */
	public void clear();

	/**
	 * 指定したセルを選択する。(リードセル、テールセルは同じセルを指す。)
	 * (SHIFTキーを押さずにクリック、カーソル移動した時の動作)
	 * 
	 * clearフラグがtrueならば、同時にその他の選択をクリアする。
	 * (CTRLキーを押してクリックした時の動作)
	 */
	public void onFocusMoved(int row, int column);

	//
	//マウス入力の取り扱い
	//
	public void onMousePressed(int row, int column, boolean shft, boolean ctrl);
	public void onMouseDragged(int row, int column, boolean shft, boolean ctrl);

	//
	//キーボード入力の取り扱い
	//
	public void onKeyLeft();
	public void onKeyRight();
	public void onKeyUp();
	public void onKeyDown();
	public void onKeyPageLeft();
	public void onKeyPageRight();
	public void onKeyPageUp();
	public void onKeyPageDown();
}
