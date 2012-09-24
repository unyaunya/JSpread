package com.unyaunya.grid;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * JGridのスクロール処理を処理するクラス
 * 行方向/列方向それぞれに、内部的にRangeModelを保持している。
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class ScrollModel implements ComponentListener, TableModelListener, Serializable {
    private static final Logger LOG = Logger.getLogger(ScrollModel.class.getName());

	/**
	 * 
	 */
	private ScrollRangeModel colRangeModel;
	private ScrollRangeModel rowRangeModel;
	private TableModel tableModel;
	private JComponent component;

	public ScrollModel(JComponent c, int columnHeaderheight, int rowHeaderWidth) {
		this(columnHeaderheight, rowHeaderWidth, 23, 60);
		setComponent(c);
	}

	private ScrollModel(int columnHeaderheight, int rowHeaderWidth, int defaultRowHeight, int defaultColumnWidth) {
		this.rowRangeModel = new ScrollRangeModel(columnHeaderheight);
		this.colRangeModel = new ScrollRangeModel(rowHeaderWidth);
		this.setDefaultRowHeight(defaultRowHeight);
		this.setDefaultColumnWidth(defaultColumnWidth);
	}

	/**
	 * スクロール対象のコンポーネントを設定する。
	 */
	private void setComponent(JComponent component) {
		if(this.component != null) {
			throw new RuntimeException("Can't reset the component");
		}
		this.component = component;
		component.addComponentListener(this);
		colRangeModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ScrollModel.this.component.repaint();
			}
		});
		rowRangeModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ScrollModel.this.component.repaint();
			}
		});
		this.component.repaint();
	}
	
	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
		tableModel.addTableModelListener(this);
		tableChanged(new TableModelEvent(tableModel));
	}
	
	/**
	 * テーブルの行数を取得する
	 * @return
	 */
	public int getRowCount() {
		return tableModel.getRowCount();
	}
	
	/**
	 * テーブルの列数を取得する
	 * @return
	 */
	public int getColumnCount() {
		return tableModel.getColumnCount();
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		LOG.info("TableModelEvent:type=" + e.getType());
		LOG.info("TableModelEvent:firstRow, lastRow, column=" + e.getFirstRow() + "," + e.getLastRow() + "," + e.getColumn());
		if(e.getFirstRow() == 0 && e.getLastRow() == Integer.MAX_VALUE && e.getColumn() == TableModelEvent.ALL_COLUMNS) {
			rowRangeModel.reset(tableModel.getRowCount());
			colRangeModel.reset(tableModel.getColumnCount(), 40);
			this.rowRangeModel.setValue(0);
			this.colRangeModel.setValue(0);
		}
		else {
			switch(e.getType()) {
			case TableModelEvent.INSERT:
				if(e.getColumn() == TableModelEvent.ALL_COLUMNS) {
					rowRangeModel.insert(e.getFirstRow(), e.getLastRow() - e.getFirstRow() + 1);
				}
				else {
					colRangeModel.insert(e.getColumn(), 1);
				}
				break;
			case TableModelEvent.DELETE:
				if(e.getColumn() == TableModelEvent.ALL_COLUMNS) {
					rowRangeModel.remove(e.getFirstRow(), e.getLastRow() - e.getFirstRow() + 1);
				}
				else {
					colRangeModel.remove(e.getColumn());
				}
				break;
			case TableModelEvent.UPDATE:
				break;
			default:
				break;
			}
		}
	}

	/**
	 * スクロールバーを設定する。
	 */
	public void setScrollBar(JScrollBar horizontalBar, JScrollBar verticalBar) {
		horizontalBar.setModel(colRangeModel);
		verticalBar.setModel(rowRangeModel);
	}

	public Rectangle getGridRect(int rowIndex, int colIndex) {
		return new Rectangle(
					colRangeModel.getPosition(colIndex),
					rowRangeModel.getPosition(rowIndex),
					colRangeModel.getSize(colIndex),
					rowRangeModel.getSize(rowIndex));
	}

	public Rectangle getRangeRect(IRange r) {
		assert(r != null);
		int top = r.getTop();
		int left = r.getLeft();
		int bottom = r.getBottom();
		int right = r.getRight();
		Rectangle cellRect = new Rectangle();
		cellRect.y = getRowPosition(top);
		cellRect.height = rowRangeModel.getDistance(top, bottom+1);
		cellRect.x = getColumnPosition(left);
		cellRect.width = colRangeModel.getDistance(left, right+1);
		return cellRect;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(
				colRangeModel.getPreferredSize(),
				rowRangeModel.getPreferredSize());
	}

	public int getRowPosition(int rowIndex) {
		return rowRangeModel.getPosition(rowIndex);
	}
	public int getDefaultRowHeight() {
		return rowRangeModel.getDefaultSize();
	}
	public void setDefaultRowHeight(int height) {
		rowRangeModel.setDefaultSize(height);
	}
	public int getDefaultColumnWidth() {
		return colRangeModel.getDefaultSize();
	}
	public void setDefaultColumnWidth(int width) {
		colRangeModel.setDefaultSize(width);
	}
	public int getRowHeight(int rowIndex) {
		return rowRangeModel.getSize(rowIndex);
	}
	
	public void setRowHeight(int rowIndex, int height) {
		rowRangeModel.setSize(rowIndex, height);
	}

	public int getColumnPosition(int columnIndex) {
		return colRangeModel.getPosition(columnIndex);
	}
	public int getColumnWidth(int columnIndex) {
		return colRangeModel.getSize(columnIndex);
	}
	public void setColumnWidth(int colIndex, int width) {
		colRangeModel.setSize(colIndex, width);
	}
	
	public int rowAtPoint(Point pt) {
		return rowRangeModel.getIndexFromCC(pt.y);
	}
	
	public int columnAtPoint(Point pt) {
		return colRangeModel.getIndexFromCC(pt.x);
	}

	public int getFixedRowNum() {
		return rowRangeModel.getFixedPartNum();
	}

	public int getFixedColumnNum() {
		return colRangeModel.getFixedPartNum();
	}

	public int getFixedAreaHight() {
		return rowRangeModel.getFixedPartSize();
	}
	public int getFixedAreaWidth() {
		return colRangeModel.getFixedPartSize();
	}
	public int getScrollAreaHeight() {
		return rowRangeModel.getScrollPartSize();
	}
	public int getScrollAreaWidth() {
		return colRangeModel.getScrollPartSize();
	}

	/**
	 * 行方向のextentを取得する。
	 * @return
	 */
	public int getRowExtent() {
		return rowRangeModel.getExtent();
	}
	/**
	 * 列方向のextentを取得する。
	 * @return
	 */
	public int getColumnExtent() {
		return colRangeModel.getExtent();
	}

	public void scrollToVisible(int rowIndex, int columnIndex) {
		colRangeModel.scrollToVisible(columnIndex);
		rowRangeModel.scrollToVisible(rowIndex);
	}

	public boolean arePanesFreezed() {
		return (this.getFixedRowNum()!=0 || this.getFixedColumnNum()!=0);
	}

	public void freezePanes(int rowIndex, int columnIndex) {
		colRangeModel.setFixedPartNum(columnIndex);
		rowRangeModel.setFixedPartNum(rowIndex);
	}

	public void unfreezePanes() {
		colRangeModel.setFixedPartNum(0);
		rowRangeModel.setFixedPartNum(0);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		//nothing to do
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		//nothing to do
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Component c = e.getComponent();
		colRangeModel.setComponentSize(c.getWidth());
		rowRangeModel.setComponentSize(c.getHeight());
	}

	@Override
	public void componentShown(ComponentEvent e) {
		//nothing to do
	}
}
