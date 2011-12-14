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
class RangeModel implements BoundedRangeModel {
	private SizeModel sizeModel;
	private int componentSize;
	private int value;
	private ArrayList<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();
	private ChangeEvent event = new ChangeEvent(this);
	private int extent = 1;
	
	RangeModel(SizeModel sizeModel) {
		this.sizeModel = sizeModel;
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
	public int translate(int position) {
		return position + sizeModel.getPosition(getValue());
	}
	public int untranslate(int position) {
		return position - sizeModel.getPosition(getValue());
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void addChangeListener(ChangeListener l) {
		System.out.println("ScrollModel.addChangeListener is called");
		System.out.println(l);
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

	int  calcExtent() {
		if(componentSize <= 0) {
			return 1;
		}
		else if(componentSize >= sizeModel.getPreferredSize()) {
			return sizeModel.getLength();
		}
		else {
			int startPos = sizeModel.getPosition(getValue());
			int endPos = startPos + componentSize;
			if(endPos > sizeModel.getPreferredSize()) {
				endPos = sizeModel.getPreferredSize();
				startPos = endPos - componentSize;
				return Math.max(1, sizeModel.getLength() - (sizeModel.getIndex(startPos) + 1));
			}
			else {
				return Math.max(1, sizeModel.getIndex(endPos) - getValue());
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.BoundedRangeModel#getMaximum()
	 */
	@Override
	public int getMaximum() {
		return sizeModel.getLength();
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
}
