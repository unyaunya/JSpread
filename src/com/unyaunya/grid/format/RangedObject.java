package com.unyaunya.grid.format;

import com.unyaunya.grid.IRange;
import com.unyaunya.grid.IRangeable;

public class RangedObject implements IRangeable{
	private IRange range;
	private Object object;

	public RangedObject(IRange range, Object object){
		this.range = range;
		this.object = object;
	}

	@Override
	public IRange getRange() {
		return range;
	}

	public Object getObject() {
		return object;
	}
}
