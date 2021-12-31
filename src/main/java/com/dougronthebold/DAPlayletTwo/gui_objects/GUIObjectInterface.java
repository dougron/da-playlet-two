package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public interface GUIObjectInterface {

	public void addObject();
	public void addGhost();
	public void addShadow();
	public double xpos();
	public double ypos();
	public double ghostXpos();
	public double ghostYpos();
	public void setName(String str);
	
	
	public void addObject(Graphics2D g2d, boolean shiftOn);
	public void addShadow(Graphics2D g2d, Color bgColor, GUIObjectParams params);
	public void addGhost(Graphics2D g2d, boolean shiftOn);
	public void addGhostConnector(Graphics2D g2d);
	public int height();
	public void setPos(double x, double y);
	public void changePos(double x, double y);
	public void changeGhostPos(double x, double y);
	public void setHeight(int h);
	public void incrementHeight(int notches, boolean fineControl);
	public void incrementGhostHeight(int notches, boolean fineControl);
	public boolean isSelected();
	public boolean isGhostSelected();
	public void setSelected(boolean b);
	public boolean isMouseOvered(int x, int y);
	public boolean isGhostMouseOvered(int x, int y);

	public void setShadowOffset(double x, double y);
	public void changeShadowOffset(double x, double Y);
	public double currentXSize();
	public double currentYSize();
	public void setMoveVector(double x, double y);
	public void moveOnVector(double multiplier);
	public boolean infoDotMouseOver(int x, int y);
	public void setInfoPanelVisible(boolean b);
	public boolean getInfoPanelVisible();
	public void setInfoText(String str);
	
	public void addButton(GUIButton gb);
	
	public PlayletObjectInterface parent();

	
	
}
