package com.unyaunya.spread;

import java.awt.Color;
import java.io.Serializable;

public class CellFormat implements Serializable {
	private Color backgroundColor;
	
	public CellFormat() {
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color value) {
		backgroundColor = value;
	}
}
