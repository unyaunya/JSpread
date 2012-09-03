/**
 * 
 */
package com.unyaunya.swing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.table.TableModel;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.format.CellFormatModel;
import com.unyaunya.grid.format.RangedColor;
import com.unyaunya.grid.selection.DefaultSelectionModel;
import com.unyaunya.grid.selection.IGridSelectionModel;
import com.unyaunya.spread.Config;
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
		setUI(new GridUI());
	}

	/**
	 * �Z���N�V�������f�����쐬����B
	 * �Z���N�V�������f���̋@�\�g�����s���ꍇ�A�h���N���X�ł��̃��\�b�h���I�[�o���C�h���A
	 * �Z���N�V�������f���̔h���N���X�̃C���X�^���X��Ԃ��悤�ɂ���B
	 * 
	 * @return
	 */
	protected IGridSelectionModel createSelectionModel() {
		return new DefaultSelectionModel(this);
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
		this.getGridSelectionModel().reset();
		setTableModel(model);
		//getScrollModel().setTableModel(this.getSpreadSheetModel());
		//this.repaint(this.getBounds());
	}

	//
	// Operation of the SpreadSheet
	//
	
	/**
	 * �I������Ă���s�̏�ɁA�V���ȍs��}������B
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
		getSpreadSheetModel().insertRow(row);
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
		getSpreadSheetModel().insertColumn(col);
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
		getSpreadSheetModel().removeRow(row);
		if(paint) {
			repaint();
		}
	}
	
	public void coupleCells(ArrayList<IRange> rangeList) {
		if(rangeList.size() > 1) {
			return;
		}
		CellRange range = new CellRange(rangeList.get(0));
		getSpreadSheetModel().coupleCells(range);
		repaint();
		LOG.info(range.toString());
	}
	
	public void coupleCells() {
		coupleCells(this.getGridSelectionModel().getSelectedRangeList());
	}

    /**
     * �Z���̃t�H�[�}�b�g���擾����B
     * @param row
     * @param column
     * @return �Z���̃t�H�[�}�b�g�B�ݒ肳��Ă��Ȃ��ꍇ��null
     */
    public IRange getCellRange(int row, int column) {
   		return getSpreadSheetModel().getCellRange(row, column);
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
    	CellFormatModel m = getSpreadSheetModel().getCellFormatModel();
    	m.addForegroundColor(new RangedColor(color, range));
    }
    
    public void setCellBackground(Color  color, IRange range) {
    	CellFormatModel m = getSpreadSheetModel().getCellFormatModel();
    	m.addBackgroundColor(new RangedColor(color, range));
    }
}
