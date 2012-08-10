package com.unyaunya.gantt.application;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;


import com.unyaunya.gantt.GanttChart;
import com.unyaunya.gantt.GanttDocument;
import com.unyaunya.spread.SpreadSheetModel;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.application.AbstractFileMenuHandler;
import com.unyaunya.swing.application.IFileMenuHandler;

public class AppWindow extends com.unyaunya.swing.application.AppFrame {
	private static Logger LOG = Logger.getLogger(AppWindow.class.getName());
	private static final long serialVersionUID = 1L;
	private GanttChart gantt; 

	public AppWindow() {
		super("工程表");
		LOG.info("AppWindow()");
	}

	private JMenu createEditMenu() {
		JMenu menu = createMenu("編集(E)", KeyEvent.VK_E);
		menu.add(new JMenuItem(getGanttChart().getDeleteAction()));
		menu.add(new JMenuItem(getGanttChart().getLevelUpAction()));
		menu.add(new JMenuItem(getGanttChart().getLevelDownAction()));
		return menu;
	}
	
	@Override
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = super.createMenuBar();
		JMenu menu;
		//Edit menu
		menuBar.add(createEditMenu());
		//Insert menu
		menu = createInsertMenu();
		menuBar.add(menu);
		//Format menu
		menu = createMenu("書式(O)", KeyEvent.VK_O);
		menu.add(new JMenuItem(getGanttChart().getForegroundColorAction()));
		menu.add(new JMenuItem(getGanttChart().getBackgroundColorAction()));
		menu.add(new JMenuItem(getGanttChart().getCellCouplingAction()));
		menuBar.add(menu);
		//Window menu
		menu = createMenu("ウィンドウ(W)", KeyEvent.VK_W);
		menu.add(new JMenuItem(getGanttChart().getFreezePanesAction()));
		menuBar.add(menu);
		return menuBar;
	}

	private JMenu createInsertMenu() {
		JMenu menu = createMenu("挿入(I)", KeyEvent.VK_I);
		menu.add(new JMenuItem(getGanttChart().getInsertRowAction()));
		menu.add(new JMenuItem(getGanttChart().getInsertColumnAction()));
		return menu;
	}

	@Override
	protected JComponent createMainComponent() {
		return new GanttChart();
	}

	private GanttChart getGanttChart() {
		return (GanttChart)getMainComponent();
	}
	
	private JSpread getSpread() {
		return gantt.getSpread();
	}

	//implementation of FileMenuHandler

	//implementation of FileMenuHandler
	@Override
	protected IFileMenuHandler createFileMenuHandler() {
		return new MyFileMenuHandler();
	}

	class MyFileMenuHandler extends AbstractFileMenuHandler {
		private FileNameExtensionFilter ssdFilter = new FileNameExtensionFilter(
		        "ガントチャート", "xml");
		
		MyFileMenuHandler(){}

		@Override
		public JFileChooser createFileChooser() {
			JFileChooser fc = super.createFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(ssdFilter);			
			return fc;
		}

		@Override
		public Object createNewDocument() {
			return new GanttDocument();
		}
		
		
		@Override
		public void onFileOpen(JFileChooser fc) {
	    	if(!ssdFilter.accept(fc.getSelectedFile())) {
	    		return;
	    	}
			try {
		    	ObjectInputStream ois;
				ois = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
		    	SpreadSheetModel tmp = (SpreadSheetModel)ois.readObject();
		    	ois.close();
	    		getSpread().setSpreadSheetModel(tmp);
	    		getGanttChart().setSpread(getSpread());
			} catch (ClassNotFoundException e) {
				LOG.info(e.getMessage());
			} catch (IOException e) {
				LOG.info(e.getMessage());
			}
		}

		public void onFileSaveAs(JFileChooser fc){
	    	if(!ssdFilter.accept(fc.getSelectedFile())) {
	    		return;
	    	}
			try {
		        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fc.getSelectedFile()));
		    	oos.writeObject(getSpread().getSpreadSheetModel());
		    	oos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
