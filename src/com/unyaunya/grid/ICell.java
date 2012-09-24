package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.border.Border;

public interface ICell {
	/**
	 * 行番号を取得する
	 * @return
	 */
	public int getRow();

	/**
	 * 行番号を設定する
	 * @param row
	 */
	public void setRow(int row);

	/**
	 * 列番号を取得する
	 * @return
	 */
	public int getColumn();
	
	/**
	 * 列番号を設定する
	 * @param column
	 */
	public void setColumn(int column);

	/**
	 * セルの値を取得する
	 * @return
	 */
	public Object getValue();

	/**
	 * セルの値を設定する
	 */
	public void setValue(Object value);

	/**
	 * セルの値を文字列として取得する
	 * @return
	 */
	public String getText();

	/**
	 * 背景色を取得する。
	 * @return
	 */
	public Color getBackgroundColor();
	
	/**
	 * 前景色を取得する。
	 * @return
	 */
	public Color getForegroundColor();

	/**
	 * ボーダーを取得する。
	 * @return
	 */
	public Border getBorder();

	/**
	 * 水平方向のアライメントを取得する。
	 * @return
	 */
	public int getHorizontalAlignment();

	/**
	 * 垂直方向のアライメントを取得する。
	 * @return
	 */
	public int getVerticalAlignment();

	/**
	 * このセルが属するセル結合範囲を取得する。
	 * セル結合していない場合は、当該セルのみを含んだセル範囲を返す。
	 * 
	 * @return
	 */
	public IRange getRange();
}
