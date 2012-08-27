/**
 * 
 */
package com.unyaunya.swing.plaf;

import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import com.unyaunya.spread.Actions;
import com.unyaunya.swing.JSpread;

/**
 * @author wata
 *
 */
public class SpreadUI extends GridUI {
    @SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SpreadUI.class.getName());

    private Actions actions;
	
	protected JSpread spread;

	//
	//  The installation/uninstall procedures and support
	//

    public static ComponentUI createUI(JComponent c) {
        return new SpreadUI();
    }

    //  Installation
    public void installUI(JComponent c) {
    	super.installUI(c);
        spread = (JSpread)c;
        //actions = new Actions(table);
        actions = spread.getActions();
        //installDefaults();
        //installDefaults2();
        //installListeners();
        installKeyboardActions();
    }

    /**
     * Register all keyboard actions on the JSpread.
     */
    protected void installKeyboardActions() {
    	InputMap keyMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    	SwingUtilities.replaceUIInputMap(spread, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, keyMap);
    	ActionMap map = getActionMap();
        SwingUtilities.replaceUIActionMap(spread, map);
    }

    InputMap getInputMap(int condition) {
    	if(actions != null) {
        	return actions.getInputMap(condition);
    	}
    	return null;
    }
    
    ActionMap getActionMap() {
    	if(actions != null) {
        	return actions.getActionMap();
    	}
    	return null;
    }

    //  Uninstallation

    public void uninstallUI(JComponent c) {
    	super.uninstallUI(c);
    	//uninstallDefaults();
        //uninstallListeners();
        //uninstallKeyboardActions();
        spread = null;
    }
}
