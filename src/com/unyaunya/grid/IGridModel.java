package com.unyaunya.grid;

import javax.swing.table.TableModel;

import com.unyaunya.grid.format.CellFormatModel;
import com.unyaunya.grid.format.CellSpanModel;

/**
 * グリッドモデルは、次の３つのサブ・モデルを管理する。
 * ・TableModel
 * ・CellFormatModel
 * ・CellSpanModel
 * 
 * @author wata
 *
 */
public interface IGridModel {
	//
	//サブ・モデル
	//
	public CellFormatModel getCellFormatModel();
	public CellSpanModel getCellSpanModel();
	public TableModel getTableModel();

	//
	//グリッドの一部を取得し、操作するインタフェースを取得する
	//

	/**
	 * セルを取得する。
	 */
	public ICell getCellAt(int row, int col);

	//
	//TableModel等にアクセスするショートカット
	//
	
	/**
	 * セル範囲を取得する
	 */
	public Range getRange(int top, int left, int bottom, int right);

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
	//@Override
	public String getColumnName(int column);
	
	/**
	 * 指定された行のレベルを取得する。
	 * @param row
	 * @return
	 */
	public int getLevel(int row);

	/**
	 * ヘッダとして扱う行の数を返す。
	 * @return
	 */
	public int getHeaderRowCount();

	//
	//行・列の操作
	//
	public void insertRow(int row);
	public void insertColumn(int column);
	public void removeRow(int row);
}
