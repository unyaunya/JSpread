package com.unyaunya.swing.plaf;

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

import com.unyaunya.grid.CellRange;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.IGridCellRenderer;
import com.unyaunya.grid.ScrollModel;
import com.unyaunya.grid.action.Actions;
import com.unyaunya.swing.JGrid;

public class GridUI extends ComponentUI {
	private static final Logger LOG = Logger.getLogger(GridUI.class.getName());

	protected JGrid grid;
	protected CellRendererPane rendererPane;
    private Actions actions;

	//
	//  The installation/uninstall procedures and support
	//

    public static ComponentUI createUI(JComponent c) {
        return new GridUI();
    }

    //  Installation
    @Override
    public void installUI(JComponent c) {
    	grid = (JGrid)c;
        rendererPane = new CellRendererPane();
        grid.add(rendererPane);
        actions = grid.getActions();
        assert(actions != null);
        //installDefaults();
        //installDefaults2();
        //installListeners();
        installKeyboardActions();
    }

    @Override
    public void uninstallUI(JComponent c) {
    	grid.remove(rendererPane);
        rendererPane = null;
    }
 
    /**
     * Return the preferred size of the table. The preferred height is the
     * row height times the number of rows.
     * The preferred width is the sum of the preferred widths of each column.
     */
    public Dimension getPreferredSize(JComponent c) {
    	return grid.getScrollModel().getPreferredSize();
    }

    public Dimension getMaximumSize(JComponent c) {
    	return getPreferredSize(c);
    }
 
    /**
     * コンポーネントの描画を行う
     * 
     * 描画領域全体を、列方向、行方向それぞれに、固定部分とスクロール部分で
     * 分割し、４つの象限を設定し、各象限ごとに描画を行う。
     */
    public void paint(Graphics g, JComponent c) {
		LOG.info("paint():start");
		JGrid gs = (JGrid)c;
		ScrollModel sm = gs.getScrollModel();

		LOG.info("row="+sm.getRowCount());
		LOG.info("col="+sm.getColumnCount());
		/*if (sm.getRowCount() <= 0 || sm.getColumnCount() <= 0) {
			LOG.info("行数または列数が0のため、描画をスキップした");
			return;
		}*/
		Rectangle clip = g.getClipBounds();
		Rectangle bounds = gs.getBounds();
		bounds.x = bounds.y = 0;
		if (!bounds.intersects(clip)) {
            // this check prevents us from painting the entire table
            // when the clip doesn't intersect our bounds at all
			LOG.info("\tbounds:"+bounds);
			LOG.info("\tclip:"+clip);
			LOG.info("クリッピング領域との交差がないため、描画をスキップした");
			return;
		}
		int fixedRowSize = sm.getFixedAreaHight();
		int fixedColumnSize = sm.getFixedAreaWidth();
		int scrollRowSize = sm.getScrollAreaHeight();
		int scrollColumnSize = sm.getScrollAreaWidth();
		LOG.info("\nfixedRowSize:"+fixedRowSize);
		LOG.info("\nfixedColumnSize:"+fixedColumnSize);
		LOG.info("\nscrollRowSize:"+scrollRowSize);
		LOG.info("\nscrollColumnSize:"+scrollColumnSize);
		
		//	左上部分の描画
		paintQuadrant(sm, g, new Rectangle(bounds.x, bounds.y, fixedColumnSize, fixedRowSize));
		//	右上部分の描画
		paintQuadrant(sm, g, new Rectangle(bounds.x + fixedColumnSize, bounds.y, scrollColumnSize, fixedRowSize));
		//	左下部分の描画
		paintQuadrant(sm, g, new Rectangle(bounds.x, bounds.y + fixedRowSize, fixedColumnSize, scrollRowSize));
		//	右下部分の描画
		paintQuadrant(sm, g, new Rectangle(bounds.x + fixedColumnSize, bounds.y + fixedRowSize, scrollColumnSize, scrollRowSize));
		//ウィンドウ固定の場合、固定部との境界線を引く。
		if(sm.arePanesFreezed()) {
			int x = bounds.x + fixedColumnSize;
			int y = bounds.y + fixedRowSize;
			//横線
			g.drawLine(bounds.x, y, bounds.x + bounds.width, y);
			//縦線
			g.drawLine(x, bounds.y, x, bounds.y + bounds.height);
		}
		LOG.info("paint():end");
	}

