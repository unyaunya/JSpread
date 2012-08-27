package com.unyaunya.gantt.application;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;


import com.unyaunya.gantt.GanttChart;
import com.unyaunya.gantt.GanttDocument;
import com.unyaunya.spread.SpreadActionProvider;
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
		SpreadActionProvider ap = new SpreadActionProvider(getGanttChart().getSpread());
		JMenu menu = createMenu("編集(E)", KeyEvent.VK_E);
		menu.add(new JMenuItem(ap.getDeleteAction()));
		menu.add(new JMenuItem(getGanttChart().getLevelUpAction()));
		menu.add(new JMenuItem(getGanttChart().getLevelDownAction()));
		return menu;
	}
	
	@Override
	protected JMenuBar createMenuBar() {
		SpreadActionProvider ap = new SpreadActionProvider(getGanttChart().getSpread());
		JMenuBar menuBar = super.createMenuBar();
		JMenu menu;
		//Edit menu
		menuBar.add(createEditMenu());
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
		SpreadActionProvider ap = new SpreadActionProvider(getGanttChart().getSpread());
		JMenu menu = createMenu("挿入(I)", KeyEvent.VK_I);
		menu.add(new JMenuItem(ap.getInsertRowAction()));
		menu.add(new JMenuItem(ap.getInsertColumnAction()));
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
			super.onFileOpen(fc);
			Object obj = getCurrentDocument();
			if(obj == null) {
				LOG.info("onFileOpen():doc is null");
			}
			else if(obj instanceof SpreadSheetModel) {
		    	SpreadSheetModel doc = (SpreadSheetModel)obj;
	    		getSpread().setSpreadSheetModel(doc);
			}
		}

		@Override
		protected Object openDocument(File file) {
	    	if(!ssdFilter.accept(file)) {
	    		return null;
	    	}
			try {
				GanttDocument doc = (GanttDocument)JAXBUtil.read(GanttDocument.class, file);
				getGanttChart().getGanttChartModel().readDocument(doc);
	    		return doc;
			} catch (IOException e) {
				LOG.info(e.getMessage());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return null;
		}

		@Override
		protected void saveDocument(Object document, File file) {
	    	if(!ssdFilter.accept(file)) {
	    		return;
	    	}
			try {
				JAXBUtil.save(document, file);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
