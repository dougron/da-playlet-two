package test;

import ChordScaleDictionary.ChordScaleDictionary;
import DataObjects.ableton_live_clip.LiveClip;
import ResourceUtils.ChordForm;
import acm.program.ConsoleProgram;

public class NullKeyErrorConsoleTest extends ConsoleProgram{

	
	public void run(){
		setSize(700, 700);
		ChordScaleDictionary csd = new ChordScaleDictionary();
		LiveClip lc = makeClip();
		println(lc.toString() + "\n-----------------------------------------");
		ChordForm cf = new ChordForm(lc);
		println(cf.toString() + "\n------------------------------------------");
//		ChordAnalysisObject cao = cf.getPrevailingChordAnalysisObject(0.0, 0.0);
//		println(cao.toString());
//		for (double d = 0.0; d < 16.0; d++){
//			println(d + ": " + cf.getPrevailingChordAnalysisObject(d, 0.0).toString());
//		}
	}
	
	private LiveClip makeClip(){
		LiveClip lc = new LiveClip(0, 0);
		lc.addNote(60, 0.0, 1.0, 100, 0);
		lc.addNote(64, 0.0, 1.0, 100, 0);
		lc.addNote(67, 0.0, 1.0, 100, 0);
		lc.length = 8.0;
		lc.setLoopStart(0.0);
		lc.setLoopEnd(8.0);
		return lc;
	}
}
