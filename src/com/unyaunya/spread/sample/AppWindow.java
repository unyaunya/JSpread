package com.unyaunya.spread.sample;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import com.unyaunya.gantt.GanttChart;
import com.unyaunya.spread.CsvTable;
import com.unyaunya.spread.SpreadSheetModel;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.JSpreadPane;

public class AppWindow extends com.unyaunya.swing.application.AppFrame {
	private static Logger LOG = Logger.getLogger(AppWindow.class.getName());
	private static final long serialVersionUID = 1L;
	private JSpreadPane spreadPane; 
	private FileNameExtensionFilter csvFilter = new FileNameExtensionFilter(
	        "CSV & TXT", "csv", "txt");
	private FileNameExtensionFilter ssdFilter = new FileNameExtensionFilter(
	        "スプレッドシート", "ssd");

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

	@Override
	protected JComponent createMainComponent() {
		return new GanttChart();
	}

	@Override
	protected JFileChooser createFileChooser() {
		JFileChooser fc = super.createFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(ssdFilter);			
		fc.addChoosableFileFilter(csvFilter);			
		return fc;
	}
	
	private JSpreadPane getSpreadPane() {
		return (JSpreadPane)getMainComponent();
	}
	
	private JSpread getSpread() {
		return spreadPane.getSpread();
	}

	//implementation of FileMenuHandler
	public void OnFileOpen(File selectedFile){
    	if(ssdFilter.accept(selectedFile)) {
	    	try {
		    	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile));
		    	SpreadSheetModel tmp = (SpreadSheetModel)ois.readObject();
		    	ois.close();
	    		getSpread().setSpreadSheetModel(tmp);
	    		getSpreadPane().setSpread(getSpread());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else if(csvFilter.accept(selectedFile)) {
	    	try {
	    		CSVReader reader = new CSVReader(new FileReader(selectedFile));
	    	    List<String[]> myEntries = reader.readAll();
	    		getSpread().setModel(new CsvTable(myEntries));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}

	public void OnFileSave(File selectedFile){
    	if(ssdFilter.accept(selectedFile)) {
			try {
		        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile));
		    	oos.writeObject(getSpread().getSpreadSheetModel());
		    	oos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	}
    	else if(csvFilter.accept(selectedFile)) {
	    	CSVWriter writer;
			try {
				writer = new CSVWriter(new FileWriter(selectedFile));
		        writer.writeAll(((CsvTable)getSpread().getModel().getTableModel()).getData());
		        writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    	}
	}
}
