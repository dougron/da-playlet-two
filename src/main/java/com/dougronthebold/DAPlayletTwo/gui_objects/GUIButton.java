package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class GUIButton {
	
	public double xoffset;
	public double yoffset;
	public Color basicColor = new Color(100, 100, 255);
	public int diameter = 10;
	public Ellipse2D.Double button;
	public Ellipse2D.Double ghostButton;
	public GUIObject parent;
	public int mode = TOGGLE;
	public int state = 0;
	public int ghostState = 0;

	public GUIButton(double xoffset, double yoffset, GUIObject guiobj){
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		parent = guiobj;
	}
	public void addButton(Graphics2D g2d, GUIObjectParams params){
		g2d.setColor(getColor(params, basicColor));
		double size = diameter * parent.heightSizeMultiplier(params);
		double x = params.xpos + (xoffset * parent.heightSizeMultiplier(params));
		double y = params.ypos + (yoffset * parent.heightSizeMultiplier(params));
		if (params.type == GUIObjectParams.IS_OBJECT){
			button = new Ellipse2D.Double(x - size / 2, y - size / 2, size, size);
		} else {
			ghostButton = new Ellipse2D.Double(x - size / 2, y - size / 2, size, size);
		}
		
		g2d.fill(button);
		addExtraStuff(g2d, params, x, y);
	}
	public void addExtraStuff(Graphics2D g2d, GUIObjectParams params, double x, double y){
		// for overiding in the children
	}
	public boolean isMouseOvered(int x, int y){
		return button.contains(x, y);
	}
	public boolean isGhostMouseOvered(int x, int y){
		if (ghostButton != null){
			return ghostButton.contains(x, y);
		} else {
			return false;
		}
		
	}
	public void buttonPressed(){
		// overide for behaviour when pressed
	}
	public void ghostButtonPressed(){
		// overide for behaviour when pressed
	}
	public Color getColor(GUIObjectParams params, Color c){
		int alpha;
		if (params.type == params.IS_GHOST){
			alpha = GHOST_ALPHA;
		} else {
			alpha = NORMAL_ALPHA;
		}
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	public void swopObjectAndGhost(){
		int x = ghostState;
		ghostState = state;
		state = x;
	}
	public void makeGhost(){
		ghostState = state;
	}
// privates -------------------------------------------------------------------
	
	
	
	private static final int GHOST_ALPHA = 127;
	private static final int NORMAL_ALPHA = 255;
	private static final double OFFSET_SIZE_FACTOR = 0.75;
	
	public static final int TRIGGER = 0;
	public static final int TOGGLE = 1;
	
}
