package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.geom.Ellipse2D;


public class NewRndSequenceButton extends GUIButton {
	
	private Color textColor = new Color(150, 0, 150);
//	private Color buttonOffColor = Color.GRAY;
	private Color buttonOnColor = Color.ORANGE;
	
	private static final long buttonOnLength = 500;			// time in milliseconds
	private long offTime = 0;
//	private BasicStroke ringStroke = new BasicStroke(3);
//	private double onStrokeWidth = 2;
//	private double offStrokeWidth = 1;
//	private int ringDiameter = 13;
//	private double lineDeviationOutFromRingCentre = 1.5;
//	private int lineDeviationInFromRingCentre = 3;
	private int centreDiameter = 10;
	

	public NewRndSequenceButton(GUIObject parent, int xoffset, int yoffset){
		super(xoffset, yoffset, parent);
		basicColor = new Color(0, 0, 0);
		diameter = 18;
		mode = GUIButton.TRIGGER;
	}
	public void addExtraStuff(Graphics2D g2d, GUIObjectParams params, double x, double y){

		makeCentreColor(g2d, params, x, y);
	}
	public void buttonPressed(){
		offTime = System.currentTimeMillis() + buttonOnLength;
		parent.parent().newRNDSequence();

	}
	public void ghostButtonPressed(){

	}
	
// privates ----------------------------------------------------------------------
	private void makeCentreColor(Graphics2D g2d, GUIObjectParams params, double x, double y){
		if (params.type == GUIObjectParams.IS_OBJECT && System.currentTimeMillis() < offTime){
			g2d.setColor(getColor(params, buttonOnColor));

			double diam = centreDiameter * parent.heightSizeMultiplier(params);
			
			g2d.fill(new Ellipse2D.Double(x - diam / 2, y - diam / 2, diam, diam));
		}


	}



}
