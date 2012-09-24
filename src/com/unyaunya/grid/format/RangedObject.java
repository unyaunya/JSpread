package com.unyaunya.grid.format;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;

@SuppressWarnings("serial")
public class RangedObject extends CellRange /*implements IRangeable*/{
	private Object object;

	public RangedObject(IRange range, Object object){
		super(range);
		this.object = object;
	}

	public IRange getRange() {
		return this;
	}

	public Object getObject() {
		return object;
	}
}
