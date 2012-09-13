package com.unyaunya.grid.editor;

import java.awt.Component;

public class EditorHandler {
	private int editingColumn;
	private int editingRow;
	private Component editorComponent;

	public EditorHandler() {
	}

	public int getEditingRow() {
		return editingRow;
	}
	public int getEditingColumn() {
		return editingColumn;
	}
	public void setEditingRow(int row) {
		editingRow = row;
	}
	public void setEditingColumn(int column) {
		editingColumn = column;
	}

	public Component getEditorComponent() {
		return editorComponent;
	}

	public void setEditorComponent(Component c) {
		this.editorComponent = c;
	}
}
