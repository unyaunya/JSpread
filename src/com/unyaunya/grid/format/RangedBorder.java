package com.unyaunya.grid.format;

import javax.swing.border.Border;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;

public class RangedBorder extends RangedObject {
	public RangedBorder(Border border, IRange range){
		super(range, border);
	}

	public RangedBorder(Border color, int row, int column){
		this(color, new CellRange(row, column));
	}

	public Border getBorder() {
		return (Border)getObject();
	}
}
