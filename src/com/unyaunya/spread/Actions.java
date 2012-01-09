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

import com.unyaunya.swing.JSpread;

public class Actions {
	private JSpread spread;
	private ActionMap actionMap;
	private InputMap inputMap;

    public class LeftAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			spread.getFocusModel().left();
		}
    }
	
    public class RightAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			spread.getFocusModel().right();
		}
    }
    public class UpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			spread.getFocusModel().up();
		}
    }
    public class DownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			spread.getFocusModel().down();
		}
    }
    public class PageUpAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			spread.getFocusModel().pageUp();
		}
    }
    public class PageDownAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			spread.getFocusModel().pageDown();
		}
    }

    //
    //
    //
    public Actions(JSpread spread) {
		this.spread = spread;
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
    	addAction(map, KeyEvent.VK_PAGE_UP, PageUpAction.class);
    	addAction(map, KeyEvent.VK_PAGE_DOWN, PageDownAction.class);
        return map;
    }    

    private void addAction(InputMap map, int key, Class<?> actionClass) {
    	addAction(map, KeyStroke.getKeyStroke(key,0), actionClass);
    }

    private void addAction(InputMap map, KeyStroke ks, Class<?> actionClass) {
    	map.put(ks, actionClass);
    }
}
