package com.unyaunya.grid;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public @SuppressWarnings("serial")
class QuadrantPanel extends JPanel {
    private static final Logger LOG = Logger.getLogger(QuadrantPanel.class.getName());

	public transient JPanel panel;
	private Component editor;
	private Rectangle boundingRect;
	private int x;
	private int y;
	
	public QuadrantPanel() {
		setBorder(BorderFactory.createLineBorder(Color.BLUE));
	}

	public void setViewPosiition(int x, int y) {
		this.x = x;
		this.y = y;
		if(editor == null) {
			return;
		}
		editor.setBounds(boundingRect.x-x, boundingRect.y-y, boundingRect.width, boundingRect.height);
		repaint();
	}

	public Rectangle getViewRect() {
		return new Rectangle(x, y, getWidth(), getHeight());
	}
	
	public boolean contains(Rectangle rect) {
		LOG.info("rect="+rect.toString());
		LOG.info("getViewRect()="+getViewRect().toString());
		return getViewRect().contains(rect);
	}
	
	@Override
	public Component add(Component editor) {
		if(this.editor != null) {
			remove(this.editor);
		}
		this.editor = editor;
		this.boundingRect = new Rectangle(editor.getBounds());
		editor.setBounds(boundingRect.x-x, boundingRect.y-y, boundingRect.width, boundingRect.height);
		super.add(editor);
		return editor;
	}

	@Override
	public void remove(Component editor) {
		if(this.editor == editor) {
			this.editor = null;
			this.boundingRect = null;
		}
		super.remove(editor);
	}
}


