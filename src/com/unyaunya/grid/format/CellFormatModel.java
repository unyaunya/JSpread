package com.unyaunya.grid.format;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.unyaunya.grid.IRange;
import com.unyaunya.grid.RangeUtil;

/**
 * セル書式データを格納するクラス
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class CellFormatModel implements TableModelListener, Serializable {
	private ArrayList<IRange> foregroundColorList;
	private ArrayList<IRange> backgroundColorList;
	private ArrayList<IRange> borderList;
	private ArrayList<IRange> horizontalAlignmentList;
	private ArrayList<IRange> verticalAlignmentList;

	/**
	 * コンストラクタ
	 */
	public CellFormatModel() {
		 foregroundColorList = new ArrayList<IRange>();
		 backgroundColorList = new ArrayList<IRange>();
		 borderList = new ArrayList<IRange>();
		 horizontalAlignmentList = new ArrayList<IRange>();
		 verticalAlignmentList = new ArrayList<IRange>();
	}

	/**
	 * 書式リストを取得する。
	 */
	public ArrayList<IRange> getForegroundColorList() {
		return foregroundColorList;
	}
	public ArrayList<IRange> getBackgroundColorList() {
		return backgroundColorList;
	}
	public ArrayList<IRange> getBorderList() {
		return borderList;
	}
	public ArrayList<IRange> getHorizontalAlignmentList() {
		return horizontalAlignmentList;
	}
	public ArrayList<IRange> getVerticalAlignmentList() {
		return verticalAlignmentList;
	}
	
	public void addBackgroundColor(RangedColor rangedColor) {
		backgroundColorList.add(0, rangedColor);
	}
	public void addForegroundColor(RangedColor rangedColor) {
		foregroundColorList.add(0, rangedColor);
	}
	public void addBorder(RangedBorder rangedBorder) {
		borderList.add(0, rangedBorder);
	}
	public void addHorizontalAlignment(RangedInteger rangedInteger) {
		horizontalAlignmentList.add(0, rangedInteger);
	}
	public void addVerticalAlignment(RangedInteger rangedInteger) {
		verticalAlignmentList.add(0, rangedInteger);
	}

	public Color getBackgroundColor(int row, int col) {
		RangedColor rc = (RangedColor)RangeUtil.getRange(backgroundColorList, row, col);
		return (rc != null) ? rc.getColor() : null;
	}

	public Color getForegroundColor(int row, int col) {
		RangedColor rc = (RangedColor)RangeUtil.getRange(foregroundColorList, row, col);
		return (rc != null) ? rc.getColor() : null;
	}

	public Border getBorder(int row, int col) {
		RangedBorder rc = (RangedBorder)RangeUtil.getRange(borderList, row, col);
		return (rc != null) ? rc.getBorder() : null;
	}

	public int getHorizontalAlignment(int row, int col) {
		RangedInteger rc = (RangedInteger)RangeUtil.getRange(horizontalAlignmentList, row, col);
		if(rc == null) {
			return SwingConstants.LEFT;
		}
		Integer v = rc.getInteger();
		return (v != null) ? v.intValue() : null;
	}

	public int getVerticalAlignment(int row, int col) {
		RangedInteger rc = (RangedInteger)RangeUtil.getRange(verticalAlignmentList, row, col);
		if(rc == null) {
			return SwingConstants.TOP;
		}
		Integer v = rc.getInteger();
		return (v != null) ? v.intValue() : null;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		RangeUtil.tableChanged(foregroundColorList, e);
		RangeUtil.tableChanged(backgroundColorList, e);
		RangeUtil.tableChanged(borderList, e);
		RangeUtil.tableChanged(horizontalAlignmentList, e);
		RangeUtil.tableChanged(verticalAlignmentList, e);
	}
	
}
