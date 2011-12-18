/**
 * 
 */
package com.unyaunya.spread;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.unyaunya.swing.JSpread;

/**
 * @author wata
 *
 */
public class DefaultKeyAdapter extends KeyAdapter {
	private JSpread spread;
	
	public DefaultKeyAdapter(JSpread spread) {
		assert(spread != null);
		this.spread = spread;
		this.spread.addKeyListener(this);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			spread.getFocusModel().left();
			break;
		case KeyEvent.VK_RIGHT:
			spread.getFocusModel().right();
			break;
		case KeyEvent.VK_UP:
			spread.getFocusModel().up();
			break;
		case KeyEvent.VK_DOWN:
			spread.getFocusModel().down();
			break;
		default:
			System.out.println(e);
			break;
		}
	}

}
