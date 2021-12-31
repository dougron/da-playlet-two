package main.java.com.dougronthebold.DAPlayletTwo.patching;
import java.util.ArrayList;
import java.util.HashMap;
/*
 * current logic for patching:
 * 
 * of all the instruments in a given channel, only the one with the closest to a processor or generator will connect
 * 
 * processors will connect to the processor next highest on the screen, unless the 
 * wire is too long, in which case it will take the next highest, until such time as there are no 
 * more higher processors. that will be stored as topProcessor and topGhostProcessor
 * 
 * the top processor will attach to the closest (x and y considered) generator this will be 
 * labeled activeGenerator and activeGhostGenerator
 * 
 * the activeGenerator will attached to the closest RythmBuffer and Contour modules, but the 
 * lowest onscreen ChordProgression
 * 
 */

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class PatchingManager {

	public HashMap<Integer, PatchObject> patchMap;
	
	public PatchingManager(){
		
	}
	public void repatch(ArrayList<PlayletObjectInterface> instList, ArrayList<PlayletObjectInterface> proList){
		addInstruments(instList);
		addProcessors(proList);
		addGenerators();
		patchToResourceLayer();
		activateWires();
	}
	public void addInstruments(ArrayList<PlayletObjectInterface> instList){
		// this is step one and reinitializes everything in the manager
		makePatchMap(instList);
		populatePatchMapWithInstrumentPatches(instList);
		
	}
//	public void connectInstrumentToGeneratorIfNeccesssary(){
//		for (Integer i: patchMap.keySet()){
//			patchMap.get(i).checkForInstrumentToGeneratorPatch();
//		}
//	}
	public void addProcessors(ArrayList<PlayletObjectInterface> proList){
		// step two...........................................................
		for (PlayletObjectInterface poi: proList){
			if (patchMap.containsKey(poi.channel())){
				patchMap.get(poi.channel()).addProcessors(poi);
			}
		}
		try {			// for debugging
			for (Integer i: patchMap.keySet()){
				patchMap.get(i).patchProcessors();
			}
		} catch (Exception ex){
			System.out.println("ex caught.....");
		}
		
	}
	public void addGenerators(){
		// step three...............................................
		
		for (Integer i: patchMap.keySet()){
//			System.out.println("\nPatching Manager: key " + i + " in method addGenerators");
//			System.out.println(patchMap.get(i).toString());
			patchMap.get(i).patchGenerators();
		}
	}
	public void patchToResourceLayer(){
		// step four ......................................................
		for (Integer i: patchMap.keySet()){
			patchMap.get(i).patchResources();
		}
	}
	
	public void activateWires(){
		// last step ........................................................
		for (Integer i: patchMap.keySet()){
			patchMap.get(i).activateWires();
			patchMap.get(i).activateGhostWires();
		}
	}
	
// privates ----------------------------------------------------------------------------------------
//	private void testForGeneratorToInstrumentPatch(){
//		for (Integer index: patchMap.keySet()){
//			patchMap.get(index).testForGeneratorToInstrumentPatch();
//			
//		}
//	}
	private void populatePatchMapWithInstrumentPatches(ArrayList<PlayletObjectInterface> instList){
		for (PlayletObjectInterface poi: instList){
			for (Wire w: poi.inWireList()){
				//System.out.println(toString() + "\n looking for index :" + poi.channel());
				patchMap.get(poi.channel()).addInstrumentWire(w);
			}
		}
	}
	
	private void makePatchMap(ArrayList<PlayletObjectInterface> instList){
		patchMap = new HashMap<Integer, PatchObject>();
		for (PlayletObjectInterface poi: instList){
			if (!patchMap.containsKey(poi.channel())){
				patchMap.put(poi.channel(), new PatchObject(poi.channel()));
			}
		}
	}
	
	public String toString(){
		String ret = "PatchingManager patchMap\n";
		for (Integer i: patchMap.keySet()){
			ret = ret + "patchMap key: " + i + "\n";
			ret = ret + patchMap.get(i).toString();
		}
		return ret;
	}
}
