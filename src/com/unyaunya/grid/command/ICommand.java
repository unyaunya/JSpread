package com.unyaunya.grid.command;

import com.unyaunya.grid.IGridModel;

/**
 * 編集コマンドのマーカーインタフェース
 * 
 * @author wata
 *
 */
public interface ICommand {
	/**
	 * コマンドを実行する。
	 */
	public void execute(IGridModel model);

	/**
	 * コマンドを取り消す。
	 */
	public void undo(IGridModel model);
}
