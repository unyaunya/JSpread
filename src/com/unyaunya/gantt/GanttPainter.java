package com.unyaunya.gantt;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.logging.Logger;

import com.unyaunya.grid.IPainter;
import com.unyaunya.grid.ScrollModel;

public class GanttPainter implements IPainter {
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(GanttPainter.class.getName());

	private GanttTableModel ganttTableModel;
	private ScrollModel scrollModel;
	
	public GanttPainter(ScrollModel scrollModel, GanttTableModel ganttTableModel) {
		assert(scrollModel != null);
		assert(ganttTableModel != null);
		this.scrollModel = scrollModel;
		this.ganttTableModel = ganttTableModel;
	}

	@Override
	public void paint(Graphics2D g2d) {
		for(int i = ganttTableModel.getHeaderRowCount(); i < ganttTableModel.getRowCount(); i++) {
			if(!scrollModel.getRows().isVisible(i)) {
				continue;
			}
			drawTask(g2d, i);
		}
	}

	private void drawTask(Graphics2D g2d, int index) {
		int c1 = ganttTableModel.getStartDateColumn(index);
		int c2 = ganttTableModel.getEndDateColumn(index);
		if(c1 < 0 || c2 < 0) {
			return;
		}
		Rectangle rectangle = new Rectangle();
		int x, y, w, h;
		h = scrollModel.getRows().getHeight(index);
		y = scrollModel.getRows().getPosition(index)+h/4;
		x = scrollModel.getColumns().getPosition(c1);
		w = scrollModel.getColumns().getPosition(c2+1) - x;
		h /= 2;
		rectangle.setBounds(x, y, w, h);
		g2d.draw(rectangle);
	}
}
