package com.unyaunya.swing.application;

public class Application {
	private static Application application;
	private AppFrame frame;
	
	public static Application getInstance() {
		if(application == null) {
			application = new Application();
		}
		return application;
	}
	
	private Application() {
		
	}

	public AppFrame getAppFrame() {
		return frame;
	}

	public void setAppFrame(AppFrame newFrame) {
		this.frame = newFrame;
	}

	public void init() {
		AppFrame f = getAppFrame();
		f.init();
	}
}
