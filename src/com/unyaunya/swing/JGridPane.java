package com.unyaunya.swing;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 * @author wata
 *
 */
@SuppressWarnings("serial")
class Layout extends BorderLayout {
	JGrid grid = null;
	JScrollBar horizontalBar = null;
	JScrollBar verticalBar = null;

	@Override
	 public void layoutContainer(Container parent) {
		int hbar_height = 16;
		int vbar_width = 16;
		int sp_height = parent.getHeight(); 
		int sp_width = parent.getWidth(); 
		if(horizontalBar != null) {
			sp_height -= hbar_height; 
		}
		if(verticalBar != null) {
			sp_width -= vbar_width; 
		}
		if(this.grid == null) {
			return;
		}
		this.grid.setBounds(0, 0, sp_width, sp_height);
		if(horizontalBar != null) {
			horizontalBar.setBounds(0, sp_height, sp_width, hbar_height);
		}
		if(verticalBar != null) {
			verticalBar.setBounds(sp_width, 0, vbar_width, sp_height);
		}
	 }

	 @Override
	 public void addLayoutComponent(Component comp,
             Object constraints) {
		 if(constraints.equals(BorderLayout.CENTER)) {
			 if(!(comp instanceof JSpread)) {
				 throw new RuntimeException("CENTERはJSpread専用");
			 }
			 this.grid = (JSpread)comp;
		 }
		 else if(constraints.equals(BorderLayout.SOUTH)) {
			 if(!(comp instanceof JScrollBar)) {
				 throw new RuntimeException("SOUTHはJScrollBar専用");
			 }
			 this.horizontalBar = (JScrollBar)comp;
		 }
		 else if(constraints.equals(BorderLayout.EAST)) {
			 if(!(comp instanceof JScrollBar)) {
				 throw new RuntimeException("EASTはJScrollBar専用");
			 }
			 this.verticalBar = (JScrollBar)comp;
		 }
		 else {
			 throw new RuntimeException("EAST,SOUTH,CENTER以外は使用禁止");
		 }
		 super.addLayoutComponent(comp, constraints);
	 }
}

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
