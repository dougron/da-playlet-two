package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public class TriangleGUIObject extends GUIObject implements GUIObjectInterface{
	
	private double xCornerPercent = 0.4;
	private double yCornerPercent = 0.4;
	private GeneralPath gp;
	private int strokeWidth = 10;
	private double[] startPoint = new double[]{0.0, -30.};
	private double[][] cornerOffset = new double[][]{			
			new double[]{-15., 15.},
			new double[]{15., 15.}
	};
	public double height = 0;		// this added in to solve the errors on this referenbce
	
	public TriangleGUIObject(PlayletObjectInterface p){
		super(p);
		setName("Triangle");
	}
	public void addObject(Graphics2D g2d){
		double xsize = default_x_size + height * heightSizeFactor;
		double ysize = default_y_size + height * heightSizeFactor;
		double xcorner = xsize * xCornerPercent;
		double ycorner = ysize * yCornerPercent;
		double actualx = xpos() - xsize / 2;
		double actualy = ypos() - ysize / 2;

		makeShape(g2d);
//		makeHeightText(g2d, actualx, actualy);
//		makeInfoDot(xsize, ysize, xcorner, ycorner, g2d, actualx, actualy);
//		makeNameText(ysize, g2d, actualx, actualy);
		makeSelectionRing(xsize, ysize, g2d, actualx, actualy);
//		makeInfoPanel(xsize, xcorner, ycorner, g2d, actualx, actualy);
	}
	public void addShadow(Graphics2D g2d, Color bgColor){
//		double heightSize = height * heightSizeFactor;
//		int strokeFactor = strokeWidth / 2;
		double xsize = default_x_size + height * heightSizeFactor + strokeWidth;
		double ysize = default_y_size + height * heightSizeFactor + strokeWidth;
		double xcorner = (xsize + strokeWidth) * xCornerPercent;
		double ycorner = (ysize + strokeWidth) * yCornerPercent;
		double actualx = xpos() - xsize / 2;
		double actualy = ypos() - ysize / 2;

		RoundRectangle2D.Double rect1 = new RoundRectangle2D.Double(
				actualx + height * shadowXOffsetHeightFactor, 
				actualy + height * shadowYOffsetHeightFactor, 
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
	}
	public boolean isMouseOvered(int x, int y){
		if (gp.contains(x, y)){
			return true;
		}
		return false;
	}
// privates ---------------------------------------------------------------------------	
	private void makeSelectionRing(double xsize, double ysize, Graphics2D g2d, double actualx, double actualy){
		if (isSelected){
			g2d.setStroke(new BasicStroke(selectionStroke));
			g2d.setColor(selectionColor);
			int posOffset = selectionOffset / 2;
			double bxsize = xsize + selectionOffset;
			double bysize = ysize + selectionOffset;
			RoundRectangle2D.Double rect2 = new RoundRectangle2D.Double(
					actualx - posOffset, actualy - posOffset, bxsize, bysize, bxsize * xCornerPercent +posOffset, bysize * yCornerPercent + posOffset);
			g2d.draw(rect2);
		}
	}
	private void makeShape(Graphics2D g2d){
		gp = new GeneralPath();
		gp.moveTo(xpos() + (startPoint[0] + height * heightSizeFactor), ypos() + (startPoint[1] + height * heightSizeFactor));
		for (double[] dArr: cornerOffset){
			gp.lineTo(xpos() + (dArr[0] + height * heightSizeFactor), ypos() + (dArr[1] + height * heightSizeFactor));
		}
		gp.closePath();
		BasicStroke bs = new BasicStroke(strokeWidth);
		g2d.setStroke(bs);
		g2d.setColor(objParams.bgColor);
		g2d.fill(gp);
		g2d.setColor(objParams.lineColor);
		g2d.draw(gp);
	}

}

