package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class OnOffButton extends GUIButton {
	
	private Color ringColor = new Color(150, 0, 150);
	private Color buttonOffColor = Color.GRAY;
	private Color buttonOnColor = Color.ORANGE;
	private BasicStroke ringStroke = new BasicStroke(3);
	private double onStrokeWidth = 2;
	private double offStrokeWidth = 1;
	private int ringDiameter = 13;
	private double lineDeviationOutFromRingCentre = 1.5;
	private int lineDeviationInFromRingCentre = 3;
	private int centreDiameter = 10;
	

	public OnOffButton(GUIObject parent, int xoffset, int yoffset){
		super(xoffset, yoffset, parent);
		basicColor = new Color(0, 0, 0);
		diameter = 18;
	}
	public void addExtraStuff(Graphics2D g2d, GUIObjectParams params, double x, double y){
//		makeRing(g2d, params, x, y);
		makeCentreColor(g2d, params, x, y);
	}
	public void buttonPressed(){
		state++;
		state = state % 2;
		if (state == 0){
			parent.parent().setOn(false);
		} else {
			parent.parent().setOn(true);
		}
		//System.out.println("State set to: " + state);
	}
	public void ghostButtonPressed(){
		ghostState++;
		ghostState = ghostState % 2;
	}
	
// privates ----------------------------------------------------------------------
	private void makeCentreColor(Graphics2D g2d, GUIObjectParams params, double x, double y){
		setCentreColor(g2d, params);
		setStrokeWidth(g2d, params);
		
		double diam = centreDiameter * parent.heightSizeMultiplier(params);
		
		g2d.draw(new Arc2D.Double(x - diam / 2, y - diam / 2, diam, diam, 60, -300, Arc2D.OPEN));
		double start = y - diam / 2 - (lineDeviationOutFromRingCentre * parent.heightSizeMultiplier(params));
		double end = y - diam / 2 + (lineDeviationInFromRingCentre * parent.heightSizeMultiplier(params));
		g2d.draw(new Line2D.Double(x, start, x, end));
	}
	private void setStrokeWidth(Graphics2D g2d, GUIObjectParams params){
		int finalWidth;
		if (params.type == GUIObjectParams.IS_OBJECT){
			if (state == 0){
				finalWidth = (int)(offStrokeWidth * parent.heightSizeMultiplier(params));
			} else {
				finalWidth = (int)(onStrokeWidth * parent.heightSizeMultiplier(params));
			}
		} else {
			if (ghostState == 0){
				finalWidth = (int)(offStrokeWidth * parent.heightSizeMultiplier(params));
			} else {
				finalWidth = (int)(onStrokeWidth * parent.heightSizeMultiplier(params));
			}
		}
		BasicStroke bs = new BasicStroke(finalWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(bs);
	}
	private void setCentreColor(Graphics2D g2d, GUIObjectParams params){
		if (params.type == GUIObjectParams.IS_OBJECT){
			if (state == 0){
				g2d.setColor(getColor(params, buttonOffColor));
			} else {
				g2d.setColor(getColor(params, buttonOnColor));
			}
		} else {
			if (ghostState == 0){
				g2d.setColor(getColor(params, buttonOffColor));
			} else {
				g2d.setColor(getColor(params, buttonOnColor));
			}
		}
	}
	private void makeRing(Graphics2D g2d, GUIObjectParams params, double x, double y){
		g2d.setColor(getColor(params, ringColor));
		g2d.setStroke(ringStroke);
		double diam = ringDiameter * parent.heightSizeMultiplier(params);
		
		g2d.draw(new Ellipse2D.Double(x - diam / 2, y - diam / 2, diam, diam));
	}
}
