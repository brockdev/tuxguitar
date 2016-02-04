package org.herac.tuxguitar.app.tools.browser.filesystem;

import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowser;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserSettings;
import org.herac.tuxguitar.app.tools.browser.base.TGBrowserFactory;
import org.herac.tuxguitar.util.TGContext;

public class TGBrowserFactoryImpl implements TGBrowserFactory{
	
	private TGContext context;
	
	public TGBrowserFactoryImpl(TGContext context) {
		this.context = context;
	}
	
	public String getType(){
		return "file.system";
	}
	
	public String getName(){
		return TuxGuitar.getProperty("browser.factory.fs.name");
	}
	
	public TGBrowser newTGBrowser(TGBrowserSettings data) {
		return new TGBrowserImpl(TGBrowserSettingsModel.createInstance(data));
	}
	
	public TGBrowserSettings dataDialog(Shell parent) {
		TGBrowserDataDialog dialog = new TGBrowserDataDialog(context);
		return dialog.open(parent);
	}
}
