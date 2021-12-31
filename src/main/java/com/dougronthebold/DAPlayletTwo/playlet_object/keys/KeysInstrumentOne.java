package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys;
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


public class KeysInstrumentOne extends PlayletObject implements PlayletObjectInterface{

	ConsoleInterface ci;
	private Color bgColor = new Color(25, 100, 25);
	
	public KeysInstrumentOne(double x, double y, String uniqueName, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.KEYS_INSTRUMENT, x, y, uniqueName, pp);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	public KeysInstrumentOne(double x, double y, String uniqueName, int clipObjectIndex, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.KEYS_INSTRUMENT, x, y, uniqueName, pp);
		setClipObjectIndex(clipObjectIndex);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	public KeysInstrumentOne(double x, double y, String uniqueName, int trackIndex, int clipIndex, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.KEYS_INSTRUMENT, x, y, uniqueName, pp);
		setTrackClipIndex(trackIndex, clipIndex);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	
	public void in(PNO pno){
		instrumentIn(pno, PlayletObject.KEYS);		
		//ci.consolePrint(pno.toString());
	}
	
	
	private void addControllerInfo(){
		controllerList.add(new DeviceParamInfo(DeviceParamInfo.default_LP, getTrackIndex(), getTrackType()));
		controllerList.add(new PanInfo("panky", getTrackIndex(), DeviceParamInfo.defaultPanOffValue, getTrackType()));
		controllerList.add(new VolumeInfo("KYVol", getTrackIndex(), 0.5, getTrackType()));
		controllerList.add(new SendInfo("dlySend", getTrackIndex(), DeviceParamInfo.defaultDelaySendIndex, DeviceParamInfo.defaultDelayOffValue, getTrackType()));
		
	}

}

