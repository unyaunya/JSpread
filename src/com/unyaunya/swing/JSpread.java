/**
 * 
 */
package com.unyaunya.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;

import com.unyaunya.spread.DefaultCellEditor;
import com.unyaunya.spread.DefaultCellRenderer;
//import com.unyaunya.spread.DefaultKeyAdapter;
import com.unyaunya.spread.FocusModel;
import com.unyaunya.spread.ISpreadCellEditor;
import com.unyaunya.spread.ISpreadCellRenderer;
import com.unyaunya.spread.RangeModel;
import com.unyaunya.spread.ScrollModel;
import com.unyaunya.spread.SelectionModel;
import com.unyaunya.spread.SpreadBorder;
import com.unyaunya.spread.SpreadModel;
import com.unyaunya.swing.plaf.SpreadUI;

/**
 * @author wata
 *
 */
public class JSpread extends JComponent implements CellEditorListener {
    private static final Logger LOG = Logger.getLogger(JSpread.class.getName());
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see #getUIClassID
     * @see #readObject
     */
    private static final String uiClassID = "SpreadUI";

	public static final Color DEFAULT_HEADER_BACKGROUND_COLOR = new Color(0xf0,0xf0,0xf0);
	public static final Color DEFAULT_SELECTION_BACKGROUND_COLOR = new Color(0xe0,0xe0,0xff);
	public static final Color DEFAULT_FOREGROUND_COLOR = new Color(0x00,0x00,0x00);

	protected Color selectionBackground = DEFAULT_SELECTION_BACKGROUND_COLOR;
	protected Color selectionForeground = DEFAULT_FOREGROUND_COLOR;
	protected static ISpreadCellRenderer defaultCellRenderer = new DefaultCellRenderer();

	private SpreadModel model;
	private ScrollModel scrollModel;
	private SelectionModel selectionModel;
	private FocusModel focusModel;
	
	transient ISpreadCellEditor defaultCellEditor = new DefaultCellEditor();
	transient ISpreadCellEditor cellEditor = null;
	transient private Component editorComponent;
	transient protected int editingColumn;
	transient protected int editingRow;

	/*
	 * constructor
	 */
	public JSpread() {
		this.setFocusable(true);

		this.model = new SpreadModel();
		this.scrollModel = new ScrollModel(this);
		this.selectionModel = new SelectionModel();
		this.focusModel = new FocusModel(this);
//		this.addKeyListener(new DefaultKeyAdapter(this));
		/*
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusForwardTraversalKeys());
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                JComponent.getManagingFocusBackwardTraversalKeys());
		*/
		setUI(new SpreadUI());
	}

	//
	// Managing TableUI
	//

	/**
	 * Returns the L&F object that renders this component.
	 *
	 * @return the <code>SpreadUI</code> object that renders this component
	 */
	public SpreadUI getUI() {
	    return (SpreadUI)ui;
	}

	/**
	 * Sets the L&F object that renders this component and repaints.
	 *
	 * @param ui  the TableUI L&F object
	 * @see UIDefaults#getUI
	 * @beaninfo
	 *        bound: true
	 *       hidden: true
	 *    attribute: visualUpdate true
	 *  description: The UI object that implements the Component's LookAndFeel.
	 */
	public void setUI(SpreadUI ui) {
	    if (this.ui != ui) {
	        super.setUI(ui);
	        repaint();
	    }
	}

	 /**
     * Returns the suffix used to construct the name of the L&F class used to
     * render this component.
     *
     * @return the string "SpreadUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    public String getUIClassID() {
        return uiClassID;
    }

	/*
	 * methods set/get various models.
	 */
	public void setModel(TableModel model) {
		if(model == null) {
			model = new SpreadModel();
		}
		if(!(model instanceof SpreadModel)) {
			model = new SpreadModel(model);
		}
		this.model = (SpreadModel)model;
		this.getSelectionModel().select(1, 1);
		scrollModel.setTableModel(model);
		this.repaint(this.getBounds());
	}
	
	public SpreadModel getModel() {
		return model;
	}

	public ScrollModel getScrollModel() {
		return scrollModel;
	}

