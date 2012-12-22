package com.unyaunya.swing.application;

import java.io.File;
import java.io.IOException;

public interface IDocumentHandler {
	/**
	 * �w�肵���t�@�C����ǂݍ���Ńh�L�������g���쐬����
	 * @param file
	 * @return
	 */
	public IDocument load(File file) throws IOException;

	/**
	 * �w�肵���h�L�������g���t�@�C���ɕۑ�����
	 * @param file
	 * @return
	 */
	public void save(IDocument document, File file) throws IOException;

}
