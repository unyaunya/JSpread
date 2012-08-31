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
 * �E�c�܂��͉������̃X�N���[���֘A���W�ϊ��������s���B
 * �E�X�N���[�����Ȃ��s�܂��͗񐔂��ݒ�ł���B
 * �EJSpreadPane���g�p����JScrollBar�ɃX�N���[���Ɋ֌W����l��`�B����B
 * �E���W�ϊ��y�уX�N���[���֌W�ʂ̎Z�o����ɂ́A�e�s/��̍���/���y�уR���|�[�l���g�̍���/�����K�v�ł���B
 * �E�e�s�܂��͗�̍����́A���̃��\�b�h�ŕύX����B
 * �@�@setDefaultSize(), setSize(), reset()
 * �E�R���|�[�l���g�̃T�C�Y(���܂��͍���)�́AScrollModel����񋟂��󂯂�B
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
class ScrollRangeModel implements BoundedRangeModel, Serializable {
    private static final Logger LOG = Logger.getLogger(ScrollRangeModel.class.getName());
	
	/**
	 * �e�s�̍����܂��͊e��̕�
	 */
	private SizeModel sizeModel;
	
	/**
	 * �R���|�[�l���g�̍����܂��͕�
	 */
	private int componentSize;
	
	/**
	 * �X�N���[�����Ȃ��Œ�̈�̍s�܂��͗�
	 */
	transient private int fixedPartNum;

	/**
	 * �X�N���[���l
	 */
	private int value;
	
	transient private int extent;

	/**
	 * �ύX���X�i�[�̃��X�g�B��{�I��JGridPane�̃X�N���[���o�[�����X�i�[�Ƃ��Đݒ肳���B
	 */
	transient private ArrayList<ChangeListener> changeListenerList;
	/**
	 * ���X�i�[�ւ̓`�B�Ɏg�p����C�x���g
	 */
	transient private ChangeEvent event;

	/**
	 * �R���X�g���N�^
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
	 * index�Ŏw�肳�ꂽ�s�܂��͗�̍���/�����擾����B
	 * @param index�@�s�܂��͗�̃C���f�b�N�X
	 * @return
	 */
	public int getSize(int index) {
		return sizeModel.getSize(index);
	}
	
	/**
	 * index�Ŏw�肳�ꂽ�s�܂��͗�̍���/����ݒ肷��B
	 * @param index�@�s�܂��͗�̃C���f�b�N�X
	 * @param width �s�܂��͗�̍���/��
	 */
	public void setSize(int index, int width) {
		sizeModel.setSize(index, width);
	}
	
	/**
	 * �s�܂��͗�̃f�t�H���g�̍���/����ݒ肷��B
	 * @param width�@�s�܂��͗�̃f�t�H���g�̍���/��
	 */
	public void setDefaultSize(int width) {
		sizeModel.setDefaultSize(width);
	}

	/**
	 * �s�܂��͗�̃f�t�H���g�̍���/�����擾����B
	 * @return
	 */
	public int getDefaultSize() {
		return sizeModel.getDefaultSize();
	}

	/**
	 * �X�N���[�����s�v�ȂɂȂ�R���|�[�l���g�̍����܂��͕����擾����B
	 * @return
	 */
	public int getPreferredSize() {
		return sizeModel.getPosition(sizeModel.getLength());
	}

	/**
	 * �Œ�̈�̍s�܂��͗񐔂�ݒ肷��B
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
	 * �Œ�̈�̍s�܂��͗񐔂��擾����B
	 * @return
	 */
	public int getFixedPartNum() {
		return fixedPartNum;
	}

	/**
	 * �R���|�[�l���g�̍����܂��͕����Z�b�g����B
	 * �X�N���[����extent�y��value���Čv�Z�����B
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
	 * �R���|�[�l���g�̍����܂��͕����擾����B
	 * @return
	 */
	public int getComponentSize() {
		return this.componentSize;
	}

	/**
	 * �X�N���[����extent�y��value�̕ύX��ʒm����B
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
	 * �Œ�̈�S�̂̍����܂��͕�
	 * @return
	 */
	public int getPreferredFixedPartSize() {
		return sizeModel.getPosition(getFixedPartNum());
	}

	/**
	 * �Œ�̈�̍����܂��͕��B
	 * �R���|�[�l���g�̈�̍����܂��͕��ɐ��񂳂�Ȃ���΁A�Œ�̈�S�̂̍����܂��͕��Ɠ������Ȃ�B
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
	 * index�Ŏw�肵���s�܂��͗�̊J�n�ʒu�B
	 * �X�N���[���l�����ꂽ�l���Z�o�����B
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
	 * �R���|�[�l���g���W���_�����W
	 */
	public int componentCoord2logicalCoord(int componentCoord) {
		if(componentCoord < getFixedPartSize()) {
			return componentCoord;
		}
		return sizeModel.getPosition(getFixedPartNum()+getValue()) + (componentCoord - getFixedPartSize());
	}

	/*
	 * �_�����W���R���|�[�l���g���W
	 */
	public int logicalCoord2componentCoord(int logicalCoord) {
		if(logicalCoord < getFixedPartSize()) {
			return logicalCoord;
		}
		return logicalCoord - sizeModel.getPosition(getFixedPartNum()+getValue()) + getFixedPartSize();
	}
	
	/**
	 * ���X�i�[��o�^����B
	 * @see javax.swing.BoundedRangeModel#addChangeListener(javax.swing.event.ChangeListener)
	 */
	@Override
	public void addChangeListener(ChangeListener l) {
		changeListenerList.add(l);
	}

	/**
	 * ���X�i�[���폜����
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
			return getMaximum();
		}
		//�ϕ����̃T�C�Y���K�v�ȃT�C�Y����������
		else {
			//�ϕ�����[�̘_���ʒu�F
			int startPos = sizeModel.getPosition(this.getFixedPartNum()+getValue());
			//- sizeModel.getPosition(this.getFixedPartNum());
			//�ϕ������[�̘_���ʒu�F
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
		//�Œ�̈悪�w�肳�ꂽ��AsetValue(0)
		if(index < getFixedPartNum()) {
			setValue(0);
		}
		//�w�肳�ꂽ�ӏ����ϗ̈�̑O�ɂ���ꍇ
		else if(index < (getFixedPartNum()+getValue())) {
			System.out.println("scrollToVisible:getFixedPartNum()");
			setValue(index - getFixedPartNum());
		}
		//�ϗ̈�̃T�C�Y���A�Z�����̂̃T�C�Y�����������ꍇ
		else if(getScrollPartSize() <= sizeModel.getSize(index)) {
			System.out.println("getScrollPartSize() <= size");
			setValue(index - getFixedPartNum());
		}
		//�w�肳�ꂽ�ӏ����ϗ̈�̌�ɂ���ꍇ
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
	 * �X�N���[���ʂ̍ŏ��l���擾����
	 * 
	 * @see javax.swing.BoundedRangeModel#getMinimum()
	 */
	@Override
	public int getMinimum() {
		return 0;
	}

	/**
	 * ���݂̃X�N���[���ʂ��擾����B
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
