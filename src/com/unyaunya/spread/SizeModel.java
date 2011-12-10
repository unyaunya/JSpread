/**
 * 
 */
package com.unyaunya.spread;

import javax.swing.SizeSequence;

/**
 * @author wata
 *
 */
class SizeModel extends SizeSequence {
	private int length = 0;
	private int defaultSize = 20;

	/**
	 * 
	 */
	public SizeModel() {
		this.length = 0;
	}

	/**
	 * @param numEntries
	 */
	public SizeModel(int numEntries) {
		this(numEntries, 0);
	}

	/**
	 * @param sizes
	 */
	public SizeModel(int[] sizes) {
		super(sizes);
		this.length = sizes.length;
	}

	/**
	 * @param numEntries
	 * @param value
	 */
	public SizeModel(int numEntries, int value) {
		super(numEntries, value);
		this.length = numEntries;
	}

	public int getLength() {
		return this.length;
	}

	@Override
	public void setSizes(int[] sizes) {
		super.setSizes(sizes);
		this.length = sizes.length;
	}

	public int getPreferredSize() {
		return getPosition(getLength());
	}

	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}

	public int getDefaultSize() {
		return defaultSize;
	}
}
