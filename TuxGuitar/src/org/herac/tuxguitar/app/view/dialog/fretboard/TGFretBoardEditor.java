package org.herac.tuxguitar.app.view.dialog.fretboard;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.editor.TGExternalBeatViewerManager;
import org.herac.tuxguitar.app.view.component.docked.TGDockedPlayingComponentEditor;
import org.herac.tuxguitar.editor.TGEditorManager;
import org.herac.tuxguitar.ui.widget.UIPanel;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TGFretBoardEditor extends TGDockedPlayingComponentEditor<TGFretBoard> {
	
	public TGFretBoardEditor(TGContext context){
		super(context);
	}
	
	@Override
	public void appendListeners() {
		TuxGuitar.getInstance().getIconManager().addLoader(this);
		TuxGuitar.getInstance().getLanguageManager().addLoader(this);
		TuxGuitar.getInstance().getScaleManager().addListener(this);
	}
	
	@Override
	protected TGFretBoard createComponentInstance(UIPanel parent) {
		return new TGFretBoard(this.context, parent);
	}
	
	@Override
	protected void hideComponentInner(){
		TGEditorManager.getInstance(this.context).removeRedrawListener(this);
		TGExternalBeatViewerManager.getInstance(this.context).removeBeatViewerListener(this);
	}
	
	@Override
	protected void showComponentInner(){
		getComponent().computePackedSize();
		
		TGEditorManager.getInstance(this.context).addRedrawListener(this);
		TGExternalBeatViewerManager.getInstance(this.context).addBeatViewerListener(this);
	}
	
	public static TGFretBoardEditor getInstance(TGContext context) {
		return TGSingletonUtil.getInstance(context, TGFretBoardEditor.class.getName(), new TGSingletonFactory<TGFretBoardEditor>() {
			public TGFretBoardEditor createInstance(TGContext context) {
				return new TGFretBoardEditor(context);
			}
		});
	}
}
