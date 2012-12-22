package com.unyaunya.swing.application;

import java.io.File;
import java.io.IOException;

public interface IDocumentHandler {
	/**
	 * 指定したファイルを読み込んでドキュメントを作成する
	 * @param file
	 * @return
	 */
	public IDocument load(File file) throws IOException;

	/**
	 * 指定したドキュメントをファイルに保存する
	 * @param file
	 * @return
	 */
	public void save(IDocument document, File file) throws IOException;

}
