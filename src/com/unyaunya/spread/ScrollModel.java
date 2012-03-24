package com.unyaunya.spread;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ScrollModel implements TableModelListener, Serializable {
	private SizeModel colSizeModel;
	private SizeModel rowSizeModel;
	private RangeModel colRangeModel;
	private RangeModel rowRangeModel;
	private TableModel tableModel;

	public ScrollModel() {
		this(23, 60);
	}

	private ScrollModel(int defaultRowHeight, int defaultColumnWidth) {
		this.rowSizeModel = new SizeModel();
		this.colSizeModel = new SizeModel();
		this.rowRangeModel = new RangeModel(rowSizeModel);
		this.colRangeModel = new RangeModel(colSizeModel);
		this.setDefaultRowHeight(defaultRowHeight);
		this.setDefaultColumnWidth(defaultColumnWidth);
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
		rowSizeModel.removeAll();
		rowSizeModel.insertEntries(0, tableModel.getRowCount(), rowSizeModel.getDefaultSize());
		colSizeModel.removeAll();
		colSizeModel.insertEntries(0, tableModel.getColumnCount(), colSizeModel.getDefaultSize());
		colSizeModel.setSize(0, 40);
		this.rowRangeModel.setValue(0);
		this.colRangeModel.setValue(0);
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

	/**
	 * @return the sizeModel
	 */
	public SizeModel getSizeModel(int direction) {
		switch(direction) {
		case Adjustable.HORIZONTAL:
			return colSizeModel;
		case Adjustable.VERTICAL:
			return rowSizeModel;
		default:
			throw new RuntimeException("illegal direction value.");
		}
	}

	public Rectangle getGridRect(int rowIndex, int colIndex) {
		return new Rectangle(
					colRangeModel.getPosition(colIndex),
					rowRangeModel.getPosition(rowIndex),
					colSizeModel.getSize(colIndex),
					rowSizeModel.getSize(rowIndex));
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(
				colSizeModel.getPreferredSize(),
				rowSizeModel.getPreferredSize());
	}

	public int getRowExtent() {
		return rowRangeModel.getExtent();
	}
	public int getColumnExtent() {
		return colRangeModel.getExtent();
	}

	public int getRowPosition(int rowIndex) {
		return rowRangeModel.getPosition(rowIndex);
	}
	public int getDefaultRowHeight() {
		return rowSizeModel.getDefaultSize();
	}
	public void setDefaultRowHeight(int height) {
		rowSizeModel.setDefaultSize(height);
	}
	public int getDefaultColumnWidth() {
		return colSizeModel.getDefaultSize();
	}
	public void setDefaultColumnWidth(int width) {
		colSizeModel.setDefaultSize(width);
	}
	public int getRowHeight(int rowIndex) {
		return rowSizeModel.getSize(rowIndex);
	}
	public void setRowHeight(int rowIndex, int height) {
		rowSizeModel.setSize(rowIndex, height);
	}

	public int getColumnPosition(int columnIndex) {
		return colRangeModel.getPosition(columnIndex);
	}
	public int getColumnWidth(int columnIndex) {
		return colSizeModel.getSize(columnIndex);
	}
	public void setColumnWidth(int colIndex, int width) {
		colSizeModel.setSize(colIndex, width);
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

	public int getFixedRowSize() {
		return rowRangeModel.getFixedPartSize();
	}

	public int getFixedColumnSize() {
		return colRangeModel.getFixedPartSize();
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

}
