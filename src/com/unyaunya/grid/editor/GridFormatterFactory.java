package com.unyaunya.grid.editor;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;

import com.unyaunya.swing.JGrid;

@SuppressWarnings("serial")
public class GridFormatterFactory extends DefaultFormatterFactory {
	private JGrid grid;
	public GridFormatterFactory(JGrid grid) {
		super();
		this.grid = grid;
	}

	public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField source) {
		return super.getFormatter(source);
	}
}
