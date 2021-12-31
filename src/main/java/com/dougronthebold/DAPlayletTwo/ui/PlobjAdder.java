package main.java.com.dougronthebold.DAPlayletTwo.ui;

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassLegato;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassRandomPitchBend;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumAddHiHat;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumAddKik;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumAddSnare;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumEmbellishKikOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumEmbellishSnareOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.Contour;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RandomBendProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RhythmBuffer;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.SloWahProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysRandomBend;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterGenerator;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterInstrumentObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterSloWahProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.resource.ChordProgression;
import test.PlobjAdderConsoleTest;

/*
 * encapsulates all the code to deal with adding PlayletObjects to the SurfaceGame
 * so that when i finally get the drag and drop functionality working, it will be easier to remove
 * and also to reduce size of SurfaceGame code
 */
public class PlobjAdder {

	public String keyInBuffer = "";
	private String[] optionArr = new String[]{
			"DI1", "DK1", "DS1", "DG", "DH1", "DKE", "DSE",		// drum stuff
			"BI1", "BP1", "BRB", "BL1", "BPS", "BPR",			// bass stuff
			"KI1", "KGP", "KRB", "KP1", 						// keys stuff
			"CP1", "RB1", "CN1",								// resource layer stuff
			"MPS", "MG", "MI"											// master channel stuff
	};
	private SurfaceGame parent;
	private PlobjAdderConsoleTest testParent;
	private boolean test = false;
	
