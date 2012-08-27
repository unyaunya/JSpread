package com.unyaunya.swing;

import java.awt.Adjustable;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.unyaunya.grid.Layout;

@SuppressWarnings("serial")
public class JGridPane extends JPanel {
	/**
	 * 
	 */
	private JGrid grid = null;
	private JScrollBar horizontalBar = new JScrollBar(Adjustable.HORIZONTAL);
	private JScrollBar verticalBar = new JScrollBar(Adjustable.VERTICAL);

	public JGridPane() {
		this(null);
	}

	public JGridPane(JGrid grid) {
		super(new Layout());
		init(grid);
	}

	private void init(JGrid spread) {
		this.add(spread, BorderLayout.CENTER);
		this.add(this.getHorizontalBar(), BorderLayout.SOUTH);
		this.add(this.getVerticalBar(), BorderLayout.EAST);
		setGrid(spread);
	}

	public void setGrid(JGrid grid) {
		this.grid = grid;
		grid.setScrollBar(this.getHorizontalBar(), this.getVerticalBar());
	}

	public JGrid getGrid() {
		return grid;
	}

	JScrollBar getHorizontalBar() {
		return horizontalBar;
	}
	JScrollBar getVerticalBar() {
		return verticalBar;
	}
}
