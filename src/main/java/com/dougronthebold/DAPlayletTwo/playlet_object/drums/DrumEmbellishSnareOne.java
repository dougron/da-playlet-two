package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums;
import java.awt.Color;

import PlugIns.DrumStaticVariables;
import PlugIns.PlugInDrumEmbellish;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Processor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class DrumEmbellishSnareOne extends PlayletObject implements PlayletObjectInterface, Processor{

	private PlugInDrumEmbellish plug = new PlugInDrumEmbellish(DrumStaticVariables.snrNote);
	
	private static final int min = 10;
	private static final int stepSize = 2;
	
	public DrumEmbellishSnareOne(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.DRUM_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(100, 50, 50, 255));
		guiObject.setHeightBehaviour(new String[]{"OFF", "1", "2", "3", "4"}, min, stepSize);
		guiObject.heightChangeForcesReRender = true;
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		
		for (int i = 0; i < (guiObject().height() - min) / stepSize; i++){
			//System.out.println("\niteration : " + i);
			plug.process(processedPNO.pnl(), ppa);
		}
		
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
	

}

