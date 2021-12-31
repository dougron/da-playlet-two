package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master;
import java.awt.Color;

import PipelineUtils.PipelineNoteList;
import ResourceUtils.ResourceObjectParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Generator;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class MasterGenerator extends PlayletObject implements PlayletObjectInterface, Generator, ResourceObjectParent{
	
//	private ResourceObject ro = new ResourceObject();
//	private PlugInBassFromRhythmBuffer plug = new PlugInBassFromRhythmBuffer();
	
	
	public MasterGenerator(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.MASTER_GENERATOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(50, 50, 200, 255));
	}
	public void render(){
		receivedPNO = makePNO();
		processedPNO = process(receivedPNO.copy());
		sendPNOCopyToOuts(processedPNO.copy());
	}

	


	
// privates -------------------------------------------------------
//	private void sendPNOCopyToOuts(PNO pno){
//		
//	}
	private PNO process(PNO pno){
//		ppa.dynamic = dynamic;
//		ppa.rb = getRhythmBufferFromResourceLayer();
//		ppa.cf = getChordFormFromResourceLayer();
//		plug.process(pno.pnl(), ppa);
		return pno;
	}
//	private ChordForm getChordFormFromResourceLayer(){
//		for (Wire w: inWireList()){
//			if (w.source.type() == PlayletObject.CHORD_PROGRESSION){
//				return w.source.getPPA().cf;
//			}
//		}
//		return new ChordForm(new LiveClip(0, 0));
//	}
//	private TwoBarRhythmBuffer getRhythmBufferFromResourceLayer(){
//		for (Wire w: inWireList()){
//			if (w.source.type() == PlayletObject.RHYTHM_BUFFER && w.on){
//				return w.source.getPPA().rb;
//			}
//		}
//		return new TwoBarRhythmBuffer();
//	}
	private PNO makePNO(){
		PNO newPNO = new PNO();
		newPNO.setPNL(new PipelineNoteList(PlayletObject.MASTER_CHANNEL));
		return newPNO;
	}
}
