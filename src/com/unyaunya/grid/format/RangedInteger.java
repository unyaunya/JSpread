package com.unyaunya.grid.format;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;

public class RangedInteger extends RangedObject {
	public RangedInteger(Integer value, IRange range){
		super(range, value);
	}

	public RangedInteger(Integer value, int row, int column){
		this(value, new CellRange(row, column));
	}

	public Integer getInteger() {
		return (Integer)getObject();
	}

	public int getInt() {
		return getInteger().intValue();
	}
}
