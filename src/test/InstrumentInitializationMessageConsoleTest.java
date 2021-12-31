package test;
import java.util.ArrayList;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import UDPUtils.OSCMessMaker;
import UDPUtils.UDPConnection;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;

public class InstrumentInitializationMessageConsoleTest extends ConsoleProgram implements ConsoleInterface, PlobjParent{

	
	public void run(){
		setSize(700, 700);
		OSCMessMaker mess = new OSCMessMaker();
		ArrayList<PlayletObjectInterface> poiList = new ArrayList<PlayletObjectInterface>();
		poiList.add(new BassInstrumentOne(0, 0, "poopy", 2, 3, this, this));
		poiList.add(new BassInstrumentOne(0, 0, "peepy", 3, 1, this, this));
		mess.addItem(DeviceParamInfo.initString);
		for (PlayletObjectInterface poi: poiList){
			poi.instrumentInitializationMessage(mess);
		}
		
		println(mess.toString());
		UDPConnection conn = new UDPConnection(7800);
		conn.sendUDPMessage(mess);
	}
	
	
	
	public void consolePrint(String str){
		println(str);
	}
	// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
}
