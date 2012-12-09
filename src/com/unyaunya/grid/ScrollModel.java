package com.unyaunya.grid;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.swing.JScrollBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.unyaunya.swing.JGrid;

/**
 * JGridのスクロール処理を処理するクラス
 * 行方向/列方向それぞれに、内部的にRangeModelを保持している。
 * 
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class ScrollModel implements ComponentListener, TableModelListener, Serializable {
    private static final Logger LOG = Logger.getLogger(ScrollModel.class.getName());

	/**
	 * 
	 */
	private ScrollRangeModel colRangeModel;
	private ScrollRangeModel rowRangeModel;
	private TableModel tableModel;
	private JGrid component;
	transient private Columns columns;
	transient private Rows rows;
	
	private CellPosition defaultSplitPosition;

	public ScrollModel(JGrid c, int columnHeaderheight, int rowHeaderWidth) {
		this(c, columnHeaderheight, rowHeaderWidth, 23, 60);
	}

	private ScrollModel(JGrid grid, int columnHeaderheight, int rowHeaderWidth, int defaultRowHeight, int defaultColumnWidth) {
		this.defaultSplitPosition = new CellPosition(0,0);
		this.rowRangeModel = new ScrollRangeModel(columnHeaderheight);
		this.colRangeModel = new ScrollRangeModel(rowHeaderWidth);
		this.columns = new Columns(grid, colRangeModel);
		this.rows = new Rows(grid, rowRangeModel);
		this.getRows().setDefaultHeight(defaultRowHeight);
		this.getColumns().setDefaultWidth(defaultColumnWidth);
		setComponent(grid);
	}

	public Columns getColumns() {
		return this.columns;
	}

	public Rows getRows() {
		return this.rows;
	}

	/**
	 * スクロール対象のコンポーネントを設定する。
	 */
	private void setComponent(JGrid component) {
		if(this.component != null) {
			throw new RuntimeException("Can't reset the component");
		}
		this.component = component;
		component.addComponentListener(this);
		colRangeModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				adjustQuadrant();
				ScrollModel.this.component.repaint();
			}
		});
		rowRangeModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				adjustQuadrant();
				ScrollModel.this.component.repaint();
			}
		});
		this.component.repaint();
	}
	
	/**
	 * グリッド四分割の基準セルのデフォルト位置を取得する。
	 * unfreezePanesを実行すると、このｾﾙ位置を基準にグリッドは四分割される
	 */
	public CellPosition getDefaultSplitPosition() {
		return defaultSplitPosition; 
	}

	/**
	 * グリッド四分割の基準セルのデフォルト位置を設定する。
	 */
	public void setDefaultSplitPosition(CellPosition newPosition) {
		defaultSplitPosition = new CellPosition(newPosition);
		CellPosition currentSplitPosiition = getCurrentSplitPosiition();
		freezePanes(Math.max(currentSplitPosiition.getRow(), defaultSplitPosition.getRow()),
				Math.max(currentSplitPosiition.getColumn(), defaultSplitPosition.getColumn()));
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
		tableModel.addTableModelListener(this);
		tableChanged(new TableModelEvent(tableModel));
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		LOG.info("TableModelEvent:type=" + e.getType());
		LOG.info("TableModelEvent:firstRow, lastRow, column=" + e.getFirstRow() + "," + e.getLastRow() + "," + e.getColumn());
		if(e.getFirstRow() == 0 && e.getLastRow() == Integer.MAX_VALUE && e.getColumn() == TableModelEvent.ALL_COLUMNS) {
			rowRangeModel.reset(tableModel.getRowCount());
			colRangeModel.reset(tableModel.getColumnCount(), 40);
			this.rowRangeModel.setValue(0);
			this.colRangeModel.setValue(0);
		}
		else {
			switch(e.getType()) {
			case TableModelEvent.INSERT:
				if(e.getColumn() == TableModelEvent.ALL_COLUMNS) {
					rowRangeModel.insert(e.getFirstRow(), e.getLastRow() - e.getFirstRow() + 1);
				}
				else {
					colRangeModel.insert(e.getColumn(), 1);
				}
				break;
			case TableModelEvent.DELETE:
				if(e.getColumn() == TableModelEvent.ALL_COLUMNS) {
					rowRangeModel.remove(e.getFirstRow(), e.getLastRow() - e.getFirstRow() + 1);
				}
				else {
					colRangeModel.remove(e.getColumn());
				}
				break;
			case TableModelEvent.UPDATE:
				break;
			default:
				break;
			}
		}
	}

	/**
	 * スクロールバーを設定する。
	 */
	public void setScrollBar(JScrollBar horizontalBar, JScrollBar verticalBar) {
		horizontalBar.setModel(colRangeModel);
		verticalBar.setModel(rowRangeModel);
	}

	public Rectangle getGridRect(int rowIndex, int colIndex) {
		return new Rectangle(
					colRangeModel.getPosition(colIndex),
					rowRangeModel.getPosition(rowIndex),
					colRangeModel.getDisplaySize(colIndex),
					rowRangeModel.getDisplaySize(rowIndex));
	}

	/**
	 * 指定したセル範囲に対応する矩形を論理座標で返す。
	 * @param r
	 * @return
	 */
	public Rectangle getRangeRect(IRange r) {
		assert(r != null);
		int top = r.getTop();
		int left = r.getLeft();
		int bottom = r.getBottom();
		int right = r.getRight();
		Rectangle cellRect = new Rectangle();
		cellRect.y = getRows().getPosition(top);
		cellRect.height = rowRangeModel.getDistance(top, bottom+1);
		cellRect.x = getColumns().getPosition(left);
		cellRect.width = colRangeModel.getDistance(left, right+1);
		return cellRect;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(
				colRangeModel.getPreferredSize(),
				rowRangeModel.getPreferredSize());
	}

	public CellPosition getCellPosition(Point pt) {
		CellPosition cp = new CellPosition();
		cp.setRow(rowRangeModel.getIndex(pt.y));
		cp.setRow(colRangeModel.getIndex(pt.x));
		return cp;
	}

	public CellPosition getCellPositionFromView(Point pt) {
		return getCellPosition(new Point(rowRangeModel.viewToModel(pt.y), colRangeModel.viewToModel(pt.x)));
	}

	public Point modelToView(Point pt) {
		Point rc = new Point();
		rc.x = colRangeModel.modelToView(pt.x);
		rc.y = rowRangeModel.modelToView(pt.y);
		return rc;
	}

	public Rectangle modelToView(Rectangle rect) {
		Rectangle rc = new Rectangle();
		rc.x = colRangeModel.modelToView(rect.x);
		rc.y = rowRangeModel.modelToView(rect.y);
		rc.width = rect.width;
		rc.height = rect.height;
		return rc;
	}

	public int getFixedAreaHeight() {
		return rowRangeModel.getFixedPartSize();
	}
	public int getFixedAreaWidth() {
		return colRangeModel.getFixedPartSize();
	}
	public int getScrollAreaHeight() {
		return rowRangeModel.getScrollPartSize();
	}
	public int getScrollAreaWidth() {
		return colRangeModel.getScrollPartSize();
	}

	/**
	 * 画面座標における行方向のスクロール量を取得する。
	 * @return
	 */
	public int getRowScrollAmount() {
		return rowRangeModel.getScrollAmount();
	}

	/**
	 * 画面座標における列方向のスクロール量を取得する。
	 * @return
	 */
	public int getColumnScrollAmount() {
		return colRangeModel.getScrollAmount();
	}

	/**
	 * 行方向のスクロール値を取得する。
	 * @return
	 */
	public int getRowScrollValue() {
		return rowRangeModel.getValue();
	}

	/**
	 * 列方向のスクロール値を取得する。
	 * @return
	 */
	public int getColumnScrollValue() {
		return colRangeModel.getValue();
	}

	/**
	 * 行方向のextentを取得する。
	 * @return
	 */
	public int getRowExtent() {
		return rowRangeModel.getExtent();
	}
	/**
	 * 列方向のextentを取得する。
	 * @return
	 */
	public int getColumnExtent() {
		return colRangeModel.getExtent();
	}

	public void scrollToVisible(int rowIndex, int columnIndex) {
		colRangeModel.scrollToVisible(columnIndex);
		rowRangeModel.scrollToVisible(rowIndex);
	}

	public boolean arePanesFreezed() {
		if(this.getRows().getCountOfFixedPart()!=getDefaultSplitPosition().getRow()) {
			return true;
		}
		if( this.getColumns().getCountOfFixedPart()!=getDefaultSplitPosition().getColumn()) {
			return true;
		}
		return false;
	}

	public CellPosition getCurrentSplitPosiition() {
		return new CellPosition(
				rowRangeModel.getFixedPartNum(),
				colRangeModel.getFixedPartNum()
				);
	}
	public void freezePanes(int rowIndex, int columnIndex) {
		rowRangeModel.setFixedPartNum(rowIndex);
		colRangeModel.setFixedPartNum(columnIndex);
		adjustQuadrant();
	}

	public void unfreezePanes() {
		rowRangeModel.setFixedPartNum(getDefaultSplitPosition().getRow());
		colRangeModel.setFixedPartNum(getDefaultSplitPosition().getColumn());
		adjustQuadrant();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		//nothing to do
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		//nothing to do
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Component c = e.getComponent();
		colRangeModel.setComponentSize(c.getWidth());
		rowRangeModel.setComponentSize(c.getHeight());
		assert(c == this.component);
		adjustQuadrant();
	}

	public void adjustQuadrant() {
		int dx = colRangeModel.getScrollAmount();
		int dy = rowRangeModel.getScrollAmount();
		LOG.info("(dx,dy)=("+dx+","+dy+")");
		JGrid grid = this.component;
		grid.getUpperLeft().setBounds(0, 0, getFixedAreaWidth(), getFixedAreaHeight());
		grid.getUpperLeft().setViewPosiition(0, 0);
		grid.getUpperRight().setBounds(getFixedAreaWidth(), 0, getScrollAreaWidth(), getFixedAreaHeight());
		grid.getUpperRight().setViewPosiition(getFixedAreaWidth()+dx, 0);
		grid.getLowerLeft().setBounds(0, getFixedAreaHeight(), getFixedAreaWidth(), getScrollAreaHeight());
		grid.getLowerLeft().setViewPosiition(0, getFixedAreaHeight()+dy);
		grid.getLowerRight().setBounds(getFixedAreaWidth(), getFixedAreaHeight(), getScrollAreaWidth(), getScrollAreaHeight());
		grid.getLowerRight().setViewPosiition(getFixedAreaWidth()+dx, getFixedAreaHeight()+dy);
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		//nothing to do
	}
}
