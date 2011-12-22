/**
 * 
 */
package com.unyaunya.swing;

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
import javax.swing.table.TableModel;

import com.unyaunya.spread.DefaultCellRenderer;
import com.unyaunya.spread.DefaultKeyAdapter;
import com.unyaunya.spread.FocusModel;
import com.unyaunya.spread.ICellRenderer;
import com.unyaunya.spread.RangeModel;
import com.unyaunya.spread.ScrollModel;
import com.unyaunya.spread.SelectionModel;
import com.unyaunya.spread.SpreadModel;

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
	public static final Color DEFAULT_SELECTION_BACKGROUND_COLOR = new Color(0xe0,0xe0,0xff);
	public static final Color DEFAULT_FOREGROUND_COLOR = new Color(0x00,0x00,0x00);
	public static final Border DEFAULT_FOCUS_BORDER = BorderFactory.createMatteBorder(1,1,2,2,Color.BLACK);

	protected Border borderForHeader = BorderFactory.createMatteBorder(0,0,1,1,Color.GRAY);
	protected Border defaultBorder = BorderFactory.createMatteBorder(0,0,1,1,Color.GRAY);
	protected Color selectionBackground = DEFAULT_SELECTION_BACKGROUND_COLOR;
	protected Color selectionForeground = DEFAULT_FOREGROUND_COLOR;
	protected static ICellRenderer defaultCellRenderer = new DefaultCellRenderer();

	private CellRendererPane rendererPane;
	private SpreadModel model;
	private ScrollModel scrollModel;
	private SelectionModel selectionModel;
	private FocusModel focusModel;
	
	/*
	 * constructor
	 */
	public JSpread() {
		this.setFocusable(true);
		this.rendererPane = new CellRendererPane();
		this.add(rendererPane);

		this.model = new SpreadModel();
		this.scrollModel = new ScrollModel(this);
		this.selectionModel = new SelectionModel();
		this.focusModel = new FocusModel(this);
		this.addKeyListener(new DefaultKeyAdapter(this));
	}
	
	/*
	 * methods set/get various models.
	 */
	public void setModel(TableModel model) {
		if(model == null) {
			model = new SpreadModel();
		}
		if(!(model instanceof SpreadModel)) {
			model = new SpreadModel(model);
		}
		this.model = (SpreadModel)model;
		this.getSelectionModel().select(1, 1);
		scrollModel.setTableModel(model);
		this.repaint(this.getBounds());
	}
	
	public SpreadModel getModel() {
		return model;
	}

	/**
	 * @return the rangeModel
	 */
	public RangeModel getRangeModel(int direction) {
		return scrollModel.getRangeModel(direction);
	}

	public SelectionModel getSelectionModel() {
		return this.selectionModel;
	}

	public FocusModel getFocusModel() {
		return this.focusModel;
	}

	/*
	 * methods delegating to TableModel
	 */
	public int getRowCount() {
		return getModel().getRowCount();
	}
	
	public int getColumnCount() {
		return getModel().getColumnCount();
	}

	/*
	 * methods delegating to ScrollModel
	 */
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
	
	public void scrollToVisible(int rowIndex, int columnIndex) {
		scrollModel.scrollToVisible(rowIndex, columnIndex);
	}

	/*
	 * methods related to UI appearance
	 */
	public Border getFocusBorder() {
    	return DEFAULT_FOCUS_BORDER;
    }

    public Border getNoFocusBorder() {
    	return defaultBorder;
    }

    public Border getHeaderBorder() {
    	return borderForHeader;
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

	public Color getSelectionBackground() {
		return this.selectionBackground;
	}
	public void setSelectionBackground(Color color) {
		this.selectionBackground = color;
		repaint();
	}
	public Color getSelectionForeground() {
		return this.selectionForeground;
		
	}
	public void setSelectionForeground(Color color) {
		this.selectionForeground = color;
		repaint();
	}

	/*
	 * methods implementing to paint
	 */
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
					s = "";
				}
				else {
					s = o.toString();
				}
				paintCell(g, cellRect, s, row, col);
			}
		}
	}

	public void repaintCell(int row, int column) {
		repaint(getCellRect(row, column));
	}

	protected void paintCell(Graphics g, Rectangle cellRect, String s, int row, int col) {
		ICellRenderer tcr = getCellRenderer(row,col);
		boolean isSelected = this.getSelectionModel().isSelected(row, col);
		boolean hasFocus = this.getFocusModel().hasFocus(row, col);
		Component c = tcr.getCellRendererComponent(this, s, isSelected, hasFocus, row, col);
        rendererPane.paintComponent(g, c, this, cellRect);
	}

	protected ICellRenderer getCellRenderer(int row, int column) {
    	ICellRenderer r = defaultCellRenderer;
    	r.setBorder(this.getCellBorder(row, column));
    	r.setBackground(this.getCellBackground(row, column));
    	r.setHorizontalAlignment(this.getHorizontalAlignment(row, column));
		return r;
    }
}
