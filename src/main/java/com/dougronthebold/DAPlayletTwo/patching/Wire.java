package main.java.com.dougronthebold.DAPlayletTwo.patching;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Comparator;

import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;


public class Wire {
	
	public PlayletObjectInterface source;
	public PlayletObjectInterface destination;
	public boolean on = false;
	public boolean ghostOn = false;
	public boolean isNullObject = false;
	
	private int height = -1;
	private Color lineColor = new Color(0, 0, 0);
	private Color ghostLineColor = new Color(0, 0, 0, 100);
	private int strokeWidth = 2;
	private int knobXSize = 25;
	private int knobYSize = 25;
	private Font lengthFont = new Font("Tahoma", Font.PLAIN, 10);
	
	private static final int STRAIGHT = 2;
	private static final int DASHED = 1;
	private static final int OFF = 0;
	private int mode = STRAIGHT;
	private int ghostMode = STRAIGHT;
	
	private BasicStroke straightStroke = new BasicStroke(2);
	private BasicStroke dashedStroke = new BasicStroke(
			2,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND,
			10.0f,
			new float[]{10.0f, 10.0f},
			0.0f);
	private BasicStroke ghostDashedStroke = new BasicStroke(
			7,
			BasicStroke.CAP_ROUND,
			BasicStroke.JOIN_ROUND,
			10.0f,
			new float[]{7.0f, 15.0f},
			0.0f);
	private int offLimit = 500;
	private int dashedLimit = 350;
	
	private boolean iWasOn = false;

	public Wire(){
		isNullObject = true;
	}
	public Wire(PlayletObjectInterface ws, PlayletObjectInterface wd){
		source = ws;
		destination = wd;
		source.addOutWire(this);
		destination.addInWire(this);
	}
	public Wire(PlayletObjectInterface ws, PlayletObjectInterface wd, boolean b){
		source = ws;
		destination = wd;
		source.addOutWire(this);
		destination.addInWire(this);
		on = b;
	}
	public void send(PNO pno){
		pno.setLastWire(this);
		destination.in(pno);
	}
	public void setSource(PlayletObjectInterface ws){
		source = ws;
	}
	public void setDestination(PlayletObjectInterface wd){
		destination = wd;
	}
	public String toString(){
		if (isNullObject){
			return "wire is nullObject";
		} else {
			String ret =  "wire from " + source.nameToString() + " to " 
					+ destination.nameToString() + " - " 
					+ onToString();
			if (iWasOn){
				ret = ret + " was on";
			} else ret = ret + " was off";
			ret = ret + " " + ghostOnToString();
			ret = ret + "\n    source/ghost x,y: " + source.guiObject().xpos() + "," + source.guiObject().ypos() + "  " + source.guiObject().ghostXpos() + "," + source.guiObject().ghostYpos();
			ret = ret + "\n    destination/ghost x,y: " + destination.guiObject().xpos() + "," + destination.guiObject().ypos() + "  " + destination.guiObject().ghostXpos() + "," + destination.guiObject().ghostYpos();
			return ret;
		}
			}
	public void setOn(boolean b){
		on = b;
	}
	public void setGhostOn(boolean b){
		ghostOn = b;
	}
	public double sourceYpos(){
		if (isNullObject){
			return -1000.0;			// arbitrarily small number
		} else {
			return source.guiObject().ypos();
		}
	}
	public double sourceGhostYpos(){
		if (isNullObject){
			return -1000.0;			// arbitrarily small number
		} else {
			return source.guiObject().ghostYpos();
		}
	}
	public boolean testCurrentStatusWithPrevious(){
		//tests whether the status of the wire was the same as prior to a Surface.doWireSwitching event, for'
		// the purposes of triggering a re-render
		if (on == iWasOn){
			return true;
		} else {
			iWasOn = on;
			return false;
		}
	}
	public String onToString(){
		if (on){
			return "on";
		} else {
			return "off";
		}
	}
	public String ghostOnToString(){
		if (ghostOn){
			return "ghostOn";
		} else {
			return "ghostOff";
		}
	}

	public boolean areBothProcessors(){
		if (source.layer() == PlayletObject.PROCESSOR_OBJECT && destination.layer() == PlayletObject.PROCESSOR_OBJECT){
			return true;
		} else {
			return false;
		}
	}
	public boolean areSameChannel(){
		if (source.type() == destination.type()){
			return true;
		} else {
			return false;
		}
	}
	public boolean isDownStream(){
		// destination is below source on screen
		if (source.guiObject().ypos() < destination.guiObject().ypos()){
			return true;
		} else {
			return false;
		}
	}
	public boolean areThereGhostsInvolved(){
		// true if either source or destination has ghost
		if (source.guiObject().hasGhost) return true;
		if (destination.guiObject().hasGhost) return true;
		return false;
	}
	public double yDifference(){
		return destination.guiObject().ypos() - source.guiObject().ypos();
	}
	public double ghostYDifference(){
		return destination.guiObject().ghostYpos() - source.guiObject().ghostYpos();
	}
	public double length(){
		if (isNullObject){
			return 100000.0;		// arbitrarily large value
		} else {
			double x = source.guiObject().xpos() - destination.guiObject().xpos();
			double y = source.guiObject().ypos() - destination.guiObject().ypos();
			return Math.sqrt((x * x) + (y * y));
		}		
	}
	public double ghostLength(){
		if (isNullObject){
			return 100000.0;
		} else {
			if (!source.guiObject().hasGhost && !destination.guiObject().hasGhost){
				return length();
			} else {
				double x = source.guiObject().ghostXpos() - destination.guiObject().ghostXpos();
				double y = source.guiObject().ghostYpos() - destination.guiObject().ghostYpos();
				return Math.sqrt((x * x) + (y * y));
			}
		}
	}

