package com.unyaunya.grid;


/**
 * �񑀍���s�����߂̃N���X
 * 
 * �����I�ɂ́AScrollModel�ւ̃v���L�V�N���X�ƂȂ��Ă���B
 * 
 * @author wata
 *
 */
public class Columns extends RowOrColumn {
	transient ScrollModel scrollModel;
	
	public Columns(ScrollModel model) {
		scrollModel = model;
	}
	
	public void setWidth(int columnIndex, int width) {
		scrollModel.setColumnWidth(columnIndex, width);
	}

	/**
	 * �񐔂��擾����
	 * @return
	 */
	public int getCount() {
		return scrollModel.getColumnCount();
	}
}
