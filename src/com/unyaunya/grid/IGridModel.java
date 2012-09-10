package com.unyaunya.grid;

import javax.swing.table.TableModel;

import com.unyaunya.grid.format.CellFormatModel;

public interface IGridModel extends TableModel {
	public Range getRange(int top, int left, int bottom, int right);

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

	/**
	 * �w�肳�ꂽ�s�̃w�b�_�[�ɕ\������l���擾����
	 * @param row
	 * @return
	 */
	public String getRowName(int row);

	/**
	 * �w�肳�ꂽ�J�����̃w�b�_�[�ɕ\������l���擾����
	 * @param row
	 * @return
	 */
	public String getColumnName(int column);
	
	public CellFormatModel getCellFormatModel();
	public CellSpanModel getCellSpanModel();

	public void insertRow(int row);
}
