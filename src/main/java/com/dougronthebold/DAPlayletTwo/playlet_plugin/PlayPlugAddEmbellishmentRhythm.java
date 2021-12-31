package main.java.com.dougronthebold.DAPlayletTwo.playlet_plugin;
import java.util.ArrayList;

import com.cycling74.max.MaxObject;

import PipelineUtils.PipelineNoteList;
import PipelineUtils.PipelineNoteObject;
import PipelineUtils.PlayPlugArgument;


public class PlayPlugAddEmbellishmentRhythm implements PlayletPlugIn{

	public String name = "EmbRhy";
	public double[] options; 
	public double[] weighting;
	public double weightingSum;
	public double chance;					// this is the chance of an embellishment being inserted
//	private int sortIndex = 10;
//	private int inputSort = 0;
//	private int zone = 1;
//	private boolean canDouble = true;
	private int active = 1;
	
	public PlayPlugAddEmbellishmentRhythm(double[] options, double[] weighting, double chance){
		sortOutWeighting(options, weighting);
		weightingSum = makeWeightingSum();
		this.options = options;
		for (double d: options){
			name = name + d + ",";
		}
		this.chance = chance;
	}
	
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
//		MaxObject.post("PlugInAddEmbellishmentRhythm.process called with pnl:");
//		postPNL(pnl);
		if (active > 0){
			ArrayList<PipelineNoteObject> pnoTempList = new ArrayList<PipelineNoteObject>();
			for (PipelineNoteObject pno: pnl.pnoList){
				if (pno.isEmbellishable && ppa.rnd.next() < chance){
					double position = pno.position + options[getWeightedIndex(ppa)];
					while (position < 0.0) position += pnl.length;
					position = position % pnl.length;
					if (!positionAlreadyTaken(position, pnl.pnoList, pnoTempList)){
						pnoTempList.add(new PipelineNoteObject(position, false, true, pno));
						pno.isEmbellishable = false;
					}
				}
			}
			pnl.add(pnoTempList);
		}		
	}

	public String name(){
		return name;
	}
//	public int sortIndex(){
//		return sortIndex;
//	}
//	public int inputSort(){
//		return inputSort;
//	}
//	public int zone(){
//		return zone;
//	}
//	public boolean canDouble(){
//		return canDouble;
//	}
//	public void setInputSort(int i){
//		inputSort = i;
//	}
	public int active(){
		return active;
	}
	public void setActive(int i){
		active = i;
	}
	
// privates ----------------------------------------------------------------------------
	
	private double makeWeightingSum(){
		double sum = 0.0;
		for (double dd: weighting){
			sum += dd;
		}
		return sum;
	}
	private void sortOutWeighting(double[] options, double[] weighting){
		if (weighting.length != options.length){
			double[] newWeighting = new double[options.length];
			for (int i = 0; i < newWeighting.length; i++){
				if (i < weighting.length){
					newWeighting[i] = weighting[i];
				} else {
					newWeighting[i] = 0.0;
				}
			}
			this.weighting = newWeighting;
		} else {
			this.weighting = weighting;
		}
	}
	private boolean positionAlreadyTaken(double pos, ArrayList<PipelineNoteObject> list1, ArrayList<PipelineNoteObject> list2){
		if (containsPosition(pos, list1) || containsPosition(pos, list2)){
			return true;
		} else {
			return false;
		}
	}
	private boolean containsPosition(double pos, ArrayList<PipelineNoteObject> list){
		for (PipelineNoteObject pno: list){
			if (pno.position == pos){
				return true;
			}
		}
		return false;
	}
	private int getWeightedIndex(PlayPlugArgument ppa){
		double r = ppa.rnd.next() * weightingSum;
		int index = -1;
		while (r > 0){
			index++;
			r -= weighting[index];								
		}
		return index;
	}
	private void postPNL(PipelineNoteList pnl){
		MaxObject.post("PlugInBassAddEmbellishmentOne PipelineNoteList.toString -----------------");
		String[] splitPost = pnl.toString().split("\n");
		for (String str: splitPost){
			MaxObject.post(str);
		}
		
	}
}
