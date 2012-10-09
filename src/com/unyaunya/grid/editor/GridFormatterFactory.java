package com.unyaunya.grid.editor;

import java.text.DateFormat;
import java.text.Format;

import javax.swing.JFormattedTextField;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

import com.unyaunya.swing.JGrid;

@SuppressWarnings("serial")
public class GridFormatterFactory extends DefaultFormatterFactory {
	private JGrid grid;
	private int row;
	private int column;
	
	public GridFormatterFactory(JGrid grid) {
		super();
		this.grid = grid;
	}

	public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField source) {
		Format f = grid.getGridModel().getCellFormatModel().getFormat(row, column);
		if(f != null) {
			if(f instanceof DateFormat) {
				return new DateFormatter((DateFormat)f);
			}
		}
		return super.getFormatter(source);
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}
}
