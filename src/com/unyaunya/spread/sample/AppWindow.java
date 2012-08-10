package com.unyaunya.spread.sample;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.unyaunya.spread.SpreadSheetModel;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.JSpreadPane;
import com.unyaunya.swing.application.AbstractFileMenuHandler;
import com.unyaunya.swing.application.IFileMenuHandler;

public class AppWindow extends com.unyaunya.swing.application.AppFrame {
	private static Logger LOG = Logger.getLogger(AppWindow.class.getName());
	private static final long serialVersionUID = 1L;
	private CsvTable csvTable;

	public AppWindow() {
		super("Sample");
		LOG.info("AppWindow()");
	}

	@Override
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = super.createMenuBar();
		JMenu menu;
		//Edit menu
		menu = createMenu("編集(E)", KeyEvent.VK_E);
		menu.add(new JMenuItem(getSpreadPane().getDeleteAction()));
		menuBar.add(menu);
		//Insert menu
		menu = createInsertMenu();
		menuBar.add(menu);
		//Format menu
		menu = createMenu("書式(O)", KeyEvent.VK_O);
		menu.add(new JMenuItem(getSpreadPane().getForegroundColorAction()));
		menu.add(new JMenuItem(getSpreadPane().getBackgroundColorAction()));
		menu.add(new JMenuItem(getSpreadPane().getCellCouplingAction()));
		menuBar.add(menu);
		//Window menu
		menu = createMenu("ウィンドウ(W)", KeyEvent.VK_W);
		menu.add(new JMenuItem(getSpreadPane().getFreezePanesAction()));
		menuBar.add(menu);
		return menuBar;
	}

	private JMenu createInsertMenu() {
		JMenu menu = createMenu("挿入(I)", KeyEvent.VK_I);
		menu.add(new JMenuItem(getSpreadPane().getInsertRowAction()));
		menu.add(new JMenuItem(getSpreadPane().getInsertColumnAction()));
		return menu;
	}

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

	@Override
	protected JComponent createMainComponent() {
		csvTable = (CsvTable)getFileMenuHandler().createNewDocument();
		JSpread	spread = new JSpread();
		spread.setModel(csvTable);
		spread.getConfig().setRowInsertionSuppoorted(true);
		return new JSpreadPane(spread);
	}

	private JSpreadPane getSpreadPane() {
		return (JSpreadPane)getMainComponent();
	}
	
	private JSpread getSpread() {
		return getSpreadPane().getSpread();
	}

	//implementation of FileMenuHandler
	@Override
	protected IFileMenuHandler createFileMenuHandler() {
		return new MyDocumentFileHandler();
	}

	class MyDocumentFileHandler extends AbstractFileMenuHandler {
		private FileNameExtensionFilter csvFilter = new FileNameExtensionFilter(
		        "CSV & TXT", "csv", "txt");
		private FileNameExtensionFilter ssdFilter = new FileNameExtensionFilter(
		        "スプレッドシート", "ssd");

		MyDocumentFileHandler() {}

		@Override
		public Object createNewDocument() {
    	    return new CsvTable(createSampleData());
		}

		@Override
		public JFileChooser createFileChooser() {
			JFileChooser fc = super.createFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(ssdFilter);			
			fc.addChoosableFileFilter(csvFilter);			
			return fc;
		}

		@Override
		public void onFileOpen(JFileChooser fc){
	    	if(ssdFilter.accept(fc.getSelectedFile())) {
		    	try {
			    	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
			    	SpreadSheetModel tmp = (SpreadSheetModel)ois.readObject();
			    	ois.close();
		    		getSpread().setSpreadSheetModel(tmp);
		    		getSpreadPane().setSpread(getSpread());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
	    	}
	    	else if(csvFilter.accept(fc.getSelectedFile())) {
		    	try {
		    		CSVReader reader = new CSVReader(new FileReader(fc.getSelectedFile()));
		    	    List<String[]> myEntries = reader.readAll();
		    	    csvTable = new CsvTable(myEntries);
		    		getSpread().setModel(csvTable);
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
		}

		public void onFileSave(JFileChooser fc){
	    	if(ssdFilter.accept(fc.getSelectedFile())) {
				try {
			        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fc.getSelectedFile()));
			    	oos.writeObject(getSpread().getSpreadSheetModel());
			    	oos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    	}
	    	else if(csvFilter.accept(fc.getSelectedFile())) {
		    	CSVWriter writer;
				try {
					writer = new CSVWriter(new FileWriter(fc.getSelectedFile()));
			        writer.writeAll(csvTable.getData());
			        writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    	}
		}

		/**
		 * 指定したファイルを読み込んでドキュメントを作成する
		 * @param file
		 * @return
		 */
		protected Object openDocument(File file) {
			//TODO
			return null;
		}

		/**
		 * 指定したドキュメントをファイルに保存する
		 * @param file
		 * @return
		 */
		protected void saveDocument(Object document, File file) {
			//TODO
		}

	}
}
