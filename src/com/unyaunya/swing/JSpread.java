/**
 * 
 */
package com.unyaunya.swing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.unyaunya.grid.GridModel;
import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.config.Config;
import com.unyaunya.grid.format.CellFormatModel;
import com.unyaunya.grid.format.RangedColor;
import com.unyaunya.grid.selection.DefaultSelectionModel;
import com.unyaunya.grid.selection.IGridSelectionModel;
import com.unyaunya.grid.table.GridTableModel;

/**
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class JSpread extends JGrid  {
    private static final Logger LOG = Logger.getLogger(JSpread.class.getName());
    
	private Config config;

	/**
	 * constructor
	 */
	public JSpread() {
		this(new Config());
	}

	/**
	 * constructor
	 */
	public JSpread(Config config) {
		super(new GridModel(new GridTableModel()));
		this.config = config;
	}

	/**
	 * セレクションモデルを作成する。
	 * セレクションモデルの機能拡張を行う場合、派生クラスでこのメソッドをオーバライドし、
	 * セレクションモデルの派生クラスのインスタンスを返すようにする。
	 * 
	 * @return
	 */
	protected IGridSelectionModel createSelectionModel() {
		return new DefaultSelectionModel(this);
	}
	
	public Config getConfig() {
		return config;
	}

	//
	// Operation of the SpreadSheet
	//
	
	/**
	 * 選択されている行の上に、新たな行を挿入する。
	 */
	public int insertRow() {
		int newRow = getGridSelectionModel().getFocusedRow()-1;
		insertRow(newRow, true);
		return newRow;
	}

	public void insertRow(int row) {
		insertRow(row, true);
	}
	
	public void insertRow(int row, boolean paint) {
		if(!getConfig().isRowInsertionSuppoorted()) {
			throw new UnsupportedOperationException();
		}
		getGridModel().insertRow(row);
		if(paint) {
			repaint();
		}
	}

	public void insertColumn() {
		insertColumn(getGridSelectionModel().getFocusedColumn()-1, true);
	}

	public void insertColumn(int row) {
		insertColumn(row, true);
	}

	public void insertColumn(int col, boolean paint) {
		if(!getConfig().isRowInsertionSuppoorted()) {
			throw new UnsupportedOperationException();
		}
		getGridModel().insertColumn(col);
		if(paint) {
			repaint();
		}
	}

	public void removeRow() {
		removeRow(getGridSelectionModel().getFocusedRow()-1, true);
	}
	public void removeRow(int row) {
		removeRow(row, true);
	}

	public void removeRow(int row, boolean paint) {
		if(!getConfig().isRowInsertionSuppoorted()) {
			throw new UnsupportedOperationException();
		}
		getGridModel().removeRow(row);
		if(paint) {
			repaint();
		}
	}
	
	public void coupleCells(ArrayList<IRange> rangeList) {
		if(rangeList.size() > 1) {
			return;
		}
		CellRange range = new CellRange(rangeList.get(0));
		getGridModel().getCellSpanModel().coupleCells(range);
		repaint();
		LOG.info(range.toString());
	}
	
	public void coupleCells() {
		coupleCells(this.getGridSelectionModel().getSelectedRangeList());
	}

    /**
     * 
     * @param color
     */
    public void setCellBackground(Color newColor) {
    	IGridSelectionModel sm = getGridSelectionModel(); 
		ArrayList<IRange> al = sm.getSelectedRangeList();
		for(int i = 0; i < al.size(); i++) {
			IRange r = al.get(i);
			setCellBackground(newColor, r);
		}
    }

    /**
     * 
     * @param color
     */
    public void setCellForeground(Color newColor) {
    	IGridSelectionModel sm = getGridSelectionModel(); 
		ArrayList<IRange> al = sm.getSelectedRangeList();
		for(int i = 0; i < al.size(); i++) {
			IRange r = al.get(i);
			setCellForeground(newColor, r);
		}
    }
    
    public void setCellForeground(Color  color, IRange range) {
    	CellFormatModel m = getGridModel().getCellFormatModel();
    	m.addForegroundColor(new RangedColor(color, range));
    }
    
    public void setCellBackground(Color  color, IRange range) {
    	CellFormatModel m = getGridModel().getCellFormatModel();
    	m.addBackgroundColor(new RangedColor(color, range));
    }
}
