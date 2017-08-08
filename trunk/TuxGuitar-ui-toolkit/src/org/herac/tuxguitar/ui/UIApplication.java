package org.herac.tuxguitar.ui;

import java.net.URL;

public interface UIApplication extends UIComponent {
	
	UIFactory getFactory();
	
	void openUrl(URL url);
	
	void start(Runnable runnable);
	
	void runInUiThread(Runnable runnable);
	
	boolean isInUiThread();
}
