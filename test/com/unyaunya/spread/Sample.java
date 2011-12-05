package com.unyaunya.spread;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

//import com.unyaunya.spread.JSpread;

class SampleTable extends AbstractTableModel {
	public SampleTable() {}
	@Override
	public int getColumnCount() {
		return 100;
	}

	@Override
	public int getRowCount() {
		return 200;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return "(" + Integer.toString(row) + "," + Integer.toString(col) +")";
	}
}

class MyMouseListener extends MouseAdapter {
	JSpread spread;
	
	public MyMouseListener(JSpread spread) {
		super();
		this.spread = spread;
		
	}
	public void mouseClicked(MouseEvent e) {
		Point pt = e.getPoint();
		System.out.println(
				"("+Integer.toString(spread.rowAtPoint(pt))+
				","+Integer.toString(spread.columnAtPoint(pt))+
				")");
	}
}

public class Sample {
	public static TableModel createModel() {
		return new SampleTable();
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSpread spread = new JSpread();
		spread.setModel(createModel());
		
		spread.addMouseListener(new MyMouseListener(spread));
		//spread.setShowGrid(true);
		//spread.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		//JScrollPane sp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		//		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//sp.setViewportView(spread);
		JSpreadPane sp = new JSpreadPane(spread);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(sp);
		f.setSize(800,600);
		f.setTitle("Spread");
		f.setVisible(true);
	}
}
