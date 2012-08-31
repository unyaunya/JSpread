/**
 * 
 */
package com.unyaunya.grid;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * ・縦または横方向のスクロール関連座標変換処理を行う。
 * ・スクロールしない行または列数が設定できる。
 * ・JSpreadPaneが使用するJScrollBarにスクロールに関係する値を伝達する。
 * ・座標変換及びスクロール関係量の算出するには、各行/列の高さ/幅及びコンポーネントの高さ/幅が必要である。
 * ・各行または列の高さは、次のメソッドで変更する。
 * 　　setDefaultSize(), setSize(), reset()
 * ・コンポーネントのサイズ(幅または高さ)は、ScrollModelから提供を受ける。
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
class ScrollRangeModel implements BoundedRangeModel, Serializable {
    private static final Logger LOG = Logger.getLogger(ScrollRangeModel.class.getName());
	
	/**
	 * 各行の高さまたは各列の幅
	 */
	private SizeModel sizeModel;
	
	/**
	 * コンポーネントの高さまたは幅
	 */
	private int componentSize;
	
	/**
	 * スクロールしない固定領域の行または列数
	 */
	transient private int fixedPartNum;

	/**
	 * スクロール値
	 */
	private int value;
	
	transient private int extent;

	/**
	 * 変更リスナーのリスト。基本的にJGridPaneのスクロールバーがリスナーとして設定される。
	 */
	transient private ArrayList<ChangeListener> changeListenerList;
	/**
	 * リスナーへの伝達に使用するイベント
	 */
	transient private ChangeEvent event;

	/**
	 * コンストラクタ
	 */
	ScrollRangeModel() {
		setup();
		this.sizeModel = new SizeModel();
		setComponentSize(0);
	}
	
	private void setup() {
		changeListenerList = new ArrayList<ChangeListener>();
		event = new ChangeEvent(this);
		extent = 1;
		fixedPartNum = 1;
	}

	public void reset(int count) {
		reset(count, sizeModel.getDefaultSize());
	}

	public void reset(int count, int defaultSize) {
		sizeModel.reset(count, defaultSize);
	}

	/**
	 * indexで指定された行または列の高さ/幅を取得する。
	 * @param index　行または列のインデックス
	 * @return
	 */
	public int getSize(int index) {
		return sizeModel.getSize(index);
	}
	
	/**
	 * indexで指定された行または列の高さ/幅を設定する。
	 * @param index　行または列のインデックス
	 * @param width 行または列の高さ/幅
	 */
	public void setSize(int index, int width) {
		sizeModel.setSize(index, width);
	}
	
	/**
	 * 行または列のデフォルトの高さ/幅を設定する。
	 * @param width　行または列のデフォルトの高さ/幅
	 */
	public void setDefaultSize(int width) {
		sizeModel.setDefaultSize(width);
	}

	/**
	 * 行または列のデフォルトの高さ/幅を取得する。
	 * @return
	 */
	public int getDefaultSize() {
		return sizeModel.getDefaultSize();
	}

	/**
	 * スクロールが不要なになるコンポーネントの高さまたは幅を取得する。
	 * @return
	 */
	public int getPreferredSize() {
		return sizeModel.getPosition(sizeModel.getLength());
	}

	/**
	 * 固定領域の行または列数を設定する。
	 * 
	 * @param fixedPartNum
	 */
	public void setFixedPartNum(int fixedPartNum) {
		if(fixedPartNum < 0) {
			fixedPartNum = 0;
		}
		this.fixedPartNum = fixedPartNum;
	}

	/**
	 * 固定領域の行または列数を取得する。
	 * @return
	 */
	public int getFixedPartNum() {
		return fixedPartNum;
	}

