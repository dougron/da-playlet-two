package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody;
import java.awt.Color;

import PlugIns.PlugInSyncopate;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Processor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class MelodySyncopator extends PlayletObject implements PlayletObjectInterface, Processor{

	
	
	private static final int min = 10;
	private static final int stepSize = 2;
	
	private double syncopationChance = 1.0;
	
	private PlugInSyncopate plug = new PlugInSyncopate(new double[]{0.5, -0.5}, new double[]{0.5, 0.5, 0.1, 0.2}, syncopationChance);
	
	public MelodySyncopator(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.LEAD_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(200, 50, 50, 255));
		guiObject.setHeightBehaviour(new String[]{"OFF", "1", "2", "3", "4"}, min, stepSize);
		guiObject.heightChangeForcesReRender = true;
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		//System.out.println("NEW RENDER ##############################################\n#################################################");
		for (int i = 0; i < (guiObject().height() - min) / stepSize; i++){
			//System.out.println("\niteration : " + i);
			plug.process(processedPNO.pnl(), processedPNO.ppa);
		}
		//System.out.println("MassProcessorOne is putting out these note positions:-------");
		//for (PipelineNoteObject p: processedPNO.pnl().pnoList){
		//	System.out.println(p.position + " with " + p.noteList.size() + " notes in  noteList embellishable = " + p.isEmbellishable);
		//}
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
}
