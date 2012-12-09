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
	private final static int COLLAPSED = 0x10000;
	private final static int HIDDEN = 0x20000;
	private final static int PARENT_COLLAPSED = 0x40000;
	
	/**
	 * 行または列の高さ・幅と位置を混合した情報を保持する。
	 * （各行・列の高さ・幅そのものを保持しているのではなく、二分探索的に効率的に検索、更新できるように工夫されている。
	 * 　詳しくはパクリ元のSizeSequenceの説明を参照。）
	 */
	private int internal[];
	
	/**
	 * 各行または列のサイズを記録する。
	 */
	private int sizes[];

	/**
	 * グループ情報等を保持する。
	 * 	 flags = [HIDDEN] + [COLLAPSED] + level
	 */
	private int flags[];
	
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

	public int getLength() {
		return this.internal.length;
	}

	private void removeAll() {
		this.sizes = emptyArray;
		this.internal = emptyArray;
		this.flags = emptyArray;
		//this.visibility = emptyArray;
	}

	public void reset(int count, int defaultSize) {
		removeAll();
		setDefaultSize(defaultSize);
		if(count > 0) {
			insertEntries(0, count, getDefaultSize());
		}
	}
	
	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	public void setSizes(int[] sizes) {
		if (getLength() != sizes.length) {
			this.sizes = new int[sizes.length];
			this.internal = new int[sizes.length];
			this.flags = new int[sizes.length];
		}
		for(int i = 0; i < sizes.length; i++) {
			this.sizes[i] = sizes[i];
		}
		setInternal(0, sizes.length, sizes);
	}

	private int setInternal(int from, int to, int[] sizes) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to)/2;
		internal[m] = sizes[m] + setInternal(from, m, sizes);
		return internal[m] + setInternal(m + 1, to, sizes);
	}
	
	private void setInternal(int[] sizes) {
		if (internal.length != sizes.length) {
			internal = new int[sizes.length];
		}
		setInternal(0, sizes.length, sizes);
	}

	public int[] getSizes() {
		int[] sizes = new int[getLength()];
		for(int i = 0; i < sizes.length; i++) {
			sizes[i] = this.sizes[i];
		}
		return sizes;
	}

	public int getDisplaySize(int index) {
		return getPosition(index + 1) - getPosition(index);
	}	

	public int getSize(int index) {
		return this.sizes[index];
	}

	/*
	public int[] getSizes() {
		int n = internal.length;
		int[] sizes = new int[n];
		getSizes(0, n, sizes);
		return sizes;
	}
	private int getSizes(int from, int to, int[] sizes) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to)/2;
		sizes[m] = internal[m] - getSizes(from, m, sizes);
		return internal[m] + getSizes(m + 1, to, sizes);
	}
	 */	
	
	public int getPosition(int index) {
		return getPosition(0, internal.length, index);
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
		    return internal[m] + getPosition(m + 1, to, index);
		}
	}
	
	public int getIndex(int position) {
		return getIndex(0, internal.length, position);
	}
	
	private int getIndex(int from, int to, int position) {
		if (to <= from) {
			return from;
		}
		int m = (from + to)/2;
		int pivot = internal[m];
		if (position < pivot) {
			return getIndex(from, m, position);
		}
		else {
			return getIndex(m + 1, to, position - pivot);
		}
	}
	
	public void setSize(int index, int size) {
		if(!isDisplayed(index)) {
			this.setInternal(index, size);
		}
		this.sizes[index] = size;
	}
	
	private void setInternal(int index, int size) {
		changeInternal(0, internal.length, index, size - getDisplaySize(index));
	}
	
	private void changeInternal(int from, int to, int index, int delta) {
		if (to <= from) {
			return;
		}
		int m = (from + to)/2;
		if (index <= m) {
			internal[m] += delta;
			changeInternal(from, m, index, delta);
		}
		else {
			changeInternal(m + 1, to, index, delta);
		}
	}
	
	public void insertEntries(int start, int length, int value) {
        this.sizes = insert(this.sizes, start, length, value);
        //this.visibility = insert(visibility, start, length, HIDDEN);
        this.flags = insert(this.flags, start, length, start > 0 ? this.flags[start-1] : 0);
        setInternal(this.sizes);
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
        this.sizes = remove(this.sizes, start, length);
        this.flags = remove(this.flags, start, length);
        setInternal(this.sizes);
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
 
	//
	//グルーピング関係
	//
	
	public int getLevel(int index) {
		return this.flags[index] % COLLAPSED;
	}

	public boolean isExpanded(int index) {
		return (this.flags[index] & COLLAPSED) == 0;
	}

	public boolean isLeaf(int index) {
		if(index < 0 || index > (getLength()-1)) {
			return false;
		}
		return this.getLevel(index) >= this.getLevel(index+1);
	}

	public boolean levelDown(int start, int length) {
		if(start < 0 || length < 0) {
			return false;
		}
		int end = start + length -1;
		if(getLength() <= end) {
			return false;
		}
		for(int i = start; i <= end; i++) {
			setLevel(i, this.getLevel(i)+1);
		}
		return true;
	}
	
	public boolean levelUp(int start, int length) {
		if(start < 0 || length < 0) {
			return false;
		}
		int end = start + length -1;
		if(getLength() <= end) {
			return false;
		}
		for(int i = start; i <= end; i++) {
			setLevel(i, this.getLevel(i)-1);
		}
		return true;
	}

	public void collapse(int index) {
		if(index < 0 || index >= getLength()) {
			return;
		}
		if(!isLeaf(index)) {
			setFlag(index, COLLAPSED);
			collapseSubordinates(index);
		}
	}

	/**
	 * indexで指定した行およびそれに従属する行を非表示にする。
	 * @param index
	 * @return
	 */
	private int collapseSubordinates(int index) {
		int currentLevel = getLevel(index);
		int i = index + 1;
		while(i < getLength()) {
			int level = getLevel(i);
			if(level <= currentLevel) {
				return i;
			}
			if(isLeaf(i)) {
				if(isDisplayed(i)) {
					setInternal(i, 0);
				}
				setFlag(i, PARENT_COLLAPSED);
				i++;
			}
			else {
				setFlag(i, PARENT_COLLAPSED);
				i = collapseSubordinates(i);
			}
		}
		return i;
	}

	public void expand(int index) {
		if(index < 0 || index >= getLength()) {
			return;
		}
		if(!isLeaf(index)) {
			resetFlag(index, COLLAPSED);
			expandSubordinates(index);
		}
	}

	private int expandSubordinates(int index) {
		int currentLevel = getLevel(index);
		int i = index + 1;
		while(i < getLength()) {
			int level = getLevel(i);
			if(level <= currentLevel) {
				return i;
			}
			resetFlag(i, PARENT_COLLAPSED);
			if(isDisplayed(i)) {
				setInternal(i, getSize(i));
			}
			if((this.flags[index] & COLLAPSED) == 0) {
				i = expandSubordinates(i);
			}
			else {
				i = nextSiblingOrUncle(i);
			}
		}
		return i;
	}

	private int nextSiblingOrUncle(int index) {
		int currentLevel = getLevel(index);
		int i = index + 1;
		while(i < getLength()) {
			int level = getLevel(i);
			if(level <= currentLevel) {
				return i;
			}
			i++;
		}
		return i;
	}

	private void setFlag(int index, int flag) {
		this.flags[index] |= flag;
	}
	private void resetFlag(int index, int flag) {
		this.flags[index] &= ~flag;
	}
	private void setLevel(int index, int level) {
		this.flags[index] = (this.flags[index] & (~0xFFFF)) | Math.max(0,  Math.min(0xFFFF, level));
	}
	
	//
	//非表示機能関連
	//
	
	public boolean isDisplayed(int index) {
		//if(index < 0 || index >= getLength()) {
		//	return false;
		//}
		return (this.flags[index] & (HIDDEN+PARENT_COLLAPSED)) == 0;
	}

	public boolean isHidden(int index) {
		//if(index < 0 || index >= a.length) {
		//	return false;
		//}
		return (this.flags[index] & HIDDEN) != 0;
	}

	public void setHidden(int index, boolean bHidden ) {
		if(bHidden == isHidden(index)) {
			return;
		}
		if(bHidden) {
			this.setFlag(index, HIDDEN);
			this.setInternal(index, 0);
		}
		else {
			this.resetFlag(index, HIDDEN);
			if(isDisplayed(index)) {
				this.setInternal(index, getSize(index));
			}
		}
	}
}
