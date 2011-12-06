/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Point;

import javax.swing.SizeSequence;

/**
 * @author wata
 *
 */
class SizeModel extends SizeSequence {
	private int reference = 0;

	/**
	 * 
	 */
	public SizeModel() {}

	/**
	 * @param numEntries
	 */
	public SizeModel(int numEntries) {
		super(numEntries);
	}

	/**
	 * @param sizes
	 */
	public SizeModel(int[] sizes) {
		super(sizes);
	}

	/**
	 * @param numEntries
	 * @param value
	 */
	public SizeModel(int numEntries, int value) {
		super(numEntries, value);
	}

	public int getIndex(int position, int maxCount) {
		int rslt = this.getIndex(position);
		if(rslt >= maxCount) {
			return -1;
		}
		else {
			return rslt;
		}
	}

	public int translate(int position) {
		return position + this.getPosition(reference);
	}
	public int untranslate(int position) {
		return position - this.getPosition(reference);
	}

	public int getReference() {
		return reference;
	}

	public void setReference(int reference) {
		this.reference = reference;
	}
}
