package com.unyaunya.grid.format;

import java.awt.Color;

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;

public class RangedColor extends RangedObject {
	public RangedColor(Color color, IRange range){
		super(range, color);
	}

	public RangedColor(Color color, int row, int column){
		this(color, new CellRange(row, column));
	}

	public Color getColor() {
		return (Color)getObject();
	}
}
