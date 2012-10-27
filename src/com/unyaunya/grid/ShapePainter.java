package com.unyaunya.grid;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.List;

import com.unyaunya.swing.JGrid;

public class ShapePainter implements IPainter {
	private JGrid grid;
	
	public ShapePainter(JGrid grid) {
		assert(grid != null);
		this.grid = grid;
	}
	@Override
	public void paint(Graphics2D g2d) {
    	List<Shape> shapes = grid.getShapeList().getShapeList();
    	for(Shape s: shapes) {
    		g2d.draw(s);
    	}
	}
}
