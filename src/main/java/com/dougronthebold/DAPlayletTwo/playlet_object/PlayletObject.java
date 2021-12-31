package main.java.com.dougronthebold.DAPlayletTwo.playlet_object;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.cycling74.max.Atom;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import DataObjects.ableton_device_control_utils.controller.ControllerInfo;
import DataObjects.ableton_live_clip.LiveClip;
import DataObjects.ableton_live_clip.LiveMidiNote;
import DataObjects.ableton_live_clip.controller_data_clips.ControllerClip;
import DataObjects.ableton_live_clip.controller_data_clips.FunctionBreakPoint;
import DataObjects.ableton_live_clip.controller_data_clips.PitchBendClip;
import LegacyStuff.ChordProgrammer2;
import LegacyStuff.RhythmBufferGUIController;
import PipelineUtils.PipelineNoteList;
import PipelineUtils.PlayPlugArgument;
import ResourceUtils.ChordForm;
import ResourceUtils.ContourData;
import ResourceUtils.ContourPack;
import ResourceUtils.ResourceObject;
import ResourceUtils.ResourceObjectParent;
import ResourceUtils.TwoBarRhythmBuffer;
import UDPUtils.OSCMessMaker;
import UDPUtils.UDPConnection;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.ButtonInstruction;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.GUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.HeightBehaviour;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.RoundRectangleGUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.RoundedTriangleGUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.TriangleGUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.StaticNumber;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;



public class PlayletObject implements PlayletObjectInterface, ResourceObjectParent{
	
	public GUIObject guiObject;
	private int defaultHeight = 10;
	public int type;
	public int debugNumber;
	private String uniqueName;
	private ArrayList<Wire> inWireList = new ArrayList<Wire>();
	private ArrayList<Wire> outWireList = new ArrayList<Wire>();
	public ArrayList<ControllerInfo> controllerList = new ArrayList<ControllerInfo>();
	public PNO receivedPNO;
	public PNO processedPNO;
	public static PNO nullPNO = new PNO();
	public static UDPConnection conn = new UDPConnection(7800);
	private ResourceObject ro = new ResourceObject(this); 	// potentially redundant
	public static int dynamic = 1;
	public static int renderNumber = -1;
	public PlayPlugArgument ppa = new PlayPlugArgument();
//	private RandomNumberSequence rnd = new RandomNumberSequence(16, 2);
	
	public static double maxProcessorOutWireLength = 300;
	public static double maxWireLengthForInstruments = 300;
	
	private int clipObjectIndex = -1;	// currently (aug2015) obsolete.
	private int trackIndex = -1;
	private int clipIndex = -1;
	private int trackType = 0;
	public PlobjParent parent;
	
	public static double barLength = 4.0;	// default is 4/4 time	

