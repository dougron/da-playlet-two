package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic;
import java.awt.Color;

import ResourceUtils.TwoBarRhythmBuffer;
import TestUtils.TestData;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Resource;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public class RhythmBuffer extends PlayletObject implements PlayletObjectInterface, Resource{
	
	TwoBarRhythmBuffer newrb = new TwoBarRhythmBuffer();

	public RhythmBuffer(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.RHYTHM_BUFFER, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(150, 200, 255, 255));
//		TestData.fourOnFloorForRhythmBuffer(newrb);
		TestData.tangoForRhythmBuffer(newrb);
		setRhythmBuffer(newrb);
	}
}
