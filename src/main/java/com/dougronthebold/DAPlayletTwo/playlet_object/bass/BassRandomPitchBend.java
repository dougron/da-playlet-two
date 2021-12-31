package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass;
import java.awt.Color;

import PlugIns.PlugInRandomBendOnLongNote;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Processor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;



public class BassRandomPitchBend extends PlayletObject implements PlayletObjectInterface, Processor{

	private PlugInRandomBendOnLongNote plug = new PlugInRandomBendOnLongNote(0.5, 0.75);
	
	public BassRandomPitchBend(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.BASS_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(50, 100, 150, 255));
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		plug.process(processedPNO.pnl(), processedPNO.ppa);
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
	

}
