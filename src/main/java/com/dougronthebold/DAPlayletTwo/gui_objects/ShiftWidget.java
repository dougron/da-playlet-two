package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class ShiftWidget{

	private double xpos;
	private double ypos;
	private double xsize;
	private double ysize;
	private double xOffset = 2;
	private Color widgetColor = new Color(0, 0, 0);
	private Color shiftOffColor = new Color(50, 50, 50);
	private Color shiftOnColor = new Color(255, 255, 255);
	
	public boolean shiftOn = false;
	
	private int textHeight = 10;	
	public String textName = "Tahoma";
	public int textOnStyle = Font.BOLD;
	public int textOffStyle = Font.BOLD;
	private Ellipse2D.Double shiftWidget;
	
	public ShiftWidget(double x, double y, double xsize, double ysize){
		xpos = x;
		ypos = y;
		this.xsize = xsize;
		this.ysize = ysize;
		shiftWidget = new Ellipse2D.Double(xpos, ypos, xsize, ysize);
	}
	public void addObject(Graphics2D g2d){		
		g2d.setColor(widgetColor);
		g2d.fill(shiftWidget);
		Font font;
		if (shiftOn){
			g2d.setColor(shiftOnColor);
			font = new Font(textName, textOnStyle, textHeight);
		} else {
			g2d.setColor(shiftOffColor);
			font = new Font(textName, textOffStyle, textHeight);
		}
		
		g2d.setFont(font);
		g2d.drawString("SHIFT", (int)(xpos + xOffset), (int)(ypos + ysize / 2 + textHeight / 2));

	}
	public void setShift(boolean b){
		shiftOn = b;
	}
	public void toggleShift(){
		if (shiftOn){
			shiftOn = false;
		} else {
			shiftOn = true;
		}
	}
	public boolean isMouseOvered(int x, int y){
		if (shiftWidget.contains(x, y)){
			return true;
		} else {
			return false;
		}
	}
}
