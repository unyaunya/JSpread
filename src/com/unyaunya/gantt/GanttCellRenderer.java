package com.unyaunya.gantt;

import java.awt.Component;
import java.awt.Graphics;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.unyaunya.grid.DefaultCellRenderer;
import com.unyaunya.swing.JGrid;

@SuppressWarnings("serial")
class TreeCellRenderer extends JPanel {
	private static Logger LOG = Logger.getLogger(TreeCellRenderer.class.getName());
	private JLabel indentLabel;
	private JLabel iconLabel;
	private Component mainComponent;
	private ImageIcon expandIcon ;
	@SuppressWarnings("unused")
	private ImageIcon collapseIcon;

	TreeCellRenderer() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		expandIcon = new ImageIcon("./icons/plus.gif");
		collapseIcon = new ImageIcon("./icons/minus.gif");
		this.indentLabel = new JLabel();
		//this.indentLabel.setSize(32, 23);
		this.iconLabel = new JLabel(expandIcon);
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
}

@SuppressWarnings("serial")
public class GanttCellRenderer extends DefaultCellRenderer {
	private TreeCellRenderer panel;
	
	public GanttCellRenderer() {
		super();
		panel = new TreeCellRenderer();
	}

	@Override
	public Component getGridCellRendererComponent(JGrid grid, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getGridCellRendererComponent(grid, value, isSelected, hasFocus, row, column);
		GanttTableModel tm = (GanttTableModel)grid.getGridModel().getTableModel();
		if(column == 2 && row >= tm.getHeaderRowCount()) {
			panel.setMainComponent(c);
			Task task = tm.getTask(row);
			if(task != null) {
				panel.setIndent(task.getLevel());
			}
			else {
				panel.setIndent(0);
			}
			JComponent jc = (JComponent)c;
			panel.setBorder(jc.getBorder());
			panel.setBackground(jc.getBackground());
			return panel;
		}
		else {
			panel.setMainComponent(null);
			c.setLocation(0, 0);
			return c;
		}
	}
}
