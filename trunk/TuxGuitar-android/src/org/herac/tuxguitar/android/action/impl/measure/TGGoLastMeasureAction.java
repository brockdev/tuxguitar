package org.herac.tuxguitar.android.action.impl.measure;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.transport.TGTransport;
import org.herac.tuxguitar.android.view.tablature.TGCaret;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.song.models.TGMeasure;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.util.TGContext;

public class TGGoLastMeasureAction extends TGActionBase {
	
	public static final String NAME = "action.measure.go-last";
	
	public TGGoLastMeasureAction(TGContext context) {
		super(context, NAME);
	}
	
	protected void processAction(TGActionContext context){
		MidiPlayer midiPlayer = MidiPlayer.getInstance(getContext());
		if( midiPlayer.isRunning() ){
			TGTransport.getInstance(getContext()).gotoLast();
		}
		else{
			TGCaret caret = getEditor().getCaret();
			TGTrack track = caret.getTrack();
			TGMeasure measure = getSongManager(context).getTrackManager().getLastMeasure(track);
			if(track != null && measure != null){
				caret.update(track.getNumber(), measure.getStart(), caret.getSelectedString().getNumber());
			}
		}
	}
}
