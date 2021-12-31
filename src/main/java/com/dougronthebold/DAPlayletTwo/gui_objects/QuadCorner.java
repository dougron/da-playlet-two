package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;


public class QuadCorner {

	public double x1;		
	public double y1;
	public double ctlx;		// this is the point
	public double ctly;
	public double x2;		
	public double y2;
	
	public QuadCorner(double x1, double y1, double ctlx, double ctly, double x2, double y2, double percent){
		this.x1 = ctlx + percent * (x1 - ctlx);
		this.y1 = ctly + percent * (y1 - ctly);
		this.ctlx = ctlx;
		this.ctly = ctly;
		this.x2 = ctlx + percent * (x2 - ctlx);
		this.y2 = ctly + percent * (y2 - ctly);
	}
	public void addCurveToGeneralPath(GeneralPath gp){
		gp.quadTo(ctlx, ctly, x2, y2);
	}
}
