package com.unyaunya.spread;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.unyaunya.swing.JSpread;

public class ScrollModel implements TableModelListener {
	private SizeModel colSizeModel;
	private SizeModel rowSizeModel;
	private RangeModel colRangeModel;
	private RangeModel rowRangeModel;
	private TableModel tableModel;

	public ScrollModel() {
		this(16, 60);
	}

	public ScrollModel(int defaultRowHeight, int defaultColumnWidth) {
		this.rowSizeModel = new SizeModel();
		this.colSizeModel = new SizeModel();
		this.rowRangeModel = new RangeModel(rowSizeModel);
		this.colRangeModel = new RangeModel(colSizeModel);
		this.setDefaultRowHeight(defaultRowHeight);
		this.setDefaultColumnWidth(defaultColumnWidth);
		
	}

	/**
	 * @return the tableModel
	 */
	public TableModel getTableModel() {
		return tableModel;
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
		case JSpread.HORIZONTAL:
			return colRangeModel;
		case JSpread.VERTICAL:
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
		case JSpread.HORIZONTAL:
			return colSizeModel;
		case JSpread.VERTICAL:
			return rowSizeModel;
		default:
			throw new RuntimeException("illegal direction value.");
		}
	}

	public Rectangle getCellRect(int rowIndex, int colIndex) {
		return new Rectangle(
					colSizeModel.getPosition(colIndex),
					rowSizeModel.getPosition(rowIndex),
					colSizeModel.getSize(colIndex),
					rowSizeModel.getSize(rowIndex));
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(
				colSizeModel.getPreferredSize(),
				rowSizeModel.getPreferredSize());
	}

	public int getRowPosition(int rowIndex) {
		return rowRangeModel.untranslate(rowSizeModel.getPosition(rowIndex));
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
	public int getColumnPosition(int columnIndex) {
		return colRangeModel.untranslate(colSizeModel.getPosition(columnIndex));
	}
	public int getColumnWidth(int columnIndex) {
		return colSizeModel.getSize(columnIndex);
	}
	
	public int rowAtPoint(Point pt) {
		return rowSizeModel.getIndex(rowRangeModel.translate(pt.y));
	}
	
	public int columnAtPoint(Point pt) {
		return colSizeModel.getIndex(colRangeModel.translate(pt.x));
	}
}
