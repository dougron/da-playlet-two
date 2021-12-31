package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class TimeDisplayWidget {

	public int xpos = 0;
	public int ypos = 0;
	
	private int textHeight = 20;	
	public String textName = "Tahoma";
	public int textStyle = Font.BOLD;
	private Color shiftOnColor = new Color(255, 255, 255);
	private int bars = 0;
	private int beats = 0;
	
	public TimeDisplayWidget(int x, int y){
		xpos = x;
		ypos = y;
	}
	public void addObject(Graphics2D g2d){		

		Font font;
		g2d.setColor(shiftOnColor);
		font = new Font(textName, textStyle, textHeight);
		
		g2d.setFont(font);
		g2d.drawString(bars + " : " + beats, (int)xpos, (int)ypos);

	}
	public void setBarsAndBeats(int br, int bt){
		bars = br;
		beats = bt;
	}
}
