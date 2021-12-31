package test;
import java.util.ArrayList;

import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;

public class WiringAndGhostTest extends ConsoleProgram implements ConsoleInterface, PlobjParent{
	
	ArrayList<Wire> wireList = new ArrayList<Wire>();
	BassGeneratorOne bg1 = new BassGeneratorOne(0, 0, "BG_1", this);
	BassGeneratorOne bg2 = new BassGeneratorOne(100, 0, "BG_2", this);
	BassInstrumentOne bi1 = new BassInstrumentOne(0, 100, "BassInst1", 0, 0, this, this);
	BassInstrumentOne bi2 = new BassInstrumentOne(100, 100, "BassInst2", 0, 0, this, this);

	public void run(){
		setSize(700, 700);
		setUpGhosts();
		setUpWires();
		for (Wire w: wireList){
			println("-----------------------------------------");
			println(w.toString());
			println("length: " + w.length() + " ghostLength: " + w.ghostLength());
		}
	}
	private void setUpGhosts(){
		bg1.guiObject().setHasGhost(true);
		bg1.guiObject().setGhostPos(50, 0);
		bi1.guiObject().setHasGhost(true);
		bi1.guiObject().setGhostPos(50, 100);
	}
	private void setUpWires(){
		wireList.add(new Wire(bg1, bi1));
		wireList.add(new Wire(bg1, bi2));
		wireList.add(new Wire(bg2, bi1));
		wireList.add(new Wire(bg2, bi2));
	}
	
	public void consolePrint(String str){
		println(str);
	}
// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
}
