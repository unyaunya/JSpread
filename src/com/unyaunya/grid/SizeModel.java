/**
 * 
 */
package com.unyaunya.grid;

//import javax.swing.SizeSequence;

/**
 * @author wata
 *　行高さまたは列幅を管理するクラス
 *　TBD:グループ情報も管理するよう拡張する。
 */
class SizeModel /*extends SizeSequence*/ {
	private static int[] emptyArray = new int[0];
	private static int VISIBLE = -1;
	
	/**
	 * 行高さまたは列幅の情報を保持する。
	 * （各行・列の高さ・幅そのものを保持しているのではなく、二分探索的に効率的に検索、更新できるように工夫されている。
	 * 　詳しくはパクリ元のSizeSequenceの説明を参照。）
	 */
	private int a[];
	
	/**
	 * 各行または列の表示・非表示を記録する。
	 * デフォルトの表示状態にある場合は、VISIBLEが格納されている。
	 * 非表示の場合は、表示状態の行高さまたは列幅を格納する。
	 */
	private int visibility[];

	/**
	 * グループ情報を保持する。
	 */
	private int groupingLevel[];
	
	/**
	 * 
	 */
	private int defaultSize = 20;

	/**
	 * 
	 */
	public SizeModel() {
		removeAll();
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
			groupingLevel = new int[sizes.length];
		}
		setSizesInternally(0, sizes.length, sizes);
	}

	/*
	private void setSizes(int length, int size) {
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
	*/

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
        this.visibility = insert(visibility, start, length, VISIBLE);
        this.groupingLevel = insert(this.groupingLevel, start, length, start > 0 ? this.groupingLevel[start-1] : 0);
		assert(a.length == this.visibility.length);
    }

	/**
	 * array[start]の位置からlength個のエントリを挿入した配列を作成して返す。
	 * 挿入されたエントリの値はすべてvalueにセットする。
	 * 
	 * @param array
	 * @param start
	 * @param length
	 * @param value
	 * @return
	 */
	private static int[] insert(int array[], int start, int length, int value) {
        int end = start + length;
        int new_length = array.length + length;
        int new_array[] = new int[new_length];
        for (int i = 0; i < start; i++) {
        	new_array[i] = array[i] ;
        }
        for (int i = start; i < end; i++) {
        	new_array[i] = value ;
        }
        for (int i = end; i < new_length; i++) {
        	new_array[i] = array[i-length] ;
        }
        return new_array;
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
        this.visibility = remove(this.visibility, start, length);
        this.groupingLevel = remove(this.groupingLevel, start, length);
		assert(a.length == this.visibility.length);
    }

	/**
	 * array[start]の位置からlength個のエントリを削除した配列を作成して返す。
	 * 
	 * @param array
	 * @param start
	 * @param length
	 * @return
	 */
    private static int[] remove(int array[], int start, int length) {
        int new_length = array.length - length;
        int new_array[] = new int[new_length];
        for (int i = 0; i < start; i++) {
        	new_array[i] = array[i] ;
        }
        for (int i = start; i < new_length; i++) {
        	new_array[i] = array[i+length] ;
        }
		return new_array;
	}
 
    //----------------------------------------
    /*
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
	*/

	public int getLength() {
		assert(a.length == this.visibility.length);
		return this.visibility.length;
	}

	private void removeAll() {
		this.a = emptyArray;
		this.groupingLevel = emptyArray;
		this.visibility = emptyArray;
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

	public int getLevel(int index) {
		return this.groupingLevel[index] % 65536;
	}

	public boolean isExpanded(int index) {
		return this.groupingLevel[index] < 65536;
	}

	public boolean isLeaf(int index) {
		if(index < 0 || index >= a.length-1) {
			return false;
		}
		return this.getLevel(index) >= this.getLevel(index+1);
	}

	public boolean levelDown(int start, int length) {
		if(start < 0 || length < 0) {
			return false;
		}
		int end = start + length -1;
		if(a.length <= end) {
			return false;
		}
		for(int i = start; i <= end; i++) {
			if(isExpanded(i)) {
				this.groupingLevel[i] = this.getLevel(i)+1;
			}
			else {
				this.groupingLevel[i] = this.getLevel(i)+1+65536;
			}
		}
		return true;
	}
	
	public boolean levelUp(int start, int length) {
		if(start < 0 || length < 0) {
			return false;
		}
		int end = start + length -1;
		if(a.length <= end) {
			return false;
		}
		for(int i = start; i <= end; i++) {
			if(isExpanded(i)) {
				this.groupingLevel[i] = Math.max(0, this.groupingLevel[i]-1);
			}
			else {
				this.groupingLevel[i] = Math.max(0, this.groupingLevel[i]-1)+65536;
			}
		}
		return true;
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
