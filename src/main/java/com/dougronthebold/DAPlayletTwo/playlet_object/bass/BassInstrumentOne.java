package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass;
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

public class BassInstrumentOne extends PlayletObject implements PlayletObjectInterface{

	ConsoleInterface ci;
	private Color bgColor = new Color(50, 200, 50);
	
	public BassInstrumentOne(double x, double y, String uniqueName, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.BASS_INSTRUMENT, x, y, uniqueName, pp);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	public BassInstrumentOne(double x, double y, String uniqueName, int clipObjectIndex, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.BASS_INSTRUMENT, x, y, uniqueName, pp);
		setClipObjectIndex(clipObjectIndex);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	public BassInstrumentOne(double x, double y, String uniqueName, int trackIndex, int clipIndex, ConsoleInterface ci, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.BASS_INSTRUMENT, x, y, uniqueName, pp);
		setTrackClipIndex(trackIndex, clipIndex);
		guiObject.setBGColor(bgColor);
		this.ci = ci;
		addControllerInfo();
	}
	public void in(PNO pno){		
		instrumentIn(pno, PlayletObject.BASS);
//		ci.consolePrint(pno.toString());
	}
	
// init message stuff all should end up in PlayletObject-------------------------------------------------
// init newClipObject track 1 0 controllerinit controller vol controller device 0 3 HP controller send 1 send1 controller pan
//	public void instrumentInitializationMessage(OSCMessMaker mess){
//		mess.addItem(PlayletObject.newClipObjectString);
//		mess.addItem(PlayletObject.trackString);
//		mess.addItem(getTrackIndex());
//		mess.addItem(getClipIndex());
//		if (controllerList.size() > 0){
//			mess.addItem(PlayletObject.controllerinitString);
//			for (ControllerInfo ci: controllerList){
//				mess.addItem(PlayletObject.controllerString);
//				ci.addToOscMessage(mess);
//			}
//		}
//	}
	
// end of init message stuff-----------------------------------------------------------------------------	
	private void addControllerInfo(){
		controllerList.add(new PanInfo(getTrackIndex(), DeviceParamInfo.defaultPanOffValue, getTrackType()));
		controllerList.add(new VolumeInfo("bassVol", getTrackIndex(), DeviceParamInfo.defaultVolOffValue, getTrackType()));
		controllerList.add(new SendInfo("delaySend", getTrackIndex(), DeviceParamInfo.defaultDelaySendIndex, DeviceParamInfo.defaultDelayOffValue, getTrackType()));
		controllerList.add(new DeviceParamInfo(DeviceParamInfo.default_HP, getTrackIndex(), getTrackType()));
	}

	
}
