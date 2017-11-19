package org.herac.tuxguitar.app.action.impl.zoom;


import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.action.TGActionManager;
import org.herac.tuxguitar.app.action.impl.settings.TGReloadStylesAction;
import org.herac.tuxguitar.app.system.config.TGConfigKeys;
import org.herac.tuxguitar.app.system.config.TGConfigManager;
import org.herac.tuxguitar.app.view.controller.TGToggleViewController;
import org.herac.tuxguitar.editor.action.TGActionBase;
import org.herac.tuxguitar.util.TGContext;

public class TGZoomInAction extends TGActionBase {

	public static final String NAME = "action.gui.zoom-in";
	
	public static final String ATTRIBUTE_CONTROLLER = TGToggleViewController.class.getName();
	
	public TGZoomInAction(TGContext context) {
		super(context, NAME);
	}
	
	protected void processAction(final TGActionContext context) {
		TGConfigManager config = TGConfigManager.getInstance(getContext());
		float zoom = config.getFloatValue(TGConfigKeys.LAYOUT_ZOOM, 1.0f);
		if(zoom < 10.0f) zoom *= 1.2f;
		config.setValue(TGConfigKeys.LAYOUT_ZOOM, zoom);
		
		TGActionManager tgActionManager = TGActionManager.getInstance(getContext());
		tgActionManager.execute(TGReloadStylesAction.NAME, context);
	}
}
