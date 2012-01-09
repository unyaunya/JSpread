/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Component;

import javax.swing.CellEditor;

import com.unyaunya.swing.JSpread;


/**
 * @author wata
 *
 */
public interface ISpreadCellEditor extends CellEditor {
	public Component getCellEditorComponent(JSpread spread,
            Object value,
            boolean isSelected,
            int row,
            int column);
}
