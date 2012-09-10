package com.unyaunya.spread;

import java.io.Serializable;
import javax.swing.table.TableModel;
import com.unyaunya.grid.AbstractGridModel;

@SuppressWarnings("serial")
public class SpreadSheetModel extends AbstractGridModel implements Serializable {
	
	public SpreadSheetModel() {
		super(new SpreadModel(null));
	}
	
	@Override
	public void setTableModel(TableModel model) {
		super.setTableModel(model);
		getSpreadModel().copyValuesFrom(model);
	}

	private SpreadModel getSpreadModel() {
		return (SpreadModel)getTableModel();
	}

	public void insertRow(int row) {
		getSpreadModel().insertRow(row,  (Object[])null);
	}

	public void insertColumn(int column) {
		getSpreadModel().insertColumn(column, (Object[])null);
	}

	public void removeRow(int row) {
		getSpreadModel().removeRow(row);
	}
}