	public PlayletObject(int shape, int type, double x, double y, String uniqueName, PlobjParent pp){
		debugNumber = StaticNumber.nextInt();
		if (shape == ROUND_RECTANGLE){
			guiObject = new RoundRectangleGUIObject(this);
		} else if (shape == HEXAGON){
//			guiObject = new HexagonGUIObject(this);
		} else if (shape == TRIANGLE){
			guiObject = new TriangleGUIObject(this);
		} else if (shape == ROUND_TRIANGLE){
			guiObject = new RoundedTriangleGUIObject(this);
		}
		this.uniqueName = uniqueName;
		guiObject.setPos(x, y);	
		guiObject.setInitialHeight(defaultHeight);
		this.type = type;
		if (layer() == INSTRUMENT_OBJECT) guiObject.setHeightBehaviour(HeightBehaviour.INSTRUMENT_BEHAVIOUR);
//		ppa.rnd = rnd;
		nullPNO.setPNL(new PipelineNoteList(0));
		parent = pp;
	}
	public PlayletObject(double x, double y){			// null object
		// this instantiator does not give values to all the instance variables. may cause kak at some point.......
		guiObject = new GUIObject(x, y);				// this instantiator does not give values to all the instance variables. may cause kak at some point.......
	}
	public String toString(){
		String inWireInfo = inWireList.size() + " inputs";
		String outWireInfo = outWireList.size() + " outputs";
		return name[type] + layerName[layer()] + uniqueName + " debug_" + debugNumber + " " + inWireInfo + " " + outWireInfo;
	}
	public String nameToString(){
		return name[type] + layerName[layer()] + uniqueName;
	}
	public String guiObjectToString(){
		return guiObject.toString();
	}
	public int layer(){
		return layerArr[type];
	}
	public int type(){
		return type;
	}
	public void setTrackType(int i){
		trackType = i;
	}
	public int getTrackType(){
		return trackType;
	}
	public int channel(){
		return channel[type];
	}
	public GUIObject guiObject(){
		return guiObject;
	}
	public static int[] connList(int layer){
		return connectionArray[layer];
	}
	public void addInWire(Wire w){
		inWireList.add(w);
	}
	public void addOutWire(Wire w){
		outWireList.add(w);
	}
	public ArrayList<Wire> inWireList(){
		return inWireList;
	}
	public ArrayList<Wire> outWireList(){
		return outWireList;
	}
	public void setAllOutWires(boolean b){
		for (Wire w: outWireList){
			w.setOn(b);
		}
	}
	public boolean hasOutputPatch(){
		for (Wire w: outWireList){
			if (w.on){
				return true;
			}
		}
		return false;
	}
	public boolean hasGhostOutputPatch(){
		if (guiObject().hasGhost){
			for (Wire w: outWireList){
				if (w.ghostOn){
					return true;
				}
			}
			return false;
		} else {
			return hasOutputPatch();
		}
		
	}
	public void setAllInWires(boolean b){
		for (Wire w: inWireList){
			w.setOn(b);
		}
	}
	public PNO receivedPNO(){
		return receivedPNO;
	}
	public PNO processedPNO(){
		return processedPNO;
	}

