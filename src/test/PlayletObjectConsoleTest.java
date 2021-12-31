package test;
import com.cycling74.max.Atom;

import AtomUtils.DougzAtomUtilities;
import LegacyStuff.ChordProgrammer2;
import LegacyStuff.ChordProgrammer2Parent;
import ResourceUtils.TwoBarRhythmBuffer;
import TestUtils.TestData;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;


public class PlayletObjectConsoleTest extends ConsoleProgram implements ConsoleInterface, ChordProgrammer2Parent, PlobjParent{

	
	public void run(){
		setSize(800, 700);
		BassGeneratorOne bsg = new BassGeneratorOne(1, 2, "poopy", this);
		BassInstrumentOne bsi = new BassInstrumentOne(4, 5, "peepe", this, this);
		ChordProgrammer2 cp = TestData.makeProgression(this);
		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
		TestData.tangoForRhythmBuffer(rb);
		bsg.setChordForm(cp.form);
		bsg.setRhythmBuffer(rb);
		Wire w = new Wire(bsg, bsi);
		bsg.addOutWire(w);
		bsi.addInWire(w);
		bsg.setAllOutWires(true);
		bsg.render();
	}
	public void consolePrint(String str){
		println(str);
	}
	public void sendChordProgrammerMessage(Atom[] atArr){
		println(DougzAtomUtilities.atomArrToString(atArr));
	}
// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
	
}
