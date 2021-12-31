package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Comparator;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import PipelineUtils.PipelinePlugIn;
import UDPUtils.OSCMessMaker;
import UDPUtils.UDPConnection;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.StaticNumber;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

/*
 * Does all the visual stuff and interactions
 */
public class GUIObject {
	
	private PlayletObjectInterface parent;
	
//	public int height = 1;
//	public int previousHeight = 1;
//	private String name;
	private int debugNumber;
	public boolean isSelected = false;
	public boolean isGhostSelected = false;
	public int selectionStroke = 2;
	public int selectionOffset = 0;
	public Color selectionColor = new Color(255, 0, 0);
	public static double shadowXOffsetHeightFactor = 2.0;
	public static double shadowYOffsetHeightFactor = 2.0;
	public int default_alpha = 128;
	public int default_x_size = 50;
	public int default_y_size = 50;
	public double heightSizeFactor = 0.25;
	public int strokeWidth = 7;
	
	private Ellipse2D.Double infoDot;
	private double xVector = 0.0;
	private double yVector = 0.0;
	private PipelinePlugIn plug;
	private boolean infoPanelVisible = false;
	private double infoPanelXsize = 100.0; 
	private double infoPanelYsize = 60.0;
	private double infoPanelXcorner = 20; 
	private double infoPanelYcorner = 20;
	
//	public Color bgColor = new Color(200, 200, 200);
//	public Color lineColor = new Color(0, 0, 0);
//	public Color shadowColor = new Color(0, 0, 0);
//	public Color infoPanelColor = new Color(240, 240, 240);
	public GUIObjectParams objParams = new GUIObjectParams(GUIObjectParams.IS_OBJECT);
	public GUIObjectParams ghostParams = new GUIObjectParams(GUIObjectParams.IS_GHOST);

	
//	private int moduleType = -1;
	private String name = "zzz";
	private String infoText = "";
	private Font infoFont = new Font("Tahoma", Font.PLAIN, 8);
	private Color infoTextColor = new Color(0, 0, 0);
	private int infoTextIndent = 10;
	public String textName = "Tahoma";
	public int textStyle = Font.BOLD;
	public int fontSize = 8;
	public double infoDotX = 10.0;
	public double infoDotY = -10.0;
	public double infoDotSize = 10.0;
	public static double baselineHeight = 25.0;		// this is the height that is treated as 1.0 in all calulations
	public static double sizeAtZero = 0.8;
	public double heightTextX = -17.5;
	public double heightTextY = -10;
	
	public ArrayList<GUIButton> buttonList = new ArrayList<GUIButton>();
	
	private HeightBehaviour hb = new HeightBehaviour(6, 60);
	
	private Color ghostConnectorColor = new Color(0, 0, 0, 50);
	private BasicStroke ghostConnectorStroke = new BasicStroke(
			10,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND,
			10.0f,
			new float[]{20.0f, 20.0f},
			0.0f);
	
	public boolean hasGhost = false;
	
	public boolean heightChangeForcesReRender = false;
	public boolean heightReRender = false;

	public GUIObject(PlayletObjectInterface p) {
		parent = p;
		debugNumber = StaticNumber.nextInt();
	}
	public GUIObject(double x, double y) {
		objParams.xpos = x;
		objParams.ypos = y;
	}
	public void addObject(){
		
	}
	public void addGhost(){
		
	}
	public void addShadow(){
		
	}
	public double xpos(){
		return objParams.xpos;
	}
	public double ypos(){
		return objParams.ypos;
	}
	public void setXPos(double x){
		objParams.xpos = x;
	}
	public void setYPos(double y){
		objParams.ypos = y;
	}
	public void setPos(double x, double y){
		objParams.xpos = x;
		objParams.ypos = y;
	}
	public double ghostXpos(){
		if (hasGhost){
			return ghostParams.xpos;
		} else {
			return objParams.xpos;
		}		
	}
	public double ghostYpos(){
		if (hasGhost){
			return ghostParams.ypos;
		} else {
			return objParams.ypos;
		}		
	}
	public void setGhostXPos(double x){
		ghostParams.xpos = x;
	}
	public void setGhostYPos(double y){
		ghostParams.ypos = y;
	}
	public void setGhostPos(double x, double y){
		ghostParams.xpos = x;
		ghostParams.ypos = y;
	}
	public void setHasGhost(boolean b){
		hasGhost = b;
	}
	
