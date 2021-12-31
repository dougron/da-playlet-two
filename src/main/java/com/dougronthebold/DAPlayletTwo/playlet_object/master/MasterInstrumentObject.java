package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master;
import java.awt.Color;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class MasterInstrumentObject extends PlayletObject implements PlayletObjectInterface{

	ConsoleInterface ci;
	private Color bgColor = new Color(50, 200, 200);
	
	public MasterInstrumentObject(double x, double y, String uniqueName, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.MASTER_INSTRUMENT, x, y, uniqueName, pp);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		setTrackType(DeviceParamInfo.ofMasterTrackType);			// default value is ofTrackType. only need to be explicit with master or return trackz
		addControllerInfo();
	}
	public MasterInstrumentObject(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.MASTER_INSTRUMENT, x, y, uniqueName, pp);
		guiObject.setBGColor(bgColor);
		setTrackType(DeviceParamInfo.ofMasterTrackType);			// default value is ofTrackType. only need to be explicit with master or return trackz
		addControllerInfo();
	}
	public void in(PNO pno){		
		instrumentIn(pno, PlayletObject.MASTER);
//		ci.consolePrint(pno.toString());
	}

	
// privates ---------------------------------------------------------------------------------------
	
	private void addControllerInfo(){
		controllerList.add(new DeviceParamInfo(DeviceParamInfo.default_HP, getTrackIndex(), getTrackType()));
	}
}
