package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public class RoundRectangleGUIObject extends GUIObject implements GUIObjectInterface{
	
	private double xCornerPercent = 0.4;
	private double yCornerPercent = 0.4;
//	private RoundRectangle2D.Double rect1;
//	private RoundRectangle2D.Double ghostRect1;
	private int strokeWidth = 8;
	
	public RoundRectangleGUIObject(PlayletObjectInterface p){
		super(p);
		setName("RoundRect");
		infoDotX = 7.5;
		infoDotY = -7.5;
	}
	public void addObject(Graphics2D g2d, boolean shiftOn){
		objParams.shiftOn = shiftOn;
		add2DObjects(g2d, objParams);
	}
	public void addGhost(Graphics2D g2d, boolean shiftOn){
		if (hasGhost){
			ghostParams.shiftOn = shiftOn;
			add2DObjects(g2d, ghostParams);
		}
	}
	public void addShadow(Graphics2D g2d, Color bgColor, GUIObjectParams params){
//		double heightSize = height * heightSizeFactor;
//		int strokeFactor = strokeWidth / 2;
		double xsize = default_x_size * heightSizeMultiplier(params) + strokeWidth * heightSizeMultiplier(params);
		double ysize = default_y_size * heightSizeMultiplier(params) + strokeWidth * heightSizeMultiplier(params);
//		double xcorner = (xsize + strokeWidth) * xCornerPercent;
//		double ycorner = (ysize + strokeWidth) * yCornerPercent;
		double xcorner = xsize * xCornerPercent;
		double ycorner = ysize * yCornerPercent;
		double actualx = objParams.xpos - xsize / 2;
		double actualy = objParams.ypos - ysize / 2;

		RoundRectangle2D.Double rect1 = new RoundRectangle2D.Double(
				actualx + height() * shadowXOffsetHeightFactor, 
				actualy + height() * shadowYOffsetHeightFactor, 
				xsize, 
				ysize, 
				xcorner, 
				ycorner);
		int r = (objParams.shadowColor.getRed() - bgColor.getRed()) / 2 + bgColor.getRed();
		int g = (objParams.shadowColor.getGreen() - bgColor.getGreen()) / 2 + bgColor.getGreen();
		int b = (objParams.shadowColor.getBlue() - bgColor.getBlue()) / 2 + bgColor.getBlue();
		Color newBGColor = new Color(r, g, b, default_alpha);
		g2d.setColor(newBGColor);
		g2d.fill(rect1);
		makeCircle(g2d);
	}
	public boolean isMouseOvered(int x, int y){
		if (objParams.rect.contains(x, y)){
			return true;
		}
		return false;
	}
	public boolean isGhostMouseOvered(int x, int y){
		if (hasGhost){
			//System.out.println("has ghost");
			if (ghostParams.rect.contains(x, y)){
				//System.out.println("isGhostMouseOvered " + ghostRect1.x + ", " + ghostRect1.y);
				return true;
			} 
		} 
		return false;
	}

// privates ---------------------------------------------------------------------------	
	private void add2DObjects(Graphics2D g2d, GUIObjectParams params){
		double xsize = default_x_size * heightSizeMultiplier(params);
		double ysize = default_y_size * heightSizeMultiplier(params);
		double xcorner = xsize * xCornerPercent;
		double ycorner = ysize * yCornerPercent;
		double actualx = params.xpos - xsize / 2;
		double actualy = params.ypos - ysize / 2;

		makeRect(xsize, ysize, xcorner, ycorner, g2d, actualx, actualy, params);
		makeHeightText(g2d, actualx, actualy, params);
		makeInfoDot(g2d, params);
		makeNameText(ysize, g2d, actualx, actualy, params);
		makeButtons(g2d, params);
		makeSelectionRing(xsize, ysize, g2d, actualx, actualy, params);
		makeInfoPanel(xsize, xcorner, ycorner, g2d, actualx, actualy, params);
	}
	private void makeSelectionRing(double xsize, double ysize, Graphics2D g2d, double actualx, double actualy, GUIObjectParams params){
		if ((isSelected && params.type == GUIObjectParams.IS_OBJECT && !params.shiftOn) || 
				(isSelected && params.type == GUIObjectParams.IS_GHOST && params.shiftOn)){
			g2d.setStroke(new BasicStroke(selectionStroke));
			g2d.setColor(selectionColor);
			double posOffset = selectionOffset * heightSizeMultiplier(params) / 2;
			double bxsize = xsize + selectionOffset * heightSizeMultiplier(params) ;
			double bysize = ysize + selectionOffset * heightSizeMultiplier(params) ;
			RoundRectangle2D.Double rect2 = new RoundRectangle2D.Double(
					actualx - posOffset, actualy - posOffset, bxsize, bysize, bxsize * xCornerPercent +posOffset, bysize * yCornerPercent + posOffset);
			g2d.draw(rect2);
		}
	}
	private void makeRect(double xsize, double ysize, double xcorner, double ycorner, Graphics2D g2d, double actualx, double actualy, GUIObjectParams params){
		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(actualx, actualy, xsize, ysize, xcorner, ycorner);
//		if (params.type == GUIObjectParams.IS_OBJECT){
//			rect1 = rect;
//		} else if (params.type == GUIObjectParams.IS_GHOST){
//			ghostRect1 = rect;
//		}
		params.rect = rect;
		
		BasicStroke bs = new BasicStroke((int)(strokeWidth * heightSizeMultiplier(params)));
		g2d.setStroke(bs);
		g2d.setColor(params.bgColor);
		g2d.fill(rect);
		g2d.setColor(params.lineColor);
		g2d.draw(rect);
		makeCentre(g2d, params);
		
	}

}
