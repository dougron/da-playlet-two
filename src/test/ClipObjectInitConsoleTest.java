package test;
import java.util.ArrayList;

import com.cycling74.max.Atom;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import DataObjects.ableton_device_control_utils.controller.ControllerInfo;
import LegacyStuff.ChordProgrammer2Parent;
import ResourceUtils.ChordForm;
import ResourceUtils.TwoBarRhythmBuffer;
import TestUtils.TestData;
import UDPUtils.OSCMessMaker;
import UDPUtils.UDPConnection;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.LegatoProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RandomBendProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.SloWahProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysInstrumentOne;




public class ClipObjectInitConsoleTest extends ConsoleProgram implements ConsoleInterface, ChordProgrammer2Parent, PlobjParent{

	private ArrayList<PlayletObjectInterface> instList = new ArrayList<PlayletObjectInterface>();
	private ArrayList<PlayletObjectInterface> genList = new ArrayList<PlayletObjectInterface>();
		
	public void run(){
		setSize(700, 700);
		
		ChordForm cf = TestData.makeProgression(this).form;
		TwoBarRhythmBuffer rb = new TwoBarRhythmBuffer();
		TestData.fourOnFloorForRhythmBuffer(rb);
		
		BassProcessorOne bp = new BassProcessorOne(100, 100, "bassProOne", this);
		SloWahProcessor sloWah = new SloWahProcessor(50, 50, "slo-wah", this);
		RandomBendProcessor randBend = new RandomBendProcessor(55, 55, "randPB", this);
		LegatoProcessor leg = new LegatoProcessor(66, 66, "legato", this);
		
		PlayletObjectInterface gen1 = new BassGeneratorOne(10, 10, "BassGen1", this);
		gen1.setChordForm(cf);
		gen1.setRhythmBuffer(rb);
		genList.add(gen1);
		
		PlayletObjectInterface gen2 = new KeysGeneratorOne(10, 10, "KeysGen1", this);
		gen2.setChordForm(cf);
		gen2.setRhythmBuffer(rb);
		genList.add(gen2);
		
		PlayletObjectInterface inst1 = new BassInstrumentOne(10, 10, "BassInst1", this, this);
		inst1.setTrackClipIndex(0, 0);
		instList.add(inst1);
		
		PlayletObjectInterface inst2 = new KeysInstrumentOne(10, 10, "KeysInst1", this, this);
		inst2.setTrackClipIndex(1, 0);
		instList.add(inst2);
		
		PlayletObjectInterface inst3 = new KeysInstrumentOne(20, 10, "KeysInst2", this, this);
		inst3.setTrackClipIndex(2, 0);
		instList.add(inst3);
		
		Wire w1 = new Wire(gen1, leg, true);
		Wire w6 = new Wire(leg, inst1, true);
//		Wire w7 = new Wire(randBend, inst1, true);
//		Wire w3 = new Wire(bp, sloWah, true);
// 		Wire w5 = new Wire(sloWah, inst1, true);
		Wire w2 = new Wire(gen2, inst2, true);
		Wire w4 = new Wire(gen2, inst3, true);
		
		setClipObjectIndices(); 
		sendClipObjectIndexInitializationMessage();
		sendTrackAndClipInitMessage();

		PlayletObject.dynamic = 3;
		for (PlayletObjectInterface plobj: genList){
			plobj.render();
		}		
		sendNullClips();
	}
// privates -----------------------------------------------------------------------
	private void sendNullClips(){
		for (PlayletObjectInterface plobj: instList){
			if (plobj.hasNoIns()){
				plobj.in(PlayletObject.nullPNO);
			}
		}
	}
	private void sendTrackAndClipInitMessage(){
		OSCMessMaker mess; 
		UDPConnection conn = new UDPConnection(7800);
		for (PlayletObjectInterface plobj: instList){
			mess = new OSCMessMaker();
			mess.addItem(DeviceParamInfo.initString);
			mess.addItem(plobj.getClipObjectIndex());
			mess.addItem(plobj.getTrackIndex());
			mess.addItem(plobj.getClipIndex());
			mess.addItem(plobj.uniqueName());
			for (ControllerInfo ci: plobj.controllerList()){
				ci.addToOscMessage(mess);
			}
			conn.sendUDPMessage(mess);
		}
		
		
	}
	private void sendClipObjectIndexInitializationMessage(){
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(DeviceParamInfo.clipObjInitMessage);
		for (PlayletObjectInterface plobj: instList){
			mess.addItem(plobj.getClipObjectIndex());
		}
		UDPConnection conn = new UDPConnection(7800);
		conn.sendUDPMessage(mess);
	}
	private void setClipObjectIndices(){
		for (int i = 0; i < instList.size(); i++){
			instList.get(i).setClipObjectIndex(i);
		}
	}
	
// ConsoleInterface methods --------------------------------------------------------
	public void sendChordProgrammerMessage(Atom[] atArr){}
	public void consolePrint(String str){
		println(str);
	}
// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
}
