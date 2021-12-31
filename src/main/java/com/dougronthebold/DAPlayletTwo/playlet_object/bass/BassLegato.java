package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass;
import java.awt.Color;

import PlugIns.PlugInLegato;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Processor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class BassLegato extends PlayletObject implements PlayletObjectInterface, Processor{

	private PlugInLegato plug = new PlugInLegato(new double[]{1.0});
	
	public BassLegato(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.BASS_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(100, 50, 100, 255));
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		plug.process(processedPNO.pnl(), ppa);
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
	

}
