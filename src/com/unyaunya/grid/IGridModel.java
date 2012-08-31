package com.unyaunya.grid;

import javax.swing.table.TableModel;

import com.unyaunya.grid.format.CellFormatModel;

public interface IGridModel extends TableModel {
	//書式はＩＣｅｌｌインタフェースにまとめる。
	/**
	 * セルを取得する。
	 */
	public ICell getCellAt(int row, int col);

	/**
	 * セルの値を取得する。
	 * getCellAt(row, col).getValue()と同じ。
	 */
	public Object getValueAt(int row, int col);

	/**
	 * セルの値を設定する。
	 * 次の処理とほぼ等しい
	 * {
	 * 		Cell cell = getCellAt(row, col);
	 * 		cell.setvalue(value);
	 * 		setCellAt(cell, row, col);
	 * }
	 */
	public void setValueAt(Object value, int row, int col);

	public CellFormatModel getCellFormatModel();
}
