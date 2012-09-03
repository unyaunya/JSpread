package com.unyaunya.grid.selection;

import java.util.ArrayList;

import com.unyaunya.grid.IRange;

public interface IGridSelectionModel {
	/**
	 * 選択をリセットして、デフォルト状態に戻す。
	 */
	public void reset();

	public void focus(int rowIndex, int columnIndex, boolean shft, boolean ctrl);

	public void focus(int row, int column);
	
	/**
	 * 指定したセルを選択する。(リードセル、テールセルは同じセルを指す。)
	 * (SHIFTキーを押さずにクリック、カーソル移動した時の動作)
	 * 
	 * clearフラグがtrueならば、同時にその他の選択をクリアする。
	 * (CTRLキーを押してクリックした時の動作)
	 */
	public void select(int row, int column, boolean clear);

	public int getFocusedRow();
	public int getFocusedColumn();
	public boolean isSelected(int row, int column);
	public boolean hasFocus(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public ArrayList<IRange> getSelectedRangeList();

	public void onMousePressed(int row, int column, boolean shft, boolean ctrl);
}
