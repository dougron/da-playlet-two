package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic;
import java.awt.Color;

import PlugIns.PlugInRandomBendOnLongNote;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class RandomBendProcessor extends PlayletObject implements PlayletObjectInterface{

	private double minimumLength = 1.0;
	private double bendChance = 0.5;
	private PlugInRandomBendOnLongNote plug = new PlugInRandomBendOnLongNote(minimumLength, bendChance);
	
	public RandomBendProcessor(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.BASS_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(200, 100, 100, 255));
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		ppa.rnd.reset();
		plug.process(processedPNO.pnl(), ppa);
		sendPNOCopyToOuts(processedPNO.copy());
	}
}
