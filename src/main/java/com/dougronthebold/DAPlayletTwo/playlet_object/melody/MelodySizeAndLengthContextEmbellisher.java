package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody;
import java.awt.Color;

import PlugIns.PlugInSizeAndLengthContextEmbellisher;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Processor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class MelodySizeAndLengthContextEmbellisher extends PlayletObject implements PlayletObjectInterface, Processor{

	
	
	private static final int min = 10;
	private static final int stepSize = 1;
//	
//	private double syncopationChance = 1.0;
//	
	private PlugInSizeAndLengthContextEmbellisher plug = new PlugInSizeAndLengthContextEmbellisher();
//			new double[]{-1.0, -0.5, -1.5}, 
//			new double[]{0.5, 0.5, 0.1, 0.2}, 
//			syncopationChance,
//			new ED("d", 1),
//			new ED("d", -1));
	
	public MelodySizeAndLengthContextEmbellisher(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.LEAD_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(200, 50, 50, 255));
		guiObject.setHeightBehaviour(new String[]{"OFF", "1", "2", "3", "4", "5", "6"}, min, stepSize);
		guiObject.heightChangeForcesReRender = true;
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		processedPNO.ppa.numberOfEmbellishments = (guiObject().height() - min) / stepSize;
		//System.out.println("NEW RENDER ##############################################\n#################################################");
		plug.process(processedPNO.pnl(), processedPNO.ppa);
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
}
