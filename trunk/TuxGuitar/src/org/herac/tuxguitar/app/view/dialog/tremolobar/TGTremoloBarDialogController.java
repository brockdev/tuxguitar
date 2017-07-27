package org.herac.tuxguitar.app.view.dialog.tremolobar;

import org.herac.tuxguitar.app.view.controller.TGOpenViewController;
import org.herac.tuxguitar.app.view.controller.TGViewContext;

public class TGTremoloBarDialogController implements TGOpenViewController {

	public void openView(TGViewContext context) {
		new TGTremoloBarDialog().show(context);
	}
}
