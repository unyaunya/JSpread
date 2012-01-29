package com.unyaunya.spread.sample;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.unyaunya.spread.CsvTable;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.JSpreadPane;

class MyMouseListener extends MouseAdapter {
	JSpread spread;
	
	public MyMouseListener(JSpread spread) {
		super();
		this.spread = spread;
		
	}
	/*
	public void mouseClicked(MouseEvent e) {
		Point pt = e.getPoint();
		System.out.println(
				"("+Integer.toString(spread.rowAtPoint(pt))+
				","+Integer.toString(spread.columnAtPoint(pt))+
				")");
	}
	*/
}

class MyFrame extends JFrame {
	private boolean isInited = false; 
	private JSpread spread; 
	private JFileChooser fileChooser;

	static List<String[]> createSampleData() {
		List<String[]> data = new ArrayList<String[]>();
		//super(200, 100);
		for(int i = 0; i < 200; i++) {
			String[] row = new String[100];
			for(int j = 0; j < 100; j++) {
				row[j] = "(" + Integer.toString(i) + "," + Integer.toString(j) +")";
			}
			data.add(row);
		}
		return data;
	}

	public MyFrame() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.spread = new JSpread();
		getSpread().setModel(new CsvTable(createSampleData()));
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
		menu.add(new JMenuItem(new SaveAsAction()));
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

	private JFileChooser getFileChooser() {
		if(this.fileChooser == null) {
			this.fileChooser = createFileChooser();
		}
		return this.fileChooser;

	}
	private JFileChooser createFileChooser() {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "CSV & TXT", "csv", "txt");
		fc.setFileFilter(filter);			
		return fc;
	}
	
	class OpenAction extends AbstractAction {
		public OpenAction() {
			super("開く...");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fc = getFileChooser();
			int returnVal = fc.showOpenDialog(MyFrame.this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	try {
		    		CSVReader reader = new CSVReader(new FileReader(fc.getSelectedFile()));
		    	    List<String[]> myEntries = reader.readAll();
		    		getSpread().setModel(new CsvTable(myEntries));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		}
	}

	class SaveAsAction extends AbstractAction {
		public SaveAsAction() {
			super("名前をつけて保存...");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fc = getFileChooser();
			fc.setDialogTitle("名前をつけて保存");
			int returnVal = fc.showOpenDialog(MyFrame.this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	System.out.println("You chose to save data in this file: " +
		    			fc.getSelectedFile());
		    	CSVWriter writer;
				try {
					writer = new CSVWriter(new FileWriter(fc.getSelectedFile()));
			        // feed in your array (or convert your data to an array)
			        String[] entries = "first#second#third".split("#");
			        writer.writeNext(entries);
			        writer.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		}
	}

	class FreezePanesAction extends AbstractAction {
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
