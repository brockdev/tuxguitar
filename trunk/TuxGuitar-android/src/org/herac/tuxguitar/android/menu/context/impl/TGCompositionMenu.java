package org.herac.tuxguitar.android.menu.context.impl;

import android.view.Menu;
import android.view.MenuInflater;

import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.menu.context.TGMenuBase;
import org.herac.tuxguitar.android.view.dialog.clef.TGClefDialogController;
import org.herac.tuxguitar.android.view.dialog.info.TGSongInfoDialogController;
import org.herac.tuxguitar.android.view.dialog.keySignature.TGKeySignatureDialogController;
import org.herac.tuxguitar.android.view.dialog.repeat.TGRepeatAlternativeDialogController;
import org.herac.tuxguitar.android.view.dialog.repeat.TGRepeatCloseDialogController;
import org.herac.tuxguitar.android.view.dialog.tempo.TGTempoDialogController;
import org.herac.tuxguitar.android.view.dialog.timeSignature.TGTimeSignatureDialogController;
import org.herac.tuxguitar.android.view.dialog.tripletFeel.TGTripletFeelDialogController;
import org.herac.tuxguitar.editor.action.composition.TGRepeatOpenAction;
import org.herac.tuxguitar.player.base.MidiPlayer;

public class TGCompositionMenu extends TGMenuBase {
	
	public TGCompositionMenu(TGActivity activity) {
		super(activity);
	}

	public void inflate(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_composition, menu);
		initializeItems(menu);
	}
	
	public void initializeItems(Menu menu) {
		boolean running = MidiPlayer.getInstance(this.findContext()).isRunning();
		
		this.initializeItem(menu, R.id.menu_composition_change_tempo, new TGTempoDialogController(), !running);
		this.initializeItem(menu, R.id.menu_composition_change_clef, new TGClefDialogController(), !running);
		this.initializeItem(menu, R.id.menu_composition_change_key_signature, new TGKeySignatureDialogController(), !running);
		this.initializeItem(menu, R.id.menu_composition_change_time_signature, new TGTimeSignatureDialogController(), !running);
		this.initializeItem(menu, R.id.menu_composition_change_triplet_feel, new TGTripletFeelDialogController(), !running);
		this.initializeItem(menu, R.id.menu_composition_change_info, new TGSongInfoDialogController(), !running);
		this.initializeItem(menu, R.id.menu_composition_repeat_alternative, new TGRepeatAlternativeDialogController(), !running);
		this.initializeItem(menu, R.id.menu_composition_repeat_close, new TGRepeatCloseDialogController(), !running);
		this.initializeItem(menu, R.id.menu_composition_repeat_open, this.createActionProcessor(TGRepeatOpenAction.NAME), !running);
	}
}
