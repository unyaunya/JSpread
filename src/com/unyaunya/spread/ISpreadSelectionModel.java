package com.unyaunya.spread;

/**
 * SHIFTキーが押された状態のクリック、カーソル移動は、現在の選択範囲を変更する。
 * CTRLキーが押されていない場合は、選択範囲リストをクリアした上でに新たな範囲を追加する。
 * CTRLキーが押下されていなければ、選択範囲リストをクリアせず新たな範囲を追加する。
 */
public interface ISpreadSelectionModel {
	/**
	 * 選択をリセットして、デフォルト状態に戻す。
	 */
	public void reset();

	/**
	 * 指定したセルを選択する。(リードセル、アンカーセルは同じセルを指す。)
	 * (SHIFTキーを押さずにクリック、カーソル移動した時の動作)
	 * 
	 * clearフラグがtrueならば、同時にその他の選択をクリアする。
	 * (CTRLキーを押してクリックした時の動作)
	 */
	public void select(int row, int column, boolean clear);

	/**
	 * 指定したセルをリードセルにする。アンカーセルは移動しない。
	 */
	public void setLeadCell(int row, int column);

	
	public boolean isCellSelected(int row, int column);
	public boolean isRowSelected(int row);
	public boolean isColumnSelected(int column);
	public CellPosition getLeadCell();
	public int getLeadSelectionRow();
	public int getLeadSelectionColumn();
	public boolean isLeadCell(int rowIndex, int columnIndex);
}
