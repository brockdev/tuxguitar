/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.app.actions.layout;

import org.eclipse.swt.events.TypedEvent;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.editors.tab.Tablature;
import org.herac.tuxguitar.graphics.control.TGLayoutHorizontal;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SetLinearLayoutAction extends Action{
	public static final String NAME = "action.view.layout-set-linear";
	
	public SetLinearLayoutAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}
	
	protected int execute(TypedEvent e){
		Tablature tablature = getEditor().getTablature();
		tablature.setViewLayout(new TGLayoutHorizontal(tablature,tablature.getViewLayout().getStyle()));
		updateTablature();
		return 0;
	}
}
