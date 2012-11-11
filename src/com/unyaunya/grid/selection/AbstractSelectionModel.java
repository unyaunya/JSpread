package com.unyaunya.grid.selection;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.event.TableModelEvent;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.RangeUtil;
import com.unyaunya.swing.JGrid;

abstract public class AbstractSelectionModel implements IGridSelectionModel {
    private static final Logger LOG = Logger.getLogger(AbstractSelectionModel.class.getName());

    private JGrid grid;

	private int minimumFocusedRow;
	private int minimumFocusedColumn;

	/**
	 * currentRangeの角であり、anchorCellの対角となるセル位置。
	 * 入力コンポーネントが配置されるセル位置でもある。 
	 */
	private CellPosition focusCell;

	private boolean shiftDown;
	private boolean controlDown;

	/**
     * 選択範囲全体を保持するリスト。複数のセル範囲を保持する。
     */
	private ArrayList<IRange> selectedRangeList;
	
	/**
	 * コンストラクタ
	 */
	public AbstractSelectionModel(JGrid grid) {
		assert(grid != null);
		this.grid = grid;
		this.focusCell = new CellPosition(getMinimumFocusedRow(), getMinimumFocusedColumn());
		this.selectedRangeList = new ArrayList<IRange>();
	}

	@Override
	public ArrayList<IRange> getSelectedRangeList() {
		return selectedRangeList;
	}

	protected JGrid getGrid() {
		return grid;
	}
	
	protected void repaint() {
		getGrid().repaint();
	}

	private CellPosition getFocusCell() {
		return focusCell;
	}

	@Override
	public int getFocusedColumn() {
		return Math.max(0, getFocusCell().getColumn());
	}

	@Override
	public int getFocusedRow() {
		return Math.max(0, getFocusCell().getRow());
	}

	//
	//セレクション範囲の制限
	//
	@Override
	public int getMinimumFocusedRow() {
		return minimumFocusedRow;
	}

	@Override
	public int getMinimumFocusedColumn() {
		return minimumFocusedColumn;
	}

	@Override
	public void setMinimumFocusedRow(int row) {
		minimumFocusedRow = row;
		getFocusCell().setRow(Math.max(getFocusCell().getRow(), minimumFocusedRow));
	}

	@Override
	public void setMinimumFocusedColumn(int column) {
		minimumFocusedColumn = column;
		getFocusCell().setColumn(Math.max(getFocusCell().getColumn(), minimumFocusedColumn));
	}
	
	@Override
	public boolean hasFocus(int row, int col) {
		IRange range = getGrid().getCellRange(row, col);
		if(range.contains(this.getFocusedRow(), this.getFocusedColumn())) {
			return true;
		}
		return false;
	}

	protected boolean isShiftDown() {
		return shiftDown;
	}

	private void setShiftDown(boolean value) {
		shiftDown = value;
	}

	protected boolean isControlDown() {
		return controlDown;
	}

	private void setControlDown(boolean value) {
		controlDown = value;
	}

	public void focus(int row, int column) {
		LOG.info("focus("+row+","+column+")");
		row = Math.max(0, Math.max(getMinimumFocusedRow(), row));
		column = Math.max(0, Math.max(getMinimumFocusedColumn(), column));
		getFocusCell().set(row, column);
	}

	@Override
	public boolean isSelected(int rowIndex, int columnIndex) {
		return RangeUtil.contains(getSelectedRangeList(), rowIndex, columnIndex);
	}

	@Override
	public boolean isRowSelected(int rowIndex) {
		return RangeUtil.containsRow(getSelectedRangeList(), rowIndex);
	}

	@Override
	public boolean isColumnSelected(int columnIndex) {
		return RangeUtil.containsColumn(getSelectedRangeList(), columnIndex);
	}

	@Override
	public void onMousePressed(MouseEvent e) {
		Point pt = e.getPoint();
		int row = grid.rowAtViewPoint(pt);
		int col = grid.columnAtViewPoint(pt);
		moveTo(row, col, e.isShiftDown(), e.isControlDown());
	}
	
