package org.herac.tuxguitar.app.view.util;

import org.herac.tuxguitar.app.ui.TGApplication;
import org.herac.tuxguitar.editor.TGEditorManager;
import org.herac.tuxguitar.ui.event.UIDisposeEvent;
import org.herac.tuxguitar.ui.event.UIDisposeListener;
import org.herac.tuxguitar.ui.resource.UIImage;
import org.herac.tuxguitar.ui.resource.UIPainter;
import org.herac.tuxguitar.ui.resource.UIRectangle;
import org.herac.tuxguitar.ui.resource.UIResourceFactory;
import org.herac.tuxguitar.ui.widget.UICanvas;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.TGSynchronizer;

public class TGBufferedPainterLocked {
	
	private TGContext context;
	private UIImage buffer;
	private TGBufferedPainterHandle handle;
	
	public TGBufferedPainterLocked(TGContext context, TGBufferedPainterHandle handle) {
		this.context = context;
		this.handle = handle;
		this.handle.getPaintableControl().addDisposeListener(new UIDisposeListener() {
			public void onDispose(UIDisposeEvent event) {
				disposePaintBuffer();
			}
		});
	}
	
	public void disposePaintBuffer() {
		if( this.buffer != null && !this.buffer.isDisposed() ) {
			this.buffer.dispose();
			this.buffer = null;
		}
	}
	
	public void fillPaintBuffer() {
		UIRectangle size = this.handle.getPaintableControl().getBounds();
		float clientWidth = size.getWidth();
		float clientHeight = size.getHeight();
		
		if( this.buffer == null || this.buffer.isDisposed() || this.buffer.getWidth() != clientWidth || this.buffer.getHeight() != clientHeight ) {
			this.disposePaintBuffer();
			this.buffer = this.getResourceFactory().createImage(clientWidth, clientHeight);
		}
		
		UIPainter tgPainter = this.buffer.createPainter();
		
		this.handle.paintControl(tgPainter);
		
		tgPainter.dispose();
	}
	
	public void paintBufferLocked(UIPainter painter) {
		TGEditorManager editor = TGEditorManager.getInstance(this.context);
		if (editor.tryLock()) {
			try {
				this.fillPaintBuffer();
			} finally {
				editor.unlock();
			}
		} else {
			// try later
			this.redrawLater();
		}
		
		if( this.buffer != null && !this.buffer.isDisposed() ) {
			painter.drawImage(this.buffer, 0, 0);
		}
	}
	
	public void redrawLater() {
		final UICanvas paintableControl = this.handle.getPaintableControl();
		TGSynchronizer.getInstance(this.context).executeLater(new Runnable() {
			public void run() {
				if(!paintableControl.isDisposed()) {
					paintableControl.redraw();
				}
			}
		});
	}
	
	public UIResourceFactory getResourceFactory() {
		return TGApplication.getInstance(this.context).getFactory();
	}
	
	public TGContext getContext() {
		return this.context;
	}
	
	public static interface TGBufferedPainterHandle {
		
		void paintControl(UIPainter painter);
		
		UICanvas getPaintableControl();
	}
}
