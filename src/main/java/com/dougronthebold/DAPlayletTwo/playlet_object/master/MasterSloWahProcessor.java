package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master;
import java.awt.Color;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import PlugIns.PlugInSlowWah;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class MasterSloWahProcessor extends PlayletObject implements PlayletObjectInterface{

	private int wahControllerIndex = 1;
	private PlugInSlowWah plug = new PlugInSlowWah(wahControllerIndex, DeviceParamInfo.default_HP.name); 	// using 'HP' as the name for wah wah.... for now
	
	public MasterSloWahProcessor(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.MASTER_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(200, 200, 50, 255));
	}
	
	public void in(PNO pno){
	
		receivedPNO = pno;
		processedPNO = pno.copy();
		plug.process(processedPNO.pnl(), ppa);
		sendPNOCopyToOuts(processedPNO.copy());
	}
}