	public void in(PNO pno){
		// default for testing. will need to be overwritten in each individual item
		receivedPNO = pno;
		processedPNO = process(pno);
//		rnd.reset();
		sendPNOCopyToOuts(processedPNO);
	}
	public void render(){
		receivedPNO = makePNO();
		processedPNO = process(receivedPNO);
		sendPNOCopyToOuts(processedPNO);
	}
	public String recievedPNORenderStory(){
		if (receivedPNO != null){
			return receivedPNO.renderStoryToString();
		} else {
			return "no inputs. no render story :(";
		}
	}
	public String processedPNORenderStory(){
		if (processedPNO != null){
			return processedPNO.renderStoryToString();
		} else {
			return "no inputs. no render story :(";
		}
	}
	public void sendMessageToLive(OSCMessMaker mm){
		conn.sendUDPMessage(mm);
	}
	public void setChordProgrammer(ChordProgrammer2 cp){
		ro.cp = cp;
	}
	public void setChordForm(ChordForm cf){
		ppa.cf = cf;
	}
	public void setRhythmBuffer(TwoBarRhythmBuffer rb){
		ppa.rb = rb;
	}
	public void setRhythmBuffer(RhythmBufferGUIController rbgc){
		ro.rbgc = rbgc;
	}
	public void setContour(ContourData cd){
		ppa.cd = cd;
	}
	public PlayPlugArgument getPPA(){
		return ppa;
	}
	public static void setDynamic(int d){
		dynamic = d;
	}
	public static int nextRenderNumber(){
		renderNumber++;
		return renderNumber;
	}
	public void setClipObjectIndex(int i){
		clipObjectIndex = i;
	}
	public int getClipObjectIndex(){
		return clipObjectIndex;
	}
	public void setTrackIndex(int i){
		trackIndex = i;
		setControllerTrackIndices(i);
	}
	public void setClipIndex(int i){
		clipIndex = i;
	}
	public void setTrackClipIndex(int t, int c){
		trackIndex = t;
		clipIndex = c;
		setControllerTrackIndices(t);
	}
	public int getTrackIndex(){
		return trackIndex;
	}
	public int getClipIndex(){
		return clipIndex;
	}
	public String uniqueName(){
		return uniqueName;
	}
	public boolean hasNoIns(){
		// used as a test to see if an instrument needs to send a null clip
		for (Wire w: inWireList){
			if (w.on){
				return false;
			}
		}
		return true;
	}
	public boolean hasNoGhostIns(){
		// used to see if the instrument needs to bother about checking for ghosts
		for (Wire w: inWireList){
			if (w.ghostOn){
				return false;
			}
		}
		return true;
	}
//	public boolean hasContour(){
//		for (Wire w: inWireList){
//			if (w.on && w.source.type() == CONTOUR){
//				return true;
//			}
//		}
//		return false;
//	}
//	public boolean hasChordProgression(){
//		for (Wire w: inWireList){
//			if (w.on && w.source.type() == CHORD_PROGRESSION){
//				return true;
//			}
//		}
//		return false;
//	}
	public ArrayList<ControllerInfo> controllerList(){
		return controllerList;
	}
	public PlobjParent parent(){
		return parent;
	}
	public void setOn(boolean b){
		// for overriding in children......
	}
	public ContourPack getContourPack(){
		// for overriding in dependants..........
		return new ContourPack();	// this is null object
	}
	public void newRNDSequence(){
		// for overiding in classes that setup the RandomNumberSequence object for a processing chain, like Generators....S
	}
	public void doButtonInstruction(ButtonInstruction bi){
		// for overiding. may end up with some universal stuff in here, like on/off etc
		// but for now its all uniqure to the implementing class
	}
//	public void incrementHeight(int i){		// this always done directly via guiObject()
//		guiObject.incrementHeight(i);
//	}
// stuff for extended classes only...............
	public ChordForm getChordFormFromResourceLayer(){
		for (Wire w: inWireList()){
			if (w.on && w.source.type() == PlayletObject.CHORD_PROGRESSION){
				//System.out.println(uniqueName + " has found a chord progressioN");
				return w.source.getPPA().cf;
			}
		}
		return new ChordForm(new LiveClip(0, 0));
	}
	public TwoBarRhythmBuffer getRhythmBufferFromResourceLayer(){
		for (Wire w: inWireList()){
			if (w.on && w.source.type() == PlayletObject.RHYTHM_BUFFER){
				return w.source.getPPA().rb;
			}
		}
		return new TwoBarRhythmBuffer();
	}
	public ContourPack getContourPackFromResourceLayer(){
		for (Wire w: inWireList()){
			if (w.on && w.source.type() == PlayletObject.CONTOUR){
				return w.source.getContourPack();
			}
		}
		return new ContourPack();
	}
//	public PlayletObjectInterface getContourFromResourceLayer(){
//		for (Wire w: inWireList()){
//			if (w.on && w.source.type() == PlayletObject.CONTOUR){
//				return w.source;
//			}
//		}
//		return null;
//	}
	public void makePlayPlugArgument(){
		ppa = new PlayPlugArgument();
		ppa.cf = getChordFormFromResourceLayer();
		ppa.rb = getRhythmBufferFromResourceLayer();
		ppa.cp = getContourPackFromResourceLayer();
		ppa.dynamic = dynamic;
	}
	
// generic in method options ----------------------------------------------------------
	public void instrumentIn(PNO pno, String name){
//		System.out.println(pno.toString() + " rendered");
		receivedPNO = pno;
		OSCMessMaker mess;
		if (trackType == DeviceParamInfo.ofTrackType){
			LiveClip lc = pno.pnl().makeLiveClip();
			lc.name = name + PlayletObject.nextRenderNumber();
			lc.clipObjectIndex = getClipObjectIndex();
			mess = new OSCMessMaker();			
			mess.addItem(DeviceParamInfo.injectMessage);
			addLiveClipToOSCMessage(lc, mess);
			conn.sendUDPMessage(mess);
		}		
		sendControllerMessages(pno.pnl().cList);
		
		mess = new OSCMessMaker();
		mess.addItem(DeviceParamInfo.injectMessage);
		if (trackType == DeviceParamInfo.ofTrackType){
			addPitchBendClipToOSCMessage(pno.pnl().pb, mess);
			conn.sendUDPMessage(mess);
		}
		
	}
//	public void processorIn(PNO pno){
//		not yet.......
//	}
// UDP message utils -------------------------------------------------------------------
	public void instrumentInitializationMessage(OSCMessMaker mess){
		mess.addItem(DeviceParamInfo.newClipObjectString);
		mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		if (trackType == DeviceParamInfo.ofMasterTrackType){
			mess.addItem(DeviceParamInfo.masterTrackString);
		} else {
			mess.addItem(getTrackIndex());			
		}
		mess.addItem(getClipIndex());
	}
	private void sendControllerMessages(ArrayList<ControllerClip> cList){
		OSCMessMaker mess;
		ArrayList<String> hasList = new ArrayList<String>();
		if (cList.size() > 0){
			for (ControllerClip cc: cList){
				mess = new OSCMessMaker();
				mess.addItem(DeviceParamInfo.injectMessage);
				addControllerClipToOSCMess(cc, mess);
				conn.sendUDPMessage(mess);
				hasList.add(cc.name);
			}
		}
		
		for (ControllerInfo ci: controllerList){
			if (!hasList.contains(ci.name())){
				mess = ci.makeOffOSCMessage();
				conn.sendUDPMessage(mess);
			}
		}
	}
	public void addControllerClipsToOSCMessage(ArrayList<ControllerClip> cList, OSCMessMaker mess){
		ArrayList<Integer> indexList = new ArrayList<Integer>();
 		if (cList.size() > 0){
			for (ControllerClip cc: cList){
				addControllerClipToOSCMess(cc, mess);
				indexList.add(cc.controllerIndex);
			}
		}
 		// controller off messages
 //		for (ControllerInfo ci: controllerList){
 //			if (!indexList.contains(ci.controllerIndex())){
 //				ci.addOffMessageToOSC(mess, clipObjectIndex);
 //			}
 //		}
 		
	}

