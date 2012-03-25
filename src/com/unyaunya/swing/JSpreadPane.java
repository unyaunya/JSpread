/**
 * 
 */
package com.unyaunya.swing;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.unyaunya.spread.Layout;

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
		this.add(spread, BorderLayout.CENTER);
		this.add(this.horizontalBar, BorderLayout.SOUTH);
		this.add(this.verticalBar, BorderLayout.EAST);
		setSpread(spread);
	}

	private void resized() {
		Rectangle rect = getSpread().getBounds();
		getSpread().getRangeModel(Adjustable.HORIZONTAL).setComponentSize(rect.width);
		getSpread().getRangeModel(Adjustable.VERTICAL).setComponentSize(rect.height);
	}
	
	public void setSpread(JSpread spread) {
		this.spread = spread;
		this.horizontalBar.setModel(getSpread().getRangeModel(Adjustable.HORIZONTAL));
		this.verticalBar.setModel(getSpread().getRangeModel(Adjustable.VERTICAL));
		getSpread().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resized();
			}
		});
		resized();
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