    /**
     * paintメソッドが設定した象限一つの描画を担当する。
     * 
     * @param scrollModel
     * @param g
     * @param rect 象限を囲む矩形
     */
    private void paintQuadrant(ScrollModel sm, Graphics g, Rectangle rect){
	    Rectangle currentClip = g.getClipBounds();
		if(!rect.intersects(currentClip)) {
			return;
		}
    	Rectangle clip = rect.intersection(currentClip);
		//LOG.info("\tpaintCells(currentClip,rect):");
		//LOG.info("\t\trect:"+currentClip);
		//LOG.info("\t\tclip:"+clip);
		Point upperLeft = clip.getLocation();
	    Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
	    g.setClip(clip.x, clip.y, clip.width, clip.height);
	    {
			int rMin = sm.rowAtPoint(upperLeft);
			int rMax = sm.rowAtPoint(lowerRight);
			int cMin = sm.columnAtPoint(upperLeft);
			int cMax = sm.columnAtPoint(lowerRight);
			rMax = Math.min(rMax, sm.getRowCount()-1);
			cMax = Math.min(cMax, sm.getColumnCount()-1);
			LOG.info("(rMin,rMax,cMin,cMax)=(" + rMin + "," + rMax + "," + cMin + "," + cMax + ")");
			QuadrantPainter qp = new QuadrantPainter(rMin, rMax, cMin, cMax);
			qp.paintCells(sm, g);
	    }
	    g.setClip(currentClip.x, currentClip.y, currentClip.width, currentClip.height);
	}

    class QuadrantPainter {
    	public int rMin;
    	public int rMax;
    	public int cMin;
    	public int cMax;
    	int rowSpan;
    	int colSpan;
		private boolean[][] map;
   
    	QuadrantPainter(int rMin, int rMax, int cMin, int cMax) {
    		this.rMin = rMin;
    		this.rMax = rMax;
    		this.cMin = cMin;
    		this.cMax = cMax;
    		this.rowSpan = rMax - rMin + 1;
    		this.colSpan = cMax - cMin + 1;
    		this.map = new boolean[rowSpan][colSpan];
    	}

		boolean isMarked(int row, int col) {
			return map[row-rMin][col-cMin];
		}

    	void mark(int row, int col) {
    		map[row-rMin][col-cMin] = true;
    	}
    	
    	void mark(IRange range) {
    		for(int rr = range.getTop()-rMin; rr < range.getBottom()-rMin; rr++) {
    			for(int cc = range.getLeft()-cMin; cc < range.getRight()-cMin; cc++) {
    				if(rr >= 0 && rr < rowSpan && cc >= 0 && cc < colSpan) {
    					map[rr][cc] = true;
    				}
    			}
    		}
    	}

        /**
         * 指定した行、列範囲のセルを描画する。
         * @param sm
         * @param g
         * @param rMin
         * @param rMax
         * @param cMin
         * @param cMax
         */
    	void paintCells(ScrollModel sm, Graphics g) {
    		if(rowSpan <= 0 || colSpan <= 0) {
    			return;
    		}
    		for(int row = rMin; row <= rMax; row++) {
    			for(int col = cMin; col <= cMax; col++) {
    				if(!isMarked(row, col)) {
        				IRange range = paintCell(g, row, col);
        				mark(range);
    				}
    			}
    		}
    	}
    }
  
	/**
	 * row,colで指定したセルを描画する。
	 * セルが連結されている場合は、連結領域も同時に描画する。
	 * 返り値は、描画したセル範囲を返す。
	 * 
	 * @param g
	 * @param row
	 * @param col
	 * @return 描画したセル範囲
	 */
	protected IRange paintCell(Graphics g, int row, int col) {
		Rectangle cellRect = grid.getCellRect(row, col);
		if(cellRect != null) {
			IGridCellRenderer tcr = grid.getCellRenderer(row,col);
			Component c = grid.prepareRenderer(tcr, row, col);
			rendererPane.paintComponent(g, c, grid, cellRect);
		}
		IRange range = grid.getCellRange(row, col);
		if(range == null) {
			range = new CellRange(row, col);
		}
		return range;
	}

	protected InputMap getInputMap(int condition) {
    	if(actions != null) {
        	return actions.getInputMap(condition);
    	}
    	return null;
    }
    
    protected ActionMap getActionMap() {
    	if(actions != null) {
        	return actions.getActionMap();
    	}
    	return null;
    }

    /**
     * Register all keyboard actions on the JGrid.
     */
    protected void installKeyboardActions() {
    	InputMap keyMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    	SwingUtilities.replaceUIInputMap(grid, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, keyMap);
    	ActionMap map = getActionMap();
        SwingUtilities.replaceUIActionMap(grid, map);
    }

}