	private void addControllerClipToOSCMess(ControllerClip cc, OSCMessMaker mess){
		mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		if (trackType == DeviceParamInfo.ofMasterTrackType){
			mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);		
		} else {
			mess.addItem(trackIndex);
		}
		mess.addItem(DeviceParamInfo.controllerMessage);
//		mess.addItem(cc.controllerIndex);
		mess.addItem(cc.name);
		mess.addItem(cc.onOff);
		mess.addItem(cc.offValue);
		mess.addItem(cc.length);
		mess.addItem(cc.offset);
		mess.addItem(cc.resolution);
		for (FunctionBreakPoint fbp: cc.fbpList){
			mess.addItem(fbp.position);
			mess.addItem(fbp.value);
		}
		
				
	}
	public void addPitchBendClipToOSCMessage(PitchBendClip pb, OSCMessMaker mess){
		if (pb == null){
			mess.addItem(DeviceParamInfo.pitchbendMessage);
			mess.addItem(trackIndex);			// clipObjectIndex is of no use to the pitch bend unit as it is on the actual track that the virtual instrument is on in Live
			mess.addItem(0);			// this is an off message 0 means off.
			mess.addItem(DeviceParamInfo.defaultPitchBendOffValue);
		} else {
			mess.addItem(DeviceParamInfo.pitchbendMessage);
			mess.addItem(trackIndex);
			mess.addItem(pb.onOff);
			mess.addItem(pb.offValue);
			mess.addItem(pb.length);
			mess.addItem(pb.offset);
			mess.addItem(pb.resolution);
			mess.addItem(pb.pitchBendRange);
			for (FunctionBreakPoint fbp: pb.fbpList){
				mess.addItem(fbp.position);
				mess.addItem(fbp.value);
			}
		}
		
	}
	public void addLiveClipToOSCMessage(LiveClip lc, OSCMessMaker mess){	//
		// format for injection, makes 'notes' and 'param' messages.....
		// live clip always gets a param message, but param messages can be sent on their own as well

		// notes message
		mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		mess.addItem(trackIndex);
		mess.addItem(DeviceParamInfo.notesMessage);
		addLCNotesToOSCMess(lc, mess);
		mess.addItem(lc.noteList.size());		
		// param message
		mess.addItem(DeviceParamInfo.paramMessage);
		paramListForNotesMessageToOSCMessage(lc, mess);

	}
	private void addLCNotesToOSCMess(LiveClip lc, OSCMessMaker mess){		// for inject
		for (LiveMidiNote lmn: lc.noteList){
			mess.addItem(lmn.note);
			mess.addItem(lmn.position);
			mess.addItem(lmn.length);
			mess.addItem(lmn.velocity);
			mess.addItem(lmn.mute);
		}
	}
	private void paramListForNotesMessageToOSCMessage(LiveClip lc, OSCMessMaker mess){

		mess.addItem(lc.length);
		mess.addItem(lc.loopStart);
		mess.addItem(lc.loopEnd);
		mess.addItem(lc.startMarker);
		mess.addItem(lc.endMarker);
		mess.addItem(lc.signatureNumerator);
		mess.addItem(lc.signatureDenominator);
		mess.addItem(lc.offset);
		mess.addItem(lc.clip);
		mess.addItem(lc.track);
		mess.addItem(lc.name);
	}
	private void paramListStandAloneToOSCMessage(LiveClip lc, OSCMessMaker mess){
		mess.addItem(DeviceParamInfo.trackTypeArr[trackType]);
		mess.addItem(trackIndex); 
		mess.addItem(lc.length);
		mess.addItem(lc.loopStart);
		mess.addItem(lc.loopEnd);
		mess.addItem(lc.startMarker);
		mess.addItem(lc.endMarker);
		mess.addItem(lc.signatureNumerator);
		mess.addItem(lc.signatureDenominator);
		mess.addItem(lc.offset);
		mess.addItem(lc.clip);
		mess.addItem(lc.track);
		mess.addItem(lc.name);
	}
	

	
