package test;
import java.util.ArrayList;
import java.util.HashMap;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;

import DataObjects.ableton_live_clip.LiveClip;
import ResourceUtils.ChordForm;
import main.java.com.dougronthebold.DAPlayletTwo.graph_melody.GraphMelodyKernel;
import main.java.com.dougronthebold.DAPlayletTwo.graph_melody.Phrase;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.SineWaveGraph;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.SquareWaveGraph;

public class GraphMelodyMaxTest extends MaxObject{

	private int messageOutlet = 0;
	private int consoleOutlet = 1;
	private int beatsPerBar = 4;
	private double barLength = 4.0;
	private GraphMelodyKernel gmk = new GraphMelodyKernel();
	private String chordFormMessageID = "chordform";
	private String sqWaveMessageID = "squarewave";
	private String sqPlotMessageID = "sqplot";
	private String sineWaveMessageID = "sinewave";
	private String sinePlotMessageID = "sineplot";
	private String generatorListID = "generatorlist";
	private String waveLengthParameterID = "wavelength";
	private String amplitudeParameterID = "amplitude";
	private String offsetParameterID = "offset";
	private String isOutputParameterID = "output";
	private String waveLengthSourceListID = "wavelengthsourcelist";
	private String amplitudeSourceListID = "amplitudesourcelist";
	private String offsetSourceListID = "offsetsourcelist";
	private String waveLengthSourceID = "wavelengthsource";
	private String amplitudeSourceID = "amplitudesource";
	private String offsetSourceID = "offsetsource";
	private String waveLengthModDepthID = "wavelengthmoddepth";
	private String amplitudeModDepthID = "amplitudemoddepth";
	private String offsetModDepthID = "offsetmoddepth";
	private int mixPlotIndex = -1;
	private String mixPlotName = "output";
	
	private String noneString = "none";
	private HashMap<Integer, SineWaveGraph> sinePanelMap = new HashMap<Integer, SineWaveGraph>();
	
