package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.border.Border;

public interface ICell {
	/**
	 * �s�ԍ����擾����
	 * @return
	 */
	public int getRow();

	/**
	 * �s�ԍ���ݒ肷��
	 * @param row
	 */
	public void setRow(int row);

	/**
	 * ��ԍ����擾����
	 * @return
	 */
	public int getColumn();
	
	/**
	 * ��ԍ���ݒ肷��
	 * @param column
	 */
	public void setColumn(int column);

	/**
	 * �Z���̒l���擾����
	 * @return
	 */
	public Object getValue();

	/**
	 * �Z���̒l��ݒ肷��
	 */
	public void setValue(Object value);

	/**
	 * �Z���̒l�𕶎���Ƃ��Ď擾����
	 * @return
	 */
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

	/**
	 * ���̃Z����������Z�������͈͂��擾����B
	 * �Z���������Ă��Ȃ��ꍇ�́A���Y�Z���݂̂��܂񂾃Z���͈͂�Ԃ��B
	 * 
	 * @return
	 */
	public IRange getRange();
}
