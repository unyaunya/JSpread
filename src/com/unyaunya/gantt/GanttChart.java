package com.unyaunya.gantt;

import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.JSpreadPane;

public class GanttChart extends JSpreadPane {

	public GanttChart() {
		super(new JSpread());
		getSpread().getConfig().setRowInsertionSuppoorted(true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
