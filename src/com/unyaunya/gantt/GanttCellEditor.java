package com.unyaunya.gantt;

import java.awt.Component;

import com.unyaunya.grid.editor.DefaultCellEditor;
import com.unyaunya.swing.JGrid;

@SuppressWarnings("serial")
public class GanttCellEditor extends DefaultCellEditor {
	public GanttCellEditor(JGrid grid) {
		super(grid);
	}

	@Override
	public Component getCellEditorComponent(JGrid grid, Object value,
			boolean isSelected, int row, int column) {
		switch(column) {
		case 3:
		case 4:
			return super.getCellEditorComponent(grid, value, isSelected, row, column);
		default:
			return super.getCellEditorComponent(grid, value, isSelected, row, column);
		}
	}

}
