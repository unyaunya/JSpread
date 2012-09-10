package com.unyaunya.grid;

import java.awt.Color;

import javax.swing.border.Border;

@SuppressWarnings("serial")
public class Cell extends CellPosition implements ICell {
	private IGridModel model;

	public Cell(IGridModel model, int row, int column) {
		super(row, column);
		this.model = model;
	}

	protected IGridModel getModel() {
		return model;
	}

	@Override
	public String getText() {
		if(getValue() != null) {
			return getValue().toString();
		}
		return null;
	}

	@Override
	public Object getValue() {
		return getModel().getValueAt(getRow(), getColumn());
	}

	/**
	 * �Z���̒l��ݒ肷��
	 */
	@Override
	public void setValue(Object value) {
		getModel().setValueAt(value, getRow(), getColumn());
	}

	@Override
	public IRange getRange() {
   		return getModel().getCellSpanModel().getCellRange(getRow(), getColumn());
	}

	@Override
	public Color getBackgroundColor() {
		return getModel().getCellFormatModel().getBackgroundColor(getRow(), getColumn());
	}

	@Override
	public Color getForegroundColor() {
		return getModel().getCellFormatModel().getForegroundColor(getRow(), getColumn());
	}

	/**
	 * �{�[�_�[���擾����B
	 * @return
	 */
	@Override
	public Border getBorder() {
		return getModel().getCellFormatModel().getBorder(getRow(), getColumn());
	}

	/**
	 * ���������̃A���C�����g���擾����B
	 * @return
	 */
	@Override
	public int getHorizontalAlignment() {
		return getModel().getCellFormatModel().getHorizontalAlignment(getRow(), getColumn());
	}

	/**
	 * ���������̃A���C�����g���擾����B
	 * @return
	 */
	@Override
	public int getVerticalAlignment() {
		return getModel().getCellFormatModel().getVerticalAlignment(getRow(), getColumn());
	}
}
