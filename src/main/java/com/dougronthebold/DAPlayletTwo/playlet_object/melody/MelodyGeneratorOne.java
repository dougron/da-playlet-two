package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody;
import java.awt.Color;

import PipelineUtils.PipelineNoteList;
import PlugIns.PlugInMelodyPhrasedGuideTone;
import ResourceUtils.RandomNumberSequence;
import ResourceUtils.ResourceObjectParent;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.ButtonInstruction;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.NewRndSequenceButton;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Generator;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class MelodyGeneratorOne extends PlayletObject implements PlayletObjectInterface, Generator, ResourceObjectParent{
	
//	private ResourceObject ro = new ResourceObject();
	private PlugInMelodyPhrasedGuideTone plug = new PlugInMelodyPhrasedGuideTone();
	private RandomNumberSequence rnd = new RandomNumberSequence(16, 3);	// size = 16, seed is arbitrarily = 3
	
	public MelodyGeneratorOne(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.LEAD_GENERATOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(150, 200, 0, 255));
		guiObject.addButton(new NewRndSequenceButton(guiObject, 24, -24));
		guiObject.addButton(new MelodyNoteDensityButton(guiObject, -24, -24));
	}
	public void render(){
		makePlayPlugArgument();
		ppa.rnd = rnd;
		if (ppa.hasContour() && ppa.hasChordProgression() && ppa.hasContour()){
			if (!ppa.cp.isNullObject){
				receivedPNO = makePNO();
				receivedPNO.ppa = ppa;
				processedPNO = process(receivedPNO.copy());
				sendPNOCopyToOuts(processedPNO.copy());
			}	
		}	
	}
	public void newRNDSequence(){
		rnd = new RandomNumberSequence(16, (int)(System.currentTimeMillis() % 10000));	// size = 16
		parent.setForceReRenderOn();
	}
	public void doButtonInstruction(ButtonInstruction bi){
		if (bi.instruction == PlayletObject.BUTTON_SET_NOTE_DENSITY){
			plug.setNoteDensity(bi.doubleParam);
			parent.setForceReRenderOn();
		}
	}


	
// privates -------------------------------------------------------
//	private void sendPNOCopyToOuts(PNO pno){
//		
//	}
	
	private PNO process(PNO pno){
		plug.process(pno.pnl(), pno.ppa);		
		return pno;
	}


	private PNO makePNO(){
		PNO newPNO = new PNO();
		newPNO.setPNL(new PipelineNoteList(PlayletObject.LEAD_CHANNEL));
		return newPNO;
	}
}
