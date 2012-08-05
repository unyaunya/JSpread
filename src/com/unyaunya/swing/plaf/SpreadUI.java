/**
 * 
 */
package com.unyaunya.swing.plaf;

import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ActionMap;
import javax.swing.CellRendererPane;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import com.unyaunya.spread.Actions;
import com.unyaunya.spread.ICellRange;
import com.unyaunya.spread.ISpreadCellRenderer;
import com.unyaunya.spread.RangeModel;
import com.unyaunya.spread.ScrollModel;
import com.unyaunya.spread.SpreadModel;
import com.unyaunya.swing.JSpread;

/**
 * @author wata
 *
 */
public class SpreadUI extends ComponentUI {
    //private static final Logger LOG = Logger.getLogger(SpreadUI.class.getName());

    private Actions actions;
	
	protected JSpread table;
	protected CellRendererPane rendererPane;

	//
	//  The installation/uninstall procedures and support
	//

    public static ComponentUI createUI(JComponent c) {
        return new SpreadUI();
    }

    //  Installation
    public void installUI(JComponent c) {
        table = (JSpread)c;
        //actions = new Actions(table);
        actions = table.getActions();
        rendererPane = new CellRendererPane();
        table.add(rendererPane);
        //installDefaults();
        //installDefaults2();
        //installListeners();
        installKeyboardActions();
    }

    /**
     * Register all keyboard actions on the JSpread.
     */
    protected void installKeyboardActions() {
    	InputMap keyMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    	SwingUtilities.replaceUIInputMap(table, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, keyMap);
    	ActionMap map = getActionMap();
        SwingUtilities.replaceUIActionMap(table, map);
    }

    InputMap getInputMap(int condition) {
    	if(actions != null) {
        	return actions.getInputMap(condition);
    	}
    	return null;
    }
    
    ActionMap getActionMap() {
    	if(actions != null) {
        	return actions.getActionMap();
    	}
    	return null;
    }

    //  Uninstallation

    public void uninstallUI(JComponent c) {
        //uninstallDefaults();
        //uninstallListeners();
        //uninstallKeyboardActions();

        table.remove(rendererPane);
        rendererPane = null;
        table = null;
    }

    /**
     * Return the preferred size of the table. The preferred height is the
     * row height times the number of rows.
     * The preferred width is the sum of the preferred widths of each column.
     */
    public Dimension getPreferredSize(JComponent c) {
    	return table.getScrollModel().getPreferredSize();
    }

    public Dimension getMaximumSize(JComponent c) {
    	return table.getScrollModel().getPreferredSize();
    }

    public void paint(Graphics g, JComponent c) {
		SpreadModel m = table.getModel();
		if (m.getRowCount() <= 0 || m.getColumnCount() <= 0) {
			return;
		}
		Rectangle clip = g.getClipBounds();
		Rectangle bounds = table.getBounds();
		bounds.x = bounds.y = 0;
		if (!bounds.intersects(clip)) {
            // this check prevents us from painting the entire table
            // when the clip doesn't intersect our bounds at all
			return;
		}
		//LOG.info("paintComponent():start");
		//LOG.info("\tclipingRect:"+clip);
		Rectangle rect = new Rectangle(bounds);
		RangeModel colRangeModel = table.getRangeModel(Adjustable.HORIZONTAL);
		RangeModel rowRangeModel = table.getRangeModel(Adjustable.VERTICAL);
		//
		rect.x = bounds.x;
		rect.y = bounds.y;
		rect.width = colRangeModel.getFixedPartSize();
		rect.height = rowRangeModel.getFixedPartSize();
		paintQuadrant(g, clip, rect);
		//
		rect.x = bounds.x + colRangeModel.getFixedPartSize();
		rect.y = bounds.y;
		rect.width = colRangeModel.getScrollPartSize();
		rect.height = rowRangeModel.getFixedPartSize();
		paintQuadrant(g, clip, rect);
		//
		rect.x = bounds.x;
		rect.y = bounds.y + rowRangeModel.getFixedPartSize();
		rect.width = colRangeModel.getFixedPartSize();
		rect.height = rowRangeModel.getScrollPartSize();
		paintQuadrant(g, clip, rect);
		//
		rect.x = bounds.x + colRangeModel.getFixedPartSize();
		rect.y = bounds.y + rowRangeModel.getFixedPartSize();
		rect.width = colRangeModel.getScrollPartSize();
		rect.height = rowRangeModel.getScrollPartSize();
		paintQuadrant(g, clip, rect);
		//ウィンドウ固定の場合、固定部との境界線を引く。
		if(table.arePanesFreezed()) {
			int x = bounds.x + colRangeModel.getFixedPartSize();
			int y = bounds.y + rowRangeModel.getFixedPartSize();
			//横線
			g.drawLine(bounds.x, y, bounds.x + bounds.width, y);
			//縦線
			g.drawLine(x, bounds.y, x, bounds.y + bounds.height);
		}
		//LOG.info("paintComponent():end");
	}
	
    private void paintQuadrant(Graphics g, Rectangle clipingRect, Rectangle rect){
		ScrollModel scrollModel = table.getScrollModel();
		//LOG.info("\tpaintCells(clipingRect,rect):");
		//LOG.info("\t\trect:"+clipingRect);
		Rectangle clip = rect.intersection(clipingRect);
		//LOG.info("\t\tclip:"+clip);
		Point upperLeft = clip.getLocation();
	    Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
	    Rectangle currentClip = g.getClipBounds();
	    g.setClip(clip.x, clip.y, clip.width, clip.height);
		paintCells(g,	scrollModel.rowAtPoint(upperLeft),
						scrollModel.rowAtPoint(lowerRight),
						scrollModel.columnAtPoint(upperLeft),
						scrollModel.columnAtPoint(lowerRight));
	    g.setClip(currentClip.x, currentClip.y, currentClip.width, currentClip.height);
	}

	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax) {
		SpreadModel m = table.getModel();
		rMax = Math.min(rMax, m.getRowCount()-1);
		cMax = Math.min(cMax, m.getColumnCount()-1);
		int rowSpan = rMax - rMin + 1;
		int colSpan = cMax - cMin + 1;
		if(rowSpan <= 0 || colSpan <= 0) {
			return;
		}
		boolean[][] map = new boolean[rowSpan][colSpan];
		for(int i = 0; i < rowSpan; i++) {
			for(int j = 0; j < colSpan; j++) {
				if(!map[i][j]) {
					int row = rMin+i;
					int col = cMin+j;
					ICellRange range = table.getCellRange(row, col);
					Rectangle cellRect;
					if(range == null) {
						map[i][j] = true;
						cellRect = table.getCellRect(row, col);
						if(cellRect != null) {
							paintCell(g, cellRect, row, col);
						}
					}
					else {
						for(int rr = range.getTop()-rMin; rr < range.getBottom()-rMin; rr++) {
							for(int cc = range.getLeft()-cMin; cc < range.getRight()-cMin; cc++) {
								if(rr >= 0 && rr < rowSpan && cc >= 0 && cc < colSpan) {
									map[rr][cc] = true;
								}
							}
						}
						cellRect = table.getCellRect(range.getTop(), range.getLeft());
						if(cellRect != null) {
							paintCell(g, cellRect, range.getTop(), range.getLeft());
						}
					}
				}
			}
		}
	}

	private void paintCell(Graphics g, Rectangle cellRect, int row, int col) {
		ISpreadCellRenderer tcr = table.getCellRenderer(row,col);
		Component c = table.prepareRenderer(tcr, row, col);
		rendererPane.paintComponent(g, c, table, cellRect);
	}
}
