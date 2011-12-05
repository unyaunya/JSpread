/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.JPanel;
import javax.swing.SizeSequence;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.TableModel;

/**
 * @author wata
 *
 */
public class JSpread extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Color DEFAULT_HEADER_BACKGROUND_COLOR = new Color(0xf0,0xf0,0xf0);
	private SizeSequence rowSizes = new SizeSequence();
	private SizeSequence colSizes = new SizeSequence();
	private CellRendererPane rendererPane = new CellRendererPane();
	private int leftMostColumn = 0;
	private int topMostRow = 0;
	
	protected SpreadModel model = new SpreadModel();
	protected ICellRenderer defaultCellRenderer = new DefaultCellRenderer();
	protected Border borderForHeader = BorderFactory.createMatteBorder(0,0,1,1,Color.GRAY);
	protected Border defaultBorder = BorderFactory.createMatteBorder(0,0,1,1,Color.GRAY);;

	public JSpread() {
		this.add(rendererPane);
		defaultCellRenderer.setBorder(defaultBorder);
	}
	public void setModel(TableModel model) {
		if(model == null) {
			model = new SpreadModel();
		}
		if(!(model instanceof SpreadModel)) {
			model = new SpreadModel(model);
		}
		this.model = (SpreadModel)model;
		rowSizes.insertEntries(0, model.getRowCount(), 20);
		colSizes.insertEntries(0, model.getColumnCount(), 80);
		colSizes.insertEntries(0, 1, 40);
	}
	public SpreadModel getModel() {
		return model;
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		SpreadModel m = getModel();
		Rectangle clip = g.getClipBounds();
		Rectangle bounds = this.getBounds();
		bounds.x = bounds.y = 0;
		if (m.getRowCount() <= 0 || m.getColumnCount() <= 0 || !bounds.intersects(clip)) {
            // this check prevents us from painting the entire table
            // when the clip doesn't intersect our bounds at all
			return;
		}
		Point upperLeft = clip.getLocation();
	    Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
		paintCells(g, rowAtPoint(upperLeft), rowAtPoint(lowerRight), columnAtPoint(upperLeft), columnAtPoint(lowerRight));
	}

	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
		SpreadModel m = getModel();
		String s;
		Rectangle cellRect = new Rectangle();
		for(int row = rMin; row <= rMax; row++) {
			cellRect.y = rowSizes.getPosition(row);
			cellRect.height = rowSizes.getSize(row);
			for(int col = cMin; col <= cMax; col++) {
				cellRect.x = colSizes.getPosition(col);
				cellRect.width = colSizes.getSize(col);
				Object o = m.getValueAt(row, col);
				if(o == null) {
					s = "null";
				}
				else {
					s = o.toString();
				}
				paintCell(g, cellRect, s, row, col);
			}
		}
	}
	
	protected void paintCell(Graphics g, Rectangle cellRect, String s, int row, int col) {
		ICellRenderer tcr = getCellRenderer(row,col);
		Component c = tcr.getCellRendererComponent(this, s, false, false, row, col);
        rendererPane.paintComponent(g, c, this, cellRect);
	}

	protected ICellRenderer getCellRenderer(int row, int column) {
    	ICellRenderer r = defaultCellRenderer;
    	r.setBorder(this.getCellBorder(row, column));
    	r.setBackground(this.getCellBackground(row, column));
    	r.setHorizontalAlignment(this.getHorizontalAlignment(row, column));
		return r;
    }

    protected Border getCellBorder(int row, int column) {
    	if(row <= 0 || column <= 0) {
    		return borderForHeader;
    	}
    	else {
    		return defaultBorder;
    	}
    }
    protected Color getCellBackground(int row, int column) {
    	if(row <= 0 || column <= 0) {
    		return DEFAULT_HEADER_BACKGROUND_COLOR;
    	}
    	else {
    		return Color.WHITE;
    	}
    }
    protected int getHorizontalAlignment(int row, int column) {
    	if(row <= 0 || column <= 0) {
    		return SwingConstants.CENTER;
    	}
    	else {
    		return SwingConstants.LEFT;
    	}
    }

	public Rectangle getCellRect(int rowIndex, int colIndex) {
		return new Rectangle(
					colSizes.getPosition(colIndex),
					rowSizes.getPosition(rowIndex),
					colSizes.getSize(colIndex),
					rowSizes.getSize(rowIndex));
	}

	public int rowAtPoint(Point pt) {
		int top = rowSizes.getPosition(this.getTopMostRow());
		int rslt = rowSizes.getIndex(top+pt.y);
		if(rslt >= getModel().getRowCount()) {
			return -1;
		}
		else {
			return rslt;
		}
	}
	public int columnAtPoint(Point pt) {
		int left = colSizes.getPosition(this.getLeftMostColumn());
		int rslt = colSizes.getIndex(left+pt.x);
		if(rslt >= getModel().getColumnCount()) {
			return -1;
		}
		else {
			return rslt;
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(colSizes.getPosition(getModel().getColumnCount()), rowSizes.getPosition(getModel().getRowCount()));
	}
	public void setLeftMostColumn(int leftMostColumn) {
		this.leftMostColumn = leftMostColumn;
		repaint();
	}
	public int getLeftMostColumn() {
		return leftMostColumn;
	}
	public void setTopMostRow(int topMostRow) {
		this.topMostRow = topMostRow;
		repaint();
	}
	public int getTopMostRow() {
		return topMostRow;
	}
}
