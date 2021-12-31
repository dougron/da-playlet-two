package main.java.com.dougronthebold.DAPlayletTwo.patching;
import java.util.ArrayList;
import java.util.Collections;

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.ui.Surface;

/*
 * stores the wires that make a channel specific Wire patching setup
 */
public class PatchObject {
	
	private int channel;
	private ArrayList<Wire> activeWireList = new ArrayList<Wire>();
	private ArrayList<Wire> activeGhostWireList = new ArrayList<Wire>();
	private ArrayList<PlayletObjectInterface> proList = new ArrayList<PlayletObjectInterface>();
	private ArrayList<PlayletObjectInterface> ghostProList = new ArrayList<PlayletObjectInterface>();
	
	private Wire shortestInstrumentWire = Surface.nullWire;
	private boolean hasInstrumentPatch = false;
	private Wire shortestGhostInstrumentWire = Surface.nullWire;
	private boolean hasGhostInstrumentPatch = false;
	
	private PlayletObjectInterface topProcessor;
	private PlayletObjectInterface topGhostProcessor;
	
	private Wire shortestGeneratorPatch = Surface.nullWire;
	private Wire shortestGhostGeneratorPatch = Surface.nullWire;
	private boolean hasGenerator = false;
	private boolean hasGhostGenerator = false;
	
	private Wire chordProgressionWire = Surface.nullWire;
	private Wire ghostChordProgressionWire = Surface.nullWire;
	private Wire rhythmBufferWire = Surface.nullWire;
	private Wire ghostRhythmBufferWire = Surface.nullWire;
	private Wire contourWire = Surface.nullWire;
	private Wire ghostContourWire = Surface.nullWire;
	
	private boolean hasChordProgression = false;
	private boolean hasGhostChordProgression = false;
	private boolean hasRhythmBuffer = false;
	private boolean hasGhostRhythmBuffer = false;
	private boolean hasContour = false;
	private boolean hasGhostContour = false;

	public PatchObject(int channel){
		this.channel = channel;
	}
//	public void testForGeneratorToInstrumentPatch(ArrayList<PlayletObjectInterface>){
//		if (!hasInstrumentPatch || !hasGhostInstrumentPatch){
//			ArrayList<Wire> generatorPatchList = new ArrayList<Wire>();
//			for
//			if (!hasInstrumentPatch) testForGeneratorInRange(generatorPatchList);
//			if (!hasGhostInstrumentPatch) testForGhostGeneratorInRange(generatorPatchList);
//		}
//		
//	}
	public void patchResources(){
		if (hasGenerator){
			doChordProgression();			
			doRhythmBuffer();
			doContour();
		}
		if (hasGhostGenerator){
			doGhostChordProgression();
			doGhostRhythmBuffer();
			doGhostContour();
		}
	}
	
	public void patchGenerators(){
		if (hasInstrumentPatch && !hasGenerator){
			for (Wire w: topProcessor.inWireList()){
				if (w.source.channel() == topProcessor.channel()){
					if (w.source.layer() == PlayletObject.GENERATOR_OBJECT){
						if (w.length() < shortestGeneratorPatch.length() && w.length() < PlayletObject.maxProcessorOutWireLength){
							hasGenerator = true;
							shortestGeneratorPatch = w;
						}
					}
				}
			}
			if (hasGenerator) activeWireList.add(shortestGeneratorPatch);
		}
		
		if (hasGhostInstrumentPatch && !hasGhostGenerator){
			for (Wire w: topGhostProcessor.inWireList()){
				if (w.source.channel() == topGhostProcessor.channel()){
					if (w.source.layer() == PlayletObject.GENERATOR_OBJECT){
						if (w.ghostLength() < shortestGhostGeneratorPatch.ghostLength() && w.ghostLength() < PlayletObject.maxProcessorOutWireLength){
							hasGhostGenerator = true;
							shortestGhostGeneratorPatch = w;
						}
					}
				}
			}
			if (hasGhostGenerator) activeGhostWireList.add(shortestGhostGeneratorPatch);
		}
		
	}
	public void patchProcessors(){
		if (hasInstrumentPatch){
			activeWireList.add(shortestInstrumentWire);
			patchFromInstrumentPatchSource();
		} else {
//			patchFromLowestProcessor();		// this could illustrate a patch even if there is not connection to an instrument. currently i'm not going to do it cos of time
		}
		if (hasGhostInstrumentPatch){
			activeGhostWireList.add(shortestGhostInstrumentWire);
			patchFromGhostInstrumentPatchSource();
		}
	}