	public static Comparator<Wire> yLengthComparator = new Comparator<Wire>(){
		public int compare(Wire wire1, Wire wire2){
			if (wire1.yDifference() < wire2.yDifference()) return -1;
			if (wire1.yDifference() > wire2.yDifference()) return 1;
			return 0;
		}
	};
	public static Comparator<Wire> ghostYLengthComparator = new Comparator<Wire>(){
		public int compare(Wire wire1, Wire wire2){
			if (wire1.ghostYDifference() < wire2.ghostYDifference()) return -1;
			if (wire1.ghostYDifference() > wire2.ghostYDifference()) return 1;
			return 0;
		}
	};
	public static Comparator<Wire> lengthComparator = new Comparator<Wire>(){
		public int compare(Wire wire1, Wire wire2){
			if (wire1.length() < wire2.length()) return -1;
			if (wire1.length() > wire2.length()) return 1;
			return 0;
		}
	};
	public static Comparator<Wire> ghostLengthComparator = new Comparator<Wire>(){
		public int compare(Wire wire1, Wire wire2){
			if (wire1.ghostLength() < wire2.ghostLength()) return -1;
			if (wire1.ghostLength() > wire2.ghostLength()) return 1;
			return 0;
		}
	};
	public static Comparator<Wire> sourceYposComparator = new Comparator<Wire>(){
		public int compare(Wire wire1, Wire wire2){
			if (wire1.source.guiObject().ypos() < wire2.source.guiObject().ypos()) return -1;
			if (wire1.source.guiObject().ypos() > wire2.source.guiObject().ypos()) return 1;
			return 0;
		}
	};
	public static Comparator<Wire> sourceGhostYposComparator = new Comparator<Wire>(){
		public int compare(Wire wire1, Wire wire2){
			if (wire1.source.guiObject().ghostYpos() < wire2.source.guiObject().ghostYpos()) return -1;
			if (wire1.source.guiObject().ghostYpos() > wire2.source.guiObject().ghostYpos()) return 1;
			return 0;
		}
	};
//	public static Comparator<Wire> yPosComparator = new Comparator<Wire>(){
//		public int compare(Wire wire1, Wire wire2){
//			if (wire1.position < note2.position) return -1;
//			if (note1.position > note2.position) return 1;
//			return 0;
//		}
//	};
	public void addObject(Graphics2D g2d){
		if (on){
			addLine(g2d);
		}
		if (ghostOn){
			addGhostLine(g2d);
		}
		
	}

// privates ----------------------------------------------------------------------------
	private void addGhostLine(Graphics2D g2d){
		double x1 = source.guiObject().ghostXpos();
		double y1 = source.guiObject().ghostYpos();
		double x2 = destination.guiObject().ghostXpos();
		double y2 = destination.guiObject().ghostYpos();
		g2d.setStroke(ghostDashedStroke);
		g2d.setColor(ghostLineColor);
		g2d.drawLine((int)x1, (int)y1, (int)x2, (int)y2);		
	}
	
	private void addLine(Graphics2D g2d){
		double x1 = source.guiObject().xpos();
		double y1 = source.guiObject().ypos();
		double x2 = destination.guiObject().xpos();
		double y2 = destination.guiObject().ypos();
		int length = (int)Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		setMode(length);
		
		if (mode != OFF){
			if (mode == STRAIGHT){
				g2d.setStroke(straightStroke);
			} else if (mode == DASHED){
				g2d.setStroke(dashedStroke);
			}
			g2d.setColor(lineColor);
			g2d.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
		// add knob
			int newx = (int)((x1 - x2) / 2 + x2 - knobXSize / 2);
			int newy = (int)((y1 - y2) / 2 + y2 - knobYSize / 2);
			g2d.fillOval(newx, newy, knobXSize, knobYSize);
		// add knob value
			g2d.setColor(new Color(255, 255, 255));
			g2d.setFont(lengthFont);
			String lengthText = "" + length;
			int textX = newx + knobXSize / 2 - lengthText.length() * lengthFont.getSize() / 4;
			int textY = newy + knobYSize / 2 + lengthFont.getSize() / 3;
			g2d.drawString(lengthText, textX, textY);
		}
		
	}
	private void setMode(int length){
//		if (source.type() == PlayletObject.CHORD_PROGRESSION || destination.isChordProgression){
//			chordProgressionTest();
//		} else {
//			defaultModeBasedOnLength(length);
//		}		
	}
	private void chordProgressionTest(){
//		PlayletObject pTest = parent.lowestChordProgression();
//		if (plobj1 == pTest || plobj2 == pTest){
//			mode = STRAIGHT;
//		} else {
//			mode = OFF;
//		}
	}
	private void defaultModeBasedOnLength(int length){
//		if (length > offLimit){
//			mode = OFF;
//		} else if (length > dashedLimit){
//			mode = DASHED;
//		} else {
//			mode = STRAIGHT;
//		}
	}
}
