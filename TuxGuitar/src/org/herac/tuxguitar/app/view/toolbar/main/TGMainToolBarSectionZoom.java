package org.herac.tuxguitar.app.view.toolbar.main;

import org.herac.tuxguitar.app.action.impl.zoom.TGZoomInAction;
import org.herac.tuxguitar.app.action.impl.zoom.TGZoomOutAction;
import org.herac.tuxguitar.ui.toolbar.UIToolActionItem;

public class TGMainToolBarSectionZoom extends TGMainToolBarSection {
	
	private UIToolActionItem zoomIn;
		private UIToolActionItem zoomOut;
	
	public TGMainToolBarSectionZoom(TGMainToolBar toolBar) {
		super(toolBar);
	}
	
	public void createSection() {
		this.zoomIn = this.getToolBar().getControl().createActionItem();
		this.zoomIn.addSelectionListener(this.createActionProcessor(TGZoomInAction.NAME));

		this.zoomOut = this.getToolBar().getControl().createActionItem();
		this.zoomOut.addSelectionListener(this.createActionProcessor(TGZoomOutAction.NAME));

		this.loadIcons();
		this.loadProperties();
	}
	
	public void loadProperties(){
		this.zoomIn.setToolTipText(this.getText("view.zoom-in"));
		this.zoomOut.setToolTipText(this.getText("view.zoom-out"));
	}
	
	public void loadIcons(){
		this.zoomIn.setImage(this.getIconManager().getZoomIn());
		this.zoomOut.setImage(this.getIconManager().getZoomOut());
	}
	
	public void updateItems(){
		//Nothing to do
	}
}
