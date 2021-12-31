package main.java.com.dougronthebold.DAPlayletTwo.interfaces;

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.Contour;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RhythmBuffer;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.resource.ChordProgression;

/*
 * generator creates a PipelineNoteObject (or equivalent) and sends it through its PipelineList
 */
public class GeneratorOLD {

	private ChordProgression cp;
	private RhythmBuffer rb;
	private Contour cn;
	
	private boolean connectToCP = true;
	private boolean connectToRB = true;
	private boolean connectToCN = true;
	
	public GeneratorOLD(){
		
	}
	public void setChordProgression(ChordProgression cp){
		this.cp = cp;
	}
	public void setRhythmBuffer(RhythmBuffer rb){
		this.rb = rb;
	}
	public void setContour(Contour cn){
		this.cn = cn;
	}
}
