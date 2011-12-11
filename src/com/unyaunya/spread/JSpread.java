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
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableModel;

/**
 * @author wata
 *
 */
public class JSpread extends JPanel {
	/**
	 * 
	 */
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;
	
	public static final Color DEFAULT_HEADER_BACKGROUND_COLOR = new Color(0xf0,0xf0,0xf0);

	private static final long serialVersionUID = 1L;
	protected SpreadModel model = new SpreadModel();
	protected ICellRenderer defaultCellRenderer = new DefaultCellRenderer();
	protected Border borderForHeader = BorderFactory.createMatteBorder(0,0,1,1,Color.GRAY);
	protected Border defaultBorder = BorderFactory.createMatteBorder(0,0,1,1,Color.GRAY);;

	private ScrollModel scrollModel = new ScrollModel();
	private CellRendererPane rendererPane = new CellRendererPane();
	
	public JSpread() {
		this.add(rendererPane);
		defaultCellRenderer.setBorder(defaultBorder);
		getRangeModel(HORIZONTAL).addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				repaint();
			}
		});		
		getRangeModel(VERTICAL).addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				repaint();
			}
		});
	}
	public void setModel(TableModel model) {
		if(model == null) {
			model = new SpreadModel();
		}
		if(!(model instanceof SpreadModel)) {
			model = new SpreadModel(model);
		}
		this.model = (SpreadModel)model;
		scrollModel.getSizeModel(VERTICAL).removeAll();
		scrollModel.getSizeModel(VERTICAL).insertEntries(0, model.getRowCount(), 20);
		scrollModel.getSizeModel(HORIZONTAL).removeAll();
		scrollModel.getSizeModel(HORIZONTAL).insertEntries(0, model.getColumnCount(), 80);
		scrollModel.getSizeModel(HORIZONTAL).setSize(0, 40);
		model.addTableModelListener(scrollModel);
		repaint();
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
		paintCells(g,	scrollModel.rowAtPoint(upperLeft),
						scrollModel.rowAtPoint(lowerRight),
						scrollModel.columnAtPoint(upperLeft),
						scrollModel.columnAtPoint(lowerRight));
	}
	
	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
		SpreadModel m = getModel();
		String s;
		Rectangle cellRect = new Rectangle();
		rMax = Math.min(rMax, m.getRowCount()-1);
		cMax = Math.min(cMax, m.getColumnCount()-1);
		for(int row = rMin; row <= rMax; row++) {
			cellRect.y = scrollModel.getRowPosition(row);
			cellRect.height = scrollModel.getRowHeight(row);
			for(int col = cMin; col <= cMax; col++) {
				cellRect.x = scrollModel.getColumnPosition(col);
				cellRect.width = scrollModel.getColumnWidth(col);
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
		return scrollModel.getCellRect(rowIndex, colIndex);
	}

	public int rowAtPoint(Point pt) {
		return scrollModel.rowAtPoint(pt);
	}
	
	public int columnAtPoint(Point pt) {
		return scrollModel.columnAtPoint(pt);
	}
	
	public Dimension getPreferredSize() {
		return scrollModel.getPreferredSize();
	}
	
	/**
	 * @return the rangeModel
	 */
	public RangeModel getRangeModel(int direction) {
		return scrollModel.getRangeModel(direction);
	}
}