	public void addProcessors(PlayletObjectInterface poi){
//		double poiypos = poi.guiObject().ypos();			// debugging
//		double siwypos = shortestInstrumentWire.source.guiObject().ypos();	// for debugging can be removed if yellow
		if (hasInstrumentPatch && poi.guiObject().ypos() < shortestInstrumentWire.source.guiObject().ypos()){	// checking that the object is obove the last processor on the screen
			proList.add(poi);
		}
//		double gpoiypos = poi.guiObject().ghostYpos();			// debugging
//		double gsiwypos = shortestGhostInstrumentWire.source.guiObject().ghostYpos();	// for debugging can be removed if yellow
		if (hasGhostInstrumentPatch && poi.guiObject().ghostYpos() < shortestGhostInstrumentWire.source.guiObject().ghostYpos()){
			ghostProList.add(poi);
		}
	}
	public void addInstrumentWire(Wire w){
//		double wl = w.length();		// for debugging can be removed if yellow
//		ArrayList<Wire> generatorPatchList = new ArrayList<Wire>();
		if (w.source.channel() == channel){
			if (w.source.layer() == PlayletObject.PROCESSOR_OBJECT || w.source.layer() == PlayletObject.GENERATOR_OBJECT){
				if (w.length() < shortestInstrumentWire.length() && w.length() < PlayletObject.maxWireLengthForInstruments){
					shortestInstrumentWire = w;
					shortestGeneratorPatch = Surface.nullWire;
					hasInstrumentPatch = true;
					hasGenerator = false;
					if (w.source.layer() == PlayletObject.GENERATOR_OBJECT){
						shortestGeneratorPatch = w;
						hasGenerator = true;
					}
				}
//				double wgl = w.ghostLength();		// for debugging can be removed if yellow
				if (w.ghostLength() < shortestGhostInstrumentWire.ghostLength() && w.ghostLength() < PlayletObject.maxWireLengthForInstruments){
					shortestGhostInstrumentWire = w;
					shortestGhostGeneratorPatch = Surface.nullWire;
					hasGhostInstrumentPatch = true;
					hasGhostGenerator = false;
					if (w.source.layer() == PlayletObject.GENERATOR_OBJECT){
						shortestGhostGeneratorPatch = w;
						hasGhostGenerator = true;
					}
				}
			} //else if (w.source.layer() == PlayletObject.GENERATOR_OBJECT){
//				generatorPatchList.add(w);
//			}
//			if (!hasInstrumentPatch) testForGeneratorInRange(generatorPatchList);
//			if (!hasGhostInstrumentPatch) testForGhostGeneratorInRange(generatorPatchList);
		}		
	}
	public void activateWires(){
		for (Wire w: activeWireList){
			w.setOn(true);
		}
	}
	public void activateGhostWires(){
		for (Wire w: activeGhostWireList){
			w.setGhostOn(true);
		}
	}
	
	
	public String toString(){
		String ret = "PatchObject: channel - " + channel + "\n";
		if (hasInstrumentPatch && hasGenerator){
			ret = ret + "activeWireList-------------------------\n";
			for (Wire w: activeWireList){
				ret = ret + w.toString() + "\n";
			}
			ret = ret + "particularparameters------------------------------------\n";
			ret = ret + "topProcessor : " + topProcessor.toString() + "\n";
			ret = ret + "shortestInstrumentWire: " + shortestInstrumentWire.toString() + "\n";
		} else {
			ret = ret + "no connection to instrument and/or generator";
		}
		ret = ret + "\nghost patch ------------------------\n";
		if (hasGhostInstrumentPatch && hasGhostGenerator){
			ret = ret + "activeGhostWireList-------------------------\n";
			for (Wire w: activeGhostWireList){
				ret = ret + w.toString() + "\n";
			}
			ret = ret + "particularparameters------------------------------------\n";
			ret = ret + "topGhostProcessor : " + topGhostProcessor.toString() + "\n";
			ret = ret + "shortestGhostInstrumentWire: " + shortestGhostInstrumentWire.toString() + "\n";
		} else {
			ret = ret + "no ghost connection to instrument and/or generator";
		}
		
		return ret;
	}
	
// private -----------------------------------------------------------------------
	
