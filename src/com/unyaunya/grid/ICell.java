package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.border.Border;

public interface ICell extends IRangeable {
	public int getRow();
	public int getColumn();
	public Object getValue();
	public String getText();

	/**
	 * �w�i�F���擾����B
	 * @return
	 */
	public Color getBackgroundColor();
	
	/**
	 * �O�i�F���擾����B
	 * @return
	 */
	public Color getForegroundColor();

	/**
	 * �{�[�_�[���擾����B
	 * @return
	 */
	public Border getBorder();

	/**
	 * ���������̃A���C�����g���擾����B
	 * @return
	 */
	public int getHorizontalAlignment();

	/**
	 * ���������̃A���C�����g���擾����B
	 * @return
	 */
	public int getVerticalAlignment();
}
