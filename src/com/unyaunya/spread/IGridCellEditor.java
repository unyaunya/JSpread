/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;

import javax.swing.CellEditor;

import com.unyaunya.swing.JEditableGrid;


/**
 * @author wata
 *
 */
public interface IGridCellEditor extends CellEditor {
	public Component getCellEditorComponent(JEditableGrid spread,
            Object value,
            boolean isSelected,
            int row,
            int column);
}
