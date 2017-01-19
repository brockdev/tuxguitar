package org.herac.tuxguitar.io.midi;

import org.herac.tuxguitar.io.base.TGSongWriter;
import org.herac.tuxguitar.io.plugin.TGSongWriterPlugin;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.plugin.TGPluginException;

public class MidiSongWriterPlugin extends TGSongWriterPlugin {
	
	public MidiSongWriterPlugin() {
		super(false);
	}
	
	public String getModuleId(){
		return MidiPlugin.MODULE_ID;
	}

	protected TGSongWriter createOutputStream(TGContext context) throws TGPluginException {
		return new MidiSongWriter();
	}
}
