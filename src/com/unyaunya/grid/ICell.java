package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.border.Border;

public interface ICell extends IRangeable {
	public int getRow();
	public int getColumn();
	public Object getValue();
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
}
