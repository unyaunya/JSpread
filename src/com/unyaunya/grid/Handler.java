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
 * キーボード、マウス入力ハンドラ
 * 
 * ・コントロールキー、シフトキーの押下状態を保持する。
 * ・マウスによる、列幅、行高さのリサイズを制御する。
 * 　（マウスカーソルの形状制御も行う。）
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
	 * セレクションモデルを取得する。
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
	 * マウスカーソル付近のリサイズ境界位置を取得する。
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
	 * 行・列ヘッダのボーダ付近であれば、リサイズカーソルをセットする。
	 * それ以外に行・列ヘッダ内であれば、行・列選択カーソルをセットする。
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		Point pt = e.getPoint();
		int row = grid.rowAtPoint(pt);
		int col = grid.columnAtPoint(pt);
		Cursor nextCursor = null;
		resizeBorderIndex = -1;

		//マウスカーソル付近のリサイズ境界位置を取得する。
		int colIndex = getNearbyResizeColumnBorderIndex(pt, row, col);
		int rowIndex = getNearbyResizeRowBorderIndex(pt, row, col);
		
		//マウスカーソルが、列方向のリサイズ境界付近にある場合
		if(colIndex != -1) {
			nextCursor = COLUMN_RESIZE_CURSOR;
			resizeBorderIndex = colIndex;
		}
		//マウスカーソルが、行方向のリサイズ境界付近にある場合
		else if(rowIndex != -1) {
			nextCursor = ROW_RESIZE_CURSOR;
			resizeBorderIndex = rowIndex;
		}
		//マウスカーソルが、ヘッダ部左上角にある場合
		else if(row == -1 && col == -1) {
			nextCursor = null;
		}
		//マウスカーソルが、列ヘッダにある場合
		else if(row == -1) {
			nextCursor = COLUMN_SELECT_CURSOR;
		}
		//マウスカーソルが、行ヘッダにある場合
		else if(col == -1) {
			nextCursor = ROW_SELECT_CURSOR;
		}

		//カーソルをセットする
		if(currentCursor != nextCursor) {
			grid.setCursor(nextCursor);
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
			getSelectionModel().onMousePressed(row, col, e.isShiftDown(), e.isControlDown());
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
			grid.getGridSelectionModel().onMouseDragged(row, col, e.isShiftDown(), e.isControlDown());
		}
	}
}
