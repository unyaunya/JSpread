package com.unyaunya.grid.selection;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.unyaunya.grid.IRange;

public interface IGridSelectionModel extends KeyListener, TableModelListener {
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
	public void onMousePressed(MouseEvent e);
	public void onMouseDragged(MouseEvent e);

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
	
	//テーブル変更への対応
	@Override
	public void tableChanged(TableModelEvent e);
}
