package com.unyaunya.swing.application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
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
@SuppressWarnings("serial")
public abstract class AppFrame extends JFrame {
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static Logger LOG = Logger.getLogger(AppFrame.class.getName());
	
	private boolean isInited = false; 
	private JComponent	mainComponent;
	transient IFileMenuHandler fileMenuHandler; 
	
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
		menu.add(createMenuItem(new AbstractAction("新規作成"){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				getFileMenuHandler().onFileNew();
			}
		}, KeyEvent.VK_N));
		menu.add(createMenuItem(getFileMenuHandler().getOpenAction(), KeyEvent.VK_O));
		menu.add(createMenuItem(new AbstractAction("保存"){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				getFileMenuHandler().onFileSave();
			}
		}, KeyEvent.VK_S));
		menu.add(createMenuItem(getFileMenuHandler().getSaveAsAction(), KeyEvent.VK_A));
		menu.addSeparator();
		menu.add(createMenuItem(new AbstractAction("印刷プレビュー"){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				getFileMenuHandler().onFilePrintPreview();
			}
		}, KeyEvent.VK_V));
		menu.add(createMenuItem(new AbstractAction("印刷"){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent event) {
				getFileMenuHandler().onFilePrint();
			}
		}, KeyEvent.VK_P));
		menu.addSeparator();
		menu.add(new JMenuItem(getFileMenuHandler().getExitAction()));
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

	//implementation of FileMenuHandler
	abstract protected IFileMenuHandler createFileMenuHandler();
	
	protected IFileMenuHandler getFileMenuHandler() {
		if(this.fileMenuHandler == null) {
			this.fileMenuHandler = createFileMenuHandler();
		}
		return this.fileMenuHandler;
	}

}
