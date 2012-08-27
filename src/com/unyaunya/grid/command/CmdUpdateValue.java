package com.unyaunya.grid.command;

import com.unyaunya.grid.IGridModel;

public class CmdUpdateValue implements ICommand {
	private Object currentValue;
	private Object newValue;
	private int row;
	private int column;
	
	public CmdUpdateValue(Object currentValue, Object newValue, int row, int column) {
		this.currentValue = currentValue;
		this.newValue = newValue;
		this.row = row;
		this.column = column;
	}
	
	@Override
	public void execute(IGridModel model) {
		model.setValueAt(newValue, row, column);
	}

	/**
	 * コマンドを取り消す。
	 */
	public void undo(IGridModel model) {
		model.setValueAt(currentValue, row, column);
	}
}
