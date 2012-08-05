package com.unyaunya.swing.application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * 
 * @author wata
 * swingアプリケーションのメインウィンドウの基底クラス
 */
public class AppFrame extends JFrame implements FileMenuHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOG = Logger.getLogger(AppFrame.class.getName());

	private boolean isInited = false; 
	private JComponent	mainComponent;
	private JFileChooser fileChooser;
	
	public AppFrame(String title) {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(title);
	}

	public void init() {
		if(!isInited) {
			this.setLayout(new BorderLayout());
			this.add(BorderLayout.NORTH, createMenuBar());
			add(BorderLayout.CENTER, getMainComponent());
			setSize(800,600);
			setVisible(true);
		}
		isInited = true;
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createFileMenu());
		return menuBar;
	}

	protected JMenu createFileMenu() {
		JMenu menu = createMenu("ファイル(F)", KeyEvent.VK_F);
		menu.add(createMenuItem(new OpenAction(), KeyEvent.VK_F));
		menu.add(new JMenuItem(new SaveAsAction()));
		menu.addSeparator();
		menu.add(new JMenuItem(new ExitAction()));
		return menu;
	}
	
	protected JComponent createMainComponent() {
		return new JPanel();
	}

	protected JComponent getMainComponent() {
		if(mainComponent == null) {
			mainComponent = createMainComponent();
		}
		return mainComponent;
	}
	
	public static JMenu createMenu(String name, int mnemonic) {
		JMenu menu = new JMenu(name);
		menu.setMnemonic(mnemonic);
		return menu;
	}
	public static JMenuItem createMenuItem(Action action, int mnemonic) {
		JMenuItem mi = new JMenuItem(action);
		mi.setMnemonic(mnemonic);
		return mi;
	}

	protected final JFileChooser getFileChooser() {
		if(this.fileChooser == null) {
			this.fileChooser = createFileChooser();
		}
		return this.fileChooser;
	}

	protected JFileChooser createFileChooser() {
		return new JFileChooser();
	}

	//implementation of FileMenuHandler
	public void OnFileOpen(JFileChooser fc){
    	LOG.info("You chose to open data in this file: " +
    			fc.getSelectedFile().toString() + "/" +
    			fc.getTypeDescription(fc.getSelectedFile()));
	}
	public void OnFileSave(JFileChooser fc){
    	LOG.info("You chose to save data in this file: " +
    			fc.getSelectedFile().toString() + "/" +
    			fc.getTypeDescription(fc.getSelectedFile()));
	}

	public void OnExit() {
    	LOG.info("OnExit() called.");
    	System.exit(0);
	}
	
	final class OpenAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public OpenAction() {
			super("開く...");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fc = getFileChooser();
			int returnVal = fc.showOpenDialog(AppFrame.this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	OnFileOpen(fc);
		    }
		}
	}

	final class SaveAsAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SaveAsAction() {
			super("名前をつけて保存...");
		}
		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fc = getFileChooser();
			fc.setDialogTitle("名前をつけて保存");
			int returnVal = fc.showSaveDialog(AppFrame.this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	OnFileSave(fc);
		    }
		}
	}

	final class ExitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public ExitAction() {
			super("終了");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			OnExit();
		}
	}
}
