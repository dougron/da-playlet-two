package test;
import DataObjects.ableton_live_clip.LiveClip;
import PipelineUtils.PipelinePlugIn;
import ResourceUtils.ChordForm;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysProcessorTwo;

/*
 * testing the limits and issues around the LiveClip.makeChordAnalysis() method
 * This stems from problems analyzing progressions which have had chromatic embellishments......
 */
public class ChordAnalysisDeepConsoleTest extends ConsoleProgram implements ConsoleInterface, PlobjParent{

	private PipelinePlugIn[] plugArr; 
	
	public void run(){
		setSize(700, 700);
		LiveClip lc = makeLiveClip();
		ChordForm cf = new ChordForm(lc);
		KeysGeneratorOne kg = new KeysGeneratorOne(1.0, 1.0, "KG", this);
		kg.tempCF = cf;
		KeysProcessorOne kp1 = new KeysProcessorOne(2.0, 2.0, "KP1", this);
		KeysProcessorTwo kp2 = new KeysProcessorTwo(2.0, 2.0, "KP1", this);
		KeysProcessorOne kp3 = new KeysProcessorOne(2.0, 2.0, "KP1", this);
		KeysProcessorOne kp4 = new KeysProcessorOne(2.0, 2.0, "KP1", this);
		KeysInstrumentOne ki = new KeysInstrumentOne(10, 10, "KI", 0, 0, this, this);
		
		Wire w1 = new Wire(kg, kp1);
		w1.setOn(true);
		Wire w2 = new Wire(kp1, kp2);
		w2.setOn(true);
		Wire w3 = new Wire(kp2, kp3);
		w3.setOn(true);
		Wire w4 = new Wire(kp3, kp4);
		w4.setOn(true);
		Wire w5 = new Wire(kp4, ki);
		w5.setOn(true);
		
		kg.render();
		LiveClip result = ki.receivedPNO.pnl().makeLiveClip();
		result.makeChordAnalysis();
		println(result.toString());
//		println(result.chunkArrayToString());
//		println(cf.toString());
	}
	
	public void consolePrint(String str){
		println(str);
	}
	
	private LiveClip makeLiveClip(){
		LiveClip lc = new LiveClip(0, 0);
		lc.addNote(40, 0.0, 8.0, 100, 0);
		lc.addNote(43, 0.0, 8.0, 100, 0);
		lc.addNote(47, 0.0, 8.0, 100, 0);
		lc.length = 8.0;
		return lc;
	}
// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
}
