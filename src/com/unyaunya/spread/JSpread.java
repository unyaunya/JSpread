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
import javax.swing.JScrollBar;
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
	private SizeModel rowModel = new SizeModel();
	private SizeModel colModel = new SizeModel();
	private ScrollModel horizontalScrollModel = new ScrollModel(colModel);
	private ScrollModel verticalScrollModel = new ScrollModel(rowModel);
	private CellRendererPane rendererPane = new CellRendererPane();
	
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
		rowModel.insertEntries(0, model.getRowCount(), 20);
		colModel.insertEntries(0, model.getColumnCount(), 80);
		colModel.setSize(0, 40);
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
		upperLeft = translate(upperLeft);
	    lowerRight = translate(lowerRight);
		paintCells(g, rowAtPoint(upperLeft), rowAtPoint(lowerRight), columnAtPoint(upperLeft), columnAtPoint(lowerRight));
	}

	private Point translate(Point pt) {
		return new Point(this.horizontalScrollModel.translate(pt.x), verticalScrollModel.translate(pt.y));
	}
	private Rectangle untranslate(Rectangle rect) {
		return new Rectangle(
				horizontalScrollModel.untranslate(rect.x),
				verticalScrollModel.untranslate(rect.y),
				rect.width, rect.height);
	}
	
	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
		SpreadModel m = getModel();
		String s;
		Rectangle cellRect = new Rectangle();
		rMax = Math.min(rMax, m.getRowCount()-1);
		cMax = Math.min(cMax, m.getColumnCount()-1);
		for(int row = rMin; row <= rMax; row++) {
			cellRect.y = rowModel.getPosition(row);
			cellRect.height = rowModel.getSize(row);
			for(int col = cMin; col <= cMax; col++) {
				cellRect.x = colModel.getPosition(col);
				cellRect.width = colModel.getSize(col);
				Object o = m.getValueAt(row, col);
				if(o == null) {
					s = "null";
				}
				else {
					s = o.toString();
				}
				paintCell(g, untranslate(cellRect), s, row, col);
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
					colModel.getPosition(colIndex),
					rowModel.getPosition(rowIndex),
					colModel.getSize(colIndex),
					rowModel.getSize(rowIndex));
	}

	public int rowAtPoint(Point pt) {
		return rowModel.getIndex(pt.y);
	}
	public int columnAtPoint(Point pt) {
		return colModel.getIndex(pt.x);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(colModel.getPreferredSize(), rowModel.getPreferredSize());
	}
	
	public void setLeftMostColumn(int leftMostColumn) {
		horizontalScrollModel.setValue(leftMostColumn);
		repaint();
	}
	public int getLeftMostColumn() {
		return horizontalScrollModel.getValue();
	}
	public void setTopMostRow(int topMostRow) {
		verticalScrollModel.setValue(topMostRow);
		repaint();
	}
	public int getTopMostRow() {
		return verticalScrollModel.getValue();
	}

	public void setScrollBarPosition(JScrollBar horizontalBar, JScrollBar verticalBar) {
		Rectangle bounds = this.getBounds();
		SpreadModel model = this.getModel();
		
		horizontalScrollModel.setComponentSize(bounds.width);
		verticalScrollModel.setComponentSize(bounds.height);
		horizontalBar.setMaximum(horizontalScrollModel.getMaximum());
		horizontalBar.setVisibleAmount(horizontalScrollModel.getExtent());
		horizontalBar.setValue(horizontalScrollModel.getValue());
		verticalBar.setMaximum(verticalScrollModel.getMaximum());
		verticalBar.setVisibleAmount(verticalScrollModel.getExtent());
		verticalBar.setValue(verticalScrollModel.getValue());

		System.out.println(bounds);
		System.out.println(colModel.getPreferredSize());
		System.out.println(rowModel.getPreferredSize());
		System.out.println(colModel.getLength());
		System.out.println(rowModel.getLength());
		System.out.println(this.getModel().getColumnCount());
		System.out.println(this.getModel().getRowCount());
		System.out.println(horizontalScrollModel.getMaximum());
		System.out.println(verticalScrollModel.getMaximum());
		System.out.println("[horizontalBar]");
		System.out.println(horizontalBar.getMaximum());
		System.out.println(horizontalBar.getVisibleAmount());
		System.out.println(horizontalBar.getValue());
		System.out.println("[verticalBar]");
		System.out.println(verticalBar.getMaximum());
		System.out.println(verticalBar.getVisibleAmount());
		System.out.println(verticalBar.getValue());
		//System.out.println(colModel.getScrollMax(bounds.width));
		//System.out.println(rowModel.getScrollMax(bounds.height));
		//horizontalBar.setValue(0);
		//verticalBar.setValue(0);
	}
}
