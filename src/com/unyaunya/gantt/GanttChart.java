package com.unyaunya.gantt;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.unyaunya.grid.Columns;
import com.unyaunya.grid.ICell;
import com.unyaunya.spread.SpreadSheetModel;
import com.unyaunya.swing.JGridPane;
import com.unyaunya.swing.JSpread;

public class GanttChart extends JGridPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger LOG = Logger.getLogger(GanttChart.class.getName());
	private GanttDocument document = new GanttDocument();
	
	public GanttChart() {
		super(new JSpread());
		getSpread().setSpreadSheetModel(new GanttChartModel());
		getSpread().getConfig().setRowInsertionSuppoorted(true);
		init();
	}

	public JSpread getSpread() {
		return (JSpread)getGrid();
	}

	private void init() {
		SpreadSheetModel m = getSpread().getSpreadSheetModel();
		Columns columns = getSpread().getColumns();
		int row = 1;
		ICell cell = m.getCellAt(1,1);
		cell.setValue("ID");
		cell.setColumn(2);
		cell.setValue("階層");
		m.setValueAt("タスク名", row, 3);
		m.setValueAt("開始日", row, 4);
		m.setValueAt("終了日", row, 5);
		columns.setWidth(1, 32);
		columns.setWidth(2, 32);
		columns.setWidth(3, 160);
		columns.setWidth(4, 64);
		columns.setWidth(5, 64);
	}

	public GanttChartModel getGanttChartModel() {
		return (GanttChartModel)getSpread().getSpreadSheetModel();
	}
	/**
	 * 表示対象のドキュメントを取得する
	 * @return
	 */
	public GanttDocument getDocument() {
		return document;
	}
	
	public Action getLevelUpAction() {
		return new AbstractAction("階層を上げる") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				LOG.info("LevelUp");
			}
		};
	}

	public Action getLevelDownAction() {
		return new AbstractAction("階層を下げる") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				LOG.info("LevelUp");
			}
		};
	}

	public Action getInsertRowAction() {
		return new InsertTaskAction();
	}

	class InsertTaskAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public InsertTaskAction() {
			super("タスク");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			JSpread sp = getSpread();
			int newRow = sp.insertRow();
			sp.getSpreadSheetModel().setValueAt("新しいタスク", newRow, 1);
		}
	}

}
