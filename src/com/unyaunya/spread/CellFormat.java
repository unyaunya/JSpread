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

	@Override
	public boolean equals(Object value) {
		if(value == null) {
			return false;
		}
		if(value instanceof CellFormat) {
			CellFormat val = (CellFormat)value;
			if(this.getBackgroundColor() == null) {
				if(val.getBackgroundColor() != null) {
					return false;
				}
			}
			else {
				if(!val.getBackgroundColor().equals(val.getBackgroundColor())) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("CellFormat[backgroundColor=%s]", this.getBackgroundColor());
	}

	@Override
	public int hashCode(){
		final int multiplier = 37;
		int result = 17;
		result = multiplier * result + this.getBackgroundColor().hashCode();
		if(this.getBackgroundColor()!=null) {
			result = multiplier * result + this.getBackgroundColor().hashCode();
		}
		return result;
	}
}
