package org.herac.tuxguitar.ui.qt.widget;

import org.herac.tuxguitar.ui.qt.resource.QTImage;
import org.herac.tuxguitar.ui.resource.UIImage;
import org.herac.tuxguitar.ui.widget.UISplashWindow;
import org.qtjambi.qt.widgets.QApplication;
import org.qtjambi.qt.widgets.QSplashScreen;

public class QTSplashWindow extends QTWidget<QSplashScreen> implements UISplashWindow {
	
	private UIImage image;
	private UIImage splashImage;
	
	public QTSplashWindow() {
		super(new QSplashScreen(), null);
	}
	
	public String getText() {
		return this.getControl().windowTitle();
	}

	public void setText(String text) {
		this.getControl().setWindowTitle(text);
	}

	public UIImage getImage() {
		return this.image;
	}

	public void setImage(UIImage image) {
		this.image = image;
		this.getControl().setWindowIcon(this.image != null ? ((QTImage) this.image).createIcon() : null);
	}
	
	public UIImage getSplashImage() {
		return splashImage;
	}

	public void setSplashImage(UIImage splashImage) {
		this.splashImage = splashImage;
		this.getControl().setPixmap(this.splashImage != null ? ((QTImage) this.splashImage).createPixmap() : null);
	}

	public void open() {
		this.getControl().show();
		this.processPendingEvents();
	}
	
	public void processPendingEvents() {
		if (QApplication.hasPendingEvents()) {
			QApplication.processEvents();
		}
	}
}
