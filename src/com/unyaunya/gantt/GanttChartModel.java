package com.unyaunya.gantt;

import java.util.List;

import com.unyaunya.spread.SpreadSheetModel;

@SuppressWarnings("serial")
public class GanttChartModel extends SpreadSheetModel {
	public GanttChartModel() {}

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
