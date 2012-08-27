package com.unyaunya.grid.format;

import java.awt.Color;
import java.io.Serializable;

/**
 * ÉZÉãèëéÆÇäiî[Ç∑ÇÈÉNÉâÉX
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class CellFormat implements Serializable, Cloneable {
	private Color backgroundColor;
	private Color foregroundColor;
	
	public CellFormat() {
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color value) {
		backgroundColor = value;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color value) {
		foregroundColor = value;
	}

	@Override
	public boolean equals(Object value) {
		if(value == null) {
			return false;
		}
		if(value instanceof CellFormat) {
			CellFormat val = (CellFormat)value;
			if(!equals(this.getBackgroundColor(), val.getBackgroundColor())) {
				return false;
			}
			if(!equals(this.getForegroundColor(), val.getForegroundColor())) {
				return false;
			}
		}
		return true;
	}

	private static boolean equals(Object v1, Object v2) {
		return (v1 == null) ? (v2 == null) : v1.equals(v2);
	}
	
	@Override
	public String toString() {
		return String.format("CellFormat[backgroundColor=%s,foregroundColor=%s]",
				this.getBackgroundColor().toString(),
				this.getForegroundColor().toString()
				);
	}

	@Override
	public int hashCode(){
		final int multiplier = 37;
		int result = 17;
		result = multiplier * result + this.getBackgroundColor().hashCode();
		if(this.getBackgroundColor()!=null) {
			result = multiplier * result + this.getBackgroundColor().hashCode();
		}
		if(this.getForegroundColor()!=null) {
			result = multiplier * result + this.getForegroundColor().hashCode();
		}
		return result;
	}

	@Override
	public CellFormat clone(){
		CellFormat result = null;
		try {
			result = (CellFormat) super.clone();
			result.backgroundColor = this.backgroundColor;
			result.foregroundColor = this.foregroundColor;
		} catch (CloneNotSupportedException cnse) {
		}		
		return result;
	}
}
