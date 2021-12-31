package main.java.com.dougronthebold.DAPlayletTwo.playlet_object;
import java.util.ArrayList;

import DataObjects.ableton_device_control_utils.controller.ControllerInfo;
import LegacyStuff.ChordProgrammer2;
import LegacyStuff.RhythmBufferGUIController;
import PipelineUtils.PlayPlugArgument;
import ResourceUtils.ChordForm;
import ResourceUtils.ContourData;
import ResourceUtils.ContourPack;
import ResourceUtils.TwoBarRhythmBuffer;
import UDPUtils.OSCMessMaker;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.ButtonInstruction;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.GUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;



public interface PlayletObjectInterface {

	public String guiObjectToString();
	public String toString();
	public String nameToString();
	public String uniqueName();
	
	public int layer();
	public int type();
	public int channel();
	public GUIObject guiObject();
	public PNO receivedPNO();
	public PNO processedPNO();
	public String recievedPNORenderStory();
	public String processedPNORenderStory();
	
	public void setChordProgrammer(ChordProgrammer2 cp);
	public void setChordForm(ChordForm cf);
	public void setRhythmBuffer(RhythmBufferGUIController rbgc);
	public void setRhythmBuffer(TwoBarRhythmBuffer rb);
	public void setContour(ContourData cd);
	public PlayPlugArgument getPPA();
	
	public void in(PNO pno);
	public void render();
	
	public void setClipObjectIndex(int i);
	public int getClipObjectIndex();
	public void setTrackIndex(int i);
	public void setClipIndex(int i);
	public int getTrackIndex();
	public int getClipIndex();
//	public int getTrackType();
	public void setTrackClipIndex(int t, int c);
	
	public void addInWire(Wire w);
	public void addOutWire(Wire w);
	public ArrayList<Wire> inWireList();
	public ArrayList<Wire> outWireList();
	public void setAllOutWires(boolean b);
	
	public boolean hasNoIns();
	public boolean hasOutputPatch();
	public boolean hasGhostOutputPatch();
	
	public ArrayList<ControllerInfo> controllerList();
	
	public ContourPack getContourPack();
	public void newRNDSequence();
	public void doButtonInstruction(ButtonInstruction bi);
	
//	public void incrementHeight(int i);


	public void instrumentInitializationMessage(OSCMessMaker mess);
	public PlobjParent parent();
	public void setOn(boolean b);
}
