package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums;
import java.awt.Color;

import PipelineUtils.PipelineNoteList;
import ResourceUtils.RandomNumberSequence;
import ResourceUtils.ResourceObjectParent;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.OnOffButton;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.TestButton;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Generator;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class DrumGeneratorOne extends PlayletObject implements PlayletObjectInterface, Generator, ResourceObjectParent{
	
	private boolean on = false;
	private RandomNumberSequence rnd = new RandomNumberSequence(16, 3);	// size = 16, seed is arbitrarily = 3

	
	public DrumGeneratorOne(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.DRUM_GENERATOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(50, 50, 200, 255));
		guiObject.addButton(new OnOffButton(guiObject, 24, 24));
		guiObject.addButton(new TestButton(guiObject, -24, -24));
		guiObject.addButton(new TestButton(guiObject, -24, 24));
		guiObject.addButton(new TestButton(guiObject, 24, -24));
	}
	public void render(){
		if (on){
			makePlayPlugArgument();
			ppa.rnd = rnd;
			receivedPNO = makePNO();
			receivedPNO.ppa = ppa;
			processedPNO = receivedPNO.copy();		// does not actually create any drum music, leaving that for the plugins so we can drop in kikz and snares and the like
			sendPNOCopyToOuts(processedPNO.copy());			
		}		
	}
	public void setOn(boolean b){
		on = b;
		parent.setForceReRenderOn();
	}
	


	
// privates -------------------------------------------------------

	private PNO makePNO(){
		PNO newPNO = new PNO();
		newPNO.setPNL(new PipelineNoteList(PlayletObject.DRUM_CHANNEL));
		return newPNO;
	}
}

