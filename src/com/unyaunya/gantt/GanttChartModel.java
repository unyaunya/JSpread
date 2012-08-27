package com.unyaunya.gantt;

import java.util.List;

import com.unyaunya.spread.SpreadSheetModel;

@SuppressWarnings("serial")
public class GanttChartModel extends SpreadSheetModel {
	public GanttChartModel() {
		init();
	}

	private void init() {
		int row = 1;
		setValueAt("タスクID", row, 1);
		setValueAt("階層", row, 2);
		setValueAt("タスク名", row, 3);
		setValueAt("開始日", row, 4);
		setValueAt("終了日", row, 5);
		//setColumnWidth(1, 200);
	}

	public GanttChartModel readDocument(GanttDocument doc) { 
		//SpreadSheetModel.clear()を実装すべき
		this.setTableModel(null);
		List<Task> taskList = doc.getTasks();
		for(int i = 0; i < taskList.size(); i++) {
			this.insertRow(5);
		}
		return this;
	}
}
