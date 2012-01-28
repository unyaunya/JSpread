package com.unyaunya.spread.sample;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.unyaunya.io.CSVReader;
import com.unyaunya.spread.CsvTable;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.JSpreadPane;

//import com.unyaunya.spread.JSpread;

class SampleTable extends DefaultTableModel {
    private static final Logger LOG = Logger.getLogger(SampleTable.class.getName());

    public SampleTable() {
		super(200, 100);
		for(int i = 0; i < getRowCount(); i++) {
			for(int j = 0; j < getColumnCount(); j++) {
				setValueAt("(" + Integer.toString(i) + "," + Integer.toString(j) +")", i, j);
			}
		}
	}

    /*
    public void setValueAt(Object aValue,
            int row,
            int column) {
		//LOG.info("setValueAt(" + aValue + "," + row + "," + column + ")");
		super.setValueAt(aValue, row, column);
	}
	*/
}

class MyMouseListener extends MouseAdapter {
	JSpread spread;
	
	public MyMouseListener(JSpread spread) {
		super();
		this.spread = spread;
		
	}
	public void mouseClicked(MouseEvent e) {
		/*
		Point pt = e.getPoint();
		System.out.println(
				"("+Integer.toString(spread.rowAtPoint(pt))+
				","+Integer.toString(spread.columnAtPoint(pt))+
				")");
		*/
	}
}

class MyFrame extends JFrame {
	private boolean isInited = false; 
	private JSpread spread; 

	public MyFrame() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.spread = new JSpread();
		getSpread().setModel(new SampleTable());
	}

	public void init() {
		if(!isInited) {
			this.setLayout(new BorderLayout());
			this.add(BorderLayout.NORTH, createMenuBar());
			add(BorderLayout.CENTER, createSpreadPane());
			setSize(800,600);
			setTitle("Spread");
			setVisible(true);
		}
		isInited = true;
	}

	private JSpread getSpread() {
		return spread;
	}
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		//File menu
		JMenu menu = new JMenu("ファイル");
		menu.add(new JMenuItem(new OpenAction()));
		menu.add(new JMenuItem("bbb"));
		menu.add(new JMenuItem("ccc"));
		menuBar.add(menu);
		//Window menu
		menu = new JMenu("ウィンドウ");
		menu.add(new JMenuItem(new FreezePanesAction()));
		menuBar.add(menu);
		return menuBar;
	}
	private JSpreadPane createSpreadPane() {
		getSpread().addMouseListener(new MyMouseListener(spread));
		return new JSpreadPane(spread);
	}

	class OpenAction extends AbstractAction {
		public OpenAction() {
			super("開く...");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "CSV & TXT", "csv", "txt");
			fc.setFileFilter(filter);			
			int returnVal = fc.showOpenDialog(MyFrame.this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	System.out.println("You chose to open this file: " +
		    			fc.getSelectedFile().getName());
		    	try {
		    		CSVReader csv = new CSVReader(false);
		    		csv.read(fc.getSelectedFile());
		    		getSpread().setModel(new CsvTable(csv));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
	}

	class FreezePanesAction extends AbstractAction {
		private boolean flag = true;
		public FreezePanesAction() {
			super("ウィンドウ枠の固定");
		}

		@Override
		public Object getValue(String key) {
			if(Action.NAME.equals(key)) {
				return getActionName();
			}
			else {
				return super.getValue(key);
			}
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if(spread.arePanesFreezed()) {
				spread.unfreezePanes();
			}
			else {
				spread.freezePanes();
			}
			boolean flag = spread.arePanesFreezed();
			firePropertyChange(Action.NAME, getActionName(!flag), getActionName(flag));
		}

		private String getActionName() {
			return this.getActionName(spread.arePanesFreezed());
		}

		private String getActionName(boolean arePanesFreezed) {
			if(arePanesFreezed) {
				return "ウィンドウ枠の固定の解除";
			}
			else {
				return "ウィンドウ枠の固定";
			}
		}
	}
}

public class Sample {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyFrame f = new MyFrame();
		f.init();
	}
}