// ResourceObjectParent methods --------------------------------------------------------
	
	public void sendRhythmBufferMessage(Atom[] atArr){
		
	}
	public void sendChordProgrammerMessage(Atom[] atArr){
		
	}
	
	public void consolePrint(String str){
	
	}
	public void postSplit(String str, String splitter){
			
	}

	
// privates -------------------------------------------------
	private void setControllerTrackIndices(int i){
		for (ControllerInfo ci: controllerList){
			ci.setTrackIndex(i);
		}
	}
	public void sendPNOCopyToOuts(PNO pno){
		for (Wire w: outWireList){
			if (w.on){
				w.send(processedPNO.copy());
			}			
		}
	}
	private PNO process(PNO pno){
		PNO temp = pno.copy();
		temp.addToStory("\n processed by " + nameToString());		
		return temp;
	}
	private PNO makePNO(){
		return new PNO();
	}
	private void makeNames(){
		
	}
// comparators --------------------------------------------
	public static Comparator<PlayletObjectInterface> xposComparator = new Comparator<PlayletObjectInterface>(){
		public int compare(PlayletObjectInterface obj1, PlayletObjectInterface obj2){
			if (obj1.guiObject().xpos() < obj2.guiObject().xpos()) return -1;
			if (obj1.guiObject().xpos() > obj2.guiObject().xpos()) return 1;
			return 0;
		}
	};
	public static Comparator<PlayletObjectInterface> yposComparator = new Comparator<PlayletObjectInterface>(){
		public int compare(PlayletObjectInterface obj1, PlayletObjectInterface obj2){
			if (obj1.guiObject().ypos() < obj2.guiObject().ypos()) return -1;
			if (obj1.guiObject().ypos() > obj2.guiObject().ypos()) return 1;
			return 0;
		}
	};
	public static Comparator<PlayletObjectInterface> ghostYposComparator = new Comparator<PlayletObjectInterface>(){
		public int compare(PlayletObjectInterface obj1, PlayletObjectInterface obj2){
			if (obj1.guiObject().ghostYpos() < obj2.guiObject().ghostYpos()) return -1;
			if (obj1.guiObject().ghostYpos() > obj2.guiObject().ghostYpos()) return 1;
			return 0;
		}
	};
	

	
