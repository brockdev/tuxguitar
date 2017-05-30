package org.herac.tuxguitar.midi.synth;

import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.configuration.TGConfigManager;

public class TGSynthSettings {
	
	private static final String MIDI_PROGRAM_PREFIX = "synth.program";
	
	private TGContext context;
	private TGConfigManager config;
	
	public TGSynthSettings(TGContext context){
		this.context = context;
	}
	
	public TGConfigManager getConfig(){
		if( this.config == null ){
			this.config = new TGConfigManager(this.context, "tuxguitar-synth");
		}
		return this.config;
	}
	
	public void save(){
		this.getConfig().save();
	}
	
	public void loadPrograms(TGSynthesizer synthesizer) {
		for( int b = 0; b < TGSynthesizer.BANKS_LENGTH ; b ++ ){
			for( int p = 0 ; p < TGSynthesizer.PROGRAMS_LENGTH ; p ++ ){
				loadProgram(synthesizer, b, p);
			}
		}
	}
	
	public void loadProgram(TGSynthesizer synthesizer, int bank, int program) {
		String prefix = (MIDI_PROGRAM_PREFIX + "." + bank + "." + program);
		
		TGProgram tgProgram = TGProgramPropertiesUtil.getProgram(this.getConfig().getProperties(), prefix);
		if( tgProgram != null ) {
			synthesizer.getProgram(bank, program).copyFrom(tgProgram);
		}
	}
}
