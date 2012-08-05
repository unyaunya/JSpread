package com.unyaunya.swing.application;

import javax.swing.JFileChooser;

public interface FileMenuHandler {
	public void OnFileOpen(JFileChooser fc);
	public void OnFileSave(JFileChooser fc);
	public void OnExit();
}
