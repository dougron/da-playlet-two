package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;

public class GUIObjectParams {
	
	public Color bgColor; 
	public Color lineColor;
	public Color shadowColor;
	public Color infoPanelColor; 
	
	public int type;
	
	public double xpos;
	public double ypos;
	public int height;
	public int previousHeight;
	
	public RoundRectangle2D.Double rect;		//not a good way to do things. not sure why
	
	public boolean shiftOn;

	
	public GUIObjectParams(int type){
		if (type == IS_OBJECT){
			setObjectParams();
		} else if (type == IS_GHOST){
			setGhostParams();
		}
	}
	public boolean heightHasChanged(){
		if (height == previousHeight){
			return false;
		} else {
			previousHeight = height;
			return true;			
		}
	}
	public void makeIntoGhost(){
		setGhostParams();
	}
	public void makeIntoObject(){
		setObjectParams();
	}
	private void setObjectParams(){
		type = IS_OBJECT;
		bgColor = new Color(200, 200, 200, 255);
		lineColor = new Color(0, 0, 0, 255);
		shadowColor = new Color(0, 0, 0, 255);
		infoPanelColor = new Color(240, 240, 240, 255);
	}
	private void setGhostParams(){
		type = IS_GHOST;
		bgColor = new Color(200, 200, 200, 125);
		lineColor = new Color(0, 0, 0, 125);
		shadowColor = new Color(0, 0, 0, 125);
		infoPanelColor = new Color(240, 240, 240, 125);
	}
	
	
	public static final int IS_OBJECT = 0;
	public static final int IS_GHOST = 1; 
}
