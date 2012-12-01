package com.unyaunya.gantt.application;

import com.unyaunya.swing.application.Application;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application app = Application.getInstance();
		app.setAppFrame(new AppWindow());
		app.init();
	}
}
