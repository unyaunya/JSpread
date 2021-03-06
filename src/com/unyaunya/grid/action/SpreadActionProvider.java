package com.unyaunya.grid.action;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;

import com.unyaunya.grid.IGridModel;
import com.unyaunya.swing.JGrid;
import com.unyaunya.swing.JSpread;

public class SpreadActionProvider {
	private JSpread spread;
	private JGrid grid;

	public SpreadActionProvider(JSpread spread) {
		this.spread = spread;
		this.grid = spread;
	}

	private JSpread getSpread() {
		return spread;
	}
	
	private JGrid getGrid() {
		return grid;
	}

	public Action getForegroundColorAction() {
		return new ForegroundColorAction();
	}

	@SuppressWarnings("serial")
	class ForegroundColorAction extends AbstractAction {
		/**
		 * 
		 */
		public ForegroundColorAction() {
			super("フォントの色");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			IGridModel m = getSpread().getGridModel();
			int row = getGrid().getGridSelectionModel().getFocusedRow();
			int col = getGrid().getGridSelectionModel().getFocusedColumn();
			Color currentColor = m.getCellAt(row, col).getForegroundColor();
			Color newColor = JColorChooser.showDialog(null, "フォントの色を選択", currentColor);
			if(newColor != null) {
				getSpread().setCellForeground(newColor);
				getGrid().repaint();
			}
		}
	}

	public Action getBackgroundColorAction() {
		return new BackgroundColorAction();
	}
	public Action getCellCouplingAction() {
		return new CellCouplingAction();
	}
	public Action getDeleteAction() {
		return new DeleteAction();
	}
	public Action getFreezePanesAction() {
		return new FreezePanesAction();
	}
	public Action getInsertRowAction() {
		return new InsertRowAction();
	}
	public Action getInsertColumnAction() {
		return new InsertColumnAction();
	}
	
	class BackgroundColorAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public BackgroundColorAction() {
			super("背景色");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			IGridModel m = getGrid().getGridModel();
			int row = getGrid().getGridSelectionModel().getFocusedRow();
			int col = getGrid().getGridSelectionModel().getFocusedColumn();
			Color currentColor = m.getCellAt(row, col).getBackgroundColor();
			Color newColor = JColorChooser.showDialog(null, "背景色を選択", currentColor);
			if(newColor != null) {
		        getSpread().setCellBackground(newColor);
		        getGrid().repaint();
			}
		}
	}

	class CellCouplingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public CellCouplingAction() {
			super("セルの結合");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			getSpread().coupleCells();
		}
	}
	
	class DeleteAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public DeleteAction() {
			super("削除");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			getSpread().removeRow();
		}
	}

	class FreezePanesAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FreezePanesAction() {
			super("ウィンドウ枠の固定");
		}

		@Override
		public Object getValue(String key) {
			if(Action.NAME.equals(key)) {
				return getActionName();
			}
			else {
				return super.getValue(key);
			}
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if(getGrid().arePanesFreezed()) {
				getGrid().unfreezePanes();
			}
			else {
				getGrid().freezePanes();
			}
			boolean flag = spread.arePanesFreezed();
			firePropertyChange(Action.NAME, getActionName(!flag), getActionName(flag));
		}

		private String getActionName() {
			return this.getActionName(getGrid().arePanesFreezed());
		}

		private String getActionName(boolean arePanesFreezed) {
			if(arePanesFreezed) {
				return "ウィンドウ枠の固定の解除";
			}
			else {
				return "ウィンドウ枠の固定";
			}
		}
	}

	class InsertRowAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public InsertRowAction() {
			super("行");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			getSpread().insertRow();
		}
	}

	class InsertColumnAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public InsertColumnAction() {
			super("列");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			getSpread().insertColumn();
		}
	}
}
