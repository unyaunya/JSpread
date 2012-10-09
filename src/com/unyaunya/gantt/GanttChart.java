package com.unyaunya.gantt;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.unyaunya.grid.Columns;
import com.unyaunya.swing.JGridPane;
import com.unyaunya.swing.JSpread;

@SuppressWarnings("serial")
public class GanttChart extends JGridPane {
	private static Logger LOG = Logger.getLogger(GanttChart.class.getName());
	private GanttDocument document = new GanttDocument();
	private int[] columnWidth = {32, 32, 160, 64, 64, 48, 48};
	
	public GanttChart() {
		super(new JSpread());
		getGrid().setGridModel(new GanttChartModel());
		getGrid().setCellEditor(new GanttCellEditor(getGrid()));
		int headerRowCount = getGanttChartModel().getGanttTableModel().getHeaderRowCount();
		getGrid().getGridSelectionModel().setMinimumFocusedRow(headerRowCount);
		getGrid().getGridSelectionModel().focus(headerRowCount, 0);
		
		getSpread().getConfig().setRowInsertionSuppoorted(true);
		
		init();
	}

	public JSpread getSpread() {
		return (JSpread)getGrid();
	}

	private int getDefaultColumnWidth() {
		return 32;
	}
	
	private void init() {
		Columns columns = getSpread().getColumns();
		
		for(int i = 0; i < columns.getCount(); i++) {
			if(i < columnWidth.length) {
				columns.setWidth(i, columnWidth[i]);
			}
			else {
				columns.setWidth(i, getDefaultColumnWidth());
			}
		}
	}

	public GanttChartModel getGanttChartModel() {
		return (GanttChartModel)getSpread().getGridModel();
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
			sp.getGridModel().setValueAt("新しいタスク", newRow, 1);
		}
	}
}
