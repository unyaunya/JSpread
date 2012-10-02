package com.unyaunya.grid.format;

import java.text.Format;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;

@SuppressWarnings("serial")
public class RangedFormat extends RangedObject {
	public RangedFormat(Format format, IRange range){
		super(range, format);
	}

	public RangedFormat(Format format, int row, int column){
		this(format, new CellRange(row, column));
	}

	public Format getFormat() {
		return (Format)getObject();
	}
}

