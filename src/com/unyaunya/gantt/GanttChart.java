package com.unyaunya.gantt;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.JSpreadPane;

public class GanttChart extends JSpreadPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger LOG = Logger.getLogger(GanttChart.class.getName());
	private GanttDocument document = new GanttDocument();
	
	public GanttChart() {
		super(new JSpread());
		getSpread().getConfig().setRowInsertionSuppoorted(true);
	}

	/**
	 * �\���Ώۂ̃h�L�������g���擾����
	 * @return
	 */
	public GanttDocument getDocument() {
		return document;
	}
	
	public Action getLevelUpAction() {
		return new AbstractAction("�K�w���グ��") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				LOG.info("LevelUp");
			}
		};
	}

	public Action getLevelDownAction() {
		return new AbstractAction("�K�w��������") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				LOG.info("LevelUp");
			}
		};
	}
}
