package com.unyaunya.swing.application;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public abstract class AbstractFileMenuHandler implements IFileMenuHandler {
	private static Logger LOG = Logger.getLogger(AbstractFileMenuHandler.class.getName());

	private IDocument currentDocument = null;
	private File	currentFile = null;
	transient JFileChooser fileChooser;
	transient private Action openAction = null;
	transient private Action saveAsAction = null;
	transient private Action exitAction = null;

	public AbstractFileMenuHandler() {
		onFileNew();
	}
	
	@Override
	public File getCurrentFile() {
		return currentFile;
	}

	protected void setCurrentFile(File file) {
		this.currentFile = file;
	}

	@Override
	public IDocument getCurrentDocument() {
		return currentDocument;
	}

	protected void setCurrentDocument(IDocument document) {
		this.currentDocument = document;
		this.currentDocument.setModified(false);
	}
	
	/**
	 * �w�肵���t�@�C����ǂݍ���Ńh�L�������g���쐬����
	 * @param file
	 * @return
	 */
	abstract protected IDocument openDocument(File file);

	/**
	 * �w�肵���h�L�������g���t�@�C���ɕۑ�����
	 * @param file
	 * @return
	 */
	abstract protected void saveDocument(Object document, File file);

	@Override
	public void onFileNew(){
		LOG.info("onFileNew");
		setCurrentDocument(createNewDocument());
		setCurrentFile(null);
	}
	
	@Override
	public void onFileOpen(JFileChooser fc){
    	LOG.info("You chose to open data in this file: " +
    			fc.getSelectedFile().toString() + "/" +
    			fc.getTypeDescription(fc.getSelectedFile()));
    	setCurrentDocument(openDocument(fc.getSelectedFile()));
		setCurrentFile(fc.getSelectedFile());
	}

	
	@Override
	public void onFileSaveAs(JFileChooser fc){
    	LOG.info("You chose to save data in this file: " +
    			fc.getSelectedFile().toString() + "/" +
    			fc.getTypeDescription(fc.getSelectedFile()));
    	saveDocument(getCurrentDocument(), fc.getSelectedFile());
		setCurrentFile(fc.getSelectedFile());
	}

	@Override
	public void onFileSave() {
		File f = getCurrentFile();
		if(f == null) {
			Action action = getSaveAsAction();
			action.actionPerformed(null);
		}
		else {
		   	saveDocument(getCurrentDocument(), f);
			setCurrentFile(f);
		}
 	}

	@Override
	public void onFilePrint() {
		LOG.info("onFilePrint()");
	}

	@Override
	public void onFilePrintPreview() {
		LOG.info("onFilePrintPreview()");
	}

	@Override
	public void onFileExit() {
    	LOG.info("onExit() called.");
    	if(this.getCurrentDocument().isModified()) {
    		int ret = JOptionPane.showConfirmDialog(null,
    						"�h�L�������g�͕ύX����Ă��܂��B�ۑ����܂����H",
    						"title", JOptionPane.YES_NO_OPTION);
    		if(ret==JOptionPane.YES_OPTION) {
    			onFileSave();
    		}
    	}
    	System.exit(0);
	}
	
	/*
	@Override
	public void saveFileAs(File file) {
		this.currentFile = new File(file.getAbsolutePath());
		save();
	}

	@Override
	public void save() {
		this.currentFile = new File(currentFile.getAbsolutePath());
	}
	*/

	public JFileChooser getFileChooser() {
		if(this.fileChooser == null) {
			this.fileChooser = createFileChooser();
		}
		return this.fileChooser;
	}

	protected JFileChooser createFileChooser() {
		return new JFileChooser();
	}
	
	public Action getOpenAction() {
		if(openAction == null) {
			openAction = new AbstractAction("�J��...") {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent event) {
					JFileChooser fc = getFileChooser();
					int returnVal = fc.showOpenDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	onFileOpen(fc);
				    }
				}
			};
		}
		return openAction;
	}

	public Action getSaveAsAction() {
		if(saveAsAction == null) {
			saveAsAction = new AbstractAction("���O�����ĕۑ�...") {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent event) {
					JFileChooser fc = getFileChooser();
					fc.setDialogTitle("���O�����ĕۑ�");
					int returnVal = fc.showSaveDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	onFileSaveAs(fc);
				    }
				}
			};
		}
		return saveAsAction;
	}

	public Action getExitAction() {
		if(exitAction == null) {
			exitAction = new AbstractAction("�I��") {
				private static final long serialVersionUID = 1L;
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onFileExit();
				}
			};
		}
		return exitAction;
	}
}
