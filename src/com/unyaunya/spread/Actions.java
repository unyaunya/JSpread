package com.unyaunya.spread;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.InputMapUIResource;

import com.unyaunya.swing.JGrid;

public class Actions {
	private JGrid grid;
	private ActionMap actionMap;
	private InputMap inputMap;

    @SuppressWarnings("serial")
	public class LeftAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			grid.getHandler().left();
		}
    }
	
    @SuppressWarnings("serial")
	public class RightAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			grid.getHandler().right();
		}
    }
    @SuppressWarnings("serial")
	public class UpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			grid.getHandler().up();
		}
    }
    
    @SuppressWarnings("serial")
	public class DownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			grid.getHandler().down();
		}
    }
    
    @SuppressWarnings("serial")
	public class PageUpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			grid.getHandler().pageUp();
		}
    }
    @SuppressWarnings("serial")
	public class PageDownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			grid.getHandler().pageDown();
		}
    }

    //
    //
    //
    public Actions(JGrid spread) {
		this.grid = spread;
	}

    public ActionMap getActionMap() {
    	if (actionMap == null) {
    		actionMap = createActionMap();
    	}
    	return actionMap;
    }

    public InputMap getInputMap(int condition) {
        if (condition == JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT) {
        	if(inputMap == null) {
        		inputMap = createInputMap();
        	}
        	return inputMap;
        }
        return null;
    }

    
    public ActionMap createActionMap() {
    	ActionMap map = new ActionMapUIResource();
    	addAction(map, new LeftAction());
    	addAction(map, new RightAction());
    	addAction(map, new UpAction());
    	addAction(map, new DownAction());
    	addAction(map, new PageUpAction());
    	addAction(map, new PageDownAction());
        return map;
    }    
    
    private void addAction(ActionMap map, Action action) {
        map.put(action.getClass(), action);
    }

    public InputMap createInputMap() {
    	InputMap map = new InputMapUIResource();
    	addAction(map, KeyEvent.VK_LEFT, LeftAction.class);
    	addAction(map, KeyEvent.VK_RIGHT, RightAction.class);
    	addAction(map, KeyEvent.VK_UP, UpAction.class);
    	addAction(map, KeyEvent.VK_DOWN, DownAction.class);
    	addAction(map, KeyEvent.VK_ENTER, DownAction.class);
    	addAction(map, KeyEvent.VK_PAGE_UP, PageUpAction.class);
    	addAction(map, KeyEvent.VK_PAGE_DOWN, PageDownAction.class);
    	//
    	addShiftAction(map, KeyEvent.VK_LEFT, LeftAction.class);
    	addShiftAction(map, KeyEvent.VK_RIGHT, RightAction.class);
    	addShiftAction(map, KeyEvent.VK_UP, UpAction.class);
    	addShiftAction(map, KeyEvent.VK_DOWN, DownAction.class);
    	addShiftAction(map, KeyEvent.VK_ENTER, DownAction.class);
    	addShiftAction(map, KeyEvent.VK_PAGE_UP, PageUpAction.class);
    	addShiftAction(map, KeyEvent.VK_PAGE_DOWN, PageDownAction.class);
    	//
    	addControlAction(map, KeyEvent.VK_LEFT, LeftAction.class);
    	addControlAction(map, KeyEvent.VK_RIGHT, RightAction.class);
    	addControlAction(map, KeyEvent.VK_UP, UpAction.class);
    	addControlAction(map, KeyEvent.VK_DOWN, DownAction.class);
    	addControlAction(map, KeyEvent.VK_ENTER, DownAction.class);

    	return map;
    }    

    private void addAction(InputMap map, int key, Class<?> actionClass) {
    	addAction(map, key, 0, actionClass);
    }

    private void addShiftAction(InputMap map, int key, Class<?> actionClass) {
    	addAction(map, KeyStroke.getKeyStroke(key, java.awt.event.InputEvent.SHIFT_DOWN_MASK), actionClass);
    }

    private void addControlAction(InputMap map, int key, Class<?> actionClass) {
    	addAction(map, KeyStroke.getKeyStroke(key, java.awt.event.InputEvent.CTRL_DOWN_MASK), actionClass);
    }

    private void addAction(InputMap map, int key, int modifier, Class<?> actionClass) {
    	addAction(map, KeyStroke.getKeyStroke(key, modifier), actionClass);
    }

    private void addAction(InputMap map, KeyStroke ks, Class<?> actionClass) {
    	map.put(ks, actionClass);
    }
}
