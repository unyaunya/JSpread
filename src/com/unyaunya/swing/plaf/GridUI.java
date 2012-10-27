package com.unyaunya.swing.plaf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.CellRendererPane;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

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
     * �R���|�[�l���g�̕`����s��
     * 
     * �`��̈�S�̂��A������A�s�������ꂼ��ɁA�Œ蕔���ƃX�N���[��������
     * �������A�S�̏ی���ݒ肵�A�e�ی����Ƃɕ`����s���B
     */
   private void paintGrid(Graphics2D g, JGrid grid) {
	   assert(this.grid == grid);

	   ScrollModel sm = grid.getScrollModel();
	   //LOG.info("row="+sm.getRowCount());
	   //LOG.info("col="+sm.getColumnCount());
	   /*if (sm.getRowCount() <= 0 || sm.getColumnCount() <= 0) {
		LOG.info("�s���܂��͗񐔂�0�̂��߁A�`����X�L�b�v����");
		return;
		}*/
	   
	   //�̈�S�̂��S�ی��ɕ��������Ƃ��́A�e�ی��̋��E�̍s�E��ԍ�
	   int row[] = new int[4];
	   int col[] = new int[4];

	   row[0] = -1;
	   row[1] = sm.getFixedRowNum()-1;
	   row[2] = sm.getFixedRowNum() + sm.getRowScrollValue();
	   row[3] = sm.getRowCount()-1;
	   col[0] = -1;
	   col[1] = sm.getFixedColumnNum()-1;
	   col[2] = sm.getFixedColumnNum() + sm.getColumnScrollValue();
	   col[3] = sm.getColumnCount()-1;
	   
	   //row[], col[]�ɑΉ�����x,y���W
	   int x[] = new int[4];
	   int y[] = new int[4];
	   x[0] = 0;
	   x[1] = sm.getColumnPosition(col[1]+1);
	   x[2] = sm.getColumnPosition(col[2]);
	   x[3] = sm.getColumnPosition(col[3]+1);
	   y[0] = 0;
	   y[1] = sm.getRowPosition(row[1]+1);
	   y[2] = sm.getRowPosition(row[2]);
	   y[3] = sm.getRowPosition(row[3]+1);

	   int scrollX = sm.getColumnScrollAmount();
	   int scrollY = sm.getRowScrollAmount();
	   Rectangle lcRect = new Rectangle();

	   //	���㕔���̕`��
	   lcRect.setBounds(x[0], y[0], x[1]-x[0], y[1]-y[0]);
	   paintQuadrant(sm, g, 0, 0, lcRect, row[0], row[1], col[0], col[1]);
	   //	�E�㕔���̕`��
	   lcRect.setBounds(x[2], y[0], x[3]-x[2], y[1]-y[0]);
	   paintQuadrant(sm, g, scrollX, 0, lcRect, row[0], row[1], col[2], col[3]);
	   //	���������̕`��
	   lcRect.setBounds(x[0], y[2], x[1]-x[0], y[3]-y[2]);
	   paintQuadrant(sm, g, 0, scrollY, lcRect, row[2], row[3], col[0], col[1]);
	   //	�E�������̕`��
	   lcRect.setBounds(x[2], y[2], x[3]-x[2], y[3]-y[2]);
	   //LOG.info("rect=" + rect);
	   paintQuadrant(sm, g, scrollX, scrollY, lcRect, row[2], row[3], col[2], col[3]);
	   //
	   LOG.info("paint():end");

	   //
	   //�s��ԍ��̈�
	   //
	   
	   //����p

	   //��ԍ�

	   //�s�ԍ�

	   //
	   //�f�[�^�̈�
	   //

	   //����

	   //�E��
	   
	   //����
	   
	   //�E��
   }
    
    /**
     * �R���|�[�l���g�̕`����s��
     * 
     * �N���b�s���O�̈�̒l���`�F�b�N���A�`��̕K�v��������΁ApaintGrid()���Ăяo���B
     */
    public void paint(Graphics g, JComponent c) {
		LOG.info("paint():start");
		Rectangle clip = g.getClipBounds();
		Rectangle bounds = c.getBounds();
		bounds.x = bounds.y = 0;
		LOG.info("\tbounds=" + bounds);
		if (!bounds.intersects(clip)) {
            // this check prevents us from painting the entire table
            // when the clip doesn't intersect our bounds at all
			LOG.info("\tclip:"+clip);
			LOG.info("�N���b�s���O�̈�Ƃ̌������Ȃ����߁A�`����X�L�b�v����");
			return;
		}
		paintGrid((Graphics2D)g, (JGrid)c);
	}

	/**
     * paint���\�b�h���ݒ肵���ی���̕`���S������B
     * 
     * @param scrollModel
     * @param g
     * @param ccRect �ی����͂ދ�`
     */
    private void paintQuadrant(ScrollModel sm, Graphics2D g, int scrollX, int scrollY, Rectangle lcRect, int rMin, int rMax, int cMin, int cMax){
    	//cc: component coordination
    	//lc: logical coordination
    	LOG.info("lcRect=" + lcRect);
    	Rectangle ccCurrentClip = g.getClipBounds();
    	LOG.info("ccCurrentClip=" + ccCurrentClip);
    	LOG.info("scrollX=" + scrollX + ", scrollY=" + scrollY);
		g.translate(-scrollX, -scrollY);
    	try {
    		//�w�i�̕`��
    		grid.paintBackground(g);
    		//�O���b�h�̕`��
        	Rectangle lcCurrentClip = g.getClipBounds();
        	LOG.info("lcCurrentClip=" + lcCurrentClip);
    		if(!lcRect.intersects(lcCurrentClip)) {
    	    	LOG.info("skip: !ccRect.intersects(ccCurrentClip)");
    			return;
    		}
        	Rectangle lcClip = lcRect.intersection(lcCurrentClip);
    		//LOG.info("\tpaintCells(currentClip,rect):");
    		//LOG.info("\t\trect:"+currentClip);
    		LOG.info("\t\tlcClip:"+lcClip);
    	    g.setClip(lcClip.x, lcClip.y, lcClip.width, lcClip.height);
    	    {
    			Point upperLeft = lcClip.getLocation();
    		    Point lowerRight = new Point(lcClip.x + lcClip.width - 1, lcClip.y + lcClip.height - 1);
    			int rmin = Math.max(rMin, sm.rowAtPointLC(upperLeft));
    			int rmax = Math.min(rMax, sm.rowAtPointLC(lowerRight));
    			int cmin = Math.max(cMin, sm.columnAtPointLC(upperLeft));
    			int cmax = Math.min(cMax, sm.columnAtPointLC(lowerRight));
    			LOG.info("(rMin,rMax,cMin,cMax)=(" + rMin + "," + rMax + "," + cMin + "," + cMax + ")");
    			LOG.info("(rmin,rmax,cmin,cmax)=(" + rmin + "," + rmax + "," + cmin + "," + cmax + ")");
    			QuadrantPainter qp = new QuadrantPainter(rmin, rmax, cmin, cmax);
    			qp.paintCells(sm, g);
    	    }
    		//�O�i�̕`��
    		grid.paintForeground(g);
    	}
    	finally {
   			g.translate(+scrollX, +scrollY);
    	    g.setClip(ccCurrentClip.x, ccCurrentClip.y, ccCurrentClip.width, ccCurrentClip.height);
    	}
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
    		for(int rr = range.getTop()-rMin; rr <= range.getBottom()-rMin; rr++) {
    			for(int cc = range.getLeft()-cMin; cc <= range.getRight()-cMin; cc++) {
    				if(rr >= 0 && rr < rowSpan && cc >= 0 && cc < colSpan) {
    					map[rr][cc] = true;
    				}
    			}
    		}
    	}

        /**
         * �w�肵���s�A��͈͂̃Z����`�悷��B
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
    		//LOG.info("kkkkkkkkkkkk");
    	}
    }
  
	/**
	 * row,col�Ŏw�肵���Z����`�悷��B
	 * �Z�����A������Ă���ꍇ�́A�A���̈�������ɕ`�悷��B
	 * �Ԃ�l�́A�`�悵���Z���͈͂�Ԃ��B
	 * 
	 * @param g
	 * @param row
	 * @param col
	 * @return �`�悵���Z���͈�
	 */
	protected IRange paintCell(Graphics g, int row, int col) {
		Rectangle cellRect;
		IRange range = grid.getCellRange(row, col);
		cellRect = grid.getRangeRect(range);
		int top = range.getTop();
		int left = range.getLeft();
		IGridCellRenderer tcr = grid.getCellRenderer(top, left);
		Component c = grid.prepareRenderer(tcr, top, left);
		rendererPane.paintComponent(g, c, grid, cellRect);
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
