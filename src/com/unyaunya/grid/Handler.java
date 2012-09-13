package com.unyaunya.grid;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.event.MouseInputAdapter;

import com.unyaunya.grid.selection.IGridSelectionModel;
import com.unyaunya.swing.JGrid;

/**
 * 
 * �L�[�{�[�h�A�}�E�X���̓n���h��
 * 
 * �E�R���g���[���L�[�A�V�t�g�L�[�̉�����Ԃ�ێ�����B
 * �E�}�E�X�ɂ��A�񕝁A�s�����̃��T�C�Y�𐧌䂷��B
 * �@�i�}�E�X�J�[�\���̌`�󐧌���s���B�j
 * 
 * @author wata
 *
 */
public class Handler extends MouseInputAdapter {
    private static final Logger LOG = Logger.getLogger(Handler.class.getName());

	static final int RESIZE_ZONE_WIDTH = 3;
	private final Cursor ROW_SELECT_CURSOR = createCursor("icons/arrow_right.gif", new Point(15, 15));
	private final Cursor COLUMN_SELECT_CURSOR = createCursor("icons/arrow_down.gif", new Point(15, 15));
	private Cursor COLUMN_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
	private Cursor ROW_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
	private Cursor currentCursor = null;
	private int resizeBorderIndex = -1;
	
	private JGrid grid;

	public Handler(JGrid grid) {
		this.grid = grid;
	}

	/**
	 * �Z���N�V�������f�����擾����B
	 * @return
	 */
	private IGridSelectionModel getSelectionModel() {
		return grid.getGridSelectionModel();
	}
	
