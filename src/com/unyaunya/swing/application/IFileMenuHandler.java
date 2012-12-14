package com.unyaunya.swing.application;

import java.io.File;

import javax.swing.Action;
import javax.swing.JFileChooser;

public interface IFileMenuHandler {
	public void onFileNew();
	public void onFileOpen(JFileChooser fc);
	public void onFileSave();
	public void onFileSaveAs(JFileChooser fc);
	public void onFilePrint();
	public void onFilePrintPreview();
	public void onFileExit();
	//
	public Action getOpenAction();
	public Action getSaveAsAction();
	public Action getExitAction();
	
	/**
	 * �h�L�������g���i�[����Ă���t�@�C�����擾����B
	 * �V�K�쐬��A��x���ۑ�����Ă��Ȃ��ꍇ�́Anull��Ԃ��B
	 * @return
	 */
	public File getCurrentFile();
	
	/**
	 * �J�����g�h�L�������g(���݁A�ҏW�ΏۂƂȂ��Ă���h�L�������g)���擾����B
	 * @return
	 */
	public IDocument getCurrentDocument();

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
	 * �h�L�������g��V�K�쐬����B
	 * @return
	 */
	public IDocument createNewDocument();
}
