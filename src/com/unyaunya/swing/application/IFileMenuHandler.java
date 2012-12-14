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
	 * ドキュメントが格納されているファイルを取得する。
	 * 新規作成後、一度も保存されていない場合は、nullを返す。
	 * @return
	 */
	public File getCurrentFile();
	
	/**
	 * カレントドキュメント(現在、編集対象となっているドキュメント)を取得する。
	 * @return
	 */
	public IDocument getCurrentDocument();

	/**
	 * カレントドキュメントが変更されているかを取得する
	 * @return
	 */
	public boolean isModified();
	/**
	 * カレントドキュメントが変更されているかを設定する
	 * @return
	 */
	public void setModified(boolean isModified);

	/**
	 * ドキュメントを新規作成する。
	 * @return
	 */
	public IDocument createNewDocument();
}