	private void doContour(){
		for (Wire w: shortestGeneratorPatch.source.inWireList()){
			if (w.source.type() == PlayletObject.CONTOUR && w.length() < contourWire.length()){
				contourWire = w;
				hasContour = true;
			}
		}
		if (hasContour) activeWireList.add(contourWire);
	}
	private void doGhostContour(){
		for (Wire w: shortestGhostGeneratorPatch.source.inWireList()){
			if (w.source.type() == PlayletObject.CONTOUR && w.ghostLength() < ghostContourWire.ghostLength()){
				ghostContourWire = w;
				hasGhostContour = true;
			}
		}
		if (hasGhostContour) activeGhostWireList.add(ghostContourWire);
	}
	private void doRhythmBuffer(){
		for (Wire w: shortestGeneratorPatch.source.inWireList()){
			if (w.source.type() == PlayletObject.RHYTHM_BUFFER && w.length() < rhythmBufferWire.length()){
				rhythmBufferWire = w;
				hasRhythmBuffer = true;
			}
		}
		if (hasRhythmBuffer) activeWireList.add(rhythmBufferWire);
	}
	private void doGhostRhythmBuffer(){
		for (Wire w: shortestGhostGeneratorPatch.source.inWireList()){
			if (w.source.type() == PlayletObject.RHYTHM_BUFFER && w.ghostLength() < ghostRhythmBufferWire.ghostLength()){
				ghostRhythmBufferWire = w;
				hasGhostRhythmBuffer = true;
			}
		}
		if (hasGhostRhythmBuffer) activeGhostWireList.add(ghostRhythmBufferWire);
	}
	private void doChordProgression(){
		for (Wire w: shortestGeneratorPatch.source.inWireList()){
			if (w.source.type() == PlayletObject.CHORD_PROGRESSION && w.sourceYpos() > chordProgressionWire.sourceYpos()){
				chordProgressionWire = w;
				hasChordProgression = true;
			}
		}
		if (hasChordProgression) activeWireList.add(chordProgressionWire);
	}
	private void doGhostChordProgression(){
		for (Wire w: shortestGhostGeneratorPatch.source.inWireList()){
			if (w.source.type() == PlayletObject.CHORD_PROGRESSION && w.sourceGhostYpos() > chordProgressionWire.sourceGhostYpos()){
				ghostChordProgressionWire = w;
				hasGhostChordProgression = true;
			}
		}
		if (hasGhostChordProgression) activeGhostWireList.add(ghostChordProgressionWire);
	}
	
//	private void testForGeneratorInRange(ArrayList<Wire> wList){
		// wList assumes only patch from instrument to Generator Object
		// also assumes no viable patch found to a processor
//		for (Wire w: wList){
//			if (w.length() < shortestInstrumentWire.length() && w.length() < PlayletObject.maxWireLengthForInstruments){
//				shortestInstrumentWire = w;
//				shortestGeneratorPatch = w;
//				hasInstrumentPatch = true;
//				hasGenerator = true;
//			}
//		}
//		if (hasGenerator && hasInstrumentPatch){
//			activeWireList.add(shortestInstrumentWire);
//		}
//	}
//	private void testForGhostGeneratorInRange(ArrayList<Wire> wList){
		// wList assumes only patch from instrument to Generator Object
		// also assumes no viable patch found to a processor
//		for (Wire w: wList){
//			if (w.ghostLength() < shortestInstrumentWire.ghostLength() && w.ghostLength() < PlayletObject.maxWireLengthForInstruments){
//				shortestGhostInstrumentWire = w;
//				shortestGhostGeneratorPatch = w;
//				hasGhostInstrumentPatch = true;
//				hasGhostGenerator = true;
//			}
///		}
//		if (hasGhostGenerator && hasGhostInstrumentPatch){
//			activeGhostWireList.add(shortestGhostInstrumentWire);
//		}
//	}
	
	private void patchFromInstrumentPatchSource(){
		Wire tempWire = shortestInstrumentWire;
		PlayletObjectInterface tempPoi = tempWire.source;
		
		boolean flag = true;
		while (flag){
			ArrayList<Wire> upstreamList = getUpstreamProcessorList(tempPoi);
			Collections.sort(upstreamList, Wire.sourceYposComparator);
			
			if (upstreamList.size() == 0){
				topProcessor = tempPoi;
				flag = false;
			} else {
				tempPoi = upstreamList.get(upstreamList.size() - 1).source;
				activeWireList.add(upstreamList.get(upstreamList.size() - 1));
			}
		}
	}
	private void patchFromGhostInstrumentPatchSource(){
		Wire tempWire = shortestGhostInstrumentWire;
		PlayletObjectInterface tempPoi = tempWire.source;
		
		boolean flag = true;
		while (flag){
			ArrayList<Wire> upstreamList = getUpstreamGhostProcessorList(tempPoi);
			Collections.sort(upstreamList, Wire.sourceGhostYposComparator);
			
			if (upstreamList.size() == 0){
				topGhostProcessor = tempPoi;
				flag = false;
			} else {
				tempPoi = upstreamList.get(upstreamList.size() - 1).source;
				activeGhostWireList.add(upstreamList.get(upstreamList.size() - 1));
			}
		}
	}
	private ArrayList<Wire> getUpstreamProcessorList(PlayletObjectInterface poi){
		ArrayList<Wire> wList = new ArrayList<Wire>();
		for (Wire w: poi.inWireList()){
			if (w.source.channel() == poi.channel() && w.source.layer() == PlayletObject.PROCESSOR_OBJECT){
//				double sxpos = w.source.guiObject().ypos();
//				double poixpos = poi.guiObject().ypos();
				if (w.source.guiObject().ypos() < poi.guiObject().ypos()){
					if (w.length() < PlayletObject.maxProcessorOutWireLength){
						wList.add(w);
					}
				}
			}	
		}
		return wList;
	}
	private ArrayList<Wire> getUpstreamGhostProcessorList(PlayletObjectInterface poi){
		ArrayList<Wire> wList = new ArrayList<Wire>();
		for (Wire w: poi.inWireList()){
			if (w.source.channel() == poi.channel() && w.source.layer() == PlayletObject.PROCESSOR_OBJECT){
				if (w.source.guiObject().ghostYpos() < poi.guiObject().ghostYpos()){
					if (w.ghostLength() < PlayletObject.maxProcessorOutWireLength){
						wList.add(w);
					}
				}
			}	
		}
		return wList;
	}
	
	
}
