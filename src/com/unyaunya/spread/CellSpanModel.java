package com.unyaunya.spread;

import java.io.Serializable;
import java.util.HashMap;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;

/**
 * �Z�������Ɋւ���f�[�^���i�[����N���X
 * 
 * @author wata
 *
 */
public class CellSpanModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<CellPosition, IRange> cellRangeModel;

	/**
	 * �R���X�g���N�^
	 */
	public CellSpanModel() {
		 cellRangeModel = new HashMap<CellPosition, IRange>();
	}

	/**
	 * row,col�Ŏw�肳�ꂽ�Z���̌����͈͂��擾����B
	 * �Z���������Ă��Ȃ��ꍇ�́Anull���Ԃ����B
	 */
    public IRange getCellRange(int row, int column) {
    	if(row <= 0 || column <= 0) {
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