	public GraphMelodyMaxTest(){
		consolePrint("GraphMelodyMaxTest initialized.");
		setMaxInlets();
	}
	public void bang(){
		//outlet(consoleOutlet, "sending gui init stuff");
		sendChordFormParameters();
		sendSquareWaveParameters();
		sendSquareWavePlots();
		sendSineSelectionLists();
		sinePanelMapToConsole();
//		sendSineWavePlots();
	}
	public void formlength(double l){
		// assuming 4/4 time and l is number of bars
		consolePrint("formlength set to " + l);
		gmk.setChordForm(new ChordForm(makeCFLiveClip(l)));
		sendSquareWavePlots();
		sendSineWavePlots();
	}
	public void resolution(double r){
		consolePrint("resolution set to " + r);
		gmk.setResolution(r);
		bang();
	}
	public void kernelToString(){
		consolePrint(gmk.toString());
	}
	public void squarewavelength(int index, double value){
		gmk.setSquareWaveLength(index, value);
		bang();
	}
	public void squarewavepulsewidth(int index, double value){
		gmk.setSquareWavePulseWidth(index, value);
		bang();
	}
	public void squarewaveoffset(int index, double value){
		gmk.setSquareWaveOffset(index, value);
		bang();
	}
	public void testsqplot(){
		for (int i = 0; i < 3; i++ ){
			outlet(messageOutlet, gmk.getSquareWaveMaxPlot(i));
		}
	}
	public void sineIndex(int panelIndex, int sineIndex){
		if (sineIndex == 0){
			if (sinePanelMap.containsKey(panelIndex)) sinePanelMap.remove(panelIndex);
		} else {
			SineWaveGraph swg = gmk.sineList.get(sineIndex - 1);				// list always includes a null option at index 0 so index received must be reduced by 1			
			sinePanelMap.put(panelIndex, swg);	
			sendSineParametersToPanel(panelIndex, swg);
			System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
		}
		sinePanelMapToConsole();
		System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
	}
	public void setWaveLength(int panelIndex, double wl){
//		consolePrint("setWaveLength: " + panelIndex + ", " + wl);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			sinePanelMap.get(panelIndex).waveLength = wl;
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
		
	}
	public void setAmplitude(int panelIndex, double amp){
//		consolePrint("setAmplitude: " + panelIndex + ", " + amp);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			sinePanelMap.get(panelIndex).amplitude = amp;
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
		
	}
	public void setOffset(int panelIndex, double off){
//		consolePrint("setOffset: " + panelIndex + ", " + off);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			sinePanelMap.get(panelIndex).offset = off;
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
		
	}
	public void isOutput(int panelIndex, int val){
//		consolePrint("isOutput: " + panelIndex + ", " + val);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			SineWaveGraph sg = sinePanelMap.get(panelIndex);
			if (val == 0){
				sg.isOutput = false;
			} else {
				sg.isOutput = true;
			}
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
		
	}
	public void setWaveLengthSource(int panelIndex, String str){
//		consolePrint("setWaveLengthSource: " + panelIndex + ", " + str);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			SineWaveGraph sg = sinePanelMap.get(panelIndex);
			if (str.equals(noneString)){
				sg.modulateWaveLength = false;
				sg.waveLengthModulator = null;
			} else {
				sg.modulateWaveLength = true;
				sg.waveLengthModulator = gmk.getSineWaveGraph(str);
			}
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
	}
	public void setAmplitudeSource(int panelIndex, String str){
//		consolePrint("setAmplitudeSource: " + panelIndex + ", " + str);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			SineWaveGraph sg = sinePanelMap.get(panelIndex);
			if (str.equals(noneString)){
				sg.modulateAmplitude = false;
				sg.amplitudeModulator = null;
			} else {
				sg.modulateAmplitude = true;
				sg.amplitudeModulator = gmk.getSineWaveGraph(str);
			}
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
	}
	public void setOffsetSource(int panelIndex, String str){
//		consolePrint("setOffsetSource: " + panelIndex + ", " + str);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			SineWaveGraph sg = sinePanelMap.get(panelIndex);
			if (str.equals(noneString)){
				sg.modulateOffset = false;
				sg.offsetModulator = null;
			} else {
				sg.modulateOffset = true;
				sg.offsetModulator = gmk.getSineWaveGraph(str);
			}
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
	}
	public void setWaveLengthModDepth(int panelIndex, double val){
//		consolePrint("setWaveLengthModDepth: " + panelIndex + ", " + val);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			sinePanelMap.get(panelIndex).waveLengthModDepth = val;
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
	}
	public void setAmplitudeModDepth(int panelIndex, double val){
//		consolePrint("setAmplitudeModDepth: " + panelIndex + ", " + val);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			sinePanelMap.get(panelIndex).amplitudeModDepth = val;
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
	}
	public void setOffsetModDepth(int panelIndex, double val){
//		consolePrint("setOffsetModDepth: " + panelIndex + ", " + val);
		if (sinePanelMap.containsKey(panelIndex)){
//			consolePrint("contains panel index.....");
			sinePanelMap.get(panelIndex).offsetModDepth = val;
			sendSineWavePlots();
//			consolePrint(sinePanelMap.get(panelIndex).toString());
		} else {
			consolePrint("pnaelMap index does not exist. no sineWaveGraph assigned to this panel");
		}
	}
	public void updateSinePlots(){
		sendSineWavePlots();
	}
	public void sendPhraseListToConsole(){
		for (Phrase ph: gmk.getPhraseList()){
			System.out.println(ph.toString());
		}
	}
	
	
// privates ----------------------------------------------------------------------	
	private void sendSineParametersToPanel(int panelIndex, SineWaveGraph swg){
		System.out.println("sendSineParametersToPanel panelIndex: " + panelIndex + " sineWaveGraph: " + swg.name + "-------------------------");
		sendSinePanelMessage(panelIndex, waveLengthParameterID, swg.waveLength);
		sendSinePanelMessage(panelIndex, amplitudeParameterID, swg.amplitude);
		sendSinePanelMessage(panelIndex, offsetParameterID, swg.offset);
		sendSinePanelMessage(panelIndex, isOutputParameterID, swg.isOutput);
		sendModSourceLists(panelIndex, swg);
		sendModSourceListValues(panelIndex, swg);
		sendModDepthValues(panelIndex, swg);
	}
	private void sendModDepthValues(int panelIndex, SineWaveGraph swg){
		sendSinePanelMessage(panelIndex, waveLengthModDepthID, swg.waveLengthModDepth);
		sendSinePanelMessage(panelIndex, amplitudeModDepthID, swg.amplitudeModDepth);
		sendSinePanelMessage(panelIndex, offsetModDepthID, swg.offsetModDepth);
	}
	private void sendModSourceListValues(int panelIndex, SineWaveGraph swg){
		sendWaveLengthModSourceValue(panelIndex, swg);
		sendAmplitudeModSourceValue(panelIndex, swg);
		sendOffsetModSourceValue(panelIndex, swg);
	}
	private void sendWaveLengthModSourceValue(int panelIndex, SineWaveGraph swg){
		if (swg.modulateWaveLength){
			sendSinePanelMessage(panelIndex, waveLengthSourceID, swg.waveLengthModulator.name);
		} else {
			sendSinePanelMessage(panelIndex, waveLengthSourceID, noneString);
		}
	}
	private void sendAmplitudeModSourceValue(int panelIndex, SineWaveGraph swg){
		if (swg.modulateAmplitude){
			sendSinePanelMessage(panelIndex, amplitudeSourceID, swg.amplitudeModulator.name);
		} else {
			sendSinePanelMessage(panelIndex, amplitudeSourceID, noneString);
		}
	}
	private void sendOffsetModSourceValue(int panelIndex, SineWaveGraph swg){
		if (swg.modulateOffset){
			sendSinePanelMessage(panelIndex, offsetSourceID, swg.offsetModulator.name);
		} else {
			sendSinePanelMessage(panelIndex, offsetSourceID, noneString);
		}
	}
	private void sendModSourceLists(int panelIndex, SineWaveGraph swg){
		String[] idArr = new String[]{waveLengthSourceListID, amplitudeSourceListID, offsetSourceListID};
		Atom[] atArr = new Atom[gmk.sineList.size() + 3];
		int index = 4;
		atArr[0] = Atom.newAtom(sineWaveMessageID);
		atArr[1] = Atom.newAtom(panelIndex);
		atArr[3] = Atom.newAtom("none");
		for (SineWaveGraph swgIterator: gmk.sineList){
			if (!swgIterator.name.equals(swg.name)){
				atArr[index] = Atom.newAtom(swgIterator.name);
				index++;
			}			
		}
		for (String id: idArr){
			atArr[2] = Atom.newAtom(id);
			outlet(messageOutlet, atArr);
		}
	}
	private void sendSinePanelMessage(int panelIndex, String messageID, String str){
		Atom[] atArr = new Atom[]{
				Atom.newAtom(sineWaveMessageID),
				Atom.newAtom(panelIndex),
				Atom.newAtom(messageID),
				Atom.newAtom(str)
		};
		outlet(messageOutlet, atArr);
	}
	private void sendSinePanelMessage(int panelIndex, String messageID, boolean bool){
		int val;
		if (bool) val = 1; else val = 0;
		Atom[] atArr = new Atom[]{
				Atom.newAtom(sineWaveMessageID),
				Atom.newAtom(panelIndex),
				Atom.newAtom(messageID),
				Atom.newAtom(val)
		};
		outlet(messageOutlet, atArr);
	}
	private void sendSinePanelMessage(int panelIndex, String messageID, double value){
		Atom[] atArr = new Atom[]{
				Atom.newAtom(sineWaveMessageID),
				Atom.newAtom(panelIndex),
				Atom.newAtom(messageID),
				Atom.newAtom(value)
		};
		outlet(messageOutlet, atArr);
	}
	private void sinePanelMapToConsole(){
		System.out.println("sinePanelMap contents ...................");
		if (sinePanelMap.keySet().size() == 0){
			System.out.println("map empty.");
		} else {
			for (Integer i: sinePanelMap.keySet()){
				System.out.println("key: " + i + " attached to " + sinePanelMap.get(i).name);
			}
		}
	}
	private void sendSineSelectionLists(){
		outlet(messageOutlet, new String[]{sineWaveMessageID, "bang"});		// clears sine panels 
		sinePanelMap.clear();
		String[] strArr = new String[gmk.sineList.size() + 3];
		strArr[0] = sineWaveMessageID;
		strArr[1] = generatorListID;
		strArr[2] = "........";
		for (int i = 0; i < gmk.sineList.size(); i++){
			strArr[i + 3] = gmk.sineList.get(i).name;
		}
		outlet(messageOutlet, strArr);
	}
	private void sendChordFormParameters(){
		sendFormLengthMessage();
		sendResolutionMessage();
	}
	private void sendResolutionMessage(){
		Atom[] atArr = new Atom[3];
		atArr[0] = Atom.newAtom(chordFormMessageID);
		atArr[1] = Atom.newAtom("resolution");
		atArr[2] = Atom.newAtom(gmk.resolution());
		outlet(messageOutlet, atArr);
	}
	
