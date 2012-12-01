package com.unyaunya.gantt.application;

import java.awt.event.MouseEvent;

import com.unyaunya.grid.IGridEventHandler;
import com.unyaunya.swing.application.Application;

public class GridEventAdapter implements IGridEventHandler {

	@Override
	public void mouseClicked(MouseEvent e) {
		Application.getInstance().getAppFrame().showPopupMenu(e);
	}

}
