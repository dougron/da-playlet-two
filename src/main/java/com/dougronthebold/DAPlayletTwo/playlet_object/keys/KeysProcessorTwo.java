package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys;
import PipelineUtils.PipelineNoteList;
import PipelineUtils.PipelinePlugIn;
import PipelineUtils.PlayPlugArgument;
import PlugIns.ED;
import PlugIns.PlugInAddEmbellishmentRhythm;
import PlugIns.PlugInAssignEmbellishment;
import PlugIns.PlugInLegato;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class KeysProcessorTwo extends PlayletObject implements PlayletObjectInterface{
	
	private PipelinePlugIn[] plugArr; 

	public KeysProcessorTwo(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.KEYS_PROCESSOR, x, y, uniqueName, pp);
		plugArr = new PipelinePlugIn[]{
				new PlugInAddEmbellishmentRhythm(new double[]{-0.5},
						new double[]{1.0},
							1.0),
				new PlugInAssignEmbellishment(new ED[]{new ED("d", -1)},
						new double[]{1.0}),
				new PlugInLegato(new double[]{1.0})
		};
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		if (processedPNO.ppa.hasChordProgression()){
			process(processedPNO.pnl(), processedPNO.ppa);
		}		
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
	
	private void process(PipelineNoteList pnl, PlayPlugArgument ppa){
		for (PipelinePlugIn ppi: plugArr){
			ppi.process(pnl, ppa);
		}
	}
	
	
}
