package com.unyaunya.grid;

import com.unyaunya.swing.JGrid;

class RowOrColumn {
	transient private JGrid grid;
	transient protected ScrollModel scrollModel;
	transient protected ScrollRangeModel rangeModel;	

	public RowOrColumn(JGrid grid, ScrollRangeModel rangeModel) {
		this.grid = grid;
		this.scrollModel = grid.getScrollModel();
		this.rangeModel = rangeModel;
	}

	public void setHidden(int index, boolean bHidden ) {
		this.rangeModel.setHidden(index, bHidden);
		repaint();
	}

	public boolean isHidden(int index) {
		return this.rangeModel.isHidden(index);
	}

	protected void setDefaultSize(int size) {
		rangeModel.setDefaultSize(size);
		repaint();
	}

	protected void setSize(int index, int size) {
		rangeModel.setSize(index, size);
		repaint();
	}

	/**
	 * �s���܂��͗񐔂��擾����
	 * @return
	 */
	public int getCount() {
		return this.rangeModel.getCount();
	}

	/**
	 * �Œ蕔���̍s���܂��͗񐔂��擾����
	 * @return
	 */
	public int getCountOfFixedPart() {
		return this.rangeModel.getFixedPartNum();
	}

	/**
	 * TODO:�N���b�s���O�̈�̍l�����Ȃ���Ă��Ȃ��B
	 * 
	 * @param row
	 * @return
	 */
	public boolean isVisible(int row) {
		int fixedPart = getCountOfFixedPart();
		if(row < fixedPart) {
			return true;
		}
		else if(row < fixedPart + rangeModel.getValue()) {
			return false;
		}
		return true;
	}

	/**
	 * �w�肵���s�̏�[�܂��͗�̍��[�̍��W��Ԃ��B
	 * @param index
	 * @return
	 */
	public int getPosition(int index) {
		return rangeModel.getPosition(index);
	}

	protected void repaint() {
		grid.repaint();
	}
}
