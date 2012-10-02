package com.unyaunya.grid.format;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.RangeUtil;

/**
 * セル結合に関するデータを格納するクラス
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class CellSpanModel implements TableModelListener, Serializable {
    @SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(CellSpanModel.class.getName());

    /**
	 * 
	 */
	private ArrayList<IRange> cellSpanList;

	/**
	 * コンストラクタ
	 */
	public CellSpanModel() {
		 cellSpanList = new ArrayList<IRange>();
	}

	public ArrayList<IRange> getCellSpanList() {
		return cellSpanList;
	}
	
	/**
	 * row,colで指定されたセルの結合範囲を取得する。
	 * セル結合していない場合は、nullが返される。
	 */
    public IRange getCellRangeOld(int row, int column) {
		return RangeUtil.getRange(cellSpanList, row, column);
    }

	/**
	 * row,colで指定されたセルの結合範囲を取得する。
	 * セル結合していなくても、そのセルだけを含むIRangeオブジェクトが返される。
	 */
    public IRange getCellRange(int row, int column) {
    	IRange range = RangeUtil.getRange(cellSpanList, row, column); 
    	return (range != null) ? range : new CellRange(row, column); 
    }

	public void add(CellRange range) {
		cellSpanList.add(range);
	}
	
    public void coupleCells(CellRange range) {
		//
		IRange r = this.getCellRangeOld(range.getTop(), range.getLeft());  
		if(r == null) {
			add(range);
		}
		else {
			cellSpanList.remove(r);
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		RangeUtil.tableChanged(cellSpanList, e);
	}
}
