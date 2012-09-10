package com.unyaunya.grid;

@SuppressWarnings("serial")
public class Range extends CellRange {
	private IGridModel model;

	public Range(IGridModel model, int top, int left, int bottom, int right) {
		super(top, left, bottom, right);
		this.model = model;
	}

	protected IGridModel getModel() {
		return model;
	}
}
