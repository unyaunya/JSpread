package com.unyaunya.grid.command;

import com.unyaunya.grid.IGridModel;

/**
 * �ҏW�R�}���h�̃}�[�J�[�C���^�t�F�[�X
 * 
 * @author wata
 *
 */
public interface ICommand {
	/**
	 * �R�}���h�����s����B
	 */
	public void execute(IGridModel model);

	/**
	 * �R�}���h���������B
	 */
	public void undo(IGridModel model);
}