	/**
	 * @return the rangeModel
	 */
	public RangeModel getRangeModel(int direction) {
		return scrollModel.getRangeModel(direction);
	}

	public SelectionModel getSelectionModel() {
		return this.selectionModel;
	}

	public FocusModel getFocusModel() {
		return this.focusModel;
	}

	/*
	 * methods delegating to TableModel
	 */
	public int getRowCount() {
		return getModel().getRowCount();
	}
	
	public int getColumnCount() {
		return getModel().getColumnCount();
	}

	/*
	 * methods delegating to ScrollModel
	 */
	
	public Rectangle getCellRect(int rowIndex, int colIndex) {
		return scrollModel.getCellRect(rowIndex, colIndex);
	}
	/*

	public int rowAtPoint(Point pt) {
		return scrollModel.rowAtPoint(pt);
	}
	
	public int columnAtPoint(Point pt) {
		return scrollModel.columnAtPoint(pt);
	}
	*/
	
	public void scrollToVisible(int rowIndex, int columnIndex) {
		scrollModel.scrollToVisible(rowIndex, columnIndex);
	}

	public boolean arePanesFreezed() {
		return scrollModel.arePanesFreezed();
	}
	
	public void freezePanes() {
		scrollModel.freezePanes(focusModel.getRowIndex()-1, focusModel.getColumnIndex()-1);
	}
	public void unfreezePanes() {
		scrollModel.unfreezePanes();
	}
	
	/*
	 * methods delegating to SelectionModel
	 */
	public void select(int rowIndex, int columnIndex) {
		selectionModel.select(rowIndex, columnIndex);
	}

	/*
	 * methods related to UI appearance
	 */
	public Border getFocusBorder() {
    	return SpreadBorder.DEFAULT_FOCUS_BORDER;
    }

    public Border getNoFocusBorder() {
    	return SpreadBorder.defaultBorder;
    }

    public Border getCellBorder(boolean isSelected, boolean hasFocus, int row, int column) {
		if(hasFocus) {
			return this.getFocusBorder();
		}
		else {
			return this.getNoFocusBorder();
		}
    }
    
    protected Color getCellBackground(boolean isSelected, boolean hasFocus, int row, int column) {
		if(isSelected) {
			return this.getSelectionBackground();
		}
		else {
	    	if(row <= 0 || column <= 0) {
	    		return DEFAULT_HEADER_BACKGROUND_COLOR;
	    	}
	    	else {
	    		return Color.WHITE;
	    	}
		}
    }

    protected int getHorizontalAlignment(int row, int column) {
    	if(row <= 0 || column <= 0) {
    		return SwingConstants.CENTER;
    	}
    	else {
    		return SwingConstants.LEFT;
    	}
    }

	public Color getSelectionBackground() {
		return this.selectionBackground;
	}
	public void setSelectionBackground(Color color) {
		this.selectionBackground = color;
		repaint();
	}
	public Color getSelectionForeground() {
		return this.selectionForeground;
		
	}
	public void setSelectionForeground(Color color) {
		this.selectionForeground = color;
		repaint();
	}

	/*
	 * methods implementing to paint
	 */
	//public void repaintCell(int row, int column) {
		//repaint(getCellRect(row, column));
	//}

	public ISpreadCellRenderer getCellRenderer(int row, int column) {
		return defaultCellRenderer;
    }

	public Component prepareRenderer(ISpreadCellRenderer renderer, int row, int col) {
		SpreadModel m = getModel();
		Object s = m.getValueAt(row, col);
		boolean isSelected = this.getSelectionModel().isSelected(row, col);
		boolean hasFocus = this.getFocusModel().hasFocus(row, col);
		Border border = getCellBorder(isSelected, hasFocus, row, col);
		renderer.setBorder(border);
		renderer.setBackground(this.getCellBackground(isSelected, hasFocus, row, col));
		renderer.setHorizontalAlignment(this.getHorizontalAlignment(row, col));
		Component c = renderer.getSpreadCellRendererComponent(this, s, isSelected, hasFocus, row, col);
		return c;
	}


	/*
	 * methods related to cell editing 
	 */
	public ISpreadCellEditor getCellEditor(int row, int col) {
		ISpreadCellEditor editor = getCellEditor();
		if(editor == null) {
			editor = getDefaultCellEditor();
		}
		return editor;
	}

