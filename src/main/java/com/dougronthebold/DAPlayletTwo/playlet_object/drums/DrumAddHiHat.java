package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums;
import java.awt.Color;

import PipelineUtils.PipelineNoteList;
import PipelineUtils.PipelinePlugIn;
import PipelineUtils.PlayPlugArgument;
import PlugIns.PlugInHatOff;
import PlugIns.PlugInHatOffAccent;
import PlugIns.PlugInHatOn;
import ResourceUtils.ResourceObjectParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Generator;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class DrumAddHiHat extends PlayletObject implements PlayletObjectInterface, Generator, ResourceObjectParent{
	

	private PipelinePlugIn[][] plugArr = new PipelinePlugIn[][]{
			new PipelinePlugIn[]{new PlugInHatOn()},
			new PipelinePlugIn[]{new PlugInHatOff()},
			new PipelinePlugIn[]{new PlugInHatOn(), new PlugInHatOff()},
			new PipelinePlugIn[]{new PlugInHatOn(), new PlugInHatOff(), new PlugInHatOffAccent()}
	};
	
	private static final int min = 10;
	private static final int stepSize = 2;
	
	public DrumAddHiHat(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.DRUM_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(50, 50, 200, 255));
		guiObject.setHeightBehaviour(new String[]{"off", "onhat", "offhat", "on+off", "onoff-acc"}, min, stepSize);
		guiObject.heightChangeForcesReRender = true;
	}
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		process(processedPNO.pnl(), ppa);
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
	private void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		int index = (guiObject().objParams.height - min) / stepSize;
		if (index > 0 && index < plugArr.length + 1){
			PipelinePlugIn[] tempArr = plugArr[index - 1];
			for (PipelinePlugIn ppi: tempArr){
				ppi.process(pnl, ppa);
			}
		}
		
	}
}