	@Override
	public void onMouseDragged(MouseEvent e) {
		Point pt = e.getPoint();
		int row = grid.rowAtViewPoint(pt);
		int col = grid.columnAtViewPoint(pt);
		focus(row, col);
		repaint();
	}

	@Override
	public void clear(){}

	@Override
	public void onFocusMoved(int row, int column) {}

	//
	//Keyアクションの実装
	//

	@Override
	public void onKeyLeft() {
		move(0, -1);
	}

	public void onKeyRight() {
		move(0, +1);
	}

	public void onKeyUp() {
		move(-1, 0);
	}

	public void onKeyDown() {
		move(+1, 0);
	}

	public void onKeyPageLeft() {
		move(0, - grid.getScrollModel().getColumnExtent());
	}

	public void onKeyPageRight() {
		move(0, + grid.getScrollModel().getColumnExtent());
	}

	public void onKeyPageUp() {
		move(- grid.getScrollModel().getRowExtent(), 0);
	}

	public void onKeyPageDown() {
		move(+ grid.getScrollModel().getRowExtent(), 0);
	}

	private void move(int deltaRow, int deltaColumn) {
		LOG.info("SHIFT="+isShiftDown()+",CTRL="+isControlDown());
		int currentRow = getFocusedRow();
		int currentCol = getFocusedColumn();
		IRange range = grid.getCellRange(currentRow, currentCol);
		int row = currentRow+deltaRow;
		int col = currentCol+deltaColumn;
		if(range != null) {
			if(deltaRow > 0) {
				if(row <= range.getBottom()) {
					row = range.getBottom() + 1;
				}
			}
			else if(deltaRow < 0) {
				if(row >= range.getTop()) {
					row = range.getTop() - 1;
				}
			}
			if(deltaColumn > 0) {
				if(col <= range.getRight()) {
					col = range.getRight() + 1;
				}
			}
			else if(deltaColumn < 0) {
				if(col >= range.getLeft()) {
					col = range.getLeft() - 1;
				}
			}
		}
		int newRowIndex = _rowIndex(row);
		int newColumnIndex = _columnIndex(col);
		boolean shft = isShiftDown();
		boolean ctrl = isControlDown();
		moveTo(newRowIndex, newColumnIndex, shft, ctrl);
	}

	protected void moveTo(int newRowIndex, int newColumnIndex, boolean shft, boolean ctrl) {
		int currentRow = getFocusedRow();
		int currentCol = getFocusedColumn();
		LOG.info("(newRowIndex,newColumnIndex)=("+newRowIndex+","+newColumnIndex+")");
		if(currentRow != newRowIndex || currentCol != newColumnIndex) {
			getGrid().getEditorHandler().stopEditing();
			getGrid().scrollToVisible(newRowIndex, newColumnIndex);
		}

		if(!shft) {
			if(!ctrl) {
				clear();
			}
			onFocusMoved(newRowIndex, newColumnIndex);
		}
		focus(newRowIndex, newColumnIndex);
		repaint();
	}
	
	private int _rowIndex(int rowIndex) {
		if(rowIndex < 0) {
			rowIndex = 0;
		}
		int count = getGrid().getRows().getCount();
		if(count <= rowIndex) {
			rowIndex = count - 1; 
		}
		return rowIndex;
	}

	private int _columnIndex(int columnIndex) {
		if(columnIndex < 0) {
			columnIndex = 0;
		}
		int count = getGrid().getColumns().getCount();
		if(count <= columnIndex) {
			columnIndex = count - 1; 
		}
		return columnIndex;
	}
	
	//
	//KeyListnerインタフェースの実装
	//
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			setShiftDown(true);
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
			setControlDown(true);
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			setShiftDown(false);
		}
		if(e.getKeyCode() == KeyEvent.VK_CONTROL) {
			setControlDown(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		RangeUtil.tableChanged(selectedRangeList, e);
	}

}