	public void setName(String str){
		name = str;
	}
	public void addButton(GUIButton gb){
		buttonList.add(gb);
	}
// height stuff--------------------------------------------------
	public void setHeight(int i){
		objParams.height = i;
//		System.out.println("parent.type: " + parent.type());
		testHeight();
		
	}
	public void setInitialHeight(int i){
		objParams.height = i;
		testHeight();
		objParams.previousHeight = objParams.height;
	}
	public void setGhostHeight(int i){
		ghostParams.height = i;
//		System.out.println("parent.type: " + parent.type());
		testGhostHeight();
		
	}
//	public void incrementHeightOLD(int h){
//		objParams.height += h;
//		testHeight();
//	}
	public void incrementHeight(int notches, boolean fineControl){
		objParams.height += hb.getIncrement(notches, fineControl);
		testHeight();
	}
	public void incrementGhostHeight(int notches, boolean fineControl){
		ghostParams.height += hb.getIncrement(notches, fineControl);
		testGhostHeight();
	}
	public void setHeightBehaviour(int behave){
		hb = new HeightBehaviour(behave);
	}
	public void setHeightBehaviour(String[] strArr, int min, int stepSize){
		hb = new HeightBehaviour(strArr, min, stepSize);
	}
// ------------------------------------------------------------------
	public String toString(){
		return name + " debug_" + debugNumber;
	}
	public void addObject(Graphics2D g2d, boolean shiftOn){
		// must be overridden in children
	}
	public void addGhost(Graphics2D g2d, boolean shiftOn){
		// must be overridden in children
	}
	public void addShadow(Graphics2D g2d, Color bgColor, GUIObjectParams params){
		// must be overridden in children
	}
	public int height(){
		return objParams.height;
	}

	public int ghostHeight(){
		return ghostParams.height;
	}
	public void changePos(double x, double y){
		objParams.xpos  = x;
		objParams.ypos = y;
	}
	public void changeGhostPos(double x, double y){
		ghostParams.xpos  = x;
		ghostParams.ypos = y;
	}

	public boolean isSelected(){
		return isSelected;
	}
	public boolean isGhostSelected(){
		return isGhostSelected;
	}
	public void setSelected(boolean b){
		isSelected = b;
	}
	public void setBGColor(Color c){
		objParams.bgColor = c;
	}
	public boolean isMouseOvered(int x, int y){
//		if (rect1.contains(x, y)){
//			return true;
//		}
		return false;
	}
	public boolean isGhostMouseOvered(int x, int y){
		// needs to be overridden
		return false;
	}

	public void setShadowOffset(double x, double y){
		shadowXOffsetHeightFactor = x;
		shadowYOffsetHeightFactor = y;
	}
	public void changeShadowOffset(double x, double y){
		shadowXOffsetHeightFactor += x;
		shadowYOffsetHeightFactor += y;
	}
	public double currentXSize(){
		return default_x_size + objParams.height * heightSizeFactor;
	}
	public double currentYSize(){
		return default_y_size + objParams.height * heightSizeFactor;
	}
	public void setMoveVector(double x, double y){
		xVector = x;
		yVector = y;
	}
	public void moveOnVector(double multiplier){
		objParams.xpos += xVector * multiplier;
		objParams.ypos += yVector * multiplier;
	}
	
	public boolean infoDotMouseOver(int x, int y){
		if (infoDot.contains(x, y)){
			return true;
		} else {
			return false;
		}
	}
	public void setInfoPanelVisible(boolean b){
		infoPanelVisible = b;
	}
	public boolean getInfoPanelVisible(){
		return infoPanelVisible;
	}
	public void setInfoText(String str){
		infoText = str;
	}

