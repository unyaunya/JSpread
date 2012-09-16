package com.unyaunya.grid.selection;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.unyaunya.grid.IRange;
import com.unyaunya.grid.RangeUtil;
import com.unyaunya.swing.JGrid;

abstract public class AbstractSelectionModel implements IGridSelectionModel {
    private static final Logger LOG = Logger.getLogger(AbstractSelectionModel.class.getName());

    private JGrid grid;
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

	abstract protected void focus(int row, int column);

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
	public void onMousePressed(int row, int column, boolean shft, boolean ctrl) {
		if(!shft) {
			if(!ctrl) {
				clear();
			}
			onFocusMoved(row, column);
		}
		focus(row, column);
		repaint();
	}

	@Override
	public void onMouseDragged(int row, int column, boolean shft, boolean ctrl) {
		focus(row, column);
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
		//
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
		if(currentRow != newRowIndex || currentCol != newColumnIndex) {
			getGrid().getEditorHandler().stopEditing();
			getGrid().scrollToVisible(newRowIndex, newColumnIndex);
		}

		{
			boolean shft = isShiftDown();
			boolean ctrl = isControlDown();
			if(!shft) {
				if(!ctrl) {
					clear();
				}
				onFocusMoved(newRowIndex, newColumnIndex);
			}
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
}
