package com.unyaunya.grid;

import java.io.Serializable;
import java.util.HashMap;




/**
 * セル結合に関するデータを格納するクラス
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class CellSpanModel implements Serializable {
	/**
	 * 
	 */
	private HashMap<CellPosition, IRange> cellRangeModel;

	/**
	 * コンストラクタ
	 */
	public CellSpanModel() {
		 cellRangeModel = new HashMap<CellPosition, IRange>();
	}

	/**
	 * row,colで指定されたセルの結合範囲を取得する。
	 * セル結合していない場合は、nullが返される。
	 */
    public IRange getCellRange(int row, int column) {
    	if(row < 0 || column < 0) {
    		return null;
    	}
    	else {
    		return this.cellRangeModel.get(new CellPosition(row, column));
    	}
    }

	public void coupleCells(CellRange range) {
		IRange r = this.getCellRange(range.getTop(), range.getLeft());  
		if(r == null) {
			_coupleCells(range);
		}
		else {
			_decoupleCells(r);
		}
	}

	private void _coupleCells(IRange range) {
		CellRange value = new CellRange(range);
		for(int i = range.getTop(); i <= range.getBottom(); i++) {
			for(int j = range.getLeft(); j <= range.getRight(); j++) {
				cellRangeModel.put(new CellPosition(i, j), value);
			}
		}
	}

	private void _decoupleCells(IRange range) {
		//CellRange value = new CellRange(range);
		for(int i = range.getTop(); i <= range.getBottom(); i++) {
			for(int j = range.getLeft(); j <= range.getRight(); j++) {
				cellRangeModel.remove(new CellPosition(i, j));
			}
		}
	}
}