	private Cursor createCursor(String name, Point hotSpot) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		URL url = getClass().getClassLoader().getResource(name);
		Image img = kit.createImage(url); 
		Cursor cursor = kit.createCustomCursor(img, hotSpot, name);
		return cursor;
	}
	
	/**
	 * �}�E�X�J�[�\���t�߂̃��T�C�Y���E�ʒu���擾����B
	 */
	private int getNearbyResizeColumnBorderIndex(Point pt, int row, int col) {
		if(row != -1) {
			return -1;
		}
		if(col == -1) {
			return -1;
		}
		int left = grid.getScrollModel().getColumnPosition(col);
		if((col != 0) && ((pt.x - left) < RESIZE_ZONE_WIDTH)) {
			return col;
		}
		int right = grid.getScrollModel().getColumnPosition(col+1);
		if((right - pt.x) < RESIZE_ZONE_WIDTH) {
			return col + 1;
		}
		return -1;
	}

	private int getNearbyResizeRowBorderIndex(Point pt, int row, int col) {
		if(col != -1) {
			return -1;
		}
		if(row == -1) {
			return -1;
		}
		int top = grid.getScrollModel().getRowPosition(row);
		if((row != 0) && ((pt.y - top) < RESIZE_ZONE_WIDTH)) {
			return row;
		}
		int bottom = grid.getScrollModel().getRowPosition(row+1);
		if((bottom - pt.y) < RESIZE_ZONE_WIDTH) {
			return row+1;
		}
		return -1;
	}

	/*
	private Cursor getNextCursor(Point pt, int row, int col) {
		int colIndex = getNearbyResizeColumnBorderIndex(pt, row, col);
		if(colIndex != 0) {
			return COLUMN_RESIZE_CURSOR;
		}
		int rowIndex = getNearbyResizeRowBorderIndex(pt, row, col);
		if(rowIndex != 0) {
			return ROW_RESIZE_CURSOR;
		}
		return null;
	}
	*/

	/**
	 * �s�E��w�b�_�̃{�[�_�t�߂ł���΁A���T�C�Y�J�[�\�����Z�b�g����B
	 * ����ȊO�ɍs�E��w�b�_���ł���΁A�s�E��I���J�[�\�����Z�b�g����B
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		Point pt = e.getPoint();
		int row = grid.rowAtPoint(pt);
		int col = grid.columnAtPoint(pt);
		Cursor nextCursor = null;
		resizeBorderIndex = -1;

		//�}�E�X�J�[�\���t�߂̃��T�C�Y���E�ʒu���擾����B
		int colIndex = getNearbyResizeColumnBorderIndex(pt, row, col);
		int rowIndex = getNearbyResizeRowBorderIndex(pt, row, col);
		
		//�}�E�X�J�[�\�����A������̃��T�C�Y���E�t�߂ɂ���ꍇ
		if(colIndex != -1) {
			nextCursor = COLUMN_RESIZE_CURSOR;
			resizeBorderIndex = colIndex;
		}
		//�}�E�X�J�[�\�����A�s�����̃��T�C�Y���E�t�߂ɂ���ꍇ
		else if(rowIndex != -1) {
			nextCursor = ROW_RESIZE_CURSOR;
			resizeBorderIndex = rowIndex;
		}
		//�}�E�X�J�[�\�����A�w�b�_������p�ɂ���ꍇ
		else if(row == -1 && col == -1) {
			nextCursor = null;
		}
		//�}�E�X�J�[�\�����A��w�b�_�ɂ���ꍇ
		else if(row == -1) {
			nextCursor = COLUMN_SELECT_CURSOR;
		}
		//�}�E�X�J�[�\�����A�s�w�b�_�ɂ���ꍇ
		else if(col == -1) {
			nextCursor = ROW_SELECT_CURSOR;
		}

		//�J�[�\�����Z�b�g����
		if(currentCursor != nextCursor) {
			grid.setCursor(nextCursor);
			currentCursor = nextCursor;
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * ���[�h�Z���@�@�@�@�@�F�@�L�[���͑ΏۃZ���B
	 * �C���L�[�Ȃ��@�@�@�F�@�Z���N�V�������N���A���Ă���A�N���b�N���ꂽ�Z����I������B
	 *					�N���b�N���ꂽ�Z���̓��[�h���A���J�[�ɂȂ�B
	 * SHIFT�L�[�������F�@���݂�Range�I�u�W�F�N�g���A�A���J�[�ƃN���b�N���ꂽ�Z�����܂߂���̂ɕύX����B
	 *					���[�h�Z���́A�N���b�N���ꂽ�Z���B
	 * CTRL�L�[�������F  �@���݂�Range�I�u�W�F�N�g���A�A���J�[�ƃN���b�N���ꂽ�Z�����܂߂���̂ɕύX����B
	 *					���[�h�Z���́A�N���b�N���ꂽ�Z���B
	 * 
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		LOG.info("mousePressed");
		if(currentCursor == ROW_RESIZE_CURSOR) {
			LOG.info("�s���T�C�Y�J�n");
		}
		else if(currentCursor == COLUMN_RESIZE_CURSOR) {
			LOG.info("�񃊃T�C�Y�J�n");
		}
		else {
			Point pt = e.getPoint();
			int row = grid.rowAtPoint(pt);
			int col = grid.columnAtPoint(pt);
			getSelectionModel().onMousePressed(row, col, e.isShiftDown(), e.isControlDown());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		LOG.info("mouseReleased");
		if(currentCursor == ROW_RESIZE_CURSOR) {
			LOG.info("�s���T�C�Y�I��");
		}
		else if(currentCursor == COLUMN_RESIZE_CURSOR) {
			LOG.info("�񃊃T�C�Y�I��");
		}
		else if(currentCursor == ROW_SELECT_CURSOR) {
			LOG.info("�s�I���I��");
		}
		else if(currentCursor == COLUMN_SELECT_CURSOR) {
			LOG.info("��I���I��");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * ���[�h�Z���@�@�@�@�@�F�@�L�[���͑ΏۃZ���B
	 * �C���L�[�Ȃ��@�@�@�F�@�Z���N�V�������N���A���Ă���A�N���b�N���ꂽ�Z����I������B
	 *					�N���b�N���ꂽ�Z���̓��[�h���A���J�[�ɂȂ�B
	 * SHIFT�L�[�������F�@���݂�Range�I�u�W�F�N�g���A�A���J�[�ƃN���b�N���ꂽ�Z�����܂߂���̂ɕύX����B
	 *					���[�h�Z���́A�N���b�N���ꂽ�Z���B
	 * �}�E�X�h���b�O���@�@�F�@���݂�Range�I�u�W�F�N�g��ύX����B
	 * 
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		LOG.info("mouseDragged");
		if(currentCursor == ROW_RESIZE_CURSOR) {
			int width = e.getPoint().y - grid.getScrollModel().getRowPosition(resizeBorderIndex-1);
			LOG.info("�s���T�C�Y:"+resizeBorderIndex+"=>"+width);
			grid.getRows().setHeight(resizeBorderIndex-1, width);
			grid.repaint();
		}
		else if(currentCursor == COLUMN_RESIZE_CURSOR) {
			int height = e.getPoint().x - grid.getScrollModel().getColumnPosition(resizeBorderIndex-1);
			LOG.info("�񃊃T�C�Y:"+resizeBorderIndex+"=>"+height);
			grid.getColumns().setWidth(resizeBorderIndex-1, height);
			grid.repaint();
		}
		else {
			Point pt = e.getPoint();
			int row = grid.rowAtPoint(pt);
			int col = grid.columnAtPoint(pt);
			if(currentCursor == COLUMN_SELECT_CURSOR) {
				row = 0;
				if(col == 0) {
					col = 1;
				}
			}
			else if(currentCursor == ROW_SELECT_CURSOR) {
				col = 0;
				if(row == 0) {
					row = 1;
				}
			}
			else {
				//if(row != 0 && col != 0) {
				//}
			}
			grid.getGridSelectionModel().onMouseDragged(row, col, e.isShiftDown(), e.isControlDown());
		}
	}
}
