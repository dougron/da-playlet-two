package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody;
import java.util.ArrayList;

import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.Contour;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RhythmBuffer;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.resource.ChordProgression;

public class MelodySizeAndLengthContextEmbellisherConsoleTest 
	extends ConsoleProgram 
	implements ConsoleInterface, PlobjParent{
	
	public ArrayList<Wire> wList = new ArrayList<Wire>();

	public void run(){
		setSize(700, 900);
		ChordProgression cp = new ChordProgression(1, 1, "CP", this);
		RhythmBuffer rb = new RhythmBuffer(1, 1, "RB", this);
		Contour cn = new Contour(1, 1, "CN", this);
		MelodyGeneratorOne mg = new MelodyGeneratorOne(400, 300, "MG_1", this);
//		MelodySyncopator ms = new MelodySyncopator(400, 450, "MSync", this);
//		ms.guiObject.incrementHeight(1, false);
		MelodySizeAndLengthContextEmbellisher mslce = new MelodySizeAndLengthContextEmbellisher(400, 450, "MSiLe", this);
		mslce.guiObject().incrementHeight(2, false);
		MelodyInstrument mi = new MelodyInstrument(400, 720, "MInst1", 4, 0, this, this);
		connect(cp, mg);
		connect(rb, mg);
		connect(cn, mg);
		connect(mg, mslce);
		connect(mslce, mi);
		mg.render();
	}

// privates =-------------------------------------------------------------------------
	private void connect(PlayletObjectInterface one, PlayletObjectInterface two){
		Wire w = new Wire(one, two);
		w.setOn(true);
//		one.addOutWire(w);
//		two.addInWire(w);
		wList.add(w);
			
	}

// ConsoleInterface methods ----------------------------------------------------------

	public void consolePrint(String str){
		println(str);
	}
// PlobjParent methods ---------------------------------------------------------------
	
	public void setForceReRenderOn(){
			
	}
}
