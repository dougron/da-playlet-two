package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.ButtonInstruction;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.GUIButton;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.GUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.GUIObjectParams;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;


public class MelodyNoteDensityButton extends GUIButton {
	
	
//	private Color buttonOffColor = Color.GRAY;
//	private Color buttonOnColor = Color.ORANGE;
//	private BasicStroke ringStroke = new BasicStroke(3);
//	private double onStrokeWidth = 2;
//	private double offStrokeWidth = 1;
//	private int ringDiameter = 13;
//	private double lineDeviationOutFromRingCentre = 1.5;
//	private int lineDeviationInFromRingCentre = 3;
//	private int centreDiameter = 10;
	
	private String textName = "Tahoma";
	private int textStyle = Font.BOLD;
	private int fontSize = 10;
	private Color textColor = new Color(150, 200, 150);
	
	private String[] optionArr = new String[]{"1n", "2n", "4n"};
	private double[] lengthArr = new double[]{4.0, 2.0, 1.0};
	private int optionIndex = 0;
	

	public MelodyNoteDensityButton(GUIObject parent, int xoffset, int yoffset){
		super(xoffset, yoffset, parent);
		basicColor = new Color(0, 0, 0);
		diameter = 18;
	}
	public void addExtraStuff(Graphics2D g2d, GUIObjectParams params, double x, double y){
		makeText(g2d, params, x, y);
	}
	public void buttonPressed(){
		optionIndex++;
		optionIndex = optionIndex % 3;
		ButtonInstruction bi = new ButtonInstruction(PlayletObject.BUTTON_SET_NOTE_DENSITY);
		bi.doubleParam = lengthArr[optionIndex];
		parent.parent().doButtonInstruction(bi);
	
	}
	public void ghostButtonPressed(){
		ghostState++;
		ghostState = ghostState % 2;
	}
	
// privates ----------------------------------------------------------------------
	private void makeText(Graphics2D g2d, GUIObjectParams params, double x, double y){
		Font font = new Font(textName, textStyle, (int)(fontSize * (parent.heightSizeMultiplier(params))));
		double textx = params.xpos + ((xoffset - (fontSize / 2)) * parent.heightSizeMultiplier(params));
		double texty = params.ypos + ((yoffset + (fontSize / 2)) * parent.heightSizeMultiplier(params));
		g2d.setFont(font);
		g2d.setColor(textColor);
//		g2d.drawString("" + height, (int)actualx + strokeWidth, (int)actualy + strokeWidth + font.getSize());
		g2d.drawString(optionArr[optionIndex], (int)textx, (int)texty);
	}



}