	/**
	 * コンポーネントの高さまたは幅をセットする。
	 * スクロールのextent及びvalueが再計算される。
	 * @param componentSize
	 */
	public void setComponentSize(int componentSize) {
		this.componentSize = componentSize;
		int currentExtent = getExtent();
		int currentValue = getValue();
		setExtent(calcExtent());
		setValue(getValue());
		if(currentExtent != getExtent() || currentValue != getValue()) {
			fireChangeEvent();
		}
	}

	/**
	 * コンポーネントの高さまたは幅を取得する。
	 * @return
	 */
	public int getComponentSize() {
		return this.componentSize;
	}

	/**
	 * スクロールのextent及びvalueの変更を通知する。
	 */
	public void fireChangeEvent() {
		LOG.info("fireChangeEvent");
		if(changeListenerList == null) {
			LOG.info("\tchangeListenerList=null");
		}
		else {
			for(ChangeListener l: changeListenerList) {
				l.stateChanged(event);
			}
		}
	}

	/**
	 * 固定領域全体の高さまたは幅
	 * @return
	 */
	public int getPreferredFixedPartSize() {
		return sizeModel.getPosition(getFixedPartNum());
	}

	/**
	 * 固定領域の高さまたは幅。
	 * コンポーネント領域の高さまたは幅に制約されなければ、固定領域全体の高さまたは幅と等しくなる。
	 * @return
	 */
	public int getFixedPartSize() {
		return Math.min(getComponentSize(), getPreferredFixedPartSize());
	}
	
	/**
	 * getPreferredSize() - getPreferredFixedPartSize()
	 * @return
	 */
	public int getPreferredScrollPartSize() {
		return getPreferredSize() - getPreferredFixedPartSize();
	}

	/**
	 * getComponentSize() - getFixedPartSize()
	 * @return
	 */
	public int getScrollPartSize() {
		return getComponentSize() - getFixedPartSize();
	}
	
	/**
	 * indexで指定した行または列の開始位置。
	 * スクロール考慮された値が算出される。
	 * @param index
	 * @return
	 */
	public int getPosition(int index) {
		if(index < getFixedPartNum()) {
			return sizeModel.getPosition(index);
		}
		else {
			return getFixedPartSize() + (sizeModel.getPosition(index) - sizeModel.getPosition(getFixedPartNum()+getValue()));
		}
	}

	public int getIndexFromDeviceCoord(int deviceCoord) {
		return sizeModel.getIndex(this.componentCoord2logicalCoord(deviceCoord));
	}
	
	/*
	 * コンポーネント座標→論理座標
	 */
	public int componentCoord2logicalCoord(int componentCoord) {
		if(componentCoord < getFixedPartSize()) {
			return componentCoord;
		}
		return sizeModel.getPosition(getFixedPartNum()+getValue()) + (componentCoord - getFixedPartSize());
	}

	/*
	 * 論理座標→コンポーネント座標
	 */
	public int logicalCoord2componentCoord(int logicalCoord) {
		if(logicalCoord < getFixedPartSize()) {
			return logicalCoord;
		}
		return logicalCoord - sizeModel.getPosition(getFixedPartNum()+getValue()) + getFixedPartSize();
	}
	
	/**
	 * リスナーを登録する。
	 * @see javax.swing.BoundedRangeModel#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void addChangeListener(ChangeListener l) {
		changeListenerList.add(l);
	}

	/**
	 * リスナーを削除する
	 * @see javax.swing.BoundedRangeModel#removeChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void removeChangeListener(ChangeListener l) {
		changeListenerList.remove(l);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getExtent()
	 */
	@Override
	public int getExtent() {
		return this.extent;
	}

	/**
	 * extentの最低値は１
	 * 
	 * @return
	 */
	int  calcExtent() {
		return Math.max(1, _calcExtent());
	}
	
