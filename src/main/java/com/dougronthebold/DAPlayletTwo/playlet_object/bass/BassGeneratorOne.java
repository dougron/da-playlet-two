package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass;
import java.awt.Color;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import PipelineUtils.PipelineNoteList;
import PlugIns.PlugInBassFromRhythmBuffer;
import ResourceUtils.RandomNumberSequence;
import ResourceUtils.ResourceObjectParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Generator;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public class BassGeneratorOne extends PlayletObject implements PlayletObjectInterface, Generator, ResourceObjectParent{
	
//	private ResourceObject ro = new ResourceObject();
	private PlugInBassFromRhythmBuffer plug = new PlugInBassFromRhythmBuffer();
	private RandomNumberSequence rnd = new RandomNumberSequence(16, 3);	// size = 16, seed is arbitrarily = 3

	
	public BassGeneratorOne(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.BASS_GENERATOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(50, 50, 200, 255));
	}
	public void render(){
		makePlayPlugArgument();
		ppa.rnd = rnd;
		if (ppa.hasRhythmBuffer() && ppa.hasChordProgression()){
			receivedPNO = makePNO();
			receivedPNO.ppa = ppa;
			processedPNO = process(receivedPNO.copy());
			sendPNOCopyToOuts(processedPNO.copy());
		}
		
	}
	


	
// privates -------------------------------------------------------
//	private void sendPNOCopyToOuts(PNO pno){
//		
//	}
	private PNO process(PNO pno){
		ppa.dynamic = dynamic;
		plug.process(pno.pnl(), pno.ppa);
		return pno;
	}
//	private ChordForm getChordFormFromResourceLayer(){
//		for (Wire w: inWireList()){
//			if (w.on && w.source.type() == PlayletObject.CHORD_PROGRESSION){
//				return w.source.getPPA().cf;
//			}
//		}
//		return new ChordForm(new LiveClip(0, 0));
//	}
//	private TwoBarRhythmBuffer getRhythmBufferFromResourceLayer(){
//		for (Wire w: inWireList()){
//			if (w.on && w.source.type() == PlayletObject.RHYTHM_BUFFER){
//				return w.source.getPPA().rb;
//			}
//		}
//		return new TwoBarRhythmBuffer();
//	}
	private PNO makePNO(){
		PNO newPNO = new PNO();
		newPNO.setPNL(new PipelineNoteList(PlayletObject.BASS_CHANNEL));
		return newPNO;
	}
}
