package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys;
import java.util.ArrayList;

import PipelineUtils.PipelineNoteList;
import PlugIns.PlugInKeysPad;
import ResourceUtils.ChordForm;
import ResourceUtils.RandomNumberSequence;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Generator;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public class KeysGeneratorOne extends PlayletObject implements PlayletObjectInterface, Generator{

	private PlugInKeysPad plug = new PlugInKeysPad();
	public ChordForm tempCF;
	private RandomNumberSequence rnd = new RandomNumberSequence(16, 3);	// size = 16, seed is arbitrarily = 3

	
	public KeysGeneratorOne(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.KEYS_GENERATOR, x, y, uniqueName, pp);
	}
	
	public void render(){
		makePlayPlugArgument();
		ppa.rnd = rnd;
		if (ppa.hasChordProgression()){
			receivedPNO = makePNO();
			receivedPNO.ppa = ppa;
			processedPNO = process(receivedPNO.copy());
			sendPNOCopyToOuts(processedPNO.copy());
		}
		
	}
	
	
// privates -------------------------------------------------------

	private PNO process(PNO pno){
		
		plug.process(pno.pnl(), pno.ppa);

		return pno;
	}
	private PNO makePNO(){
		PNO newPNO = new PNO();
		newPNO.setPNL(new PipelineNoteList(PlayletObject.BASS_CHANNEL));
		return newPNO;
	}	

}