	public static Comparator<PlayletObjectInterface> yPosComparator = new Comparator<PlayletObjectInterface>(){
		public int compare(PlayletObjectInterface plobj1, PlayletObjectInterface plobj2){
			if (plobj1.guiObject().ypos() < plobj2.guiObject().ypos()) return -1;
			if (plobj1.guiObject().ypos() > plobj2.guiObject().ypos()) return 1;
			return 0;
		}
	};
	public static Comparator<GUIObject> heightComparator = new Comparator<GUIObject>(){
		public int compare(GUIObject obj1, GUIObject obj2){
			if (obj1.height() < obj2.height()) return -1;
			if (obj1.height() > obj2.height()) return 1;
			return 0;
		}
	};
	public PlayletObjectInterface parent(){
		return parent;
	}
// stuff for objects extending GUIObject but part of GUIObjectInterface
	public double heightSizeMultiplier(GUIObjectParams gop){
		return gop.height / baselineHeight * sizeAtZero + sizeAtZero;
	}
	public void makeHeightText(Graphics2D g2d, double actualx, double actualy, GUIObjectParams params){
		Font font = new Font(textName, textStyle, (int)(fontSize * (heightSizeMultiplier(params))));
		double textx = params.xpos + (heightTextX * (heightSizeMultiplier(params)));
		double texty = params.ypos + (heightTextY * (heightSizeMultiplier(params)));
		g2d.setFont(font);
//		g2d.drawString("" + height, (int)actualx + strokeWidth, (int)actualy + strokeWidth + font.getSize());
		g2d.drawString(hb.heightForGUI(params), (int)textx, (int)texty);
	}
	public void makeInfoDot(Graphics2D g2d, GUIObjectParams params){
		// top right quadrant
		double dotx = params.xpos + (infoDotX * heightSizeMultiplier(params));		
		double xs = infoDotSize * (heightSizeMultiplier(params));
		double doty = params.ypos + (infoDotY * heightSizeMultiplier(params)) - xs;
//		double ys = infoDotSize + height * heightSizeFactor;
		infoDot = new Ellipse2D.Double(dotx, doty, xs, xs);
		g2d.fill(infoDot);
	}
	public void makeCentre(Graphics2D g2d, GUIObjectParams params){
		// for debugging only really
		BasicStroke bs = new BasicStroke(1);
		g2d.setStroke(bs);
		g2d.draw(new Line2D.Double(params.xpos - 20, params.ypos, params.xpos + 20, params.ypos));
		g2d.draw(new Line2D.Double(params.xpos, params.ypos - 20, params.xpos, params.ypos + 20));
	}
	public void makeCircle(Graphics2D g2d){
		shadowCircle(g2d, PlayletObject.maxProcessorOutWireLength);
		shadowCircle(g2d, PlayletObject.maxProcessorOutWireLength * 0.5);
	}
	public void shadowCircle(Graphics2D g2d, double radius){
		double cx = xpos() - radius;
		double cy = ypos() - radius;
		BasicStroke bs = new BasicStroke(1);
		g2d.setStroke(bs);
		g2d.draw(new Ellipse2D.Double(cx, cy, radius * 2, radius * 2));
	}
	public void makeNameText(double ysize, Graphics2D g2d, double actualx, double actualy, GUIObjectParams params){
		Font font = new Font(textName, textStyle, (int)(fontSize * (heightSizeMultiplier(params))));	//(int)(fontSize + params.height * heightSizeFactor / 5));
		g2d.setFont(font);
		g2d.drawString("" + parent.uniqueName(), (int)actualx + strokeWidth, (int)(actualy + ysize - strokeWidth - font.getSize()));
	}
	public void makeInfoPanel(double xsize, double xcorner, double ycorner, Graphics2D g2d, double actualx, double actualy, GUIObjectParams params){
		if (infoPanelVisible){
//			double ix = actualx + xsize - strokeWidth;
//			double iy = actualy - infoPanelYsize + strokeWidth;
					
			double xs = infoDotSize * (heightSizeMultiplier(params));
			double ix = xpos() + (infoDotX * heightSizeMultiplier(params)) + xs / 2;
			double iy = ypos() + (infoDotY * heightSizeMultiplier(params)) - infoPanelYsize - xs / 2;
			RoundRectangle2D.Double infoRect = new RoundRectangle2D.Double(ix, iy, infoPanelXsize, infoPanelYsize, infoPanelXcorner, infoPanelYcorner);
			g2d.setColor(objParams.infoPanelColor);
			g2d.fill(infoRect);
		// info text................
			g2d.setFont(infoFont);
			g2d.setColor(infoTextColor);
			g2d.drawString(infoText, (int)ix + infoTextIndent, (int)iy + infoTextIndent + infoFont.getSize());

		}
	}
	public void addGhostConnector(Graphics2D g2d){
		if (hasGhost){
			g2d.setStroke(ghostConnectorStroke);
			g2d.setColor(ghostConnectorColor);
			g2d.drawLine((int)objParams.xpos, (int)objParams.ypos, (int)ghostParams.xpos, (int)ghostParams.ypos);
		}
	}
	public boolean heightHasChanged(){
		if (heightChangeForcesReRender){
			if (parent.hasNoIns()){
				return false;
			} else {
				return objParams.heightHasChanged();
			}			
		} else {
			return false;
		}	
	}
	public boolean ghostHeightHasChanged(){
		return ghostParams.heightHasChanged();
	}
	public void heightChangeAction(){
		objParams.previousHeight = objParams.height;
	}
	public void ghostHeightChangeAction(){
		ghostParams.previousHeight = ghostParams.height;
	}
	public void instrumentHeightChangeAction(UDPConnection conn){
		objParams.previousHeight = objParams.height;
		OSCMessMaker mess = new OSCMessMaker();
		mess.addItem(DeviceParamInfo.adjustString);
		mess.addItem(DeviceParamInfo.volString);
		mess.addItem(parent.getTrackIndex());
		double vol = hb.heightForUDPMessage(objParams);
		mess.addItem(vol);
		conn.sendUDPMessage(mess);
	}

