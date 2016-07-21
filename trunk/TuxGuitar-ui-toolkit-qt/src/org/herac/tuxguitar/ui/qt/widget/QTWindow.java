package org.herac.tuxguitar.ui.qt.widget;

import org.herac.tuxguitar.ui.event.UICloseListener;
import org.herac.tuxguitar.ui.event.UIResizeEvent;
import org.herac.tuxguitar.ui.event.UIResizeListener;
import org.herac.tuxguitar.ui.menu.UIMenuBar;
import org.herac.tuxguitar.ui.qt.event.QTCloseListenerManager;
import org.herac.tuxguitar.ui.qt.menu.QTMenuBar;
import org.herac.tuxguitar.ui.qt.resource.QTImage;
import org.herac.tuxguitar.ui.resource.UIImage;
import org.herac.tuxguitar.ui.resource.UIRectangle;
import org.herac.tuxguitar.ui.widget.UIWindow;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEvent.Type;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QContentsMargins;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QWidget;

public class QTWindow extends QTLayoutContainer<QMainWindow> implements UIWindow {
	
	private UIImage image;
	private UIMenuBar menuBar;
	private QTWindowCloseListener closeListener;
	private QTWindowResizeListener resizeListener;
	
	public QTWindow(QMainWindow widget, QTContainer parent) {
		super(widget, parent, false);
		
		this.closeListener = new QTWindowCloseListener(this);
		this.resizeListener = new QTWindowResizeListener(this);
		this.addResizeListener(this.resizeListener);
		this.connectCloseListener();
	}
	
	public QTWindow() {
		this(new QMainWindow(), null);
	}

	public QTWindow(QTWindow parent, boolean modal, boolean resizable) {
		this(new QMainWindow(parent.getControl()), parent);
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
	
	public UIMenuBar getMenuBar() {
		return this.menuBar;
	}

	public void setMenuBar(UIMenuBar menuBar) {
		this.menuBar = menuBar;
		this.getControl().setMenuBar(this.menuBar != null ? ((QTMenuBar) this.menuBar).getControl() : null);
	}
	
	public void open() {
		this.getControl().show();
	}

	public void close() {
		this.getControl().close();
	}
	
	public void computeMargins() {
		super.computeMargins();
		
		QContentsMargins margins = this.getContainerChildMargins();
		QWidget menuWidget = this.getControl().menuWidget();
		if( menuWidget != null ) {
			margins.top += menuWidget.sizeHint().height();
			
			this.setContainerChildMargins(margins);
		}
	}
	
	public void setBounds(UIRectangle bounds) {
		this.resizeListener.setBounds(bounds);
		super.setBounds(bounds);
	}
	
	public void join() {
		while(!this.isDisposed()) {
			if (QApplication.hasPendingEvents()) {
				QApplication.processEvents();
				if(!this.isDisposed()) {
					QApplication.sendPostedEvents();
				}
			}
			Thread.yield();
		}
	}

	public void minimize() {
		this.getControl().showMinimized();
	}
	
	public void maximize() {
		this.getControl().showMaximized();
	}
	
	public boolean isMaximized() {
		return this.getControl().isMaximized();
	}
	
	public void moveToTop() {
		// 
	}
	
	public void connectCloseListener() {
		this.getEventFilter().connect(Type.Close, this.closeListener);
	}
	
	public void addCloseListener(UICloseListener listener) {
		this.closeListener.addListener(listener);
	}

	public void removeCloseListener(UICloseListener listener) {
		this.closeListener.removeListener(listener);
	}
	
	private class QTWindowResizeListener implements UIResizeListener {
		
		private UIRectangle bounds;
		private QTWindow window;
		
		public QTWindowResizeListener(QTWindow window) {
			this.window = window;
		}
		
		public void onResize(UIResizeEvent event) {
			UIRectangle bounds = this.window.getBounds();
			if( this.bounds == null || !this.bounds.equals(bounds)) {
				this.bounds = bounds;
				this.window.layout();
			}
		}
		
		public void setBounds(UIRectangle bounds) {
			this.bounds = bounds;
		}
	}
	
	private class QTWindowCloseListener extends QTCloseListenerManager {
		
		public QTWindowCloseListener(QTWindow window) {
			super(window);
		}
		
		public void handle(QEvent event) {
			if(!this.isEmpty()) {
				super.handle(event);
			} else {
				this.getControl().dispose();
			}
		}
	}
}