	public PlobjAdder(SurfaceGame sg){
		parent = sg;
	}
	public PlobjAdder(PlobjAdderConsoleTest p){
		testParent = p;
		test = true;
	}
	public void testKeyInBuffer(String key){
		//testParent.println(keyInBuffer + " adding item " + key + "---------------------------------");
		keyInBuffer += key;
		//testParent.println("new keyInBuffer: " + keyInBuffer);
		int testLen = keyInBuffer.length();
		boolean isPartOfItem = false;
		boolean isWholeItem = false;
		String subStr;
		for (String str: optionArr){
			
			if (testLen > str.length()){
				subStr = str;
			} else {
				subStr = str.substring(0, testLen);
			}
//			System.out.println(str + " with testLen = " + testLen);
//			subStr = str.substring(0, testLen);
			//testParent.println(str + ", " + subStr);
			if (keyInBuffer.equals(subStr)){
				isPartOfItem = true;
				if (keyInBuffer.equals(str)){
					isWholeItem = true;
					break;
				}
			} 
		}
		if (isWholeItem){
			testAndAdd();
			keyInBuffer = "";
		} else if (!isPartOfItem){
			keyInBuffer = "";
		}
	}
	private void testAndAdd(){
		//testParent.println("keyInBuffer match: " + keyInBuffer);
		if (keyInBuffer.equals("BG1")){
			addBassGeneratorOne();
		} else if (keyInBuffer.equals("BI1")){	
			addBassInstrumentOne();
		} else if (keyInBuffer.equals("BP1")){	
			addBassProcessorOne();
		} else if (keyInBuffer.equals("BRB")){	
			addBassRandomBend();
		} else if (keyInBuffer.equals("BL1")){	
			addBassLegatoOne();
		} else if (keyInBuffer.equals("BPS")){	
			addSloWahBassProcessor();
		} else if (keyInBuffer.equals("BPR")){	
			addRandomBendBassProcessor();
		} else if (keyInBuffer.equals("KI1")){	
			addKeysInstrumentOne();
		} else if (keyInBuffer.equals("KGP")){	
			addKeysGeneratorPad();
		} else if (keyInBuffer.equals("KRB")){	
			addKeysRandomBend();
		} else if (keyInBuffer.equals("KP1")){	
			addKeysProcessorOne();
		} else if (keyInBuffer.equals("CP1")){	
			addChordProgressionOne();
		} else if (keyInBuffer.equals("RB1")){	
			addRhythmBufferOne();
		} else if (keyInBuffer.equals("CN1")){	
			addContourOne();
		} else if (keyInBuffer.equals("MPS")){	
			addMasterSloWah();
		} else if (keyInBuffer.equals("DI1")){	
			addDrumInstrument();
		} else if (keyInBuffer.equals("DK1")){	
			addDrumKik();
		} else if (keyInBuffer.equals("DS1")){	
			addDrumSnare();
		} else if (keyInBuffer.equals("DG")){	
			addDrumGenerator();
		} else if (keyInBuffer.equals("DH1")){	
			addDrumHatOne();
		} else if (keyInBuffer.equals("DKE")){	
			addDrumKikEmbellish();
		} else if (keyInBuffer.equals("DSE")){	
			addDrumSnareEmbellish();
		}
		keyInBuffer = "";
	}
    private boolean hasMasterInstrument(){
    	for (PlayletObjectInterface poi: parent.instList){
    		if (poi.type() == PlayletObject.MASTER_INSTRUMENT){
    			return true;
    		}
    	}
    	return false;
    }
    private boolean hasMasterGenerator(){
    	for (PlayletObjectInterface poi: parent.genList){
    		if (poi.type() == PlayletObject.MASTER_GENERATOR){
    			return true;
    		}
    	}
    	return false;
    }
    private void addDrumInstrument(){
    	parent.plobjToLoadList(new DrumInstrumentOne(parent.mouseX, parent.mouseY, "DI1", 0, 0, parent, parent));
    }
    private void addDrumKik(){
    	parent.plobjToLoadList(new DrumAddKik(parent.mouseX, parent.mouseY, "KIK", parent));
    }
    private void addDrumSnare(){
    	parent.plobjToLoadList(new DrumAddSnare(parent.mouseX, parent.mouseY, "SNR", parent));
    }
    private void addDrumHatOne(){
    	parent.plobjToLoadList(new DrumAddHiHat(parent.mouseX, parent.mouseY, "HAT", parent));
    }
    private void addDrumGenerator(){
    	parent.plobjToLoadList(new DrumGeneratorOne(parent.mouseX, parent.mouseY, "DrGen", parent));
    }
    private void addDrumKikEmbellish(){
    	parent.plobjToLoadList(new DrumEmbellishKikOne(parent.mouseX, parent.mouseY, "KikEmb", parent));
    }
    private void addDrumSnareEmbellish(){
    	parent.plobjToLoadList(new DrumEmbellishSnareOne(parent.mouseX, parent.mouseY, "SnrEmb", parent));
    }
    private void addBassLegatoOne(){
    	parent.plobjToLoadList(new BassLegato(parent.mouseX, parent.mouseY, "BLeg1", parent));
    }
    private void addBassRandomBend(){
    	parent.plobjToLoadList(new BassRandomPitchBend(parent.mouseX, parent.mouseY, "BRB", parent));
    }
    private void addKeysGeneratorPad(){
    	parent.plobjToLoadList(new KeysGeneratorOne(parent.mouseX, parent.mouseY, "KGP", parent));
    }
    private void addContourOne(){
    	parent.plobjToLoadList(new Contour(parent.mouseX, parent.mouseY, "CN_1", parent));
    }
    private void addRhythmBufferOne(){
    	parent.plobjToLoadList(new RhythmBuffer(parent.mouseX, parent.mouseY, "RB_1", parent));
    }
    private void addKeysProcessorOne(){
    	parent.plobjToLoadList(new KeysProcessorOne(parent.mouseX, parent.mouseY, "KP_1", parent));
    }
    private void addKeysRandomBend(){
    	parent.plobjToLoadList(new KeysRandomBend(parent.mouseX, parent.mouseY, "KRB", parent));
    }
    private void addKeysInstrumentOne(){
    	parent.plobjToLoadList(new KeysInstrumentOne(parent.mouseX, parent.mouseY, "KeysInst1", parent, parent));
    }
    private void addMasterGenerator(){
    	if (!hasMasterGenerator()){
    		parent.plobjToLoadList(new MasterGenerator(parent.mouseX, parent.mouseY, "MG", parent));
    	}
    }
    private void addMasterInstrument(){
    	if (!hasMasterInstrument()){
    		parent.plobjToLoadList(new MasterInstrumentObject(parent.mouseX, parent.mouseY, "Master", parent));
    	}
    }
    private void addMasterSloWah(){
    	parent.plobjToLoadList(new MasterSloWahProcessor(parent.mouseX, parent.mouseY, "MSloWah", parent));	  	    	
    }
    private void addBassProcessorOne(){
    	parent.plobjToLoadList(new BassProcessorOne(parent.mouseX, parent.mouseY, "BP_1", parent));
    }
    private void addSloWahBassProcessor(){
    	parent.plobjToLoadList(new SloWahProcessor(parent.mouseX, parent.mouseY, "BSloWah", parent));
    }
    private void addRandomBendBassProcessor(){
    	parent.plobjToLoadList(new RandomBendProcessor(parent.mouseX, parent.mouseY, "BSloWah", parent));
    }
    private void addChordProgressionOne(){
    	parent.plobjToLoadList(new ChordProgression(parent.mouseX, parent.mouseY, "CP_1", parent));
    }
    private void addBassInstrumentOne(){
    	parent.plobjToLoadList(new BassInstrumentOne(parent.mouseX, parent.mouseY, "BassInst1", parent, parent));
    }
    private void addBassGeneratorOne(){
    	parent.plobjToLoadList(new BassGeneratorOne(parent.mouseX, parent.mouseY, "BassGen1", parent));		// temporary
    }

}
