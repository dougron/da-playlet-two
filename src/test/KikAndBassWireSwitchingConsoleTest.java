package test;
import java.util.ArrayList;

import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.PatchingManager;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumAddKik;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysInstrumentOne;

public class KikAndBassWireSwitchingConsoleTest extends ConsoleProgram implements ConsoleInterface, PlobjParent{

	ArrayList<Wire> wList = new ArrayList<Wire>();
	
	ArrayList<PlayletObjectInterface> instList = new ArrayList<PlayletObjectInterface>();
	ArrayList<PlayletObjectInterface> proList = new ArrayList<PlayletObjectInterface>();
	ArrayList<PlayletObjectInterface> genList = new ArrayList<PlayletObjectInterface>();
	ArrayList<PlayletObjectInterface> bigList = new ArrayList<PlayletObjectInterface>();
	
	public void run(){
		setSize(700, 700);
		makePOILists();
		postStatus();
		PatchingManager pm = new PatchingManager();
		// calls to do the wire switching, may make it into one call
		pm.repatch(instList, proList);
		println("\n\n\n" + pm.toString());
	}
// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
// privates --------------------------------------------------------------------	
	private void makePOILists(){
		addPOI(new BassInstrumentOne(50, 50, "BassInst2", 2, 0, this, this));
		addPOI(new DrumInstrumentOne(0, 50, "DRUM", 0, 0, this, this));
		addPOI(new DrumAddKik(0, 25, "KIK", this));
		addPOI(new DrumGeneratorOne(0, 0, "DG_1", this));
		addPOI(new KeysInstrumentOne(60, 50, "KI1", 2, 0, this, this));
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
}
