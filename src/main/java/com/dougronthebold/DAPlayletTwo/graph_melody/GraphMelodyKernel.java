package main.java.com.dougronthebold.DAPlayletTwo.graph_melody;
import java.util.ArrayList;

import DataObjects.ableton_live_clip.LiveClip;
import ResourceUtils.ChordForm;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.SineWaveGraph;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.SquareWaveGraph;

public class GraphMelodyKernel {
	
	private double resolution = 0.5;
	private ChordForm cf = new ChordForm(eightBarsOfC());
	public SquareWaveGraph[] swgRawArr = new SquareWaveGraph[]{
			new SquareWaveGraph(16.0),
			new SquareWaveGraph(8.0)
	};
	public SquareWaveGraph swgMix = new SquareWaveGraph(32.0);
	public ArrayList<SineWaveGraph> sineList = new ArrayList<SineWaveGraph>();
	
	private static SineWaveGraph nullSineGraph = new SineWaveGraph("null", false);
	
	
	
	public GraphMelodyKernel(){
		addDefaultSineWaveGraphs();
	}
	public void setResolution(double d){
		resolution = d;
	}
	public void setChordForm(ChordForm cf){
		this.cf = cf;
	}
	public String toString(){
		String ret = "MelodyGraphKernel toString ==============================\n";
		ret = ret + "resolution:    " + resolution + "\n";
		if (cf != null) ret = ret + "ChordForm: ---------\n" + cf.toString() + "\n";
		for (int i = 0; i < 2; i++){
			ret += "SquareWave raw material index " + i + ":----\n" + swgRawArr[i].toString() + "\n";
		}
		ret += "SquareWave mix graph:----\n" + swgMix.toString() + "\n";
		return ret;
	}
	public void setSquareWaveLength(int index, double value){
		if (index > 1){
			swgMix.setWaveLength(value);
		} else {
			swgRawArr[index].setWaveLength(value);
		}
	}
	public void setSquareWavePulseWidth(int index, double value){
		if (index > 1){
			swgMix.setPulseWidth(value);
		} else {
			swgRawArr[index].setPulseWidth(value);
		}
	}
	public void setSquareWaveOffset(int index, double value){
		if (index > 1){
			swgMix.setOffset(value);
		} else {
			swgRawArr[index].setOffset(value);
		}
	}
	public double[] getSineWaveMaxPlot(int index){
		
		if (index < sineList.size()){
			return getSinePlot(sineList.get(index));
		}
		return new double[0];
	}
	public double[] getSineWaveMixPlot(){
		double[] arr = new double[(int)(cf.length() / resolution + 1)];
		int i = 0;
		for (double pos = 0.0; pos <= cf.length(); pos += resolution){
			arr[i] = getSineTotal(pos);
			i++;
		}
		return arr;
		
	}
	public int[] getSquareWaveMaxPlot(int index){
		
		if (index == 2){
			return getSQPlot(swgMix);
		} else if (index == 3) {
			return getSquareWaveMixOutput();
		} else {
			return getSQPlot(swgRawArr[index]);
		}
	}
	public int[] getSquareWaveMixOutput(){
		int[] mix = getSQPlot(swgMix);
		int[][] raw = new int[][]{
				getSQPlot(swgRawArr[0]),
				getSQPlot(swgRawArr[1])
		};
		int[] output = new int[mix.length];
		for (int i = 0; i < mix.length; i++){
			output[i] = raw[mix[i]][i];
		}
		return output;
	}
	public double length(){
		if (cf != null){
			return cf.length();
		} else {
			return 0.0;
		}		
	}
	public double resolution(){
		return resolution;
	}
	public SineWaveGraph getSineWaveGraph(String str){
		for (SineWaveGraph swg: sineList){
			if (swg.name.equals(str)){
				return swg;
			}
		}
		return nullSineGraph;
	}
	public String getSineName(int index){
		if (index < sineList.size()){
			return sineList.get(index).name;
		} else {
			return "no name";
		}
	}
	public double getSineTotal(double pos){
		int count = 0;
		double val = 0;
		for (SineWaveGraph swg: sineList){
			if (swg.isOutput){
				count++;
				val += swg.getValue(pos);
			}
		}
		return val / count;
	}
	public ArrayList<Phrase> getPhraseList(){
		ArrayList<Phrase> phList = new ArrayList<Phrase>();
		double tempStartPoint = 0.0;
		boolean lookingForEnd = true;
		int previousValue = 0;
		int currentValue = 0;
		System.out.println("getPhraseList called............................");
		for (double pos = 0.0; pos <= cf.length(); pos += resolution){	
			System.out.println("pos: " + pos);
			previousValue = currentValue;
			currentValue = getSquareWaveValue(pos);
			System.out.println("previousValue = " + previousValue + " currentValue = " + currentValue + " looking4end " + lookingForEnd);
			System.out.println("tempStartPoint = " + tempStartPoint);
			if (pos == 0.0 && currentValue == 0) lookingForEnd = false;
			if (currentValue != previousValue){
				if (lookingForEnd){
					phList.add(new Phrase(tempStartPoint, 0.0, pos - tempStartPoint));
					lookingForEnd = false;
				} else {
					tempStartPoint = pos;
					lookingForEnd = true;
				}
			}
		}
		if (lookingForEnd){
			phList.add(new Phrase(tempStartPoint, 0.0, cf.length() - tempStartPoint));
		}
		System.out.println("getPhraseList done.");
		return phList;
	}
	public int getSquareWaveValue(double pos){
		return swgRawArr[swgMix.getValue(pos)].getValue(pos);
	}
	
// privates ---------------------------------------------------------------------------------------------

	
	private void addDefaultSineWaveGraphs(){
		sineList.add(new SineWaveGraph("A", false));
		sineList.add(new SineWaveGraph("B", true));
		sineList.add(new SineWaveGraph("C", true));
	}
	private LiveClip oneBarOfC(){
		LiveClip lc = new LiveClip(0, 0);
		lc.length = 4.0;
		lc.addNote(60, 0.0, 1.0, 100, 0);
		lc.addNote(64, 0.0, 1.0, 100, 0);
		lc.addNote(67, 0.0, 1.0, 100, 0);
		return lc;
	}
	private LiveClip eightBarsOfC(){
		LiveClip lc = new LiveClip(0, 0);
		lc.length = 32.0;
		lc.addNote(60, 0.0, 1.0, 100, 0);
		lc.addNote(64, 0.0, 1.0, 100, 0);
		lc.addNote(67, 0.0, 1.0, 100, 0);
		return lc;
	}
	private double[] getSinePlot(SineWaveGraph swg){
		double[] arr = new double[(int)(cf.length() / resolution + 1)];
		int i = 0;
		for (double pos = 0.0; pos <= cf.length(); pos += resolution){
			arr[i] = swg.getValue(pos);
			i++;
		}
		return arr;
	}
	private int[] getSQPlot(SquareWaveGraph swg){
		int[] arr = new int[(int)(cf.length() / resolution)];
		int i = 0;
		for (double pos = 0.0; pos < cf.length(); pos += resolution){
			arr[i] = swg.getValue(pos);
			i++;
		}
		return arr;
	}
	
	
	
	
	
	
	
	
}
