/**
 * 
 */
package com.unyaunya.swing;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.unyaunya.spread.Layout;

class MyMouseListener extends MouseAdapter {
	JSpread spread;
	
	public MyMouseListener(JSpread spread) {
		super();
		this.spread = spread;
	}

	/*
	public void mouseClicked(MouseEvent e) {
		Point pt = e.getPoint();
		System.out.println(
				"("+Integer.toString(spread.rowAtPoint(pt))+
				","+Integer.toString(spread.columnAtPoint(pt))+
				")");
	}
	*/
}


/**
 * @author wata
 *
 */
public class JSpreadPane extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JSpread spread = null;
	JScrollBar horizontalBar = new JScrollBar(Adjustable.HORIZONTAL);
	JScrollBar verticalBar = new JScrollBar(Adjustable.VERTICAL);

	public JSpreadPane() {
		this(null);
	}

	/**
	 * 
	 */
	public JSpreadPane(JSpread spread) {
		super(new Layout());
		init(spread);
	}

	private void init(JSpread spread) {
		spread.addMouseListener(new MyMouseListener(spread));
		this.add(spread, BorderLayout.CENTER);
		this.add(this.horizontalBar, BorderLayout.SOUTH);
		this.add(this.verticalBar, BorderLayout.EAST);
		setSpread(spread);
	}

	/**
	 * @param spread
	 */
	public void setSpread(JSpread spread) {
		this.spread = spread;
		spread.setScrollBar(this.horizontalBar, this.verticalBar);
	}
	
	public JSpread getSpread() {
		return spread;
	}
	JScrollBar getHorizontalBar() {
		return horizontalBar;
	}
	JScrollBar getVerticalBar() {
		return verticalBar;
	}

	public Action getForegroundColorAction() {
		return new ForegroundColorAction();
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
	
	class ForegroundColorAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public ForegroundColorAction() {
			super("フォントの色");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			int row = getSpread().getSelectionModel().getRowOfLeadCell();
			int col = getSpread().getSelectionModel().getColumnOfLeadCell();
			Color currentColor = getSpread().getCellBackground(row, col);
			Color newColor = JColorChooser.showDialog(null, "フォントの色を選択", currentColor);
			if(newColor != null) {
		        getSpread().setCellForeground(newColor);
		        getSpread().repaint();
			}
		}
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
			int row = getSpread().getSelectionModel().getRowOfLeadCell();
			int col = getSpread().getSelectionModel().getColumnOfLeadCell();
			Color currentColor = getSpread().getCellBackground(row, col);
			Color newColor = JColorChooser.showDialog(null, "背景色を選択", currentColor);
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
			if(spread.arePanesFreezed()) {
				spread.unfreezePanes();
			}
			else {
				spread.freezePanes();
			}
			boolean flag = spread.arePanesFreezed();
			firePropertyChange(Action.NAME, getActionName(!flag), getActionName(flag));
		}

		private String getActionName() {
			return this.getActionName(spread.arePanesFreezed());
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
