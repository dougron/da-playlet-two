package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class TestButton extends GUIButton {
	
	private Color ringColor = new Color(150, 0, 150);
	private Color buttonOffColor = new Color(10, 10, 10);
	private Color buttonOnColor = new Color(0, 150, 0);
	private BasicStroke ringStroke = new BasicStroke(2);
	private int ringDiameter = 13;
	private int centreDiameter = 10;
	

	public TestButton(GUIObject parent, int xoffset, int yoffset){
		super(xoffset, yoffset, parent);
		basicColor = new Color(0, 0, 0);
		diameter = 18;
	}
	public void addExtraStuff(Graphics2D g2d, GUIObjectParams params, double x, double y){
		makeRing(g2d, params, x, y);
		makeCentreColor(g2d, params, x, y);
	}
	public void buttonPressed(){
		state++;
		state = state % 2;
		//System.out.println("State set to: " + state);
	}
	public void ghostButtonPressed(){
		ghostState++;
		ghostState = ghostState % 2;
	}
	
// privates ----------------------------------------------------------------------
	private void makeCentreColor(Graphics2D g2d, GUIObjectParams params, double x, double y){
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
		
		
		
		double diam = centreDiameter * parent.heightSizeMultiplier(params);
		
		g2d.fill(new Ellipse2D.Double(x - diam / 2, y - diam / 2, diam, diam));
	}
	private void makeRing(Graphics2D g2d, GUIObjectParams params, double x, double y){
		g2d.setColor(getColor(params, ringColor));
		g2d.setStroke(ringStroke);
		double diam = ringDiameter * parent.heightSizeMultiplier(params);
		
		g2d.draw(new Ellipse2D.Double(x - diam / 2, y - diam / 2, diam, diam));
	}
}
