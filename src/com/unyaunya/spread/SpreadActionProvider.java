package com.unyaunya.spread;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;

import com.unyaunya.grid.IGridModel;
import com.unyaunya.swing.JSpread;

public class SpreadActionProvider {
	private JSpread spread;

	public SpreadActionProvider(JSpread spread) {
		this.spread = spread;
	}

	private JSpread getSpread() {
		return spread;
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
			super("�t�H���g�̐F");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			IGridModel m = getSpread().getGridModel();
			int row = getSpread().getSelectionModel().getRowOfLeadCell();
			int col = getSpread().getSelectionModel().getColumnOfLeadCell();
			Color currentColor = m.getCellAt(row, col).getForegroundColor();
			Color newColor = JColorChooser.showDialog(null, "�t�H���g�̐F��I��", currentColor);
			if(newColor != null) {
				getSpread().setCellForeground(newColor);
				getSpread().repaint();
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
			super("�w�i�F");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			IGridModel m = getSpread().getGridModel();
			int row = getSpread().getSelectionModel().getRowOfLeadCell();
			int col = getSpread().getSelectionModel().getColumnOfLeadCell();
			Color currentColor = m.getCellAt(row, col).getBackgroundColor();
			Color newColor = JColorChooser.showDialog(null, "�w�i�F��I��", currentColor);
			if(newColor != null) {
		        getSpread().setCellBackground(newColor);
		        getSpread().repaint();
			}
		}
	}

	class CellCouplingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public CellCouplingAction() {
			super("�Z���̌���");
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
			super("�폜");
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
			super("�E�B���h�E�g�̌Œ�");
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
			if(getSpread().arePanesFreezed()) {
				getSpread().unfreezePanes();
			}
			else {
				getSpread().freezePanes();
			}
			boolean flag = spread.arePanesFreezed();
			firePropertyChange(Action.NAME, getActionName(!flag), getActionName(flag));
		}

		private String getActionName() {
			return this.getActionName(getSpread().arePanesFreezed());
		}

		private String getActionName(boolean arePanesFreezed) {
			if(arePanesFreezed) {
				return "�E�B���h�E�g�̌Œ�̉���";
			}
			else {
				return "�E�B���h�E�g�̌Œ�";
			}
		}
	}

	class InsertRowAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public InsertRowAction() {
			super("�s");
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
			super("��");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			getSpread().insertColumn();
		}
	}

}
