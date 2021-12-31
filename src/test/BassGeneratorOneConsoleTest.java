package test;
import com.cycling74.max.Atom;

import LegacyStuff.ChordProgrammer2Parent;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RhythmBuffer;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.resource.ChordProgression;


public class BassGeneratorOneConsoleTest extends ConsoleProgram implements ChordProgrammer2Parent, ConsoleInterface, PlobjParent{

	public void run(){
		setSize(500, 700);
//		ChordForm cf = TestData.makeProgression(this).form;
//		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
//		TestData.tangoForRhythmBuffer(rb);
		ChordProgression cp = new ChordProgression(1, 1, "CP", this);
		RhythmBuffer rb = new RhythmBuffer(1, 1, "RB", this);
		BassGeneratorOne bs = new BassGeneratorOne(1, 1, "test", this);
		BassInstrumentOne bi = new BassInstrumentOne(200, 200, "bassInst", this, this);
//		BassProcessorOne bp = new BassProcessorOne(100, 100, "bassProOne");
//		BassProcessorOne bp2 = new BassProcessorOne(101, 101, "bassProOne");
//		BassProcessorOne bp3 = new BassProcessorOne(101, 101, "bassProOne");
//		bs.setChordForm(cf);
//		bs.setRhythmBuffer(rb);
		
		Wire w1 = new Wire(bs, bi);
		w1.setOn(true);
		Wire w2 = new Wire(cp, bs);
		w2.setOn(true);
		Wire w3 = new Wire(rb, bs);
		w3.setOn(true);
//		Wire w4 = new Wire(bp3, bi);
//		w4.setOn(true);
		bs.render();
		println("done");
	}

	
// ChordProgrammer2Parent methods -------------------------------------
	
	public void sendChordProgrammerMessage(Atom[] atArr){}
	public void consolePrint(String str){
		println(str);
	}
// plobjParent methods ----------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
}
