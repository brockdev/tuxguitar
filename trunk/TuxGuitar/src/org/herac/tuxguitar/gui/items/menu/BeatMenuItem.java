/*
 * Created on 02-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.items.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.actions.insert.InsertTextAction;
import org.herac.tuxguitar.gui.actions.note.ChangeTiedNoteAction;
import org.herac.tuxguitar.gui.actions.note.CleanBeatAction;
import org.herac.tuxguitar.gui.actions.note.DecrementNoteSemitoneAction;
import org.herac.tuxguitar.gui.actions.note.IncrementNoteSemitoneAction;
import org.herac.tuxguitar.gui.actions.note.SetVoiceAutoAction;
import org.herac.tuxguitar.gui.actions.note.SetVoiceDownAction;
import org.herac.tuxguitar.gui.actions.note.SetVoiceUpAction;
import org.herac.tuxguitar.gui.actions.note.ShiftNoteDownAction;
import org.herac.tuxguitar.gui.actions.note.ShiftNoteUpAction;
import org.herac.tuxguitar.gui.items.MenuItems;
import org.herac.tuxguitar.song.models.TGNote;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BeatMenuItem extends MenuItems{
	
	private MenuItem noteMenuItem;
	private Menu menu;
	private MenuItem tiedNote;
	private MenuItem cleanBeat;
	private MenuItem insertText;
	private MenuItem voiceAuto;
	private MenuItem voiceUp;
	private MenuItem voiceDown;
	private MenuItem shiftUp;
	private MenuItem shiftDown;
	private MenuItem semitoneUp;
	private MenuItem semitoneDown;
	private DurationMenuItem durationMenuItem;
	private ChordMenuItem chordMenuItem;
	private NoteEffectsMenuItem effectMenuItem;
	private DynamicMenuItem dynamicMenuItem;
	
	public BeatMenuItem(Shell shell,Menu parent, int style) {
		this.noteMenuItem = new MenuItem(parent, style);
		this.menu = new Menu(shell, SWT.DROP_DOWN);
	}
	
	public void showItems(){
		//--Tied Note
		this.tiedNote = new MenuItem(this.menu, SWT.PUSH);
		this.tiedNote.addSelectionListener(TuxGuitar.instance().getAction(ChangeTiedNoteAction.NAME));
		
		//--Tied Note
		this.cleanBeat = new MenuItem(this.menu, SWT.PUSH);
		this.cleanBeat.addSelectionListener(TuxGuitar.instance().getAction(CleanBeatAction.NAME));
		
		//--Duration--
		this.durationMenuItem = new DurationMenuItem(this.menu.getShell(),this.menu,SWT.CASCADE);
		this.durationMenuItem.showItems();
		
		//--Chord--
		this.chordMenuItem = new ChordMenuItem(this.menu.getShell(),this.menu,SWT.CASCADE);
		this.chordMenuItem.showItems();
		
		//--Effects--
		this.effectMenuItem = new NoteEffectsMenuItem(this.menu.getShell(),this.menu,SWT.CASCADE);
		this.effectMenuItem.showItems();
		
		//--Dynamic--
		this.dynamicMenuItem = new DynamicMenuItem(this.menu.getShell(),this.menu,SWT.CASCADE);
		this.dynamicMenuItem.showItems();
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		this.insertText = new MenuItem(this.menu, SWT.PUSH);
		this.insertText.addSelectionListener(TuxGuitar.instance().getAction(InsertTextAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--Semitone Down
		this.voiceAuto = new MenuItem(this.menu, SWT.PUSH);
		this.voiceAuto.addSelectionListener(TuxGuitar.instance().getAction(SetVoiceAutoAction.NAME));
		
		//--Semitone Up
		this.voiceUp = new MenuItem(this.menu, SWT.PUSH);
		this.voiceUp.addSelectionListener(TuxGuitar.instance().getAction(SetVoiceUpAction.NAME));
		
		//--Semitone Down
		this.voiceDown = new MenuItem(this.menu, SWT.PUSH);
		this.voiceDown.addSelectionListener(TuxGuitar.instance().getAction(SetVoiceDownAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--Semitone Up
		this.semitoneUp = new MenuItem(this.menu, SWT.PUSH);
		this.semitoneUp.addSelectionListener(TuxGuitar.instance().getAction(IncrementNoteSemitoneAction.NAME));
		
		//--Semitone Down
		this.semitoneDown = new MenuItem(this.menu, SWT.PUSH);
		this.semitoneDown.addSelectionListener(TuxGuitar.instance().getAction(DecrementNoteSemitoneAction.NAME));
		
		//--SEPARATOR--
		new MenuItem(this.menu, SWT.SEPARATOR);
		
		//--Shift Up
		this.shiftUp = new MenuItem(this.menu, SWT.PUSH);
		this.shiftUp.addSelectionListener(TuxGuitar.instance().getAction(ShiftNoteUpAction.NAME));
		
		//--Shift Down
		this.shiftDown = new MenuItem(this.menu, SWT.PUSH);
		this.shiftDown.addSelectionListener(TuxGuitar.instance().getAction(ShiftNoteDownAction.NAME));
		
		this.noteMenuItem.setMenu(this.menu);
		
		this.loadIcons();
		this.loadProperties();
	}
	
	public void update(){
		TGNote note = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getSelectedNote();
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.tiedNote.setEnabled(!running);
		this.cleanBeat.setEnabled(!running);
		this.voiceAuto.setEnabled(!running);
		this.voiceUp.setEnabled(!running);
		this.voiceDown.setEnabled(!running);
		this.semitoneUp.setEnabled(!running && note != null);
		this.semitoneDown.setEnabled(!running && note != null);
		this.shiftUp.setEnabled(!running && note != null);
		this.shiftDown.setEnabled(!running && note != null);
		this.insertText.setEnabled(!running);
		this.durationMenuItem.update();
		this.chordMenuItem.update();
		this.effectMenuItem.update();
		this.dynamicMenuItem.update();
	}
	
	public void loadProperties(){
		setMenuItemTextAndAccelerator(this.noteMenuItem, "beat", null);
		setMenuItemTextAndAccelerator(this.cleanBeat, "beat.clean", CleanBeatAction.NAME);
		setMenuItemTextAndAccelerator(this.tiedNote, "note.tiednote", ChangeTiedNoteAction.NAME);
		setMenuItemTextAndAccelerator(this.voiceAuto, "note.voice-auto", SetVoiceAutoAction.NAME);
		setMenuItemTextAndAccelerator(this.voiceUp, "note.voice-up", SetVoiceUpAction.NAME);
		setMenuItemTextAndAccelerator(this.voiceDown, "note.voice-down", SetVoiceDownAction.NAME);
		setMenuItemTextAndAccelerator(this.semitoneUp, "note.semitone-up", IncrementNoteSemitoneAction.NAME);
		setMenuItemTextAndAccelerator(this.semitoneDown, "note.semitone-down", DecrementNoteSemitoneAction.NAME);
		setMenuItemTextAndAccelerator(this.shiftUp, "note.shift-up", ShiftNoteUpAction.NAME);
		setMenuItemTextAndAccelerator(this.shiftDown, "note.shift-down", ShiftNoteDownAction.NAME);
		setMenuItemTextAndAccelerator(this.insertText, "text.insert", InsertTextAction.NAME);
		
		this.durationMenuItem.loadProperties();
		this.chordMenuItem.loadProperties();
		this.effectMenuItem.loadProperties();
		this.dynamicMenuItem.loadProperties();
		
	}
	
	public void loadIcons(){
		this.tiedNote.setImage(TuxGuitar.instance().getIconManager().getNoteTied());
	}
}
