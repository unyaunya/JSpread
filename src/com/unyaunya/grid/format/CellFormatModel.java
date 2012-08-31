package com.unyaunya.grid.format;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * セル書式データを格納するクラス
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class CellFormatModel implements Serializable {
	private ArrayList<RangedColor> foregroundColorList;
	private ArrayList<RangedColor> backgroundColorList;
	private ArrayList<RangedBorder> borderList;
	private ArrayList<RangedInteger> horizontalAlignmentList;
	private ArrayList<RangedInteger> verticalAlignmentList;

	/**
	 * コンストラクタ
	 */
	public CellFormatModel() {
		 foregroundColorList = new ArrayList<RangedColor>();
		 backgroundColorList = new ArrayList<RangedColor>();
		 borderList = new ArrayList<RangedBorder>();
		 horizontalAlignmentList = new ArrayList<RangedInteger>();
		 verticalAlignmentList = new ArrayList<RangedInteger>();
	}

	/**
	 * 書式リストを取得する。
	 */
	public ArrayList<RangedColor> getForegroundColorList() {
		return foregroundColorList;
	}
	public ArrayList<RangedColor> getBackgroundColorList() {
		return backgroundColorList;
	}
	public ArrayList<RangedBorder> getBorderList() {
		return borderList;
	}
	public ArrayList<RangedInteger> getHorizontalAlignmentList() {
		return horizontalAlignmentList;
	}
	public ArrayList<RangedInteger> getVerticalAlignmentList() {
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
		RangedColor rc = (RangedColor)getRangedObject(backgroundColorList, row, col);
		return (rc != null) ? rc.getColor() : null;
	}

	public Color getForegroundColor(int row, int col) {
		RangedColor rc = (RangedColor)getRangedObject(foregroundColorList, row, col);
		return (rc != null) ? rc.getColor() : null;
	}

	public Border getBorder(int row, int col) {
		RangedBorder rc = (RangedBorder)getRangedObject(borderList, row, col);
		return (rc != null) ? rc.getBorder() : null;
	}

	public int getHorizontalAlignment(int row, int col) {
		RangedInteger rc = (RangedInteger)getRangedObject(horizontalAlignmentList, row, col);
		if(rc == null) {
			return SwingConstants.LEFT;
		}
		Integer v = rc.getInteger();
		return (v != null) ? v.intValue() : null;
	}

	public int getVerticalAlignment(int row, int col) {
		RangedInteger rc = (RangedInteger)getRangedObject(verticalAlignmentList, row, col);
		if(rc == null) {
			return SwingConstants.TOP;
		}
		Integer v = rc.getInteger();
		return (v != null) ? v.intValue() : null;
	}

	static private RangedObject getRangedObject(ArrayList<?> list, int row, int col) {
		for(int i = 0; i < list.size(); i++) {
			RangedObject rf = (RangedObject)list.get(i);
			assert(rf != null);
			assert(rf.getRange() != null);
			if(rf.getRange().contains(row, col)) {
				return rf;
			}
		}
		return null;
	}
}