	public void manifestGhost(){
		if (hasGhost){
			objParams.xpos = ghostParams.xpos;
			objParams.ypos = ghostParams.ypos;
			objParams.height = ghostParams.height;
			ghostSwopButtons();
			setHasGhost(false);
    	}	
	}
	public void makeGhost(){
		ghostParams.xpos = objParams.xpos;
		ghostParams.ypos = objParams.ypos;
		ghostParams.height = objParams.height;
		duplicateGhostButtons();
		setHasGhost(true);
	}
	public void swopObjAndGhost(){
		if (hasGhost){
			GUIObjectParams temp = ghostParams;
			int oldHeight = objParams.height;
			ghostParams = objParams;
			objParams = temp;
			ghostParams.makeIntoGhost();
			objParams.makeIntoObject();
			objParams.previousHeight = oldHeight;
			ghostSwopButtons();
		}		
	}
	public void makeButtons(Graphics2D g2d, GUIObjectParams params){
		for (GUIButton gb: buttonList){
			gb.addButton(g2d, params);
		}
	}
	public boolean testForButtonPress(int x, int y){
		for (GUIButton gbut: buttonList){
			//System.out.println("    GUIButton: " + gbut.getClass().getName());
			if (gbut.isMouseOvered(x, y)){
				//System.out.println("      isMouseOvered");
				gbut.buttonPressed();
				return true;
			} else {
				//System.out.println("      is not Mouseoverd");
			}
		}
		return false;
	}
	public boolean testForGhostButtonPress(int x, int y){
		for (GUIButton gbut: buttonList){
			//System.out.println("    GUIButton: " + gbut.getClass().getName());
			if (gbut.isGhostMouseOvered(x, y)){
				//System.out.println("      isMouseOvered");
				gbut.ghostButtonPressed();
				return true;
			} else {
				//System.out.println("      is not Mouseoverd");
			}
		}
		return false;
	}
	
//--------------------------------------------------------------------------------------------
	private void duplicateGhostButtons(){
		for (GUIButton butt: buttonList){
			butt.makeGhost();
		}
	}
	private void ghostSwopButtons(){
		for (GUIButton butt: buttonList){
			butt.swopObjectAndGhost();
		}
	}
	private void testHeight(){
		boolean heightNeededConstraining = hb.testHeight(objParams);
		if (!heightNeededConstraining){
			if (hb.behaviourModel == HeightBehaviour.INSTRUMENT_BEHAVIOUR){
				instrumentHeightChangeAction(PlayletObject.conn);
			}
		}
	}

	private void testGhostHeight(){
		hb.testHeight(ghostParams);
	}

}
