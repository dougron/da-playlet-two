package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic;
import java.awt.Color;

import DataObjects.contour.FourPointContour;
import ResourceUtils.ContourPack;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.Resource;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class Contour extends PlayletObject implements PlayletObjectInterface, Resource{
	
	private ContourPack contourPack;

	public Contour(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.CONTOUR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(200, 255, 150, 255));
		makeContourPack();
	}
	public ContourPack getContourPack(){
		return contourPack;
	}
	
	
// privates ---------------------------------------------------------------
	private void makeContourPack(){
		contourPack = new ContourPack(new FourPointContour(0.5, 1.0, -1.0));
		contourPack.addToContourList(new FourPointContour(FourPointContour.UP));
		contourPack.addToContourList(new FourPointContour(FourPointContour.DOWN));
		contourPack.addToContourList(new FourPointContour(FourPointContour.UPDOWN));
		contourPack.addToContourList(new FourPointContour(FourPointContour.DOWNUP));
	}
}
