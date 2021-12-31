package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody;
import java.awt.Color;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import DataObjects.ableton_device_control_utils.PanInfo;
import DataObjects.ableton_device_control_utils.SendInfo;
import DataObjects.ableton_device_control_utils.VolumeInfo;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class MelodyInstrument extends PlayletObject implements PlayletObjectInterface{

	ConsoleInterface ci;
	private Color bgColor = new Color(50, 200, 50);
	
	public MelodyInstrument(double x, double y, String uniqueName, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.LEAD_INSTRUMENT, x, y, uniqueName, pp);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	public MelodyInstrument(double x, double y, String uniqueName, int clipObjectIndex, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.LEAD_INSTRUMENT, x, y, uniqueName, pp);
		setClipObjectIndex(clipObjectIndex);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	public MelodyInstrument(double x, double y, String uniqueName, int trackIndex, int clipIndex, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.LEAD_INSTRUMENT, x, y, uniqueName, pp);
		setTrackClipIndex(trackIndex, clipIndex);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	public void in(PNO pno){		
		instrumentIn(pno, PlayletObject.LEAD);
		ci.consolePrint(pno.toString());
	}
	

	
 	private void addControllerInfo(){
		controllerList.add(new PanInfo(getTrackIndex(), DeviceParamInfo.defaultPanOffValue, getTrackType()));
		controllerList.add(new VolumeInfo("bassVol", getTrackIndex(), DeviceParamInfo.defaultVolOffValue, getTrackType()));
		controllerList.add(new SendInfo("delaySend", getTrackIndex(), DeviceParamInfo.defaultDelaySendIndex, DeviceParamInfo.defaultDelayOffValue, getTrackType()));
		controllerList.add(new DeviceParamInfo(DeviceParamInfo.default_HP, getTrackIndex(), getTrackType()));
	}

	
}