	int  _calcExtent() {
		int scrollPartSize = getScrollPartSize();
		int preferredScrollPartSize = getPreferredScrollPartSize();

		//可変部分のサイズがゼロの場合
		if(scrollPartSize <= 0) {
			return 0;
		}
		//スクロール量を考慮せずに、可変部分のサイズが必要なサイズよりも大きい場合
		else if(scrollPartSize >= preferredScrollPartSize) {
			return getMaximum();
		}
		//可変部分のサイズが必要なサイズよりも小さい
		else {
			//可変部分上端の論理位置：
			int startPos = sizeModel.getPosition(this.getFixedPartNum()+getValue());
			//- sizeModel.getPosition(this.getFixedPartNum());
			//可変部分下端の論理位置：
			int endPos = startPos + scrollPartSize;
			if(endPos > getPreferredSize()) {
				endPos = getPreferredSize();
				startPos = endPos - scrollPartSize;
				return getMaximum() - (sizeModel.getIndex(startPos) + 0);
			}
			else {
				return sizeModel.getIndex(endPos) - getFixedPartNum() - getValue();
			}
		}
	}

	public void scrollToVisible(int index) {
		//固定領域が指定されたら、setValue(0)
		if(index < getFixedPartNum()) {
			setValue(0);
		}
		//指定された箇所が可変領域の前にある場合
		else if(index < (getFixedPartNum()+getValue())) {
			System.out.println("scrollToVisible:getFixedPartNum()");
			setValue(index - getFixedPartNum());
		}
		//可変領域のサイズが、セル自体のサイズよりも小さい場合
		else if(getScrollPartSize() <= sizeModel.getSize(index)) {
			System.out.println("getScrollPartSize() <= size");
			setValue(index - getFixedPartNum());
		}
		//指定された箇所が可変領域の後にある場合
		else {
			int gap = getScrollPartSize() - (sizeModel.getPosition(index+1) - sizeModel.getPosition(getFixedPartNum() + getValue()));
			LOG.info("scrollToVisible:getScrollPartSize() - (sizeModel.getPosition(index+1) - sizeModel.getPosition(getFixedPartNum() + getValue())) = " + gap);
			int n = 0;
			while(gap < 0) {
				gap += sizeModel.getSize(getFixedPartNum() + getValue()+n);
				n++;
			}
			setValue(getValue()+n);
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getMaximum()
	 */
	@Override
	public int getMaximum() {
		return Math.max(0, sizeModel.getLength() - getFixedPartNum());
	}

	/**
	 * スクロール量の最小値を取得する
	 * 
	 * @see javax.swing.BoundedRangeModel#getMinimum()
	 */
	@Override
	public int getMinimum() {
		return 0;
	}

	/**
	 * 現在のスクロール量を取得する。
	 * 
	 * @see javax.swing.BoundedRangeModel#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getValueIsAdjusting()
	 */
	@Override
	public boolean getValueIsAdjusting() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setExtent(int)
	 */
	@Override
	public void setExtent(int extent) {
		LOG.info("setExtent(" + extent + ")");
		this.extent = extent;
		fireChangeEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setMaximum(int)
	 */
	@Override
	public void setMaximum(int arg0) {
		LOG.info("setMaximum(" + arg0 + ")");
		fireChangeEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setMinimum(int)
	 */
	@Override
	public void setMinimum(int arg0) {
		LOG.info("setMinimum(" + arg0 + ")");
		fireChangeEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setRangeProperties(int, int, int, int, boolean)
	 */
	@Override
	public void setRangeProperties(int arg0, int arg1, int arg2, int arg3,
			boolean arg4) {
		LOG.info("setRangeProperties(" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + ")");
		fireChangeEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setValue(int)
	 */
	@Override
	public void setValue(int value) {
		if(value > (getMaximum() - getExtent())) {
			value = getMaximum() - getExtent();
		}
		if(value < 0) {
			value = 0;
		}
		this.value = value;
		LOG.info("setValue(" + value + ")");
		fireChangeEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setValueIsAdjusting(boolean)
	 */
	@Override
	public void setValueIsAdjusting(boolean arg0) {
		// TODO Auto-generated method stub
		fireChangeEvent();
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		setup();
	}
}