// static variables -----------------------------------------
	
	public static final int ROUND_RECTANGLE = 0;
	public static final int HEXAGON = 1;
	public static final int TRIANGLE = 2;
	public static final int ROUND_TRIANGLE = 3;
	
	// values for variable 'type'
	public static final int CHORD_PROGRESSION = 0;
	public static final int RHYTHM_BUFFER = 1;
	public static final int CONTOUR = 2;
	
	public static final int DRUM_GENERATOR = 3;
	public static final int KIK_GENERATOR = 4;
	public static final int SNR_GENERATOR = 5;
	public static final int HAT_GENERATOR = 6;
	public static final int TOMS_GENERATOR = 7;
	public static final int CYMBAL_GENERATOR = 8;
	public static final int BASS_GENERATOR = 9;
	public static final int KEYS_GENERATOR = 10;
	public static final int LEAD_GENERATOR = 11;
	public static final int MULTI_GENERATOR = 12;
	
	public static final int DRUM_PROCESSOR = 13;
	public static final int KIK_PROCESSOR = 14;
	public static final int SNR_PROCESSOR = 15;
	public static final int HAT_PROCESSOR = 16;
	public static final int TOMS_PROCESSOR = 17;
	public static final int CYMBAL_PROCESSOR = 18;
	public static final int BASS_PROCESSOR = 19;
	public static final int KEYS_PROCESSOR = 20;
	public static final int LEAD_PROCESSOR = 21;
	public static final int MULTI_PROCESSOR = 22;
	
	public static final int DRUM_INSTRUMENT = 23;
	public static final int KIK_INSTRUMENT = 24;
	public static final int SNR_INSTRUMENT = 25;
	public static final int HAT_INSTRUMENT = 26;
	public static final int TOMS_INSTRUMENT = 27;
	public static final int CYMBAL_INSTRUMENT = 28;
	public static final int BASS_INSTRUMENT = 29;
	public static final int KEYS_INSTRUMENT = 30;
	public static final int LEAD_INSTRUMENT = 31;
	public static final int MULTI_INSTRUMENT = 32;
	
	public static final int RETURN_GENERATOR = 33;
	public static final int RETURN_PROCESSOR = 34;
	public static final int RETURN_INSTRUMENT = 35;
	
	public static final int MASTER_GENERATOR = 36;
	public static final int MASTER_PROCESSOR = 37;
	public static final int MASTER_INSTRUMENT = 38;
	
	// channel constants-------------------------------------
	public static final int RESOURCE_CHANNEL = -1;
	public static final int DRUM_CHANNEL = 0;
	public static final int KIK_CHANNEL = 1;
	public static final int SNR_CHANNEL = 2;
	public static final int HAT_CHANNEL = 3;
	public static final int TOMS_CHANNEL = 4;
	public static final int CYMBAL_CHANNEL = 5;
	public static final int BASS_CHANNEL = 6;
	public static final int KEYS_CHANNEL = 7;
	public static final int LEAD_CHANNEL = 8;
	public static final int MULTI_CHANNEL = 9;
	public static final int RETURN_CHANNEL = 10;
	public static final int MASTER_CHANNEL = 11;
	
	// layer constants --------------------------------------
	public static final int RESOURCE_OBJECT = 0;
	public static final int GENERATOR_OBJECT = 1;
	public static final int PROCESSOR_OBJECT = 2;
	public static final int INSTRUMENT_OBJECT = 3;
	
	public static final String[] layerName = new String[]{
		"", "gen", "pro", "inst"
	};
	
	
	public HashMap<Integer, String> plobjName = new HashMap<Integer, String>();
	
