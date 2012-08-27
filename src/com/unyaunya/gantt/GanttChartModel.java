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
		setValueAt("�^�X�NID", row, 1);
		setValueAt("�K�w", row, 2);
		setValueAt("�^�X�N��", row, 3);
		setValueAt("�J�n��", row, 4);
		setValueAt("�I����", row, 5);
		//setColumnWidth(1, 200);
	}

	public GanttChartModel readDocument(GanttDocument doc) { 
		//SpreadSheetModel.clear()���������ׂ�
		this.setTableModel(null);
		List<Task> taskList = doc.getTasks();
		for(int i = 0; i < taskList.size(); i++) {
			this.insertRow(5);
		}
		return this;
	}
}
