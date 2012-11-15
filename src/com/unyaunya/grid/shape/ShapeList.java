package com.unyaunya.grid.shape;

//import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

public class ShapeList {
	private List<Shape> shapeList; 
	public ShapeList() {
		this.shapeList = new ArrayList<Shape>(); 
		//addShape(new Rectangle(60, 80, 100, 32));
	}

	public List<Shape> getShapeList() {
		return shapeList;
	}

	public void addShape(Shape shape) {
		shapeList.add(shape);
	}
}
