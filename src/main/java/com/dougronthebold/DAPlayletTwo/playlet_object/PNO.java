package main.java.com.dougronthebold.DAPlayletTwo.playlet_object;
import PipelineUtils.PipelineNoteList;
import PipelineUtils.PlayPlugArgument;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.StaticNumber;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;

/*
 * Wrapper for the PipelineNoteList for use in the Playlet. 
 * 
 */
public class PNO {
	
	private int copyOf;
	private int unitNumber;
	private String renderStory;
	private PipelineNoteList pnl;
	public Wire lastWire;
//	public RandomNumberSequence rnd;
	
//	public ChordForm cf;		// this is to allow a Generator object to pass the ChordForm it used to generate the clip
								// down the processing chain. Currently a stop gap measureto solve the DiatonicEmbellishment
								// problem of no ChordForm attached to processing units.....
//	public TwoBarRhythmBuffer rb;	// as above except the problem arose with the PlugInKikFromRhythmBuffer plugin in
									// a DRUM_PROCESSOR module
	public PlayPlugArgument ppa;
		
	public PNO(){
		unitNumber = StaticNumber.nextInt();
		renderStory = "blank PNO " + unitNumber;
	}
	public PNO(int copy, String oldRenderStory){
		copyOf = copy;
		unitNumber = StaticNumber.nextInt();
		renderStory = oldRenderStory + "\nPNO " + unitNumber + " copy of " + copyOf;
	}
	public PNO copy(){
		// note: this returns a new instance of PNO, but wraps the original PipelineNoteList.
		// this is because initially it seems to be too much of a ballache to make a deepcopy of 
		// a PipelineNoteList, and also does not seem to be any point other than the idea that 
		// a Playlet rendering chain can be re-rendered from the middle using previously generated 
		// data, a classic case of me outhinking myself 
		PNO copy = new PNO(unitNumber, renderStory);
		copy.setPNL(pnl);
		copy.ppa = ppa;
//		if (cf != null) copy.cf = cf;
//		if (rb != null) copy.rb = rb;
//		if (rnd != null) copy.rnd = rnd;
 		return copy;
	}
	public void addToStory(String str){
		renderStory += str;
	}
	public String renderStoryToString(){
		return renderStory;
	}
	public void setPNL(PipelineNoteList pnl){
		this.pnl = pnl;
	}
	public PipelineNoteList pnl(){
		return pnl;
	}
	public String toString(){
		return renderStoryToString() + "\n" + pnl.toString() + "\nnoteCount: " + pnl.pnoList.size();
	}
	public void setLastWire(Wire w){
		lastWire = w;
	}
}
