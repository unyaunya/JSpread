package com.unyaunya.grid.format;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;

@SuppressWarnings("serial")
public class RangedInteger extends RangedObject {
	public RangedInteger(Integer value, IRange range){
		super(range, value);
	}

	public RangedInteger(Integer value, int top, int left, int bottom, int right){
		this(value, new CellRange(top, left, bottom, right));
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
