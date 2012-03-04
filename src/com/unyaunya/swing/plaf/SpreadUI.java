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
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.CellRendererPane;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import com.unyaunya.spread.Actions;
import com.unyaunya.spread.CellRange;
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
    private static final Logger LOG = Logger.getLogger(SpreadUI.class.getName());

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
		paintCells(g, clip, rect);
		//
		rect.x = bounds.x + colRangeModel.getFixedPartSize();
		rect.y = bounds.y;
		rect.width = colRangeModel.getScrollPartSize();
		rect.height = rowRangeModel.getFixedPartSize();
		paintCells(g, clip, rect);
		//
		rect.x = bounds.x;
		rect.y = bounds.y + rowRangeModel.getFixedPartSize();
		rect.width = colRangeModel.getFixedPartSize();
		rect.height = rowRangeModel.getScrollPartSize();
		paintCells(g, clip, rect);
		//
		rect.x = bounds.x + colRangeModel.getFixedPartSize();
		rect.y = bounds.y + rowRangeModel.getFixedPartSize();
		rect.width = colRangeModel.getScrollPartSize();
		rect.height = rowRangeModel.getScrollPartSize();
		paintCells(g, clip, rect);
		//LOG.info("paintComponent():end");
	}
	
    private void paintCells(Graphics g, Rectangle clipingRect, Rectangle rect){
		ScrollModel scrollModel = table.getScrollModel();
		//LOG.info("\tpaintCells(clipingRect,rect):");
		//LOG.info("\t\trect:"+clipingRect);
		Rectangle clip = rect.intersection(clipingRect);
		//LOG.info("\t\tclip:"+clip);
		Point upperLeft = clip.getLocation();
	    Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
		paintCells(g,	scrollModel.rowAtPoint(upperLeft),
						scrollModel.rowAtPoint(lowerRight),
						scrollModel.columnAtPoint(upperLeft),
						scrollModel.columnAtPoint(lowerRight),
						0,0);
	}

	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax, int horizontalOffset, int verticalOffset) {
		//LOG.info("\t\tpaintCells(int rMin, int rMax, int cMin, int cMax):");
		//LOG.info("\t\t\t(rMin, rMax, cMin, cMax)=("+rMin+","+rMax+","+cMin+","+cMax+")");
		SpreadModel m = table.getModel();
		ScrollModel scrollModel = table.getScrollModel();
		rMax = Math.min(rMax, m.getRowCount()-1);
		cMax = Math.min(cMax, m.getColumnCount()-1);
		Rectangle cellRect = new Rectangle();
		for(int row = rMin; row <= rMax; row++) {
			cellRect.y = scrollModel.getRowPosition(row) + verticalOffset;
			cellRect.height = scrollModel.getRowHeight(row);
			for(int col = cMin; col <= cMax; col++) {
				cellRect.x = scrollModel.getColumnPosition(col) + horizontalOffset;
				cellRect.width = scrollModel.getColumnWidth(col);
				ICellRange r = this.table.getCellRange(row, col);
				if(r == null) {
					paintCell(g, cellRect, row, col);
				}
				else {
					if(r.getTop() == row && r.getLeft() == col) {
						Rectangle rect = _getCellRect(r, horizontalOffset, verticalOffset);
						paintCell(g, rect, row, col);
						//paintCell(g, cellRect, row, col);
						LOG.info(String.format("(%d, %d)=%s", row, col, rect));
					}
					else {
					}
				}
			}
		}
	}

	private Rectangle _getCellRect(ICellRange r, int horizontalOffset, int verticalOffset) {
		int top = r.getTop();
		int left = r.getLeft();
		int bottom = r.getBottom();
		int right = r.getRight();
		ScrollModel scrollModel = table.getScrollModel();
		Rectangle cellRect = new Rectangle();
		cellRect.y = scrollModel.getRowPosition(top) + verticalOffset;
		cellRect.height = scrollModel.getRowPosition(bottom+1) - cellRect.y;
		cellRect.x = scrollModel.getColumnPosition(left) + horizontalOffset;
		cellRect.width = scrollModel.getColumnPosition(right+1) - cellRect.x;
		return cellRect;
	}
	
	private void paintCell(Graphics g, Rectangle cellRect, int row, int col) {
		ISpreadCellRenderer tcr = table.getCellRenderer(row,col);
		Component c = table.prepareRenderer(tcr, row, col);
		rendererPane.paintComponent(g, c, table, cellRect);
	}
}
