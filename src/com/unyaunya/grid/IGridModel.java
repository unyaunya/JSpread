package com.unyaunya.grid;

import javax.swing.table.TableModel;

import com.unyaunya.grid.format.CellFormatModel;

public interface IGridModel extends TableModel {
	public Range getRange(int top, int left, int bottom, int right);

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

	/**
	 * 指定された行のヘッダーに表示する値を取得する
	 * @param row
	 * @return
	 */
	public String getRowName(int row);

	/**
	 * 指定されたカラムのヘッダーに表示する値を取得する
	 * @param row
	 * @return
	 */
	public String getColumnName(int column);
	
	public CellFormatModel getCellFormatModel();
	public CellSpanModel getCellSpanModel();

	public void insertRow(int row);
}
