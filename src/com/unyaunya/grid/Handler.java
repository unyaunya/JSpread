package com.unyaunya.grid;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.event.MouseInputAdapter;

import com.unyaunya.swing.JGrid;

public class Handler extends MouseInputAdapter implements KeyListener {
    private static final Logger LOG = Logger.getLogger(Handler.class.getName());

	static final int RESIZE_ZONE_WIDTH = 3;
	private final Cursor ROW_SELECT_CURSOR = createCursor("icons/arrow_right.gif", new Point(15, 15));
	private final Cursor COLUMN_SELECT_CURSOR = createCursor("icons/arrow_down.gif", new Point(15, 15));
	private Cursor COLUMN_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
	private Cursor ROW_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);
	private Cursor currentCursor = null;
	private int resizeBorderIndex = 0;
	private boolean shiftDown;
	private boolean controlDown;
	
	private JGrid grid;

	public Handler(JGrid grid) {
		this.grid = grid;
	}
	
	private Cursor createCursor(String name, Point hotSpot) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		URL url = getClass().getClassLoader().getResource(name);
		Image img = kit.createImage(url); 
		Cursor cursor = kit.createCustomCursor(img, hotSpot, name);
		return cursor;
	}
	
	private boolean isShiftDown() {
		return shiftDown;
	}

	private void setShiftDown(boolean value) {
		shiftDown = value;
	}

	private boolean isControlDown() {
		return controlDown;
	}

	private void setControlDown(boolean value) {
		controlDown = value;
	}
	
	private int getNearbyResizeColumnBorderIndex(Point pt, int row, int col) {
		if(row != 0) {
			return 0;
		}
		if(col == 0) {
			return 0;
		}
		int left = grid.getScrollModel().getColumnPosition(col);
		if((col != 1) && ((pt.x - left) < RESIZE_ZONE_WIDTH)) {
			return col;
		}
		int right = grid.getScrollModel().getColumnPosition(col+1);
		if((right - pt.x) < RESIZE_ZONE_WIDTH) {
			return col + 1;
		}
		return 0;
	}

	private int getNearbyResizeRowBorderIndex(Point pt, int row, int col) {
		if(col != 0) {
			return 0;
		}
		if(row == 0) {
			return 0;
		}
		int top = grid.getScrollModel().getRowPosition(row);
		if((row != 1) && ((pt.y - top) < RESIZE_ZONE_WIDTH)) {
			return row;
		}
		int bottom = grid.getScrollModel().getRowPosition(row+1);
		if((bottom - pt.y) < RESIZE_ZONE_WIDTH) {
			return row+1;
		}
		return 0;
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
	
	@Override
	public void mouseMoved(MouseEvent e) {
		Point pt = e.getPoint();
		int row = grid.rowAtPoint(pt);
		int col = grid.columnAtPoint(pt);
		Cursor nextCursor = null;
		resizeBorderIndex = 0;
		int colIndex = getNearbyResizeColumnBorderIndex(pt, row, col);
		int rowIndex = getNearbyResizeRowBorderIndex(pt, row, col);
		if(colIndex != 0) {
			nextCursor = COLUMN_RESIZE_CURSOR;
			resizeBorderIndex = colIndex;
		}
		else if(rowIndex != 0) {
			nextCursor = ROW_RESIZE_CURSOR;
			resizeBorderIndex = rowIndex;
		}
		else if(row == 0 && col == 0) {
			nextCursor = null;
		}
		else if(row == 0) {
			nextCursor = COLUMN_SELECT_CURSOR;
		}
		else if(col == 0) {
			nextCursor = ROW_SELECT_CURSOR;
		}

		if(currentCursor != nextCursor) {
			if(nextCursor != null) {
				grid.setCursor(nextCursor);
			}
			else {
				grid.setCursor(null);
				//LOG.info("("+row+","+col+")");
			}
			currentCursor = nextCursor;
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * リードセル　　　　　：　キー入力対象セル。
	 * 修飾キーなし　　　：　セレクションをクリアしてから、クリックされたセルを選択する。
	 *					クリックされたセルはリードかつアンカーになる。
	 * SHIFTキー押下時：　現在のRangeオブジェクトを、アンカーとクリックされたセルを含めるものに変更する。
	 *					リードセルは、クリックされたセル。
	 * CTRLキー押下時：  　現在のRangeオブジェクトを、アンカーとクリックされたセルを含めるものに変更する。
	 *					リードセルは、クリックされたセル。
	 * 
	 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		LOG.info("mousePressed");
		if(currentCursor == ROW_RESIZE_CURSOR) {
			LOG.info("行リサイズ開始");
		}
		else if(currentCursor == COLUMN_RESIZE_CURSOR) {
			LOG.info("列リサイズ開始");
		}
		else {
			Point pt = e.getPoint();
			int row = grid.rowAtPoint(pt);
			int col = grid.columnAtPoint(pt);
			grid._select(row, col, e.isShiftDown(), e.isControlDown());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		LOG.info("mouseReleased");
		if(currentCursor == ROW_RESIZE_CURSOR) {
			LOG.info("行リサイズ終了");
		}
		else if(currentCursor == COLUMN_RESIZE_CURSOR) {
			LOG.info("列リサイズ終了");
		}
		else if(currentCursor == ROW_SELECT_CURSOR) {
			LOG.info("行選択終了");
		}
		else if(currentCursor == COLUMN_SELECT_CURSOR) {
			LOG.info("列選択終了");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * リードセル　　　　　：　キー入力対象セル。
	 * 修飾キーなし　　　：　セレクションをクリアしてから、クリックされたセルを選択する。
	 *					クリックされたセルはリードかつアンカーになる。
	 * SHIFTキー押下時：　現在のRangeオブジェクトを、アンカーとクリックされたセルを含めるものに変更する。
	 *					リードセルは、クリックされたセル。
	 * マウスドラッグ時　　：　現在のRangeオブジェクトを変更する。
	 * 
	 * @see java.awt.event.MouseAdapter#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		LOG.info("mouseDragged");
		if(currentCursor == ROW_RESIZE_CURSOR) {
			int width = e.getPoint().y - grid.getScrollModel().getRowPosition(resizeBorderIndex-1);
			LOG.info("行リサイズ:"+resizeBorderIndex+"=>"+width);
			grid.getRows().setHeight(resizeBorderIndex-1, width);
			grid.repaint();
		}
		else if(currentCursor == COLUMN_RESIZE_CURSOR) {
			int height = e.getPoint().x - grid.getScrollModel().getColumnPosition(resizeBorderIndex-1);
			LOG.info("列リサイズ:"+resizeBorderIndex+"=>"+height);
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
			grid.getGridSelectionModel().focus(row, col);
		}
	}

	private void move(int deltaRow, int deltaColumn) {
		LOG.info("SHIFT="+isShiftDown()+",CTRL="+isControlDown());
		int currentRow = grid.getGridSelectionModel().getFocusedRow();
		int currentCol = grid.getGridSelectionModel().getFocusedColumn();
		ICellRange range = grid.getCellRange(currentRow, currentCol);
		int row = currentRow+deltaRow;
		int col = currentCol+deltaColumn;
		if(range != null) {
			if(deltaRow > 0) {
				if(row <= range.getBottom()) {
					row = range.getBottom() + 1;
				}
			}
			else if(deltaRow < 0) {
				if(row >= range.getTop()) {
					row = range.getTop() - 1;
				}
			}
			if(deltaColumn > 0) {
				if(col <= range.getRight()) {
					col = range.getRight() + 1;
				}
			}
			else if(deltaColumn < 0) {
				if(col >= range.getLeft()) {
					col = range.getLeft() - 1;
				}
			}
		}
		grid.select(row, col, isShiftDown(), isControlDown());
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			setShiftDown(true);
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
			setControlDown(true);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			setShiftDown(false);
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
			setControlDown(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void left() {
		move(0, -1);
	}
	public void right() {
		move(0, +1);
	}
	public void up() {
		move(-1, 0);
	}
	public void down() {
		move(+1, 0);
	}
	public void pageLeft() {
		move(0, - grid.getScrollModel().getColumnExtent());
	}
	public void pageRight() {
		move(0, + grid.getScrollModel().getColumnExtent());
	}
	public void pageUp() {
		move(- grid.getScrollModel().getRowExtent(), 0);
	}
	public void pageDown() {
		move(+ grid.getScrollModel().getRowExtent(), 0);
	}
}
