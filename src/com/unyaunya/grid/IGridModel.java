package com.unyaunya.grid;

import javax.swing.table.TableModel;

import com.unyaunya.grid.format.CellFormatModel;
import com.unyaunya.grid.format.CellSpanModel;

/**
 * �O���b�h���f���́A���̂R�̃T�u�E���f�����Ǘ�����B
 * �ETableModel
 * �ECellFormatModel
 * �ECellSpanModel
 * 
 * @author wata
 *
 */
public interface IGridModel {
	//
	//�T�u�E���f��
	//
	public CellFormatModel getCellFormatModel();
	public CellSpanModel getCellSpanModel();
	public TableModel getTableModel();

	//
	//�O���b�h�̈ꕔ���擾���A���삷��C���^�t�F�[�X���擾����
	//

	/**
	 * �Z�����擾����B
	 */
	public ICell getCellAt(int row, int col);

	//
	//TableModel���ɃA�N�Z�X����V���[�g�J�b�g
	//
	
	/**
	 * �Z���͈͂��擾����
	 */
	public Range getRange(int top, int left, int bottom, int right);

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
	//@Override
	public String getColumnName(int column);
	
	/**
	 * �w�肳�ꂽ�s�̃��x�����擾����B
	 * @param row
	 * @return
	 */
	public int getLevel(int row);

	/**
	 * �w�b�_�Ƃ��Ĉ����s�̐���Ԃ��B
	 * @return
	 */
	public int getHeaderRowCount();

	//
	//�s�E��̑���
	//
	public void insertRow(int row);
	public void insertColumn(int column);
	public void removeRow(int row);
}
