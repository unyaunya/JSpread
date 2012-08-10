/**
 * 
 */
package com.unyaunya.spread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.SizeSequence;

/**
 * @author wata
 *　行高さまたは列幅を管理するクラス
 */
class SizeModel extends SizeSequence implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int defaultSize = 20;
	private int length = 0;

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

	@Override
	public void setSize(int index, int size) {
		if(size < 0) {
			size = 0;
		}
		super.setSize(index, size);
	}

	public void removeAll() {
		int sizes[] = {};
		this.setSizes(sizes);
	}

	public void reset(int count, int defaultSize) {
		removeAll();
		setDefaultSize(defaultSize);
		insertEntries(0, count, getDefaultSize());
	}
	
	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeInt(length);
		stream.writeInt(defaultSize);
		int[] sizes = getSizes();
		for(int i = 0; i < sizes.length; i++) {
			if(sizes[i] != defaultSize) {
				stream.writeInt(i);
				stream.writeInt(sizes[i]);
			}
		}
		stream.writeInt(-1);
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		length = stream.readInt();
		defaultSize = stream.readInt();
		int[] sizes = new int[length];
		for(int i = 0; i < length; i++) {
			sizes[i] = defaultSize;
		}
		int index = stream.readInt();
		while(index != -1) {
			sizes[index] = stream.readInt();
			index = stream.readInt();
		}
		this.setSizes(sizes);
	}
}
