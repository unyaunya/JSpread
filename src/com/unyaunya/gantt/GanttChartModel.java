package com.unyaunya.gantt;

import java.util.List;

import com.unyaunya.grid.GridModel;
import com.unyaunya.grid.table.GridTableModel;

public class GanttChartModel extends GridModel {
	public GanttChartModel() {
		super(new GridTableModel());
	}

	public GanttChartModel readDocument(GanttDocument doc) { 
		//SpreadSheetModel.clear()‚ðŽÀ‘•‚·‚×‚«
		this.setTableModel(null);
		List<Task> taskList = doc.getTasks();
		for(int i = 0; i < taskList.size(); i++) {
			this.insertRow(5);
		}
		return this;
	}
}
