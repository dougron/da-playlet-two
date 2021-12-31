package test;
import DataObjects.ableton_live_clip.LiveClip;
import PipelineUtils.PipelinePlugIn;
import PlugIns.PlugInKeysPad;
import ResourceUtils.ChordForm;
import acm.program.ConsoleProgram;

/*
 * testing the limits and issues around the LiveClip.makeChordAnalysis() method
 * This stems from problems analyzing progressions which have had chromatic embellishments......
 */
public class ChordAnalysisDeepConsoleTest2 extends ConsoleProgram{

	private PipelinePlugIn[] plugArr; 
	
	public void run(){
		setSize(700, 700);
		LiveClip lc = makeLiveClip();
		ChordForm cf = new ChordForm(lc);
		PlugInKeysPad plug = new PlugInKeysPad();
		
		println(cf.toString());
	}
	
	private LiveClip makeLiveClip(){
		LiveClip lc = new LiveClip(0, 0);
		lc.addNote(40, 0.0, 8.0, 100, 0);
		lc.addNote(43, 0.0, 8.0, 100, 0);
		lc.addNote(47, 0.0, 8.0, 100, 0);
		lc.length = 8.0;
		return lc;
	}
}
