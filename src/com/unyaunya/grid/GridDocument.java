package com.unyaunya.grid;

import javax.swing.table.TableModel;

import com.unyaunya.swing.application.IDocument;

public class GridDocument extends GridModel implements IDocument {
	private boolean modified;
	
	public GridDocument(TableModel tableModel) {
		super(tableModel);
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean isModified) {
		modified = isModified;
	}
}
