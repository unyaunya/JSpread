package com.unyaunya.swing.application;

import java.io.File;

public interface IDocument {
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
	 * 指定したファイルに保存する。
	 * TODO: streamを引数にしたバージョンを作成する。
	 * @param file
	 */
	//public void save(File file);

}
