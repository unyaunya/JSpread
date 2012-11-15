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
import com.unyaunya.grid.IGridModel;
import com.unyaunya.grid.action.SpreadActionProvider;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.application.AbstractFileMenuHandler;
import com.unyaunya.swing.application.IFileMenuHandler;

public class AppWindow extends com.unyaunya.swing.application.AppFrame {
	private static Logger LOG = Logger.getLogger(AppWindow.class.getName());
	private static final long serialVersionUID = 1L;
	private GanttChart gantt; 

	public static AppWindow instance;
	
	public static AppWindow GetInstance() {
		if(instance == null) {
			instance = new AppWindow();
		}
		return instance;
	}
	
	private AppWindow() {
		super("�H���\");
		LOG.info("AppWindow()");
	}

	private JMenu createEditMenu() {
		SpreadActionProvider ap = new SpreadActionProvider(getGanttChart().getSpread());
		JMenu menu = createMenu("�ҏW(E)", KeyEvent.VK_E);
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
		menu = createMenu("����(O)", KeyEvent.VK_O);
		menu.add(new JMenuItem(ap.getForegroundColorAction()));
		menu.add(new JMenuItem(ap.getBackgroundColorAction()));
		menu.add(new JMenuItem(ap.getCellCouplingAction()));
		menuBar.add(menu);
		//Window menu
		menu = createMenu("�E�B���h�E(W)", KeyEvent.VK_W);
		menu.add(new JMenuItem(ap.getFreezePanesAction()));
		menuBar.add(menu);
		return menuBar;
	}

	private JMenu createInsertMenu() {
		SpreadActionProvider ap = new SpreadActionProvider(getGanttChart().getSpread());
		JMenu menu = createMenu("�}��(I)", KeyEvent.VK_I);
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
		        "�K���g�`���[�g(.xml)", "xml");
		
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
			else if(obj instanceof IGridModel) {
				IGridModel doc = (IGridModel)obj;
	    		getSpread().setGridModel(doc);
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
				getGanttChart().repaint();
	    		return doc;
			} catch (IOException e) {
				LOG.info(e.getMessage());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return null;
		}

		public boolean isXmlFile(File file) {
			return file.isFile() && file.canRead() && file.getPath().endsWith(".jar");
		}
		
		@Override
		protected void saveDocument(Object document, File file) {
			if(!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath()+".xml");
			}
	    	if(!ssdFilter.accept(file)) {
	    		throw new RuntimeException("!ssdFilter.accept(file)");
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
