package com.unyaunya.gantt;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.unyaunya.grid.CellPosition;
import com.unyaunya.grid.Columns;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.selection.IGridSelectionModel;
import com.unyaunya.swing.JGridPane;
import com.unyaunya.swing.JSpread;

@SuppressWarnings("serial")
public class GanttChart extends JGridPane {
	private static Logger LOG = Logger.getLogger(GanttChart.class.getName());
	private GanttDocument document = new GanttDocument();
	private int[] columnWidth = {32, 32, 160, 64, 64, 48, 48};
	
	public GanttChart() {
		super(new JSpread());
		GanttChartModel gcm = new GanttChartModel();
		getGrid().setGridModel(gcm);
		getGrid().setTreeCellColumn(2);
		getGrid().setCellEditor(new GanttCellEditor(getGrid()));
		int headerRowCount = getGanttChartModel().getGanttTableModel().getHeaderRowCount();
		getGrid().getScrollModel().setDefaultSplitPosition(new CellPosition(headerRowCount, 0));
		getGrid().getGridSelectionModel().setMinimumFocusedRow(headerRowCount);
		getGrid().getGridSelectionModel().focus(headerRowCount, 0);
		
		getSpread().getConfig().setRowInsertionSuppoorted(true);
		getGrid().setForegroundPainter(new GanttPainter(getGrid().getScrollModel(), gcm.getGanttTableModel()));
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
				JSpread sp = getSpread();
				IGridSelectionModel sm = sp.getGridSelectionModel();
				ArrayList<IRange> srl = sm.getSelectedRangeList();
				for(int i = 0; i < srl.size(); i++) {
					IRange r = srl.get(i);
					sp.getRows().levelUp(r.getTop(), r.getBottom() - r.getTop() + 1);
				}
				sp.repaint();
			}
		};
	}

	public Action getLevelDownAction() {
		return new AbstractAction("階層を下げる") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				LOG.info("LevelDown");
				JSpread sp = getSpread();
				IGridSelectionModel sm = sp.getGridSelectionModel();
				ArrayList<IRange> srl = sm.getSelectedRangeList();
				for(int i = 0; i < srl.size(); i++) {
					IRange r = srl.get(i);
					sp.getRows().levelDown(r.getTop(), r.getBottom() - r.getTop() + 1);
				}
				sp.repaint();
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