//	plobjName.put(CHORD_PROGRESSION, "CP");
	
	public static final int[] channel = new int[]{
		RESOURCE_CHANNEL, RESOURCE_CHANNEL, RESOURCE_CHANNEL, DRUM_CHANNEL, KIK_CHANNEL, SNR_CHANNEL, HAT_CHANNEL, TOMS_CHANNEL, CYMBAL_CHANNEL, BASS_CHANNEL,
		KEYS_CHANNEL, LEAD_CHANNEL, MULTI_CHANNEL, DRUM_CHANNEL, KIK_CHANNEL, SNR_CHANNEL, HAT_CHANNEL, TOMS_CHANNEL, CYMBAL_CHANNEL, BASS_CHANNEL,
		KEYS_CHANNEL, LEAD_CHANNEL, MULTI_CHANNEL, DRUM_CHANNEL, KIK_CHANNEL, SNR_CHANNEL, HAT_CHANNEL, TOMS_CHANNEL, CYMBAL_CHANNEL, BASS_CHANNEL,
		KEYS_CHANNEL, LEAD_CHANNEL, MULTI_CHANNEL, RETURN_CHANNEL, RETURN_CHANNEL, RETURN_CHANNEL, MASTER_CHANNEL, MASTER_CHANNEL, MASTER_CHANNEL
	};
	
	public static final String[] name = new String[]{
		"CP", "RB", "CN", "DR", "KIK", "SNR", "HH", "TOM", "CYM", "BS", 
		"KEY", "LD", "MLT", "DRM", "KIK", "SNR", "HAT", "TOM", "CYM", "BS",
		"KEY", "LD", "MLT", "DR", "KIK", "SNR", "HH", "TOM", "CYM", "BS",
		"KEY", "LD", "MLT", "RET", "RET", "RET", "MST", "MST", "MST"};
	public static final int[] layerArr = new int[]{
		RESOURCE_OBJECT, RESOURCE_OBJECT, RESOURCE_OBJECT, GENERATOR_OBJECT, GENERATOR_OBJECT, GENERATOR_OBJECT, GENERATOR_OBJECT, GENERATOR_OBJECT, GENERATOR_OBJECT, GENERATOR_OBJECT,
		GENERATOR_OBJECT, GENERATOR_OBJECT, GENERATOR_OBJECT, PROCESSOR_OBJECT, PROCESSOR_OBJECT, PROCESSOR_OBJECT, PROCESSOR_OBJECT, PROCESSOR_OBJECT, PROCESSOR_OBJECT, PROCESSOR_OBJECT,
		PROCESSOR_OBJECT, PROCESSOR_OBJECT, PROCESSOR_OBJECT, INSTRUMENT_OBJECT, INSTRUMENT_OBJECT, INSTRUMENT_OBJECT, INSTRUMENT_OBJECT, INSTRUMENT_OBJECT, INSTRUMENT_OBJECT, INSTRUMENT_OBJECT, 
		INSTRUMENT_OBJECT, INSTRUMENT_OBJECT, INSTRUMENT_OBJECT, GENERATOR_OBJECT, PROCESSOR_OBJECT, INSTRUMENT_OBJECT, GENERATOR_OBJECT, PROCESSOR_OBJECT, INSTRUMENT_OBJECT
	};
	public static final int[][] connectionArray = new int[][]{	// connectionArray[RESOURCE_OBJECT] returns the array of objects that all resource objects connect to
		new int[]{GENERATOR_OBJECT},							// these are destination objects only.....
		new int[]{PROCESSOR_OBJECT, INSTRUMENT_OBJECT},
		new int[]{PROCESSOR_OBJECT, INSTRUMENT_OBJECT},
		new int[]{}
	};
	
	
	public static final String DRUMS = "drums";
	public static final String KIK = "kik";
	public static final String SNR = "snr";
	public static final String HAT = "hat";
	public static final String TOMS = "toms";
	public static final String CYMBALS = "cymb";
	public static final String BASS = "bass";
	public static final String KEYS = "keys";
	public static final String LEAD = "lead";
	public static final String MASTER = "master";

	

//	public static final String initMessage = "init";
//	public static final String deviceParamMessage = "deviceparam";		// these are contained in the classes implementing ControllerInfo 
//	public static final String sendMessage = "send";
//	public static final String panMessage = "pan";
	
	
	// controller setup constants ------------------------------
	
//
// A WHOLE BUNCH OF STATIC DEVICE PARAMETERS HAVE BEEN MOVED TO THE DeviceParamInfo class
//
	
// init message strings as Strings
// init newClipObject track 1 0 controllerinit controller vol controller device 0 3 HP controller send 1 send1 controller pan
	
		
// button instruction identifiers
	public static final int BUTTON_SET_NOTE_DENSITY = 0; 
	
	
//	public static final int instrumentHeightMin = 6;
//	public static final int instrumentHeightMax = 24;
	
	// not sure what's next................
}
