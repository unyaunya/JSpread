package com.unyaunya.grid;

import javax.swing.table.TableModel;

import com.unyaunya.grid.format.CellFormatModel;

public interface IGridModel extends TableModel {
	//�����͂h�b�������C���^�t�F�[�X�ɂ܂Ƃ߂�B
	/**
	 * �Z�����擾����B
	 */
	public ICell getCellAt(int row, int col);

	/**
	 * �Z���̒l���擾����B
	 * getCellAt(row, col).getValue()�Ɠ����B
	 */
	public Object getValueAt(int row, int col);

	/**
	 * �Z���̒l��ݒ肷��B
	 * ���̏����Ƃقړ�����
	 * {
	 * 		Cell cell = getCellAt(row, col);
	 * 		cell.setvalue(value);
	 * 		setCellAt(cell, row, col);
	 * }
	 */
	public void setValueAt(Object value, int row, int col);

	public CellFormatModel getCellFormatModel();
}
