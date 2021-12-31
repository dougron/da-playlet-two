package test;
import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import PlugIns.PlugInSlowWah;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterGenerator;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterInstrumentObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterSloWahProcessor;

public class MasterGeneratorChainConsoleTest extends ConsoleProgram implements ConsoleInterface, PlobjParent{

	
	public void run(){
		setSize(700, 700);
		PlugInSlowWah plug = new PlugInSlowWah(1, DeviceParamInfo.default_HP.name);
		
		println("done");
	}
	public void done(){
		PlayletObjectInterface mg = new MasterGenerator(0, 0, "MG", this);

		PlayletObjectInterface mp = new MasterSloWahProcessor(0, 0, "MP", this);
		PlayletObjectInterface mi = new MasterInstrumentObject(0, 0, "MI", this);
		Wire w1 = new Wire(mg, mp);
		w1.setOn(true);
		Wire w2 = new Wire(mp, mi);
		w2.setOn(true);
		mg.render();
	}
	
	public void consolePrint(String str){
		println(str);
	}
// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
}
