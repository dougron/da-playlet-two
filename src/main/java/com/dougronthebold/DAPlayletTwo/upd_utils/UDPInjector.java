package main.java.com.dougronthebold.DAPlayletTwo.upd_utils;
import java.util.ArrayList;

import DataObjects.ableton_live_clip.LiveClip;
import DataObjects.ableton_live_clip.LiveMidiNote;
import UDPUtils.OSCMessMaker;
import UDPUtils.UDPConnection;


public class UDPInjector {
	
	private static final int port = 7800;

	public UDPInjector(){
		
	}
	public void inject(ArrayList<LiveClip> lcList){
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(injectMessage);
		for (LiveClip lc: lcList){
			addLiveClipToOSCMessage(lc, mess);
		}
		sendMessage(mess);		
		
	}
	public void inject(LiveClip lc){
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(injectMessage);
		addLiveClipToOSCMessage(lc, mess);
		sendMessage(mess);
	}
	
// privates ---------------------------------------------------------------------	
	private void sendMessage(OSCMessMaker mess){
		UDPConnection conn = new UDPConnection(port);
		conn.sendUDPMessage(mess);
	}
	private void addLiveClipToOSCMessage(LiveClip lc, OSCMessMaker mess){
		// format for injection, makes 'notes' and 'param' messages.....

		// notes message
		mess.addItem(notesMessage);
		addLCNotesToOSCMess(lc, mess);
		mess.addItem(lc.noteList.size());
		mess.addItem(lc.clipObjectIndex);
		// param message
		mess.addItem(paramMessage);
		paramListToOSCMessage(lc, mess);

	}

	private void addLCNotesToOSCMess(LiveClip lc, OSCMessMaker mess){		
		for (LiveMidiNote lmn: lc.noteList){
			mess.addItem(lmn.note);
			mess.addItem(lmn.position);
			mess.addItem(lmn.length);
			mess.addItem(lmn.velocity);
			mess.addItem(lmn.mute);
		}
	}
	private void paramListToOSCMessage(LiveClip lc, OSCMessMaker mess){

		mess.addItem(lc.length);
		mess.addItem(lc.loopStart);
		mess.addItem(lc.loopEnd);
		mess.addItem(lc.startMarker);
		mess.addItem(lc.endMarker);
		mess.addItem(lc.signatureNumerator);
		mess.addItem(lc.signatureDenominator);
		mess.addItem(lc.offset);
		mess.addItem(lc.clip);
		mess.addItem(lc.track);
		mess.addItem(lc.name);
		mess.addItem(lc.clipObjectIndex); 

	}
	
	private static final String injectMessage = "inject";
	private static final String notesMessage = "notes";
	private static final String paramMessage = "param";
	private static final String controllerMessage = "controller";
	private static final String pitchbendMessage = "pitchbend";
}
