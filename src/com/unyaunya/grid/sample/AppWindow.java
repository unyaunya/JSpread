package com.unyaunya.grid.sample;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.unyaunya.grid.GridDocument;
import com.unyaunya.grid.GridDocumentHandler;
import com.unyaunya.grid.IGridModel;
import com.unyaunya.grid.action.SpreadActionProvider;
import com.unyaunya.grid.table.GridTableModel;
import com.unyaunya.swing.JGridPane;
import com.unyaunya.swing.JSpread;
import com.unyaunya.swing.application.AbstractFileMenuHandler;
import com.unyaunya.swing.application.IDocument;
import com.unyaunya.swing.application.IFileMenuHandler;

@SuppressWarnings("serial")
public class AppWindow extends com.unyaunya.swing.application.AppFrame {
	private static Logger LOG = Logger.getLogger(AppWindow.class.getName());

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
		GridDocumentHandler documentHandler = new GridDocumentHandler();

		MyDocumentFileHandler() {}

		@Override
		public IDocument createNewDocument() {
			GridDocument tmp = new GridDocument(new GridTableModel());
			tmp.setTableModel(new CsvTable(createSampleData()));
    	    return tmp;
		}

		@Override
		public JFileChooser createFileChooser() {
			JFileChooser fc = super.createFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(documentHandler.ssdFilter);			
			fc.addChoosableFileFilter(documentHandler.csvFilter);			
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
		@Override
		protected IDocument openDocument(File file) {
    		try {
    			return documentHandler.load(file);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 指定したドキュメントをファイルに保存する
		 * @param file
		 * @return
		 */
		@Override
		protected void saveDocument(Object document, File file) {
			try {
				documentHandler.save((IDocument)document, file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
