/**
 * 
 */
package com.unyaunya.grid;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Format;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.unyaunya.swing.JGrid;

@SuppressWarnings("serial")
class TreeCellRenderer extends JPanel {
	public static final int ICON_NONE = 0;
	public static final int ICON_EXPAND = 1;
	public static final int ICON_COLLAPSE = 2;
			
	private static Logger LOG = Logger.getLogger(TreeCellRenderer.class.getName());
	private JLabel indentLabel;
	private JLabel iconLabel;
	private Component mainComponent;
	private ImageIcon expandIcon ;
	private ImageIcon collapseIcon;

	TreeCellRenderer() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		expandIcon = new ImageIcon("./icons/plus.png");
		collapseIcon = new ImageIcon("./icons/minus.png");
		this.indentLabel = new JLabel();
		//this.indentLabel.setSize(32, 23);
		this.iconLabel = new JLabel();
		this.iconLabel.setOpaque(false);
		this.add(indentLabel);
		this.add(iconLabel);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		LOG.info(this.mainComponent.toString());
		LOG.info(this.mainComponent.getBounds().toString());
		LOG.info(this.getBounds().toString());
	}

	public void setMainComponent(Component c) {
		if(this.mainComponent == c) {
			return;
		}
		if(mainComponent != null) {
			this.remove(mainComponent);
		}
		if(c != null) {
			super.add(c);
		}
		this.mainComponent = c;
	}

	/**
	 * CellRenderePane#paintComponent内で、このメソッドが呼ばれるので、
	 * その際に、子コンポーネントのレイアウトを行う。
	 */
	@Override
	public void setBounds(int x, int y, int w, int h) {
	   super.setBounds(x, y, w, h);
	   doLayout();
	}

	public void setIndent(int level) {
		String indent = "";
		for(int i = 0; i < level; i++) {
			indent += "　";
		}
		indentLabel.setText(indent);
	}

	public void setIcon(int iconType) {
		Icon icon;
		switch(iconType) {
		case ICON_EXPAND:
			icon = expandIcon;
			break;
		case ICON_COLLAPSE:
			icon = collapseIcon;
			break;
		default:
			icon = null;
			break;
		}
		this.iconLabel.setIcon(icon);
	}
}

/**
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class DefaultCellRenderer extends JLabel implements IGridCellRenderer {
	private Format format;
	private TreeCellRenderer panel;

	public DefaultCellRenderer() {
    	super();
    	setOpaque(true);
		panel = new TreeCellRenderer();
    }


	protected String getValueString(JGrid grid, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return value.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.unyaunya.spread.ICellRenderer#getCellRendererComponent()
	 */
	@Override
	public Component getGridCellRendererComponent(JGrid grid, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = _getGridCellRendererComponent(grid, value, isSelected, hasFocus, row, column);
		int tcc = grid.getTreeCellColumn();
		if(column == tcc && row >= grid.getGridModel().getHeaderRowCount()) {
			panel.setMainComponent(c);
			panel.setIndent(grid.getRows().getLevel(row));
			JComponent jc = (JComponent)c;
			panel.setBorder(this.getBorder());
			panel.setBackground(this.getBackground());
			if(grid.getRows().isLeaf(row)) {
				panel.setIcon(TreeCellRenderer.ICON_NONE);
			}
			else {
				if(grid.getRows().isExpanded(row)) {
					panel.setIcon(TreeCellRenderer.ICON_COLLAPSE);
				}
				else {
					panel.setIcon(TreeCellRenderer.ICON_EXPAND);
				}
			}
			return panel;
		}
		else {
			panel.setMainComponent(null);
			c.setLocation(0, 0);
			return c;
		}
	}

	private Component _getGridCellRendererComponent(JGrid grid, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(value == null) {
			value = "";
		}
		setText(getValueString(grid, value, isSelected, hasFocus, row, column));
		if(isSelected) {
			super.setForeground(grid.getSelectionForeground());
		}
		else {
			
		}
		return this;
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}
	
	@Override
	public Format getFormat() {
		return format;
	}
	
	class ExpandHandler extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("FFFFFFFFFFFFFFFFFF");
		}
	}
}