	private void sendFormLengthMessage(){
		Atom[] atArr = new Atom[3];
		atArr[0] = Atom.newAtom(chordFormMessageID);
		atArr[1] = Atom.newAtom("formlength");
		atArr[2] = Atom.newAtom(gmk.length() / 4);
		outlet(messageOutlet, atArr);
	}
	private void sendSineWavePlots(){
		consolePrint("sendSineWavePlots ----------------------------------");
		for (int i = 0; i < gmk.sineList.size(); i++){
			consolePrint("sending index: " + i);
			consolePrint(gmk.sineList.get(i).toString());
//			String n = gmk.getSineName(i);
//			double[] arr = gmk.getSineWaveMaxPlot(i);
//			System.out.println("sine index: " + i + " length: " + arr.length + " ----------------------");
//			for (double d: arr){
//				System.out.println(d);
//			}
			outlet(messageOutlet, makeSinePlotMessage(gmk.getSineWaveMaxPlot(i), i, gmk.getSineName(i)));
		}
		outlet(messageOutlet, makeSinePlotMessage(gmk.getSineWaveMixPlot(), mixPlotIndex, mixPlotName));
	}
	private Atom[] makeSinePlotMessage(double[] arr, int plotIndex, String n){
		Atom[] atArr = new Atom[arr.length + 5];
		atArr[0] = Atom.newAtom(sinePlotMessageID);
		atArr[1] = Atom.newAtom(plotIndex);
		int index = 2;
		for (double i: arr){
			atArr[index] = Atom.newAtom(i);
			index++;
		}
		atArr[index] = Atom.newAtom(gmk.length());
		index++;
		atArr[index] = Atom.newAtom(barLength);
		index++;
		atArr[index] = Atom.newAtom(n);
//		System.out.println("Atom array ---------");
//		for (Atom at: atArr){
//			System.out.println(at);
//		}
		return atArr;
	}
	private void sendSquareWavePlots(){
		for (int i = 0; i < 4; i++){
			outlet(messageOutlet, makeSQPlotMessage(gmk.getSquareWaveMaxPlot(i), i));
		}
		
	}
	private Atom[] makeSQPlotMessage(int[] arr, int plotIndex){
		Atom[] atArr = new Atom[arr.length + 4];
		atArr[0] = Atom.newAtom(sqPlotMessageID);
		atArr[1] = Atom.newAtom(plotIndex);
		int index = 2;
		for (int i: arr){
			atArr[index] = Atom.newAtom(i);
			index++;
		}
		atArr[index] = Atom.newAtom(gmk.length());
		index++;
		atArr[index] = Atom.newAtom(barLength);
		return atArr;
	}
	private void sendSquareWaveParameters(){
		outlet(messageOutlet, makeSQMessage(gmk.swgRawArr[0], "raw0"));
		outlet(messageOutlet, makeSQMessage(gmk.swgRawArr[1], "raw1"));
		outlet(messageOutlet, makeSQMessage(gmk.swgMix, "sqmix"));
	}
	private Atom[] makeSQMessage(SquareWaveGraph swg, String id){
		Atom[] atArr = new Atom[5];
		atArr[0] = (Atom.newAtom(sqWaveMessageID));
		atArr[1] = (Atom.newAtom(id));
		atArr[2] = (Atom.newAtom(swg.waveLength));
		atArr[3] = (Atom.newAtom(swg.pulseWidth));
		atArr[4] = (Atom.newAtom(swg.offset));
		return atArr;
	}
	private void consolePrint(String str){
		String[] splitStr = str.split("\n");
		for (String s: splitStr){
			outlet(consoleOutlet, s);
		}
		
	}
	private LiveClip makeCFLiveClip(double l){
		LiveClip lc = new LiveClip(0, 0);
		lc.length = l * beatsPerBar;
		lc.addNote(64, 0.0, 1.0, 100, 0);
		lc.addNote(67, 0.0, 1.0, 100, 0);
		lc.addNote(71, 0.0, 1.0, 100, 0);
		return lc;
	}
	private void setMaxInlets(){
		declareInlets(new int[]{
				DataTypes.ALL,  
				DataTypes.INT}
		);
		declareOutlets(new int[]{ 
				DataTypes.ALL, 
				DataTypes.ALL}
		);
		setInletAssist(new String[]{
			"messages in",
			"erm...."}
		);
		setOutletAssist(new String[]{
			"messages out",
			"dump out"}
		);
	}
}
