package main.java.com.dougronthebold.DAPlayletTwo.playlet_plugin;
import PipelineUtils.PipelineNoteList;
import PipelineUtils.PipelineNoteObject;
import PipelineUtils.PlayPlugArgument;
import ResourceUtils.ChordForm;
import ResourceUtils.TwoBarRhythmBuffer;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;

public class PlayPlugBassFromRB implements PlayletPlugIn{

	private String name = "Ba_RB";			// abrreviated name for GUI space issue
	private int sortIndex = 0;
	private int inputSort = 0;
	private int zone = 0;
	private boolean canDouble = false;
	private int active = 1;
	private double default_rhythmBuffer_loopLength = 32.0;
	private static final int[] loBassRegister = new int[]{36, 55};
	private static final int default_velocity = 100;
	private static final double default_noteLength = 0.25;
	private static final int[] dynamicVelocity = new int[]{40, 65, 90, 127}; 
//	private TwoBarRhythmBuffer rb;
//	private ChordForm form;

	
	public PlayPlugBassFromRB(){		
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		if (active > 0){
//			MaxObject.post("PlugInBassFromRhythmBuffer processed as active");
			pnl.length = ppa.cf.length();
			TwoBarRhythmBuffer rb = ppa.rb;
			double formLength = pnl.length;		//ro.cp.formLength;		
			ChordForm form = ppa.cf;

			addRhythmPositions(formLength, rb, pnl);
			addChordAnalysis(pnl, form);
			addNotes(pnl);
			doVelocity(pnl, ppa);
		}
		
	}
	public String name(){
		return name;
	}

	public int active(){
		return active;
	}
	public void setActive(int i){
		active = i;
	}
	
// privates -----------------------------------------------------------------------------
	private void doVelocity(PipelineNoteList pnl, PlayPlugArgument ppa){		
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.setFixedVelocity(dynamicVelocity[PlayletObject.dynamic]);
		}
	}
	private void addNotes(PipelineNoteList pnl){
		for (PipelineNoteObject pno: pnl.pnoList){
			pno.addNote(closestNoteInRegister(pno, loBassRegister));
		}
	}
	private int closestNoteInRegister(PipelineNoteObject pno, int[] register){
		int note = pno.cao.simpleRoot();
		while (note < register[0]){
			note += 12;
		}
		return note;
	}
	
	private void addChordAnalysis(PipelineNoteList pnl, ChordForm form){
		for (PipelineNoteObject pno: pnl.pnoList){
			// commented out as all stuff related to the old ChordScaleDictionary is buggered
//			pno.cao = form.getPrevailingChordAnalysisObject(pno.position, 0.0);		//0.0 - injectDelay, in this case 0.0
		}
	}
	
	private void addRhythmPositions(double formLength, TwoBarRhythmBuffer rb, PipelineNoteList pnl){
		double inc = 0.0;
		boolean flag = true;
		while (flag){
			for (int i = 0; i < rb.buffy.length; i++){
				double pos = (double)i * 0.25;
//				MaxObject.post("i: " + i + " pos: " + pos + " inc: " + inc);
				if (pos + inc >= formLength) {
//					MaxObject.post("PlugInBassFromRhythmBuffer:break off addRhythmPositions with pos + inc = " + pos + inc + " formLength = " + formLength);
					flag = false;
					break;
				}
				if (rb.buffy[i] == 1 && flag){
//					MaxObject.post("PlugInBassFromRhythmBuffer: adding PipelineNoteObject at pos + inc = " + pos + inc);
					pnl.addNoteObject(pos + inc, true, true);
				}
			}
			inc += 8.0;				// magic number = 2 bars, need to chenge when refactoring for other time signatures
		}
	}
}
