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

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

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

        rendererPane = new CellRendererPane();
        table.add(rendererPane);
        //installDefaults();
        //installDefaults2();
        //installListeners();
        //installKeyboardActions();
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
		System.out.println("paintComponent():start");
		System.out.println("\tclipingRect:"+clip);
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
		System.out.println("paintComponent():end");
	}
	
    private void paintCells(Graphics g, Rectangle clipingRect, Rectangle rect){
		ScrollModel scrollModel = table.getScrollModel();
		System.out.println("\tpaintCells(clipingRect,rect):");
		System.out.println("\t\trect:"+clipingRect);
		Rectangle clip = rect.intersection(clipingRect);
		System.out.println("\t\tclip:"+clip);
		Point upperLeft = clip.getLocation();
	    Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
		paintCells(g,	scrollModel.rowAtPoint(upperLeft),
						scrollModel.rowAtPoint(lowerRight),
						scrollModel.columnAtPoint(upperLeft),
						scrollModel.columnAtPoint(lowerRight),
						0,0);
	}

	private void paintCells(Graphics g, int rMin, int rMax, int cMin, int cMax, int horizontalOffset, int verticalOffset) {
		System.out.println("\t\tpaintCells(int rMin, int rMax, int cMin, int cMax):");
		System.out.println("\t\t\t(rMin, rMax, cMin, cMax)=("+rMin+","+rMax+","+cMin+","+cMax+")");
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
				paintCell(g, cellRect, row, col);
			}
		}
	}

	private void paintCell(Graphics g, Rectangle cellRect, int row, int col) {
		ISpreadCellRenderer tcr = table.getCellRenderer(row,col);
		Component c = table.prepareRenderer(tcr, row, col);
		rendererPane.paintComponent(g, c, table, cellRect);
	}

}
