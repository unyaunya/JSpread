package com.unyaunya.spread;

import java.io.Serializable;
import java.util.HashMap;

public class CellSpanModel implements Serializable {
	private HashMap<CellPosition, ICellRange> cellRangeModel;

	public CellSpanModel() {
		 cellRangeModel = new HashMap<CellPosition, ICellRange>();
	}

    public ICellRange getCellRange(int row, int column) {
    	if(row <= 0 || column <= 0) {
    		return null;
    	}
    	else {
    		return this.cellRangeModel.get(new CellPosition(row, column));
    	}
    }

	public void coupleCells(CellRange range) {
		ICellRange r = this.getCellRange(range.getTop(), range.getLeft());  
		if(r == null) {
			_coupleCells(range);
		}
		else {
			_decoupleCells(r);
		}
	}

	private void _coupleCells(ICellRange range) {
		CellRange value = new CellRange(range);
		for(int i = range.getTop(); i <= range.getBottom(); i++) {
			for(int j = range.getLeft(); j <= range.getRight(); j++) {
				cellRangeModel.put(new CellPosition(i, j), value);
			}
		}
	}

	private void _decoupleCells(ICellRange range) {
		CellRange value = new CellRange(range);
		for(int i = range.getTop(); i <= range.getBottom(); i++) {
			for(int j = range.getLeft(); j <= range.getRight(); j++) {
				cellRangeModel.remove(new CellPosition(i, j));
			}
		}
	}
}
