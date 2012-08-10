package com.unyaunya.spread;

import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * JSpreadのスクロール処理を処理するクラス
 * 行方向/列方向それぞれに、内部的にRangeModelを保持している。
 * 
 * @author wata
 *
 */
public class ScrollModel implements ComponentListener, TableModelListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RangeModel colRangeModel;
	private RangeModel rowRangeModel;
	private TableModel tableModel;
	private JComponent component;

	public ScrollModel(JComponent c) {
		this(23, 60);
		setComponent(c);
	}

	private ScrollModel(int defaultRowHeight, int defaultColumnWidth) {
		this.rowRangeModel = new RangeModel();
		this.colRangeModel = new RangeModel();
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
	
	@Override
	public void tableChanged(TableModelEvent e) {
		rowRangeModel.reset(tableModel.getRowCount());
		colRangeModel.reset(tableModel.getColumnCount(), 40);
		this.rowRangeModel.setValue(0);
		this.colRangeModel.setValue(0);
	}

	/**
	 * スクロールバーを設定する。
	 */
	public void setScrollBar(JScrollBar horizontalBar, JScrollBar verticalBar) {
		horizontalBar.setModel(colRangeModel);
		verticalBar.setModel(rowRangeModel);
	}
	
	/**
	 * @return the rangeModel
	 */
	public RangeModel getRangeModel(int direction) {
		switch(direction) {
		case Adjustable.HORIZONTAL:
			return colRangeModel;
		case Adjustable.VERTICAL:
			return rowRangeModel;
		default:
			throw new RuntimeException("illegal direction value.");
		}
	}

	public Rectangle getGridRect(int rowIndex, int colIndex) {
		return new Rectangle(
					colRangeModel.getPosition(colIndex),
					rowRangeModel.getPosition(rowIndex),
					colRangeModel.getSize(colIndex),
					rowRangeModel.getSize(rowIndex));
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
		return rowRangeModel.getIndexFromDeviceCoord(pt.y);
	}
	
	public int columnAtPoint(Point pt) {
		return colRangeModel.getIndexFromDeviceCoord(pt.x);
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
		return (this.getFixedRowNum()!=1 || this.getFixedColumnNum()!=1);
	}

	public void freezePanes(int rowIndex, int columnIndex) {
		colRangeModel.setFixedPartNum(columnIndex);
		rowRangeModel.setFixedPartNum(rowIndex);
	}

	public void unfreezePanes() {
		colRangeModel.setFixedPartNum(1);
		rowRangeModel.setFixedPartNum(1);
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
