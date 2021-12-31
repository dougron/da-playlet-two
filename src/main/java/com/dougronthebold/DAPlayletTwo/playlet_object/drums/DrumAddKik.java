package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums;
import java.awt.Color;

import PipelineUtils.PipelineNoteList;
import PipelineUtils.PipelinePlugIn;
import PipelineUtils.PlayPlugArgument;
import PlugIns.PlugInKik58Euclidean;
import PlugIns.PlugInKikFourOnFloor;
import PlugIns.PlugInKikFromRhythmBuffer;
import PlugIns.PlugInKikFunkOne;
import PlugIns.PlugInKikTwoOnFloor;
import ResourceUtils.ResourceObjectParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Generator;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class DrumAddKik extends PlayletObject implements PlayletObjectInterface, Generator, ResourceObjectParent{
	

	private PipelinePlugIn[] plugArr = new PipelinePlugIn[]{
			new PlugInKikFromRhythmBuffer(),
			new PlugInKikTwoOnFloor(),
			new PlugInKik58Euclidean(),
			new PlugInKikFunkOne(),
			new PlugInKikFourOnFloor()
	};
	
	private static final int min = 10;
	private static final int stepSize = 2;
	
	public DrumAddKik(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.DRUM_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(50, 50, 200, 255));
		guiObject.setHeightBehaviour(new String[]{"noKIK", "RB", "2/2", "5/8Euc", "funk", "4/4"}, min, stepSize);
		guiObject.heightChangeForcesReRender = true;
	}
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		if (processedPNO.ppa.hasRhythmBuffer()){
			process(processedPNO.pnl(), processedPNO.ppa);
		}		
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
	private void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		int index = (guiObject().objParams.height - min) / stepSize;
		if (index > 0 && index < plugArr.length + 1){
			plugArr[index - 1].process(pnl, ppa);
		}
		
	}
}
