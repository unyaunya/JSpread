package com.unyaunya.spread;

import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.unyaunya.grid.ICell;
import com.unyaunya.grid.ICellRange;
import com.unyaunya.grid.format.CellFormat;

class SpreadSheetCell implements ICell {
	private SpreadSheetModel model;
	private int row;
	private int column;

	SpreadSheetCell(SpreadSheetModel model, int row, int column) {
		this.model = model;
		this.row = row;
		this.column = column;
	}
	
	@Override
	public Color getBackgroundColor() {
		CellFormat format = model.getCellFormat(row,  column);
		Color color = null; 
		if(format != null) {
			color = format.getBackgroundColor();
		}
		if(color == null) {
    		color = Color.WHITE; 
		}
		return color;
	}


	@Override
	public Border getBorder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public Color getForegroundColor() {
		CellFormat format = model.getCellFormat(row,  column);
		Color color = null; 
		if(format != null) {
			color = format.getForegroundColor();
		}
		if(color == null) {
    		color = Color.BLACK; 
		}
		return color;
	}

	@Override
	public int getHorizontalAlignment() {
		// TODO Auto-generated method stub
		return SwingConstants.LEFT;
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public String getText() {
		Object value = getValue();
		return value == null ? null : value.toString();
	}

	@Override
	public Object getValue() {
		return model.getValueAt(row, column);
	}

	@Override
	public ICellRange getCellRange() {
		return model.getCellRange(row, column);
	}
}
