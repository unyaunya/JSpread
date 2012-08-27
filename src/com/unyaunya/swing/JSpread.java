/**
 * 
 */
package com.unyaunya.swing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.table.TableModel;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.ICellRange;
import com.unyaunya.grid.format.CellFormat;
import com.unyaunya.grid.selection.DefaultSelectionModel;
import com.unyaunya.spread.Config;
import com.unyaunya.spread.ISpreadSelectionModel;
import com.unyaunya.spread.RangeDescriptor;
import com.unyaunya.spread.SpreadSheetModel;
import com.unyaunya.swing.plaf.GridUI;

/**
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class JSpread extends JEditableGrid  {
    private static final Logger LOG = Logger.getLogger(JSpread.class.getName());
    
	private Config config;

	/**
	 * �w�b�_�̈�̍���
	 */
	private int headerHeight = 16;
	
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
		super(new SpreadSheetModel());
		this.config = config;
		this.setGridSelectionModel(new DefaultSelectionModel(this));
		setUI(new GridUI());
	}

	public Config getConfig() {
		return config;
	}

	/**
	 * �w�b�_�̈�̍������擾����
	 */
	public int getHeaderHeight() {
		return headerHeight;
	}
	
    //
    //
    //
	public SpreadSheetModel getSpreadSheetModel() {
		return (SpreadSheetModel)getGridModel();
	}

	public void setTableModel(TableModel model) {
		if(model instanceof SpreadSheetModel) {
			super.setTableModel((SpreadSheetModel)model);
		}
		else {
			throw new RuntimeException("model must be an instance of SpreadSheetModel");
		}
	}

	public void setSpreadSheetModel(SpreadSheetModel model) {
		this.getSelectionModel().reset();
		setTableModel(model);
		//getScrollModel().setTableModel(this.getSpreadSheetModel());
		//this.repaint(this.getBounds());
	}

	public ISpreadSelectionModel getSelectionModel() {
		return (ISpreadSelectionModel)getGridSelectionModel();
	}

	//
	// Operation of the SpreadSheet
	//
	
	/**
	 * �I������Ă���s�̏�ɁA�V���ȍs��}������B
	 */
	public int insertRow() {
		int newRow = getSelectionModel().getRowOfLeadCell()-1;
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
		getSpreadSheetModel().insertRow(row);
		if(paint) {
			repaint();
		}
	}

	public void insertColumn() {
		insertColumn(getSelectionModel().getColumnOfLeadCell()-1, true);
	}

	public void insertColumn(int row) {
		insertColumn(row, true);
	}

	public void insertColumn(int col, boolean paint) {
		if(!getConfig().isRowInsertionSuppoorted()) {
			throw new UnsupportedOperationException();
		}
		getSpreadSheetModel().insertColumn(col);
		if(paint) {
			repaint();
		}
	}

	public void removeRow() {
		removeRow(getSelectionModel().getRowOfLeadCell()-1, true);
	}
	public void removeRow(int row) {
		removeRow(row, true);
	}
	public void removeRow(int row, boolean paint) {
		if(!getConfig().isRowInsertionSuppoorted()) {
			throw new UnsupportedOperationException();
		}
		getSpreadSheetModel().removeRow(row);
		if(paint) {
			repaint();
		}
	}
	
	public void coupleCells(RangeDescriptor desc) {
		if(desc.isMultiRange()) {
			return;
		}
		CellRange range = new CellRange((CellRange)desc.getSelectedRangeList().get(0));
		getSpreadSheetModel().coupleCells(range);
		repaint();
		LOG.info(range.toString());
	}
	
	public void coupleCells() {
		coupleCells(this.getSelectionModel().getRangeDescriptor());
	}

    /**
     * �Z���̃t�H�[�}�b�g���擾����B
     * @param row
     * @param column
     * @return �Z���̃t�H�[�}�b�g�B�ݒ肳��Ă��Ȃ��ꍇ��null
     */
    public CellFormat getCellFormat(int row, int column) {
        return this.getSpreadSheetModel().getCellFormatModel().get(row, column);
    }

    public void setCellFormat(int row, int column, CellFormat value) {
        this.getSpreadSheetModel().getCellFormatModel().add(row, column, value);
    }

    /**
     * 
     * @param color
     */
    public void setCellBackground(Color newColor) {
    	ISpreadSelectionModel sm = getSelectionModel(); 
		ArrayList<ICellRange> al = sm.getRangeDescriptor().getSelectedRangeList();
		for(int i = 0; i < al.size(); i++) {
			ICellRange r = al.get(i);
			for(int row = r.getTop(); row <= r.getBottom(); row++) {
				for(int col = r.getLeft(); col <= r.getRight(); col++) {
			        setCellBackground(row, col, newColor);
				}
			}
		}
    }

    /**
     * 
     * @param color
     */
    public void setCellForeground(Color newColor) {
    	ISpreadSelectionModel sm = getSelectionModel(); 
		ArrayList<ICellRange> al = sm.getRangeDescriptor().getSelectedRangeList();
		for(int i = 0; i < al.size(); i++) {
			ICellRange r = al.get(i);
			for(int row = r.getTop(); row <= r.getBottom(); row++) {
				for(int col = r.getLeft(); col <= r.getRight(); col++) {
			        setCellForeground(row, col, newColor);
				}
			}
		}
    }
    
    public void setCellForeground(int row, int column, Color color) {
		CellFormat format = this.getCellFormat(row,  column);
		if(format == null) {
			format = new CellFormat();
		}
		format.setForegroundColor(color);
        this.setCellFormat(row, column, format);
    }

    public void setCellBackground(int row, int column, Color color) {
		CellFormat format = this.getCellFormat(row,  column);
		if(format == null) {
			format = new CellFormat();
		}
		format.setBackgroundColor(color);
        this.setCellFormat(row, column, format);
    }

    public ICellRange getCellRange(int row, int column) {
   		return getSpreadSheetModel().getCellRange(row, column);
    }
}
