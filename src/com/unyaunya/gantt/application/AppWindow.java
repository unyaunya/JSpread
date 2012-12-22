package com.unyaunya.gantt.application;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.unyaunya.gantt.GanttChart;
import com.unyaunya.gantt.GanttDocument;
import com.unyaunya.gantt.GanttDocumentHandler;
import com.unyaunya.grid.IGridModel;
import com.unyaunya.grid.IRange;
import com.unyaunya.grid.Rows;
import com.unyaunya.grid.action.SpreadActionProvider;
import com.unyaunya.grid.selection.IGridSelectionModel;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.application.AbstractFileMenuHandler;
import com.unyaunya.swing.application.IDocument;
import com.unyaunya.swing.application.IFileMenuHandler;

@SuppressWarnings("serial")
public class AppWindow extends com.unyaunya.swing.application.AppFrame {
	private static Logger LOG = Logger.getLogger(AppWindow.class.getName());

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
		GanttChart gc = new GanttChart();
		gc.getGrid().setGridEventHandler(new GridEventAdapter());
		return gc;
	}

	private GanttChart getGanttChart() {
		return (GanttChart)getMainComponent();
	}
	
	private JSpread getSpread() {
		return getGanttChart().getSpread();
	}

	//implementation of FileMenuHandler

	//implementation of FileMenuHandler
	@Override
	protected IFileMenuHandler createFileMenuHandler() {
		return new MyFileMenuHandler();
	}

	class MyFileMenuHandler extends AbstractFileMenuHandler {
		private GanttDocumentHandler documentHandler = new GanttDocumentHandler();
		
		MyFileMenuHandler(){}

		@Override
		public JFileChooser createFileChooser() {
			JFileChooser fc = super.createFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(documentHandler.ssdFilter);			
			return fc;
		}

		@Override
		public IDocument createNewDocument() {
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
		protected IDocument openDocument(File file) {
			try {
				GanttDocument doc = (GanttDocument)documentHandler.load(file);
				if(doc != null)  {
					getGanttChart().getGanttChartModel().readDocument(doc);
					getGanttChart().repaint();
				}
	    		return doc;
			} catch (IOException e) {
				LOG.info(e.getMessage());
	    		return null;
			}
		}

		public boolean isXmlFile(File file) {
			return file.isFile() && file.canRead() && file.getPath().endsWith(".jar");
		}
		
		@Override
		protected void saveDocument(Object document, File file) {
			try {
				documentHandler.save((GanttDocument)document, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected JPopupMenu createPopupMenu() {
		JPopupMenu menu = super.createPopupMenu();
		menu.add(new JMenuItem(new AbstractAction("表示しない") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JSpread sp = getSpread();
				Rows rows = sp.getRows();
				IGridSelectionModel sm = sp.getGridSelectionModel();
				ArrayList<IRange> srl = sm.getSelectedRangeList();
				boolean bAutoPaint = sp.getAutoPaint();
				sp.setAutoPaint(false);
				for(int i = 0; i < srl.size(); i++) {
					IRange r = srl.get(i);
					for(int j = r.getTop(); j <= r.getBottom(); j++) {
						if(sm.isRowSelected(j)) {
							rows.setHidden(j, true);
						}
					}
				}
				sp.setAutoPaint(bAutoPaint);
			}
		}));
		menu.add(new JMenuItem(new AbstractAction("すべて表示する") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JSpread sp = getSpread();
				Rows rows = sp.getRows();
				boolean bAutoPaint = sp.getAutoPaint();
				sp.setAutoPaint(false);
				for(int i = 0; i < rows.getCount(); i++) {
					rows.setHidden(i, false);
				}
				sp.setAutoPaint(bAutoPaint);
			}
		}));
		menu.add(new JMenuItem(new AbstractAction("Collapse") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JSpread sp = getSpread();
				Rows rows = sp.getRows();
				IGridSelectionModel sm = sp.getGridSelectionModel();
				ArrayList<IRange> srl = sm.getSelectedRangeList();
				boolean bAutoPaint = sp.getAutoPaint();
				sp.setAutoPaint(false);
				for(int i = 0; i < srl.size(); i++) {
					IRange r = srl.get(i);
					for(int j = r.getTop(); j <= r.getBottom(); j++) {
						if(sm.isRowSelected(j)) {
							rows.collapse(j);
						}
					}
				}
				sp.setAutoPaint(bAutoPaint);
			}
		}));
		menu.add(new JMenuItem(new AbstractAction("Expand") {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JSpread sp = getSpread();
				Rows rows = sp.getRows();
				IGridSelectionModel sm = sp.getGridSelectionModel();
				ArrayList<IRange> srl = sm.getSelectedRangeList();
				boolean bAutoPaint = sp.getAutoPaint();
				sp.setAutoPaint(false);
				for(int i = 0; i < srl.size(); i++) {
					IRange r = srl.get(i);
					for(int j = r.getTop(); j <= r.getBottom(); j++) {
						if(sm.isRowSelected(j)) {
							rows.expand(j);
						}
					}
				}
				sp.setAutoPaint(bAutoPaint);
			}
		}));
		menu.add(new JMenuItem(getGanttChart().getLevelUpAction()));
		menu.add(new JMenuItem(getGanttChart().getLevelDownAction()));
		return menu;
	}


}
