/**
 * 
 */
package com.unyaunya.spread;

import java.util.ArrayList;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author wata
 *
 */
public class RangeModel implements BoundedRangeModel {
	private SizeModel sizeModel;
	private int componentSize;
	private int value;
	private ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();
	private ChangeEvent event = new ChangeEvent(this);
	private int extent = 1;
	private int fixedPartNum = 1;
	
	RangeModel(SizeModel sizeModel) {
		this.sizeModel = sizeModel;
	}
	
	public void setFixedPartNum(int fixedPartNum) {
		if(fixedPartNum < 0) {
			fixedPartNum = 0;
		}
		this.fixedPartNum = fixedPartNum;
	}

	public int getFixedPartNum() {
		return fixedPartNum;
	}

	public void setComponentSize(int componentSize) {
		this.componentSize = componentSize;
		setExtent(calcExtent());
		setValue(getValue());
		fireChangeEvent();
	}

	public void fireChangeEvent() {
		System.out.println("fireChangeEvent");
		for(ChangeListener l: changeListenerList) {
			l.stateChanged(event);
		}
	}
	public int getComponentSize() {
		return this.componentSize;
	}

	public int getPreferredFixedPartSize() {
		return sizeModel.getPosition(getFixedPartNum());
	}

	public int getFixedPartSize() {
		return Math.min(getComponentSize(), getPreferredFixedPartSize());
	}
	
	public int getScrollPartSize() {
		return getComponentSize() - getFixedPartSize();
	}

	public int getPreferredScrollPartSize() {
		return sizeModel.getPreferredSize() - getPreferredFixedPartSize();
	}
	
	//
	public int getPosition(int index) {
		if(index < getFixedPartNum()) {
			return sizeModel.getPosition(index);
		}
		else {
			return getFixedPartSize() + (sizeModel.getPosition(index) - sizeModel.getPosition(getFixedPartNum()+getValue()));
		}
	}

	public int getIndexFromDeviceCoord(int deviceCoord) {
		if(deviceCoord > getFixedPartSize()) {
			deviceCoord = this.translate(deviceCoord);
		}
		return sizeModel.getIndex(deviceCoord);
	}
	
	/*
	 * �f�o�C�X���W���_�����W
	 */
	public int translate(int position) {
		return position + sizeModel.getPosition(getValue());
	}

	/*
	 * �_�����W���f�o�C�X���W
	 */
	/*
	public int untranslate(int position) {
		return position - sizeModel.getPosition(getValue());
	}
	*/
	
	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void addChangeListener(ChangeListener l) {
		changeListenerList.add(l);
	}

	/* (non-Javadoc)
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
	 * extent�̍Œ�l�͂P
	 * 
	 * @return
	 */
	int  calcExtent() {
		return Math.max(1, _calcExtent());
	}
	
	int  _calcExtent() {
		int scrollPartSize = getScrollPartSize();
		int preferredScrollPartSize = getPreferredScrollPartSize();

		//�ϕ����̃T�C�Y���[���̏ꍇ
		if(scrollPartSize <= 0) {
			return 0;
		}
		//�X�N���[���ʂ��l�������ɁA�ϕ����̃T�C�Y���K�v�ȃT�C�Y�����傫���ꍇ
		else if(scrollPartSize >= preferredScrollPartSize) {
			return sizeModel.getLength() - getFixedPartNum();
		}
		//�ϕ����̃T�C�Y���K�v�ȃT�C�Y����������
		else {
			//�ϕ�����[�̘_���ʒu�F
			int startPos = sizeModel.getPosition(this.getFixedPartNum()+getValue()) - sizeModel.getPosition(this.getFixedPartNum());
			int endPos = startPos + scrollPartSize;
			if(endPos > preferredScrollPartSize) {
				endPos = preferredScrollPartSize;
				startPos = endPos - scrollPartSize;
				return sizeModel.getLength() - (sizeModel.getIndex(startPos) + 1);
			}
			else {
				return sizeModel.getIndex(endPos) - getValue();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getMaximum()
	 */
	@Override
	public int getMaximum() {
		return Math.max(0, sizeModel.getLength() - getFixedPartNum());
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getMinimum()
	 */
	@Override
	public int getMinimum() {
		return 0;
	}

	/* (non-Javadoc)
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
		System.out.println("setExtent(" + extent + ")");
		this.extent = extent;
		fireChangeEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setMaximum(int)
	 */
	@Override
	public void setMaximum(int arg0) {
		System.out.println("setMaximum(" + arg0 + ")");
		fireChangeEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setMinimum(int)
	 */
	@Override
	public void setMinimum(int arg0) {
		System.out.println("setMinimum(" + arg0 + ")");
		fireChangeEvent();
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#setRangeProperties(int, int, int, int, boolean)
	 */
	@Override
	public void setRangeProperties(int arg0, int arg1, int arg2, int arg3,
			boolean arg4) {
		System.out.println("setRangeProperties(" + arg0 + ", " + arg1 + ", " + arg2 + ", " + arg3 + ")");
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
		System.out.println("setValue(" + value + ")");
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

	public void scrollToVisible(int index) {
		//�Œ�̈悪�w�肳�ꂽ��AsetValue(0)
		if(index < getFixedPartNum()) {
			setValue(0);
		}
		//�w�肳�ꂽ�ӏ����ϗ̈�̑O�ɂ���ꍇ
		else if(index < (getFixedPartNum()+getValue())) {
			System.out.println("scrollToVisible:getFixedPartNum()");
			setValue(index);
		}
		//�ϗ̈�̃T�C�Y���A�Z�����̂̃T�C�Y�����������ꍇ
		else if(getScrollPartSize() <= sizeModel.getSize(index)) {
			System.out.println("getScrollPartSize() <= size");
			setValue(index);
		}
		//�w�肳�ꂽ�ӏ����ϗ̈�̌�ɂ���ꍇ
		else {
			int gap = getScrollPartSize() - (sizeModel.getPosition(index+1) - sizeModel.getPosition(getFixedPartNum() + getValue()));
			System.out.println("scrollToVisible:getScrollPartSize() - (sizeModel.getPosition(index+1) - sizeModel.getPosition(getFixedPartNum() + getValue())) = " + gap);
			int n = 0;
			while(gap < 0) {
				gap += sizeModel.getSize(getFixedPartNum() + getValue()+n);
				n++;
			}
			setValue(getValue()+n);
		}
	}
}
