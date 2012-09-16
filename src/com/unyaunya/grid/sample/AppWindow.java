package com.unyaunya.grid.sample;

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

import com.unyaunya.grid.GridModel;
import com.unyaunya.grid.IGridModel;
import com.unyaunya.grid.action.SpreadActionProvider;
import com.unyaunya.grid.table.GridTableModel;
import com.unyaunya.swing.JGridPane;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.application.AbstractFileMenuHandler;
import com.unyaunya.swing.application.IFileMenuHandler;

public class AppWindow extends com.unyaunya.swing.application.AppFrame {
	private static Logger LOG = Logger.getLogger(AppWindow.class.getName());
	private static final long serialVersionUID = 1L;

	public AppWindow() {
		super("Sample");
		LOG.info("AppWindow()");
	}

	@Override
	protected JMenuBar createMenuBar() {
		SpreadActionProvider ap = new SpreadActionProvider(getSpread());
		JMenuBar menuBar = super.createMenuBar();
		JMenu menu;
		//Edit menu
		menu = createMenu("編集(E)", KeyEvent.VK_E);
		menu.add(new JMenuItem(ap.getDeleteAction()));
		menuBar.add(menu);
		//Insert menu
		menu = createInsertMenu();
		menuBar.add(menu);
		//Format menu
		menu = createMenu("書式(O)", KeyEvent.VK_O);
		menu.add(new JMenuItem(ap.getForegroundColorAction()));
		menu.add(new JMenuItem(ap.getBackgroundColorAction()));
		menu.add(new JMenuItem(ap.getCellCouplingAction()));
		menuBar.add(menu);
		//Window menu
		menu = createMenu("ウィンドウ(W)", KeyEvent.VK_W);
		menu.add(new JMenuItem(ap.getFreezePanesAction()));
		menuBar.add(menu);
		return menuBar;
	}

	private JMenu createInsertMenu() {
		SpreadActionProvider ap = new SpreadActionProvider(getSpread());
		JMenu menu = createMenu("挿入(I)", KeyEvent.VK_I);
		menu.add(new JMenuItem(ap.getInsertRowAction()));
		menu.add(new JMenuItem(ap.getInsertColumnAction()));
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
		JSpread	spread = new JSpread();
		spread.setGridModel((IGridModel)getFileMenuHandler().createNewDocument());
		spread.getConfig().setRowInsertionSuppoorted(true);
		return new JGridPane(spread);
	}

	private JGridPane getGridPane() {
		return (JGridPane)getMainComponent();
	}
	
	private JSpread getSpread() {
		return (JSpread)getGridPane().getGrid();
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
			GridModel tmp = new GridModel(new GridTableModel());
			tmp.setTableModel(new CsvTable(createSampleData()));
    	    return tmp;
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
			super.onFileOpen(fc);
			Object obj = getCurrentDocument();
			if(obj == null) {
				LOG.info("onFileOpen():doc is null");
			}
			IGridModel doc = (IGridModel)obj;
    		getSpread().setGridModel(doc);
		}

		/**
		 * 指定したファイルを読み込んでドキュメントを作成する
		 * @param file
		 * @return
		 */
		protected Object openDocument(File file) {
	    	if(ssdFilter.accept(file)) {
		    	try {
			    	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			    	IGridModel tmp = (IGridModel)ois.readObject();
			    	ois.close();
			    	return tmp;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
	    	}
	    	else if(csvFilter.accept(file)) {
		    	try {
		    		CSVReader reader = new CSVReader(new FileReader(file));
		    	    List<String[]> myEntries = reader.readAll();
		    	    GridModel tmp = new GridModel(new CsvTable(myEntries));
		    	    return tmp;
		    	} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
			return null;
		}

		/**
		 * 指定したドキュメントをファイルに保存する
		 * @param file
		 * @return
		 */
		protected void saveDocument(Object document, File file) {
			GridModel doc = (GridModel)document;
	    	if(ssdFilter.accept(file)) {
				try {
			        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			    	oos.writeObject(doc);
			    	oos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    	}
	    	else if(csvFilter.accept(file)) {
		    	CSVWriter writer;
				List<String[]> data = new ArrayList<String[]>();
				for(int i = 1; i < doc.getRowCount(); i++) {
					String[] row = new String[doc.getColumnCount()-1];
					for(int j = 1; j < doc.getColumnCount(); j++) {
						Object value = doc.getValueAt(i, j);
						if(value != null) {
							row[j-1] = value.toString();
						}
						else {
							row[j-1] = null;
						}
					}
					data.add(row);
				}
				try {
					writer = new CSVWriter(new FileWriter(file));
			        writer.writeAll(data);
			        writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    	}
		}
	}
}
