/**
 * 
 */
package com.unyaunya.grid.editor;

import java.awt.Component;

import javax.swing.CellEditor;

import com.unyaunya.swing.JGrid;


/**
 * @author wata
 *
 */
public interface IGridCellEditor extends CellEditor {
	public Component getCellEditorComponent(JGrid grid,
            Object value,
            boolean isSelected,
            int row,
            int column);
}
