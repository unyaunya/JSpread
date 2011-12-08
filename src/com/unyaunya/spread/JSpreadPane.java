/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

/**
 * @author wata
 *
 */
public class JSpreadPane extends JPanel {
	JSpread spread = null;
	JScrollBar horizontalBar = new JScrollBar(Adjustable.HORIZONTAL);
	JScrollBar verticalBar = new JScrollBar(Adjustable.VERTICAL);
	
	/**
	 * 
	 */
	public JSpreadPane(JSpread spread) {
		super(new Layout());
		this.spread = spread;
		this.add(this.spread, BorderLayout.CENTER);
		this.add(this.horizontalBar, BorderLayout.SOUTH);
		this.add(this.verticalBar, BorderLayout.EAST);
		this.horizontalBar.setModel(getSpread().getScrollModel(JSpread.HORIZONTAL));
		this.verticalBar.setModel(getSpread().getScrollModel(JSpread.VERTICAL));
		getSpread().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Rectangle rect = e.getComponent().getBounds();
				getSpread().getScrollModel(JSpread.HORIZONTAL).setComponentSize(rect.width);
				getSpread().getScrollModel(JSpread.VERTICAL).setComponentSize(rect.height);
			}
		});
		
	}

	JSpread getSpread() {
		return spread;
	}
	JScrollBar getHorizontalBar() {
		return horizontalBar;
	}
	JScrollBar getVerticalBar() {
		return verticalBar;
	}

}
