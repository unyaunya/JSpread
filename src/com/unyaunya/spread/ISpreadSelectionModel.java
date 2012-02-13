package com.unyaunya.spread;

/**
 * SHIFT�L�[�������ꂽ��Ԃ̃N���b�N�A�J�[�\���ړ��́A���݂̑I��͈͂�ύX����B
 * CTRL�L�[��������Ă��Ȃ��ꍇ�́A�I��͈̓��X�g���N���A������łɐV���Ȕ͈͂�ǉ�����B
 * CTRL�L�[����������Ă��Ȃ���΁A�I��͈̓��X�g���N���A�����V���Ȕ͈͂�ǉ�����B
 */
public interface ISpreadSelectionModel {
	/**
	 * �I�������Z�b�g���āA�f�t�H���g��Ԃɖ߂��B
	 */
	public void reset();

	/**
	 * �w�肵���Z����I������B(���[�h�Z���A�e�[���Z���͓����Z�����w���B)
	 * (SHIFT�L�[���������ɃN���b�N�A�J�[�\���ړ��������̓���)
	 * 
	 * clear�t���O��true�Ȃ�΁A�����ɂ��̑��̑I�����N���A����B
	 * (CTRL�L�[�������ăN���b�N�������̓���)
	 */
	public void select(int row, int column, boolean clear);

	/**
	 * �w�肵���Z�����A���J�[�Z���ɂ���B���[�h�Z���͈ړ����Ȃ��B
	 */
	public void setTailCell(int row, int column);

	/**
	 * �w�肵���Z�������[�h�Z���ɂ���B�e�[���Z���͈ړ����Ȃ��B
	 */
	//public void setLeadCell(int row, int column);

	/**
	 * �S�Z����I������B
	 * �i�\�w�b�_�̍���p�̃Z�����N���b�N�������̓���j
	 */
	public void selectAll();

	/**
	 * �w�肵���s�S�̂�I������B
	 */
	//public void selectRow(int row, boolean clear);

	/**
	 * �w�肵����S�̂�I������B
	 */
	//public void selectColumn(int column, boolean clear);
	
	
	
	public boolean isCellSelected(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public int getRowOfLeadCell();
	public int getColumnOfLeadCell();
	public boolean isLeadCell(int rowIndex, int columnIndex);
}
