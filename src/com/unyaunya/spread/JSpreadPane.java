/**
 * 
 */
package com.unyaunya.spread;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

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
		//
		this.horizontalBar.setMaximum(100);
		this.horizontalBar.setValue(0);
		this.verticalBar.setMaximum(100);
		this.verticalBar.setValue(0);
		this.horizontalBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				JSpreadPane.this.getSpread().setLeftMostColumn(e.getValue());
			}});
		this.verticalBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				JSpreadPane.this.getSpread().setTopMostRow(e.getValue());
			}});
	}

	protected void setScrollBarPosition() {
		if(this.spread == null) {
			return;
		}
		Rectangle bounds = this.spread.getBounds();
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
