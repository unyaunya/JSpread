package com.unyaunya.spread.sample;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
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
	private static Logger LOG = Logger.getLogger(MyFrame.class.getName());
	private static final long serialVersionUID = 1L;
	private boolean isInited = false; 
	private JSpread spread; 
	private JFileChooser fileChooser;

	static List<String[]> createSampleData() {
		List<String[]> data = new ArrayList<String[]>();
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
		getSpread().getConfig().setRowInsertionSuppoorted(true);
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

	private static JMenu createMenu(String name, int mnemonic) {
		JMenu menu = new JMenu(name);
		menu.setMnemonic(mnemonic);
		return menu;
	}
	private static JMenuItem createMenuItem(Action action, int mnemonic) {
		JMenuItem mi = new JMenuItem(action);
		mi.setMnemonic(mnemonic);
		return mi;
	}
	
	private JSpread getSpread() {
		return spread;
	}
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		//File menu
		menu = createMenu("ファイル(F)", KeyEvent.VK_F);
		menu.add(createMenuItem(new OpenAction(), KeyEvent.VK_F));
		menu.add(new JMenuItem(new SaveAsAction()));
		menu.add(new JMenuItem("ccc"));
		menuBar.add(menu);
		//Edit menu
		menu = createMenu("編集(E)", KeyEvent.VK_E);
		menu.add(new JMenuItem(new DeleteAction()));
		menuBar.add(menu);
		//Insert menu
		menu = createMenu("挿入(I)", KeyEvent.VK_I);
		menu.add(new JMenuItem(new InsertRowAction()));
		menu.add(new JMenuItem(new InsertColumnAction()));
		menuBar.add(menu);
		//Format menu
		menu = createMenu("書式(O)", KeyEvent.VK_O);
		menu.add(new JMenuItem(new CellCouplingAction()));
		menuBar.add(menu);
		//Window menu
		menu = createMenu("ウィンドウ(W)", KeyEvent.VK_W);
		menu.add(new JMenuItem(new FreezePanesAction()));
		menuBar.add(menu);
		return menuBar;
	}

	/*
	private JMenu createInsertMenu() {
		JMenu menu = new JMenu("挿入");
		menu.add(new JMenuItem(new InsertRowAction()));
		menu.add(new JMenuItem(new InsertColumnAction()));
		return menu;
	}
	*/

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
	
	private DefaultTableModel getTableModel() {
		return (DefaultTableModel)getSpread().getModel().getTableModel();
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
			        writer.writeAll(((CsvTable)spread.getModel().getTableModel()).getData());
			        writer.close();
				} catch (IOException e1) {
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

	class InsertRowAction extends AbstractAction {
		public InsertRowAction() {
			super("行");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			getSpread().insertRow();
		}
	}

	class InsertColumnAction extends AbstractAction {
		public InsertColumnAction() {
			super("列");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			throw new UnsupportedOperationException();
		}
	}

	class DeleteAction extends AbstractAction {
		public DeleteAction() {
			super("削除");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			getSpread().removeRow();
		}
	}

	class CellCouplingAction extends AbstractAction {
		public CellCouplingAction() {
			super("セルの結合");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			getSpread().coupleCells();
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
