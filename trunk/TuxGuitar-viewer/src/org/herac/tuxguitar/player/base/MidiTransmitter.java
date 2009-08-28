package org.herac.tuxguitar.player.base;

import java.util.ArrayList;
import java.util.List;

public class MidiTransmitter {
	
	private List receivers;
	
	public MidiTransmitter(){
		this.receivers = new ArrayList();
	}
	
	public void sendSystemReset() throws MidiPlayerException {
		for( int i = 0 ; i < this.receivers.size() ; i ++ ){
			MidiReceiverItem receiver = (MidiReceiverItem) this.receivers.get( i );
			receiver.getReceiver().sendSystemReset();
		}
	}
	
	public void sendAllNotesOff() throws MidiPlayerException {
		for( int i = 0 ; i < this.receivers.size() ; i ++ ){
			MidiReceiverItem receiver = (MidiReceiverItem) this.receivers.get( i );
			receiver.getReceiver().sendAllNotesOff();
		}
	}
	
	public void sendNoteOn(int channel, int key, int velocity) throws MidiPlayerException {
		for( int i = 0 ; i < this.receivers.size() ; i ++ ){
			MidiReceiverItem receiver = (MidiReceiverItem) this.receivers.get( i );
			receiver.getReceiver().sendNoteOn(channel, key, velocity);
		}
	}
	
	public void sendNoteOff(int channel, int key, int velocity) throws MidiPlayerException {
		for( int i = 0 ; i < this.receivers.size() ; i ++ ){
			MidiReceiverItem receiver = (MidiReceiverItem) this.receivers.get( i );
			receiver.getReceiver().sendNoteOff(channel, key, velocity);
		}
	}
	
	public void sendProgramChange(int channel, int value) throws MidiPlayerException {
		for( int i = 0 ; i < this.receivers.size() ; i ++ ){
			MidiReceiverItem receiver = (MidiReceiverItem) this.receivers.get( i );
			receiver.getReceiver().sendProgramChange(channel, value);
		}
	}
	
	public void sendControlChange(int channel, int controller, int value) throws MidiPlayerException {
		for( int i = 0 ; i < this.receivers.size() ; i ++ ){
			MidiReceiverItem receiver = (MidiReceiverItem) this.receivers.get( i );
			receiver.getReceiver().sendControlChange(channel, controller, value);
		}
	}
	
	public void sendPitchBend(int channel, int value) throws MidiPlayerException {
		for( int i = 0 ; i < this.receivers.size() ; i ++ ){
			MidiReceiverItem receiver = (MidiReceiverItem) this.receivers.get( i );
			receiver.getReceiver().sendPitchBend(channel, value);
		}
	}
	
	public void addReceiver(String id, MidiReceiver receiver){
		MidiReceiverItem item = getReceiver( id );
		if( item == null ){
			this.receivers.add( new MidiReceiverItem( id , receiver ) );
		}
	}
	
	public void removeReceiver(String id){
		MidiReceiverItem item = getReceiver( id );
		if( item != null ){
			this.receivers.remove( item );
		}
	}
	
	private MidiReceiverItem getReceiver(String id){
		if( id != null ){
			for( int i = 0 ; i < this.receivers.size() ; i ++ ){
				MidiReceiverItem receiver = (MidiReceiverItem) this.receivers.get( i );
				if( receiver.getId() != null && receiver.getId().equals( id ) ){
					return receiver;
				}
			}
		}
		return null;
	}
	
	private class MidiReceiverItem {
		private String id;
		private MidiReceiver receiver;
		
		public MidiReceiverItem(String id, MidiReceiver receiver){
			this.id = id;
			this.receiver = receiver;
		}
		
		public String getId() {
			return this.id;
		}
		
		public MidiReceiver getReceiver() {
			return this.receiver;
		}
	}
}