	/**
	 * Returns the active cell editor 
	 * @return
	 */
	public ISpreadCellEditor getCellEditor() {
		return cellEditor;
	}

	public void setCellEditor(ISpreadCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

	public ISpreadCellEditor getDefaultCellEditor() {
		return defaultCellEditor;
	}

	private Component getEditorComponent() {
		return editorComponent;
	}

	public boolean editCellAt(int row, int column) {
		return editCellAt(row, column, null);
	}

	public boolean editCellAt(int row, int column, EventObject e){
		if (row < 0 || row >= getRowCount() || column < 0 || column >= getColumnCount()) {
			return false;
		}
		ISpreadCellEditor editor = getCellEditor(row, column);
		editorComponent = prepareEditor(editor, row, column);
		editorComponent.setBounds(getCellRect(row, column));
		add(editorComponent);
		editorComponent.validate();
		
		setCellEditor(editor);
		editor.addCellEditorListener(this);
		LOG.info("editCellAt("+row+","+column+")");
		return true;
	}

	public Component prepareEditor(ISpreadCellEditor editor, int row, int column) {
		Object value = getModel().getValueAt(row, column);
		boolean hasFocus = getFocusModel().isCellFocused(row, column);
		Component comp = editor.getCellEditorComponent(this, value, hasFocus, row, column);
		/*
		if (comp instanceof JComponent) {
			JComponent jComp = (JComponent)comp;
			if (jComp.getNextFocusableComponent() == null) {
				jComp.setNextFocusableComponent(this);			}
		}
		*/
		return comp;
	}

	public void removeEditor() {
		ISpreadCellEditor editor = getCellEditor();
		if(editor != null) {
			editor.removeCellEditorListener(this);
		}
		if (editorComponent != null) {
			remove(editorComponent);
			//Rectangle cellRect = getCellRect(editingRow, editingColumn, false);
			setCellEditor(null);
			//setEditingColumn(-1);
			//setEditingRow(-1);
			editorComponent = null;
			//repaint(cellRect);
		}
	}
	
	@Override
	protected boolean processKeyBinding(KeyStroke ks,
            KeyEvent e,
            int condition,
            boolean pressed){
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
		if (!retValue && condition == WHEN_ANCESTOR_OF_FOCUSED_COMPONENT &&
				isFocusOwner()) {
			Component editorComponent = getEditorComponent();
			if(editorComponent == null) {
				// Only attempt to install the editor on a KEY_PRESSED,
				if (e == null || e.getID() != KeyEvent.KEY_PRESSED) {
					return false;
				}
				// Don't start when just a modifier is pressed
				int code = e.getKeyCode();
				if (code == KeyEvent.VK_SHIFT || code == KeyEvent.VK_CONTROL || code == KeyEvent.VK_ALT) {
					return false;
				}
				// Try to install the editor
				
				//int leadRow = getSelectionModel().getLeadSelectionIndex();
				//int leadColumn = getColumnModel().getSelectionModel().getLeadSelectionIndex();
				//if (leadRow != -1 && leadColumn != -1 && !isEditing()) {
				int row = getFocusModel().getRowIndex();
				int col = getFocusModel().getColumnIndex();
				if (!editCellAt(row, col)) {
					return false;
				}
				editorComponent = getEditorComponent();
				if (editorComponent == null) {
					return false;
				}
			}
			if (editorComponent instanceof JComponent) {
				//retValue = ((JComponent)editorComponent).processKeyBinding(ks, e, WHEN_FOCUSED, pressed);
				//if (getSurrendersFocusOnKeystroke()) {
				editorComponent.requestFocus();
				//}
			}
		}
		return retValue;
	}

	@Override
	public void editingCanceled(ChangeEvent arg0) {
		removeEditor();
		LOG.info("editingCanceled");
	}

	@Override
	public void editingStopped(ChangeEvent arg0) {
		ISpreadCellEditor editor = getCellEditor();
		if (editor != null) {
			Object value = editor.getCellEditorValue();
			getModel().setValueAt(value, editingRow, editingColumn);
			removeEditor();
		}
		LOG.info("editingStopped");
	}
}
