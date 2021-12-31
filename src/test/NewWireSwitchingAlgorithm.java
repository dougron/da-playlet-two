package test;
import java.util.ArrayList;

import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.PatchingManager;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.Contour;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RhythmBuffer;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysProcessorTwo;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.resource.ChordProgression;

public class NewWireSwitchingAlgorithm extends ConsoleProgram implements ConsoleInterface, PlobjParent{
	
	ArrayList<Wire> wList = new ArrayList<Wire>();
	
	ArrayList<PlayletObjectInterface> instList = new ArrayList<PlayletObjectInterface>();
	ArrayList<PlayletObjectInterface> proList = new ArrayList<PlayletObjectInterface>();
	ArrayList<PlayletObjectInterface> genList = new ArrayList<PlayletObjectInterface>();
	ArrayList<PlayletObjectInterface> bigList = new ArrayList<PlayletObjectInterface>();
	KeysGeneratorOne kg1 = new KeysGeneratorOne(0, 10, "KeysGenPad", this);
	KeysProcessorOne kp1 = new KeysProcessorOne(0, 20, "KP1", this);
	KeysProcessorTwo kp2 = new KeysProcessorTwo(0, 30, "KP2", this);							//out
	KeysInstrumentOne ki1 = new KeysInstrumentOne(0, 40, "KI1", 2, 0, this, this);
	ChordProgression cp1 = new ChordProgression(0, 0, "CP_1", this);
	RhythmBuffer rb1 = new RhythmBuffer(10, 0, "RB_1", this);
	Contour cn1 = new Contour(10, 0, "CN_1", this);

	public void run(){
		setSize(700, 700);
		makePOILists();
		addGhostsAndStuff();
		postStatus();
		PatchingManager pm = new PatchingManager();
		// calls to do the wire switching, may make it into one call
		pm.repatch(instList, proList);
//		pm.addProcessors(proList);
//		pm.addGenerators();
//		pm.patchToResourceLayer();
//		pm.activateWires();
		// -------------------------------
		println("\n\n\n" + pm.toString());
 	}
	
	private void addGhostsAndStuff(){
		kp1.guiObject().setHasGhost(true);
		kp1.guiObject().setGhostPos(0.0, 36.0);
		kp2.guiObject().setHasGhost(true);
		kp2.guiObject().setGhostPos(0.0, 25.0);
		cp1.guiObject().setHasGhost(true);
		cp1.guiObject().setGhostPos(0.0, 8.0);
		rb1.guiObject().setHasGhost(true);
		rb1.guiObject().setGhostPos(8.0, 8.0);
		cn1.guiObject().setHasGhost(true);
		cn1.guiObject().setGhostPos(8.0, 8.0);
	}
	
	private void makePOILists(){
		addPOI(ki1);
		addPOI(kp1);
		addPOI(kp2);
		addPOI(kg1);
    	addPOI(cp1);
    	addPOI(new ChordProgression(0, 5, "CP_2", this));
    	addPOI(new RhythmBuffer(0, 0, "RB_2", this));
    	addPOI(rb1);
    	addPOI(new Contour(0, 0, "CN_2", this));
    	addPOI(cn1);
	}
	private void addPOI(PlayletObjectInterface poi){
		addPlobjToTheRightList(poi);
		addWires(poi);
		bigList.add(poi);
	}
	private void addPlobjToTheRightList(PlayletObjectInterface plobj){
		if (plobj.layer() == PlayletObject.RESOURCE_OBJECT){
			//resList.add(plobj);
		} else if (plobj.layer() == PlayletObject.GENERATOR_OBJECT){
			genList.add(plobj);
		} else if (plobj.layer() == PlayletObject.PROCESSOR_OBJECT){
			proList.add(plobj);
		} else if (plobj.layer() == PlayletObject.INSTRUMENT_OBJECT){
			instList.add(plobj);
		}
	}	
	private void addWires(PlayletObjectInterface plobj){
		int[] connArr = PlayletObject.connList(plobj.layer());
		for (PlayletObjectInterface poi: bigList){
			if (isInArray(poi.layer(), connArr)){
				wList.add(new Wire(plobj, poi));
			}
			if (isInArray(plobj.layer(), PlayletObject.connList(poi.layer()))){
				wList.add(new Wire(poi, plobj));
			}
		}
	}
	private boolean isInArray(int i, int[] arr){
		for (int test: arr){
			if (i == test){
				return true;
			}
		}
		return false;
	}

	private void switchOffAllWires(){
		for (Wire w: wList){
			w.setOn(false);
		}
	}
//	private void addWires(){
//		wList.add(new Wire(kg1, kp1));
//		wList.add(new Wire(kg1, kp2));
//		wList.add(new Wire(kg1, ki1));
//		
//		wList.add(new Wire(kp1, kp2));
//		wList.add(new Wire(kp2, kp1));
//		wList.add(new Wire(kp1, ki1));
//		wList.add(new Wire(kp2, ki1));
//	}
	private void postStatus(){
		postPOIArr(genList, "genList");
		postPOIArr(proList, "proList");
		postPOIArr(instList, "instList");
		postWires();
	}
	private void postWires(){
		println("wireList--------------------------------");
		for (Wire w: wList){
			println(w.toString());
		}
	}
	private void postPOIArr(ArrayList<PlayletObjectInterface> pArr, String name){
		println(name + "----------------------------------------");
		for (PlayletObjectInterface poi: pArr){
			println(poi.toString());
		}
	}
	public void consolePrint(String str){
		
	}
	// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
}
