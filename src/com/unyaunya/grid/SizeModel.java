/**
 * 
 */
package com.unyaunya.grid;

//import javax.swing.SizeSequence;

/**
 * @author wata
 *　行高さまたは列幅を管理するクラス
 */
class SizeModel /*extends SizeSequence*/ {
	private static int[] emptyArray = new int[0];
	private static int VISIBLE = -1;
	
	private int a[];
	
	/**
	 * 各行または列の表示・非表示を記録する。
	 * デフォルトの表示状態にある場合は、VISIBLEが格納されている。
	 * 非表示の場合は、表示状態の行高さまたは列幅を格納する。
	 */
	private int visibility[];
	
	/**
	 * 
	 */
	private int defaultSize = 20;

	/**
	 * 
	 */
	public SizeModel() {
		a = emptyArray;
		initVisibility(0);
		assert(a.length == this.visibility.length);
	}

	/**
	 * @param numEntries
	 */
	public SizeModel(int numEntries) {
		this(numEntries, 0);
	}

	/**
	 * @param numEntries
	 * @param value
	 */
	public SizeModel(int numEntries, int value) {
		this();
		insertEntries(0, numEntries, value);
	}

	/**
	 * @param sizes
	 */
	public SizeModel(int[] sizes) {
		super();
		setSizes(sizes);
	}
	
	public void setSizes(int[] sizes) {
		if (a.length != sizes.length) {
			a = new int[sizes.length];
		}
		setSizesInternally(0, sizes.length, sizes);
	}

	void setSizes(int length, int size) {
		if (a.length != length) {
			a = new int[length];
		}
		setSizesInternally(0, length, size);
	}

	private int setSizesInternally(int from, int to, int size) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to)/2;
		a[m] = size + setSizesInternally(from, m, size);
		return a[m] + setSizesInternally(m + 1, to, size);
	}

	private void _setSizes(int[] sizes) {
		if (a.length != sizes.length) {
			a = new int[sizes.length];
		}
		setSizesInternally(0, sizes.length, sizes);
	}

/*
 * 	@Override
	public void setSizes(int[] sizes) {
		super.setSizes(sizes);
		if (this.visibility.length != sizes.length) {
			this.visibility = new int[sizes.length];
		}
		for(int i = 0; i < sizes.length; i++) {
			this.visibility[i] = sizes[i];
		}
	}
 */

	private int setSizesInternally(int from, int to, int[] sizes) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to)/2;
		a[m] = sizes[m] + setSizesInternally(from, m, sizes);
		return a[m] + setSizesInternally(m + 1, to, sizes);
	}
	
	public int[] getSizes() {
		int n = a.length;
		int[] sizes = new int[n];
		getSizes(0, n, sizes);
		return sizes;
	}
	
	private int getSizes(int from, int to, int[] sizes) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to)/2;
		sizes[m] = a[m] - getSizes(from, m, sizes);
		return a[m] + getSizes(m + 1, to, sizes);
	}
	
	public int getPosition(int index) {
		return getPosition(0, a.length, index);
	}
	
	private int getPosition(int from, int to, int index) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to)/2;
		if (index <= m) {
			return getPosition(from, m, index);
		}
		else {
		    return a[m] + getPosition(m + 1, to, index);
		}
	}
	
	public int getIndex(int position) {
		return getIndex(0, a.length, position);
	}
	
	private int getIndex(int from, int to, int position) {
		if (to <= from) {
			return from;
		}
		int m = (from + to)/2;
		int pivot = a[m];
		if (position < pivot) {
			return getIndex(from, m, position);
		}
		else {
			return getIndex(m + 1, to, position - pivot);
		}
	}
	
	public int getSize(int index) {
		return getPosition(index + 1) - getPosition(index);
	}
	
	public void setSize(int index, int size) {
		changeSize(0, a.length, index, size - getSize(index));
	}
	
	private void changeSize(int from, int to, int index, int delta) {
		if (to <= from) {
			return;
		}
		int m = (from + to)/2;
		if (index <= m) {
			a[m] += delta;
			changeSize(from, m, index, delta);
		}
		else {
			changeSize(m + 1, to, index, delta);
		}
	}
	
	public void insertEntries(int start, int length, int value) {
        int sizes[] = getSizes();
        int end = start + length;
        int n = this.a.length + length;
        a = new int[n];
        for (int i = 0; i < start; i++) {
        	a[i] = sizes[i] ;
        }
        for (int i = start; i < end; i++) {
        	a[i] = value ;
        }
        for (int i = end; i < n; i++) {
        	a[i] = sizes[i-length] ;
        }
        _setSizes(a);
        //
        sizes = new int[a.length];
        for (int i = 0; i < start; i++) {
        	sizes[i] = visibility[i] ;
        }
        for (int i = start; i < end; i++) {
        	sizes[i] = VISIBLE ;
        }
        for (int i = end; i < n; i++) {
        	sizes[i] = visibility[i-length] ;
        }
        visibility = sizes;
		assert(a.length == this.visibility.length);
    }
	
    public void removeEntries(int start, int length) {
        int sizes[] = getSizes();
        //int end = start + length;
        int n = visibility.length - length;
        a = new int[n];
        for (int i = 0; i < start; i++) {
             a[i] = sizes[i] ;
        }
        for (int i = start; i < n; i++) {
            a[i] = sizes[i+length] ;
        }
        _setSizes(a);
        //
        sizes = new int[a.length];
        for (int i = 0; i < start; i++) {
        	sizes[i] = visibility[i] ;
        }
        for (int i = start; i < n; i++) {
        	sizes[i] = visibility[i+length] ;
        }
        visibility = sizes;
		assert(a.length == this.visibility.length);
    }
 
    //----------------------------------------
	private void initVisibility(int numEntries) {
		if(numEntries == 0) {
			this.visibility = emptyArray;
		}
		else {
			this.visibility = new int[numEntries];
			for(int i = 0; i < visibility.length; i++) {
				this.visibility[i] = VISIBLE;
			}
		}
	}

	public int getLength() {
		assert(a.length == this.visibility.length);
		return this.visibility.length;
	}

	private void removeAll() {
		this.visibility = emptyArray;
		this.setSizes(visibility);
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

	public boolean isHidden(int index) {
		//if(index < 0 || index >= a.length) {
		//	return false;
		//}
		return this.visibility[index] == VISIBLE;
	}

	public void setHidden(int index, boolean bHidden ) {
		if(bHidden == isHidden(index)) {
			return;
		}
		if(bHidden) {
			setSize(index, visibility[index]);
			visibility[index] = VISIBLE;
		}
		else {
			visibility[index] = getSize(index);
			setSize(index, 0);
		}
	}

}
