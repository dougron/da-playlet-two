package test;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;

public class BassInstrumentVolumeAdjustConsoleTest extends ConsoleProgram implements ConsoleInterface, PlobjParent{

	
	public void run(){
		setSize (700, 900);
		PlayletObjectInterface bs1 = new BassInstrumentOne(10, 10, "BS1", 1, 2, this, this);
		bs1.guiObject().incrementHeight(1, false);
	}
	public void consolePrint(String str){
		println(str);
	}
// plobjParent methods ----------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
}
