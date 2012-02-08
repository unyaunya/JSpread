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
	 * �w�肵���Z����I������B(���[�h�Z���A�A���J�[�Z���͓����Z�����w���B)
	 * (SHIFT�L�[���������ɃN���b�N�A�J�[�\���ړ��������̓���)
	 * 
	 * clear�t���O��true�Ȃ�΁A�����ɂ��̑��̑I�����N���A����B
	 * (CTRL�L�[�������ăN���b�N�������̓���)
	 */
	public void select(int row, int column, boolean clear);

	/**
	 * �w�肵���Z�������[�h�Z���ɂ���B�A���J�[�Z���͈ړ����Ȃ��B
	 */
	public void setLeadCell(int row, int column);

	
	public boolean isCellSelected(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public CellPosition getLeadCell();
	public int getLeadSelectionRow();
	public int getLeadSelectionColumn();
	public boolean isLeadCell(int rowIndex, int columnIndex);
}
