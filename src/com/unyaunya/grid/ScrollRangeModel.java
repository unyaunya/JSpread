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
 * �EJGridPane���g�p����JScrollBar�ɃX�N���[���Ɋ֌W����l��`�B����B
 * �E���W�ϊ��y�уX�N���[���֌W�ʂ̎Z�o����ɂ́A�e�s/��̍���/���y�уR���|�[�l���g�̍���/�����K�v�ł���B
 * �E�e�s�܂��͗�̍����́A���̃��\�b�h�ŕύX����B
 * �@�@setDefaultSize(), setSize(), reset()
 * �E�R���|�[�l���g�̃T�C�Y(���܂��͍���)�́AScrollModel����񋟂��󂯂�B
 * 
 * 
 * getPreferredSize() = getFixedPartSize() + getScrollPartSize()
 * getFixedPartSize() = 
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
class ScrollRangeModel implements BoundedRangeModel, Serializable {
    private static final Logger LOG = Logger.getLogger(ScrollRangeModel.class.getName());
	
	/**
	 * �w�b�_���̍����܂��͊e��̕�
	 */
	private int headerSize;

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
	ScrollRangeModel(int headerSize) {
		setup();
		this.headerSize = headerSize;
		this.sizeModel = new SizeModel();
		setComponentSize(0);
	}
	
	private void setup() {
		changeListenerList = new ArrayList<ChangeListener>();
		event = new ChangeEvent(this);
		extent = 1;
		fixedPartNum = 0;
	}

	public void reset(int count) {
		reset(count, sizeModel.getDefaultSize());
	}

	public void reset(int count, int defaultSize) {
		sizeModel.reset(count, defaultSize);
	}

	public void insert(int index) {
		insert(index, 1);
	}
	
	public void insert(int index, int length) {
		sizeModel.insertEntries(index, length, getDefaultSize());
		fireChangeEvent();
	}
	
	public void remove(int index) {
		remove(index, 1);
	}

	public void remove(int index, int length) {
		sizeModel.removeEntries(index, length);
		fireChangeEvent();
	}
	
	public int getHeaderSize() {
		return headerSize;
	}

	/**
	 * index�Ŏw�肳�ꂽ�s�܂��͗�̍���/�����擾����B
	 * @param index�@�s�܂��͗�̃C���f�b�N�X
	 * @return
	 */
	public int getDisplaySize(int index) {
		if(index < 0) {
			return headerSize;
		}
		return sizeModel.getDisplaySize(index);
	}

	public int getCount() {
		return sizeModel.getLength();
	}
	
	/**
	 * index�Ŏw�肳�ꂽ�s�܂��͗�̍���/����ݒ肷��B
	 * @param index�@�s�܂��͗�̃C���f�b�N�X
	 * @param width �s�܂��͗�̍���/��
	 */
	public void setSize(int index, int width) {
		sizeModel.setSize(index, width);
	}

	public void setHidden(int index, boolean bHidden ) {
		sizeModel.setHidden(index, bHidden);
	}
	
	public boolean isHidden(int index) {
		return sizeModel.isDisplayed(index);
	}

	public int getLevel(int index) {
		return sizeModel.getLevel(index);
	}

	public boolean levelDown(int start, int length) {
		return sizeModel.levelDown(start, length);
	}
	
	public boolean levelUp(int start, int length) {
		return sizeModel.levelUp(start, length);
	}

	public void collapse(int index) {
		sizeModel.collapse(index);
	}

	public void expand(int index) {
		sizeModel.expand(index);
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
		//return headerSize + sizeModel.getPosition(sizeModel.getLength());
		return getPosition(sizeModel.getLength());
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
		return Math.max(0, fixedPartNum);
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
		//return headerSize + sizeModel.getPosition(getFixedPartNum());
		return getPosition(getFixedPartNum());
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
	
	int getIndex(int lc) {
		if(headerSize > lc) {
			return -1;
		}
		return sizeModel.getIndex(lc - headerSize);
	}

	/**
	 * ��ʍ��W�ɂ�����X�N���[���ʂ��擾����B
	 * @return
	 */
	public int getScrollAmount() {
		if(getValue() <= 0) {
			return 0;
		}
		return getPosition(getFixedPartNum()+getValue()) - getPosition(getFixedPartNum());
	}
	
	/**
	 * �R���|�[�l���g���W���_�����W
	 */
	public int viewToModel(int position) {
		//�R���|�[�l���g���W���Œ�̈���̏ꍇ
		if(position < getFixedPartSize()) {
			return position;
		}
		//�R���|�[�l���g���ϗ̈���̏ꍇ
		else {
			//delta : �ϗ̈�擪����̋���
			int delta = position - getFixedPartSize();
			//return = �ϗ̈�擪�̘_���ʒu + delta
			return getPosition(getFixedPartNum()+getValue()) + delta;
		}
	}

	/**
	 * �_�����W���R���|�[�l���g���W
	 */
	public int modelToView(int position) {
		//�_�����W���Œ�̈���̏ꍇ
		if(position < getFixedPartSize()) {
			return position;
		}
		//�_�����W���ϗ̈���̏ꍇ
		else {
			//delta : �ϗ̈�擪����̋���
			int delta = position - getPosition(getFixedPartNum()+getValue());
			if(delta >= 0) {
				return getFixedPartSize() + delta;
			}
			else {
				return Integer.MIN_VALUE;
			}
		}
	}

	/**
	 * index�Ŏw�肵���s�̏�[�܂��͗�̍��[�̘_�����W���擾����B
	 * @param index
	 * @return
	 */
	public int getPosition(int index) {
		if(index < 0) {
			return 0;
		}
		return headerSize + sizeModel.getPosition(index);
	}

	public int getDistance(int from_index, int to_index) {
		return getPosition(to_index) - getPosition(from_index);
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
	int calcExtent() {
		return Math.max(1, _calcExtent());
	}
	
	private int _calcExtent() {
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
				int startIndex = sizeModel.getIndex(startPos);
				if(startPos > getPosition(startIndex)) {
					startIndex += 1;
				}
				return getMaximum() - startIndex;
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
			LOG.info("scrollToVisible:getFixedPartNum()");
			setValue(index - getFixedPartNum());
		}
		//�ϗ̈�̃T�C�Y���A�Z�����̂̃T�C�Y�����������ꍇ
		else if(getScrollPartSize() <= sizeModel.getDisplaySize(index)) {
			LOG.info("getScrollPartSize() <= size");
			setValue(index - getFixedPartNum());
		}
		//�w�肳�ꂽ�ӏ����ϗ̈�̌�ɂ���ꍇ
		else {
			int gap = getScrollPartSize() - (sizeModel.getPosition(index+1) - sizeModel.getPosition(getFixedPartNum() + getValue()));
			LOG.info("scrollToVisible:getScrollPartSize() - (sizeModel.getPosition(index+1) - sizeModel.getPosition(getFixedPartNum() + getValue())) = " + gap);
			int n = 0;
			while(gap < 0) {
				gap += sizeModel.getDisplaySize(getFixedPartNum() + getValue()+n);
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
		fireChangeEvent();
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		setup();
	}
}
