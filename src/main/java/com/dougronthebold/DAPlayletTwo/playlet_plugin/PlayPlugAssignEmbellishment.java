package main.java.com.dougronthebold.DAPlayletTwo.playlet_plugin;
import com.cycling74.max.MaxObject;

import PipelineUtils.Embellishment;
import PipelineUtils.PipelineNoteList;
import PipelineUtils.PipelineNoteObject;
import PipelineUtils.PlayPlugArgument;
import PlugIns.ChordToneEmbellishment;
import PlugIns.DiatonicEmbellishment;
import PlugIns.ED;
import PlugIns.SemitoneEmbellishment;


public class PlayPlugAssignEmbellishment implements PlayletPlugIn{
	public static final String name = "PlugInAssignEmbellishment";
	public Embellishment[] embOptionArr; 
	private double[] weighting;
	private double weightingSum;
	private int sortIndex = 20;
	private int inputSort = 0;
	private int zone = 1;
	private boolean canDouble = true;
	private int active = 1;
	private double percentageOfEmbellishingVelocity = 0.8;		// if embellished note is a Guide tone
//	private ResourceObject ro;
	
	public PlayPlugAssignEmbellishment(ED[] edArr, double[] weighting){
//		this.ro = ro;
		sortOutWeighting(edArr.length, weighting);
		weightingSum = makeWeightingSum();
		embOptionArr = makeEmbellishmentOptions(edArr);
		
	}
	public void process(PipelineNoteList pnl, PlayPlugArgument ppa){
//		MaxObject.post("PlugInAssignEmbellishment.process() called");
		for (PipelineNoteObject pno: pnl.pnoList){
//			splitPost(pno.toString(), "\n");
			if (pno.isEmbellishable && !pno.isGuideTone){
				pno.embellishmentType = embOptionArr[getWeightedIndex(ppa)];
				pno.addNote(pno.embellishmentType.getNote(pno.pnoEmbellishing, ppa));
				if (pno.pnoEmbellishing.isGuideTone){
					pno.setFixedVelocity((int)(pno.pnoEmbellishing.velocity * percentageOfEmbellishingVelocity));
				} else {
					pno.setFixedVelocity((int)(pno.pnoEmbellishing.velocity));
				}
				
			}
		}
//		MaxObject.post("PlugInAssignEmbellishment.process complete. results follow.......");
//		postPNL(pnl);
//		MaxObject.post("-------------------------------------");
	}
	
	public String name(){
		return name;
	}
	public int sortIndex(){
		return sortIndex;
	}
	public int inputSort(){
		return inputSort;
	}
	public int zone(){
		return zone;
	}
	public boolean canDouble(){
		return canDouble;
	}
	public void setInputSort(int i){
		inputSort = i;
	}
	public int active(){
		return active;
	}
	public void setActive(int i){
		active = i;
	}
	
// privates ----------------------------------------------------------------------------------
	
	private Embellishment[] makeEmbellishmentOptions(ED[] edArr){
		Embellishment[] embArr = new Embellishment[edArr.length];
		for (int i = 0; i < edArr.length; i++){
			if (edArr[i].type.equals("s")){
				embArr[i] = new SemitoneEmbellishment(edArr[i].value);
			} else if (edArr[i].type.equals("d")){
				embArr[i] = new DiatonicEmbellishment(edArr[i].value);
			} else if (edArr[i].type.equals("c")){
				embArr[i] = new ChordToneEmbellishment(edArr[i].value);
			}
				
		}
		return embArr;
	}
	private double makeWeightingSum(){
		double sum = 0.0;
		for (double dd: weighting){
			sum += dd;
		}
		return sum;
	}
	private void sortOutWeighting(int length, double[] weighting){
		if (weighting.length != length){
			double[] newWeighting = new double[length];
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
	private int getWeightedIndex(PlayPlugArgument ppa){
		double r = ppa.rnd.next() * weightingSum;
		int index = -1;
		while (r > 0){
			index++;
			r -= weighting[index];								
		}
		return index;
	}
	private void splitPost(String str, String splitString){
		String[] strArr = str.split(splitString);
		for (String s: strArr){
			MaxObject.post(s);
		}
	}
	private void postPNL(PipelineNoteList pnl){
		MaxObject.post("PlugInBassAddEmbellishmentOne PipelineNoteList.toString -----------------");
		String[] splitPost = pnl.toString().split("\n");
		for (String str: splitPost){
			MaxObject.post(str);
		}
		
	}
}
