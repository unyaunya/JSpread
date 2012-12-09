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
	 * 行数または列数を取得する
	 * @return
	 */
	public int getCount() {
		return this.rangeModel.getCount();
	}

	/**
	 * 固定部分の行数または列数を取得する
	 * @return
	 */
	public int getCountOfFixedPart() {
		return this.rangeModel.getFixedPartNum();
	}

	/**
	 * TODO:クリッピング領域の考慮がなされていない。
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
	 * 指定した行の上端または列の左端の座標を返す。
	 * @param index
	 * @return
	 */
	public int getPosition(int index) {
		return rangeModel.getPosition(index);
	}

	public int getLevel(int index) {
		return rangeModel.getLevel(index);
	}

	public boolean levelDown(int start, int length) {
		return rangeModel.levelDown(start, length);
	}
	
	public boolean levelUp(int start, int length) {
		return rangeModel.levelUp(start, length);
	}

	public void collapse(int index) {
		rangeModel.collapse(index);
	}

	public void expand(int index) {
		rangeModel.expand(index);
	}

	protected void repaint() {
		grid.repaint();
	}
}
