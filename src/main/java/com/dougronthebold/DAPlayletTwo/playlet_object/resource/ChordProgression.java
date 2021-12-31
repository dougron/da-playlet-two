package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.resource;
import java.awt.Color;

import ResourceUtils.ChordForm;
import TestUtils.TestData;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Resource;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public class ChordProgression extends PlayletObject implements PlayletObjectInterface, Resource{

	public ChordProgression(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.CHORD_PROGRESSION, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(255, 200, 150, 255));
//		LiveClip lc = TestData.liveClipForTestForm();
//		PlugInLegato pl = new PlugInLegato(new double[]{1.0});
//		PipelineNoteList pnl = new PipelineNoteList(lc);
		ChordForm newcf = new ChordForm(TestData.liveClipForTestForm());
		setChordForm(newcf);
	}
	
}
