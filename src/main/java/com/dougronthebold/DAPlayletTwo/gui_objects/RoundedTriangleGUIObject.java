package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
/*
 * VCurrently needs to be updated to RoundRectangle spec in terms of ghosts
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public class RoundedTriangleGUIObject extends GUIObject implements GUIObjectInterface{
	
	private double cornerPercent = 0.3;

	private GeneralPath gp;

	private double[][] cornerOffset = new double[][]{
			new double[]{0.0, -40.},
			new double[]{-30., 0.},
			new double[]{-20., 20.},
			new double[]{20., 20.},
			new double[]{30., 0.},
			new double[]{20., -40.},
	};
	public double height = 0;		// this added in to solve the errors on this referenbce
	
	public RoundedTriangleGUIObject(PlayletObjectInterface p){
		super(p);
		setName("Triangle");
	}
	public void addObject(Graphics2D g2d){
		double xsize = default_x_size + height * heightSizeFactor;
		double ysize = default_y_size + height * heightSizeFactor;
		double xcorner = xsize * cornerPercent;
		double ycorner = ysize * cornerPercent;
		double actualx = xpos() - xsize / 2;
		double actualy = ypos() - ysize / 2;

		makeShape(g2d);
//		makeHeightText(g2d, actualx, actualy);
//		makeInfoDot(g2d);
//		makeNameText(ysize, g2d, actualx, actualy);
		makeSelectionRing(xsize, ysize, g2d, actualx, actualy);
//		makeInfoPanel(xsize, xcorner, ycorner, g2d, actualx, actualy);
//		makeCentre(g2d);
	}
	public void addShadow(Graphics2D g2d, Color bgColor){
		GeneralPath genp = new GeneralPath();
		ArrayList<QuadCorner> cornerList = makeCorners(xpos() + height * shadowXOffsetHeightFactor, ypos() + height * shadowYOffsetHeightFactor, strokeWidth * heightSizeMultiplier() / 2);
		QuadCorner first = cornerList.get(0);
		QuadCorner last = cornerList.get(cornerList.size() - 1);
		genp.moveTo(last.x2, last.y2);
		first.addCurveToGeneralPath(genp);
		for (int i = 1; i < cornerList.size(); i++){
			QuadCorner now = cornerList.get(i);
			genp.lineTo(now.x1, now.y1);
			now.addCurveToGeneralPath(genp);
		}
		genp.closePath();
		int r = (objParams.shadowColor.getRed() - bgColor.getRed()) / 2 + bgColor.getRed();
		int g = (objParams.shadowColor.getGreen() - bgColor.getGreen()) / 2 + bgColor.getGreen();
		int b = (objParams.shadowColor.getBlue() - bgColor.getBlue()) / 2 + bgColor.getBlue();
		Color newBGColor = new Color(r, g, b, default_alpha);
		g2d.setColor(newBGColor);
		g2d.fill(genp);
		makeCircle(g2d);
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
			
			GeneralPath genp = new GeneralPath();
			ArrayList<QuadCorner> cornerList = makeCorners(xpos(), ypos(), 0.0);
			QuadCorner first = cornerList.get(0);
			QuadCorner last = cornerList.get(cornerList.size() - 1);
			genp.moveTo(last.x2, last.y2);
			first.addCurveToGeneralPath(genp);
			for (int i = 1; i < cornerList.size(); i++){
				QuadCorner now = cornerList.get(i);
				genp.lineTo(now.x1, now.y1);
				now.addCurveToGeneralPath(genp);
			}
			genp.closePath();
			g2d.draw(genp);
			
		}
	}
	private void makeShape(Graphics2D g2d){
		gp = new GeneralPath();
		ArrayList<QuadCorner> cornerList = makeCorners(xpos(), ypos(), 0.0);	// for the shape we need 0.0 extra
		QuadCorner first = cornerList.get(0);
		QuadCorner last = cornerList.get(cornerList.size() - 1);
		gp.moveTo(last.x2, last.y2);
		first.addCurveToGeneralPath(gp);
		for (int i = 1; i < cornerList.size(); i++){
			QuadCorner now = cornerList.get(i);
			gp.lineTo(now.x1, now.y1);
			now.addCurveToGeneralPath(gp);
		}
		gp.closePath();
		BasicStroke bs = new BasicStroke((int)(strokeWidth * heightSizeMultiplier()));
		g2d.setStroke(bs);
		g2d.setColor(objParams.bgColor);
		g2d.fill(gp);
		g2d.setColor(objParams.lineColor);
		g2d.draw(gp);
	}
	private ArrayList<QuadCorner> makeCorners(double centrex, double centrey, double extra){		
		ArrayList<QuadCorner> cornerList = new ArrayList<QuadCorner>();
		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < cornerOffset.length; i++){
			if (i == 0){
				startIndex = cornerOffset.length - 1;
				endIndex = i + 1;
			} else if (i == cornerOffset.length - 1){
				startIndex = i - 1;
				endIndex = 0;
			} else {
				startIndex = i - 1;
				endIndex = i + 1;
			}
	//		double x1 = centrex + cornerOffset[startIndex][0] + Math.signum(cornerOffset[startIndex][0]) * height * heightSizeFactor + Math.signum(cornerOffset[startIndex][0]) * extra;
	//		double y1 = centrey + cornerOffset[startIndex][1] + Math.signum(cornerOffset[startIndex][1]) * height * heightSizeFactor  + Math.signum(cornerOffset[startIndex][1]) * extra;
	//		double ctlx = centrex + cornerOffset[i][0] + Math.signum(cornerOffset[i][0]) * height * heightSizeFactor + Math.signum(cornerOffset[i][0]) * extra;
	//		double ctly = centrey + cornerOffset[i][1] + Math.signum(cornerOffset[i][1]) * height * heightSizeFactor + Math.signum(cornerOffset[i][1]) * extra;
	//		double x2 = centrex + cornerOffset[endIndex][0] + Math.signum(cornerOffset[endIndex][0]) * height * heightSizeFactor + Math.signum(cornerOffset[endIndex][0]) * extra;
	//		double y2 = centrey + cornerOffset[endIndex][1] + Math.signum(cornerOffset[endIndex][1]) * height * heightSizeFactor + Math.signum(cornerOffset[endIndex][1]) * extra;

			double x1 = centrex + cornerOffset[startIndex][0] * (height / baselineHeight * sizeAtZero + sizeAtZero) + Math.signum(cornerOffset[startIndex][0]) * extra;
			double y1 = centrey + cornerOffset[startIndex][1] * (height / baselineHeight * sizeAtZero + sizeAtZero)  + Math.signum(cornerOffset[startIndex][1]) * extra;
			double ctlx = centrex + cornerOffset[i][0] * (height / baselineHeight * sizeAtZero + sizeAtZero) + Math.signum(cornerOffset[i][0]) * extra;
			double ctly = centrey + cornerOffset[i][1] * (height / baselineHeight * sizeAtZero + sizeAtZero) + Math.signum(cornerOffset[i][1]) * extra;
			double x2 = centrex + cornerOffset[endIndex][0] * (height / baselineHeight * sizeAtZero + sizeAtZero) + Math.signum(cornerOffset[endIndex][0]) * extra;
			double y2 = centrey + cornerOffset[endIndex][1] * (height / baselineHeight * sizeAtZero + sizeAtZero) + Math.signum(cornerOffset[endIndex][1]) * extra;

			
			cornerList.add(new QuadCorner(x1, y1, ctlx, ctly, x2, y2, cornerPercent));
		}
		return cornerList;
	}
	private double heightSizeMultiplier(){
		// stop gap measure to solve errors.............
		return 1;
	}

}

