package com.unyaunya.swing.application;

import java.io.File;

public interface IDocument {
	/**
	 * �J�����g�h�L�������g���ύX����Ă��邩���擾����
	 * @return
	 */
	public boolean isModified();

	/**
	 * �J�����g�h�L�������g���ύX����Ă��邩��ݒ肷��
	 * @return
	 */
	public void setModified(boolean isModified);

	/**
	 * �w�肵���t�@�C���ɕۑ�����B
	 * TODO: stream�������ɂ����o�[�W�������쐬����B
	 * @param file
	 */
	//public void save(File file);

}
