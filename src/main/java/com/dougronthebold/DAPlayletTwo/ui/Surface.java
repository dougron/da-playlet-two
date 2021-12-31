package main.java.com.dougronthebold.DAPlayletTwo.ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import DataObjects.ableton_device_control_utils.controller.ControllerInfo;
import UDPUtils.OSCMessMaker;
import UDPUtils.UDPConnection;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.BooleanGUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.GUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.ShiftWidget;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.StaticNumber;
import main.java.com.dougronthebold.DAPlayletTwo.patching.PatchingManager;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassLegato;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassRandomPitchBend;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumAddKik;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.Contour;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RandomBendProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RhythmBuffer;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.SloWahProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysRandomBend;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterGenerator;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterInstrumentObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.master.MasterSloWahProcessor;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.resource.ChordProgression;



public class Surface extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ConsoleInterface, PlobjParent{
	

	private ArrayList<PlayletObjectInterface> resList = new ArrayList<PlayletObjectInterface>();
 	private ArrayList<PlayletObjectInterface> genList = new ArrayList<PlayletObjectInterface>();
	private ArrayList<PlayletObjectInterface> proList = new ArrayList<PlayletObjectInterface>();
	private ArrayList<PlayletObjectInterface> instList = new ArrayList<PlayletObjectInterface>();
	private HashMap<Integer, ArrayList<PlayletObjectInterface>> plobjMapByType = new HashMap<Integer, ArrayList<PlayletObjectInterface>>();
	private ArrayList<Wire> wireList = new ArrayList<Wire>();
	private ArrayList<GUIObject> gobjList = new ArrayList<GUIObject>();
	public ArrayList<PlayletObjectInterface> bigPlobjList = new ArrayList<PlayletObjectInterface>();
	private StaticNumber statNum = new StaticNumber();
	private static int[] sortByYPosKeys = new int[]{
		PlayletObject.CHORD_PROGRESSION,
		PlayletObject.DRUM_PROCESSOR,
		PlayletObject.SNR_PROCESSOR,
		PlayletObject.HAT_PROCESSOR,
		PlayletObject.TOMS_PROCESSOR,
		PlayletObject.CYMBAL_PROCESSOR,
		PlayletObject.BASS_PROCESSOR,
		PlayletObject.KEYS_PROCESSOR,
		PlayletObject.LEAD_PROCESSOR,
		PlayletObject.MULTI_PROCESSOR,
	};
	public static int[] wirePatchingChannelArray = new int[]{		// channels used in wire switching
		PlayletObject.DRUM_CHANNEL,
		PlayletObject.KIK_CHANNEL,
		PlayletObject.SNR_CHANNEL,
		PlayletObject.HAT_CHANNEL,
		PlayletObject.TOMS_CHANNEL,
		PlayletObject.CYMBAL_CHANNEL,
		PlayletObject.BASS_CHANNEL,
		PlayletObject.KEYS_CHANNEL,
		PlayletObject.LEAD_CHANNEL,
		PlayletObject.MULTI_CHANNEL
	};
	public SurfaceParent parent;	
	private Random rnd = new Random();
 	
//	private PlayletObjectInterface selectedPlobj;			// this is no longer used. all selected stuff goes via selectedGobj.parent
	private GUIObject selectedGobj;
	private boolean fineControl = false;
	private boolean shiftKey = false;				// may be superceded by the ShiftWidget
	private double deltaX = 0.;
	private double deltaY = 0.;
	private Point dragPoint;
	private int mouseX = 0;
	private int mouseY = 0;
	private Graphics2D g2d;
	private Color bgColor = new Color(100, 100, 100);
	private boolean mouseInWindow = false;
	private String keyInBuffer = "";
	
	public static final Wire nullWire = new Wire();		// a monstrously long wire for comparisons.....
	public static final GUIObject nullGobj = new GUIObject(0, 0);		// null GUIObject for placeholding
	private UDPConnection conn = new UDPConnection(7800);
	
	private boolean wireHasChanged = false;
	private boolean isPlaying = false;
	private ShiftWidget shiftWidget = new ShiftWidget(50.0, 50.0, 35.0, 35.0);
	
	private PatchingManager pm = new PatchingManager();
	
	public Surface(SurfaceParent p){
		parent = p;	
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListeners();
		setOpaque(true);
		parent.statusPrint("hello world");
	}
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		doDrawing(g);
	}
	public void addPlayletObject(PlayletObjectInterface plobj){
		
		addPlayletObjectWithoutWireSwitching(plobj);
		doWireSwitching();
	}
public void addPlayletObjectWithoutWireSwitching(PlayletObjectInterface plobj){
		
		addPlobjToTheRightList(plobj);
		gobjList.add(plobj.guiObject());
		Collections.sort(gobjList, GUIObject.heightComparator);
		addWires(plobj);
		bigPlobjList.add(plobj);
		if (plobjMapByType.containsKey(plobj.type())){
			plobjMapByType.get(plobj.type()).add(plobj);
		} else {
			plobjMapByType.put(plobj.type(), new ArrayList<PlayletObjectInterface>());
			plobjMapByType.get(plobj.type()).add(plobj);
		}
	}

	public void doWireSwitching(){
		switchOffAllWires();
		switchOffAllGhostWires();
		pm.repatch(instList, proList);
		testForReRender();
		testInstrumentsForStopPlay();
	}
	
//	public void doWireSwitchingOLD(){
//		switchOffAllWires();
//		makeOnlyShortestToInstrument();
//		patchProcessors();
//		System.out.println("-----------------------------------------------");
//		for (Wire w: wireList){
//			System.out.println(w.toString());
//		}
//		testWiresForReRender();
//		testInstrumentsForStopPlay();

//		doGhostWireSwitching();
//	}
	public void doGhostWireSwitching(){
		switchOffAllGhostWires();
		makeOnlyShortestGhostToInstrument();
		patchGhostProcessors();
	}
	public void render(){
		for (PlayletObjectInterface poi: genList){
			poi.render();
		}
	}
	public void consolePrint(String str){
		// does nothing.......
	}
	// MouseWheelListener methods --------------------------------------------------
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (selectedGobj != null){
			if (shiftWidget.shiftOn){
				selectedGobj.incrementGhostHeight(notches, fineControl);
			} else {
				selectedGobj.incrementHeight(notches, fineControl);
				testForReRender();
			}		
			sortGobjAndRepaint();
		}		
	}
//		@Override
//		public void mouseWheelMoved(MouseWheelEvent e) {
//			int notches = e.getWheelRotation();
//			if (selectedGobj != null){
//				if (shiftWidget.shiftOn){
//					if (fineControl){
//						selectedGobj.incrementGhostHeight(notches);
//					} else {
//						selectedGobj.incrementGhostHeight(notches * 3);
//					}
//					if (selectedGobj.ghostHeightHasChanged()){
//						selectedGobj.ghostHeightChangeAction();
//					}
	//			} else {
//					if (fineControl){
//						selectedGobj.incrementHeight(notches);
	//				} else {
//						selectedGobj.incrementHeight(notches * 3);
//					}
//					if (selectedGobj.heightHasChanged()){
//						if (PlayletObject.layerArr[selectedGobj.parent().type()] == PlayletObject.INSTRUMENT_OBJECT){
//							selectedGobj.heightChangeAction(conn);
	//					} else {
//							selectedGobj.heightChangeAction();
//						}
//					}
//				}
//				
//				sortGobjAndRepaint();
//			}
//			
//		}
		
	// KeyListener methods ---------------------------------------------------------

		public void dealWithKeyTyped(KeyEvent e){
			
		}

		public void dealWithKeyPressed(KeyEvent e){
			parent.statusPrint("Key was Pressed......");
			int key = e.getKeyCode();
			keyInBuffer += KeyEvent.getKeyText(key);
			
			if (key == KeyEvent.VK_B){
				if (shiftKey){
					parent.statusPrint("B");
					newRandomBGColor();
				}
				
			} else if (key == KeyEvent.VK_W){
				parent.statusPrint("wireList.size(): " + wireList.size());
			} else if (key == KeyEvent.VK_S){
				showAllWires();
//				doTestRect();
			} else if (key == KeyEvent.VK_R){
				if (shiftKey){
    				renderAll();
    				keyInBuffer = "";
    			}
//			} else if (key == KeyEvent.VK_D){
//				testX += 1;
//				doTestRect();
			} else if (key == KeyEvent.VK_Z){
				addTestSuite1();
			} else if (key == KeyEvent.VK_A){
				consolePrintChannels();
			} else if (key == KeyEvent.VK_SHIFT){
				if (!shiftWidget.shiftOn){
					shiftKey = true;
					shiftWidget.setShift(true);
					testMouseOver();
					//parent.statusPrint("shiftKey true");
				}
				
			} else if (key == KeyEvent.VK_CONTROL){
				fineControl = true;
			} else if (key == KeyEvent.VK_X){
//				System.out.println("deleting gobjy wobjy");
				deleteSelectedGobj();
			} else if (key == KeyEvent.VK_I){
				sendInstrumentInitializationMessage();
			} else if (key == KeyEvent.VK_G){
				if (shiftWidget.shiftOn){
					cancelGhosts();
				} else {
					moveToGhosts();		
				}
				doWireSwitching();
			} else if (key == KeyEvent.VK_H){
				swopObjectsAndGhosts();
			} else if (key == KeyEvent.VK_J){
				removeGhostFromSelectedGobj();
				doWireSwitching();
//			} else if (key == KeyEvent.VK_2){
//				System.out.println("TOGGLE");
			}
			testKeyInBuffer();
		}

		public void dealWithKeyReleased(KeyEvent e){
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_CONTROL){
				fineControl = false;
			} else if (key == KeyEvent.VK_SHIFT){
				if (shiftWidget.shiftOn){
					shiftKey = false;
					shiftWidget.setShift(false);
					testMouseOver();
					//parent.statusPrint("shiftKey false");
				}				
			}
		}
		
	// MouseListener methods -------------------------------------------------------	

		@Override
		public void mouseClicked(MouseEvent e) {
	        parent.statusPrint("clicked.....");    
	    }   
	    @Override
	    public void mouseEntered(MouseEvent e) {
	    	parent.statusPrint("mouseEntered.....");
	    	mouseInWindow = true;
	    }
	   @Override
	    public void mouseExited(MouseEvent e) {
		   parent.statusPrint("mouseExited.....");
		   mouseInWindow = false;
	    }
	    @Override
	    public void mousePressed(MouseEvent e) {
	    	dragPoint = e.getPoint();
	    	if (selectedGobj != null){
	    		if (shiftWidget.shiftOn){
	    			deltaX = e.getX() - selectedGobj.ghostXpos();
		        	deltaY = e.getY() - selectedGobj.ghostYpos();
		        	//System.out.println("deltaX: " + deltaX + " deltaY: " + deltaY);
	    		} else {
	    			deltaX = e.getX() - selectedGobj.xpos();
		        	deltaY = e.getY() - selectedGobj.ypos();
	    		}        	
	        } else {
	        	deltaX = 0;
	        	deltaY = 0;
	        }
	    	BooleanGUIObject bgobj = mouseOverUnghostedGobj();
	    	if (shiftWidget.shiftOn && bgobj.hasGobj){
	    		bgobj.gobj.makeGhost();
	    		repaint();
//	    		testMouseOver();
	    	}
	    	if (shiftWidget.isMouseOvered(e.getX(), e.getY())){
	    		shiftWidget.toggleShift();
	    	}
	    }
	    @Override
	    public void mouseReleased(MouseEvent e) {
	    	if (selectedGobj != null){
	    		if (selectedGobj.infoDotMouseOver(e.getX(), e.getY())){
	    			if (selectedGobj.getInfoPanelVisible()){
	    				selectedGobj.setInfoPanelVisible(false);
	    			} else {
	    				selectedGobj.setInfoPanelVisible(true);
	    			}        		
	        		repaint();
	        	}
	    	}   	
	    }
	    
	// MouseMotionListener methods ------------------------------------------------
	    @Override
	    public void mouseDragged(MouseEvent e) {
	    	parent.statusPrint("dragging: " + e.getX() + ", " + e.getY() + ", " + deltaX + ", " + deltaY);
	    	if (selectedGobj != null){
	    		if (!shiftWidget.shiftOn){
	    			double x = e.getX() - deltaX;
		    		double y = e.getY() - deltaY;
		    		selectedGobj.setPos(x, y);
		    		//System.out.println("setting pos to: " + x + ", " + y);
		    		doWireSwitching();
		    		repaint();
	    		} else {
	    			double x = e.getX() - deltaX;
		    		double y = e.getY() - deltaY;
		    		selectedGobj.setGhostPos(x, y);
		    		//System.out.println("setting pos to: " + x + ", " + y);
		    		doWireSwitching();
		    		repaint();
	    		}	    		
	    	} else {
	    		double xdiff = e.getX() - dragPoint.getX();
	    		double ydiff = e.getY() - dragPoint.getY();
//	    		pLoz.changeShadowOffset(xdiff * changeShadowFactor * -1, ydiff * changeShadowFactor * -1);
	    		parent.statusPrint("drag difference: " + xdiff + ", " + ydiff);
	    	}
	    	dragPoint = e.getPoint();
	    }

	    @Override
	    public void mouseMoved(MouseEvent e) {
	    	mouseX = e.getX();
	    	mouseY = e.getY();
	    	int x = e.getX();
	    	int y = e.getY();
	    	parent.statusPrint("MouseMoved: x: " + x + " y:" + y);
	    	testMouseOver();
	    }
	 // PlobjParent methods ---------------------------------------------------------
	    public void setForceReRenderOn(){
	    	
	    }
	
// privates -----------------------------------------------------------------------------------
	    private void removeGhostFromSelectedGobj(){
	    	if (selectedGobj != null){
//	    		System.out.println("removing ghost from " + selectedGobj.toString());
	    		selectedGobj.setHasGhost(false);
	    	}
	    }
	    private void swopObjectsAndGhosts(){
	    	for (GUIObject gobj: gobjList){
	    		gobj.swopObjAndGhost();
	    	}
	    	doWireSwitching();
	    }
	    private BooleanGUIObject mouseOverUnghostedGobj(){
	    	BooleanGUIObject bg = new BooleanGUIObject();
	    	for (GUIObject gobj: gobjList){
	    		if (!gobj.hasGhost && gobj.isMouseOvered(mouseX, mouseY)){
	    			bg.hasGobj = true;
	    			bg.gobj = gobj;
	    			return bg;
	    		}
	    	}
	    	return bg;
	    }
	    
	    private void cancelGhosts(){
	    	for (GUIObject gobj: gobjList){
	    		gobj.setHasGhost(false);
	    	}
	    	repaint();
	    }
	    private void moveToGhosts(){
	    	for (GUIObject gobj: gobjList){
	    		gobj.manifestGhost();
	    	}
	    	repaint();
	    }
	    private void testInstrumentsForStopPlay(){
	    	boolean connected = false;
	    	for (PlayletObjectInterface poi: instList){
	    		if(!poi.hasNoIns()){
	    			connected = true;
	    		}
	    	}
	    	if (isPlaying && !connected){
	    		sendStopMessage();
	    		isPlaying = false;
	    	}
	    	if (!isPlaying && connected){
	    		sendPlayMessage();
	    		isPlaying = true;
	    	}
	    }
	    private void sendStopMessage(){
	    	OSCMessMaker mess = new OSCMessMaker();
	    	mess.addItem(DeviceParamInfo.globalString);
	    	mess.addItem(DeviceParamInfo.stopString);
	    	conn.sendUDPMessage(mess);
	    }
	    private void sendPlayMessage(){
	    	OSCMessMaker mess = new OSCMessMaker();
	    	mess.addItem(DeviceParamInfo.globalString);
	    	mess.addItem(DeviceParamInfo.playString);
	    	conn.sendUDPMessage(mess);
	    }
	    
	    private boolean testWiresForReRender(){
	    	//System.out.println("===================================================");
	    	boolean change = false;
	    	for (Wire w: wireList){
	    		//System.out.println("testing wire: " + w.toString());
	    		if (!w.testCurrentStatusWithPrevious()){
	    			change = true;
	    		}
	    	}
	    	return change;
	    	
	    }
	    private boolean testForHeightChange(){
	    	for (GUIObject gobj: gobjList){
	    		//System.out.println(gobj.parent().nameToString() + ": " + gobj.objParams.height + ", " + gobj.objParams.previousHeight);
	    		if (gobj.heightHasChanged()){
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    private void testForReRender(){
	    	boolean rerender = false;
	    	if (testWiresForReRender()){
	    		rerender = true;	    		
	    	} else if (testForHeightChange()){
	    		rerender = true;
	    	}
	    	if (rerender){
	    		//System.out.println("Status has changed --- RERENDER!!!!!!");
		    	newRandomBGColor();
		    	renderAll();
	    	}
	    	
	    }
	    private void sendInstrumentInitializationMessage(){
	    	//System.out.println("sending initialization message");
	    	OSCMessMaker mess = new OSCMessMaker();	
	    	mess.addItem(DeviceParamInfo.initString);
	    	mess.addItem(DeviceParamInfo.resetString);
	    	conn.sendUDPMessage(mess);
	    	for (PlayletObjectInterface poi: instList){
	    		mess = new OSCMessMaker();
	    		mess.addItem(DeviceParamInfo.initString);
	    		//System.out.println(poi.toString());
	    		poi.instrumentInitializationMessage(mess);
	    		conn.sendUDPMessage(mess);
	    		
	    	}
	    	for (PlayletObjectInterface poi: instList){
	    		for (ControllerInfo ci: poi.controllerList()){
	    			mess = ci.makeOSCMessage();
	    			mess.addItem(DeviceParamInfo.controllerString, 0);
	    			mess.addItem(DeviceParamInfo.initString, 0);
	    			conn.sendUDPMessage(mess);
	    		}
	    	}
	    	
	    	//tested udp send up to 13095 items. not sure if this is an
	    	// item count or a byte count
//	    	mess.addItem("Hello World.....");
//	    	for (int i = 0; i < 13095; i++){
//	    		mess.addItem(i);
//	    	}
	    	
	    	
	    }
	    private void renderAll(){
//	    	parent.statusPrint("rendering");
	    	for (PlayletObjectInterface poi: genList){
	    		//System.out.println(poi.nameToString() + " is rendering");
	    		poi.render();
	    	}
	    	for (PlayletObjectInterface poi: instList){
	    		if (poi.hasNoIns()){		// clear clips of unconnected Instruments
	    			//System.out.println(poi.toString());
	    			poi.in(PlayletObject.nullPNO);
	    		}
	    	}
	    }
	    private void addTestSuite1(){
	    	addPlayletObjectWithoutWireSwitching(new DrumGeneratorOne(10, 300, "DG_1", this));
	    	addPlayletObjectWithoutWireSwitching(new DrumAddKik(10, 500, "KIK", this));
	    	addPlayletObjectWithoutWireSwitching(new DrumInstrumentOne(10, 720, "DRUM", 0, 0, this, this));
//	    	BassGeneratorOne bg1 = new BassGeneratorOne(50, 300, "BG_1");
//	    	addPlayletObjectWithoutWireSwitching(bg1);
//	    	bg1.guiObject().setHasGhost(true);
//	    	bg1.guiObject().setGhostPos(30, 280);
	    	BassInstrumentOne bs1 = new BassInstrumentOne(50, 720, "BassInst1", 1, 0, this, this);
	    	addPlayletObjectWithoutWireSwitching(bs1);
//	    	for (Wire w: bs1.inWireList()){
//	    		w.setOn(true);
//	    		w.setGhostOn(true);
//	    	}
	    	addPlayletObjectWithoutWireSwitching(new BassInstrumentOne(120, 720, "BassInst2", 2, 0, this, this));
//	    	addPlayletObjectWithoutWireSwitching(new MasterInstrumentObject(400, 720, "Master", this));					//out
//	    	addPlayletObjectWithoutWireSwitching(new MasterSloWahProcessor(400, 450, "MPS", this));						//out
//	    	addPlayletObjectWithoutWireSwitching(new MasterGenerator(400, 300, "MG", this));								//out
	    	addPlayletObjectWithoutWireSwitching(new BassProcessorOne(150, 450, "BP_2", this));
//	    	addPlayletObjectWithoutWireSwitching(new KeysGeneratorOne(250, 300, "KeysGenPad", this));
//	    	addPlayletObjectWithoutWireSwitching(new KeysProcessorOne(250, 400, "KP1", this));
//	    	addPlayletObjectWithoutWireSwitching(new KeysProcessorOne(250, 500, "KP2", this));							//out
	    	addPlayletObjectWithoutWireSwitching(new KeysInstrumentOne(250, 720, "xxxKeysInst3", 3, 0, this, this));
	    	addPlayletObjectWithoutWireSwitching(new BassGeneratorOne(100, 300, "BG_1", this));
	    	addPlayletObjectWithoutWireSwitching(new ChordProgression(50, 150, "CP_1", this));
	    	addPlayletObjectWithoutWireSwitching(new ChordProgression(200, 150, "CP_2", this));
	    	addPlayletObjectWithoutWireSwitching(new RhythmBuffer(100, 100, "RB_1", this));
	    	doWireSwitching();
	    	sendInstrumentInitializationMessage();
	    	clearAllClips();
	    }
	    private void clearAllClips(){
	    	for (PlayletObjectInterface poi: instList){
	    		poi.in(PlayletObject.nullPNO);
	    	}
	    }

	    private void testKeyInBuffer(){
//			parent.statusPrint(keyInBuffer + ", " + keyInBuffer.length());
	    	if (keyInBuffer.length() == 1){
	    		if (keyInBuffer.equals("B")){	    			
	    		} else if (keyInBuffer.equals("C")){
	    		} else if (keyInBuffer.equals("K")){
	    		} else if (keyInBuffer.equals("R")){	    			
	    		} else if (keyInBuffer.equals("M")){	    		
	    		} else {
	    			keyInBuffer = "";
	    		}
	    	} else if (keyInBuffer.length() == 2){
	    		if (keyInBuffer.equals("BG")){	    			
	    		} else if (keyInBuffer.equals("BI")){
	    		} else if (keyInBuffer.equals("BP")){
	    		} else if (keyInBuffer.equals("BR")){
	    		} else if (keyInBuffer.equals("BL")){
	    		} else if (keyInBuffer.equals("CP")){
	    		} else if (keyInBuffer.equals("KI")){
	    		} else if (keyInBuffer.equals("KG")){
	    		} else if (keyInBuffer.equals("KP")){
	    		} else if (keyInBuffer.equals("KR")){
	    		} else if (keyInBuffer.equals("RB")){
	    		} else if (keyInBuffer.equals("CN")){
	    		} else if (keyInBuffer.equals("MP")){
	    		} else if (keyInBuffer.equals("MI")){
	    			addMasterInstrument();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("MG")){
	    			addMasterGenerator();
	    			keyInBuffer = "";
	    		} else {
	    			keyInBuffer = "";
	    		}
	    	} else if (keyInBuffer.length() == 3){
	    		if (keyInBuffer.equals("BG1")){
	    			addBassGeneratorOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("BI1")){	
	    			addBassInstrumentOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("BP1")){	
	    			addBassProcessorOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("BRB")){	
	    			addBassRandomBend();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("BL1")){	
	    			addBassLegatoOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("BPS")){	
	    			addSloWahBassProcessor();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("BPR")){	
	    			addRandomBendBassProcessor();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("KI1")){	
	    			addKeysInstrumentOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("KGP")){	
	    			addKeysGeneratorPad();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("KRB")){	
	    			addKeysRandomBend();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("KP1")){	
	    			addKeysProcessorOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("CP1")){	
	    			addChordProgressionOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("RB1")){	
	    			addRhythmBufferOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("CN1")){	
	    			addContourOne();
	    			keyInBuffer = "";
	    		} else if (keyInBuffer.equals("MPS")){	
	    			addMasterSloWah();
	    			keyInBuffer = "";
	    		} else {
	    			keyInBuffer = "";
	    		}
	    	} else {
	    		keyInBuffer = "";
	    	}
	    	parent.statusPrint(keyInBuffer + ", " + keyInBuffer.length());
	    }
	    private boolean hasMasterInstrument(){
	    	for (PlayletObjectInterface poi: instList){
	    		if (poi.type() == PlayletObject.MASTER_INSTRUMENT){
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    private boolean hasMasterGenerator(){
	    	for (PlayletObjectInterface poi: genList){
	    		if (poi.type() == PlayletObject.MASTER_GENERATOR){
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    private void addBassLegatoOne(){
	    	addPlayletObject(new BassLegato(mouseX, mouseY, "BLeg1", this));
	    }
	    private void addBassRandomBend(){
	    	addPlayletObject(new BassRandomPitchBend(mouseX, mouseY, "BRB", this));
	    }
	    private void addKeysGeneratorPad(){
	    	addPlayletObject(new KeysGeneratorOne(mouseX, mouseY, "KGP", this));
	    }
	    private void addContourOne(){
	    	addPlayletObject(new Contour(mouseX, mouseY, "CN_1", this));
	    }
	    private void addRhythmBufferOne(){
	    	addPlayletObject(new RhythmBuffer(mouseX, mouseY, "RB_1", this));
	    }
	    private void addKeysProcessorOne(){
	    	addPlayletObject(new KeysProcessorOne(mouseX, mouseY, "KP_1", this));
	    }
	    private void addKeysRandomBend(){
	    	addPlayletObject(new KeysRandomBend(mouseX, mouseY, "KRB", this));
	    }
	    private void addKeysInstrumentOne(){
	    	addPlayletObject(new KeysInstrumentOne(mouseX, mouseY, "KeysInst1", this, this));
	    }
	    private void addMasterGenerator(){
	    	if (!hasMasterGenerator()){
	    		addPlayletObject(new MasterGenerator(mouseX, mouseY, "MG", this));
	    	}
	    }
	    private void addMasterInstrument(){
	    	if (!hasMasterInstrument()){
	    		addPlayletObject(new MasterInstrumentObject(mouseX, mouseY, "Master", this));
	    	}
	    }
	    private void addMasterSloWah(){
	    	addPlayletObject(new MasterSloWahProcessor(mouseX, mouseY, "MSloWah", this));	  	    	
	    }
	    private void addBassProcessorOne(){
	    	addPlayletObject(new BassProcessorOne(mouseX, mouseY, "BP_1", this));
	    }
	    private void addSloWahBassProcessor(){
	    	addPlayletObject(new SloWahProcessor(mouseX, mouseY, "BSloWah", this));
	    }
	    private void addRandomBendBassProcessor(){
	    	addPlayletObject(new RandomBendProcessor(mouseX, mouseY, "BSloWah", this));
	    }
	    private void addChordProgressionOne(){
	    	addPlayletObject(new ChordProgression(mouseX, mouseY, "CP_1", this));
	    }
	    private void addBassInstrumentOne(){
	    	addPlayletObject(new BassInstrumentOne(mouseX, mouseY, "BassInst1", this, this));
	    }
	    private void addBassGeneratorOne(){
	    	addPlayletObject(new BassGeneratorOne(mouseX, mouseY, "BassGen1", this));		// temporary
	    }
//	    private void addChordProgressionOne(){
//	    	addPlobj();		// temporary
//	    }
	    private void deleteSelectedGobj(){
	    	if (selectedGobj != null){
	    		parent.statusPrint("deleting selectedPlobj " + selectedGobj.toString());

	    		for (Wire w: selectedGobj.parent().inWireList()){
	    			wireList.remove(w);
	    			w.source.outWireList().remove(w);
	    		}
	    		for (Wire w: selectedGobj.parent().outWireList()){
	    			wireList.remove(w);
	    			w.destination.inWireList().remove(w);
	    		}
	    		
	    		bigPlobjList.remove(selectedGobj.parent());
	    		gobjList.remove(selectedGobj);
	    		if (selectedGobj.parent().layer() == PlayletObject.RESOURCE_OBJECT){
	    			resList.remove(selectedGobj);
	    		} else if (selectedGobj.parent().layer() == PlayletObject.GENERATOR_OBJECT){
	    			genList.remove(selectedGobj);
	    		} else if (selectedGobj.parent().layer() == PlayletObject.PROCESSOR_OBJECT){
	    			proList.remove(selectedGobj);
	    		} else if (selectedGobj.parent().layer() == PlayletObject.INSTRUMENT_OBJECT){
	    			instList.remove(selectedGobj);
	    		}
	    		selectedGobj = null;
	    		doWireSwitching();
		    	repaint();
	    	} else {
	    		OSCMessMaker mess = new OSCMessMaker();
	    		mess.addItem(DeviceParamInfo.initString);
	    		mess.addItem(DeviceParamInfo.resetString);
	    		conn.sendUDPMessage(mess);
	    	}
	    	
	    }
	    private void consolePrintChannels(){
//	    	for (PlayletObjectInterface poi: bigPlobjList){
//	    		System.out.println(poi.nameToString() + " channel: " + poi.channel());
//	    	}
	    }
	    private void sortGobjAndRepaint(){
	    	Collections.sort(gobjList, GUIObject.heightComparator);
			repaint();
	    }
	    private void testMouseOver(){
	    	GUIObject tempGobj = selectedGobj;
	    	boolean foundit = false;
	    	if (shiftWidget.shiftOn){
	    		for (GUIObject gobj: gobjList){
	    			if (gobj.isGhostMouseOvered(mouseX, mouseY)){
	    				if (gobj != selectedGobj){
	    					if (selectedGobj != null) selectedGobj.setSelected(false);
	    					selectedGobj = gobj;
	    					selectedGobj.setSelected(true);	
	    				}
	    				foundit = true;    				
	    			}
	    		}
	    	} else {
	    		for (GUIObject gobj: gobjList){
	    			if (gobj.isMouseOvered(mouseX, mouseY)){
	    				if (gobj != selectedGobj){
	    					if (selectedGobj != null) selectedGobj.setSelected(false);
	    					selectedGobj = gobj;
	    					selectedGobj.setSelected(true);
	    				}
    					foundit = true;
	    			}
	    		}
	    	}
	    	
	    	if (!foundit) {
	    		if (selectedGobj != null) selectedGobj.setSelected(false);
	    		selectedGobj = null;
	    	}
	    	repaint();
	    }
	    
	    
//	    private void testMouseOverOLD(){ // decommissioned 25 oct 2015. can be erased is not missed
//	    	GUIObject tempGobj = selectedGobj;
//	    	boolean infoViz = false;
//	    	if (selectedGobj != null){
//	    		infoViz = selectedGobj.getInfoPanelVisible();
//	    	} 
//
//	    	if (selectedGobj != null){
//	    		selectedGobj.setSelected(false);
//	        	selectedGobj = null;
//	    	}    	
//	    	for (GUIObject gobj: gobjList){
//	    		if (shiftWidget.shiftOn){
//	    			if (gobj.isGhostMouseOvered(mouseX, mouseY)){
//		    			if (selectedGobj != null){
//		    				selectedGobj.setSelected(false);
//		    		    	selectedGobj = null;
//		    			}	    			
//		    		}
//	    		} else {
//	    			if (gobj.isMouseOvered(mouseX, mouseY)){
//		    			if (selectedGobj != null){
//		    				selectedGobj.setSelected(false);
//		    		    	selectedGobj = null;
//		    			}	    			
//		    		}
//	    		}
//	    		selectedGobj = gobj;
 //   			selectedGobj.setSelected(true);
//	    		
//	    	}  
//	    	if (selectedGobj == tempGobj && selectedGobj != null){
//	    		selectedGobj.setInfoPanelVisible(infoViz);
//	    	}
//	    	if (selectedGobj != tempGobj && tempGobj != null){
//	    		tempGobj.setInfoPanelVisible(false);
//	    	}
//	    	if (selectedGobj == null){
//	    		selectedPlobj = null;
//	    	} else {
	//    		selectedPlobj = selectedGobj.parent();
//	    	}
//	    	repaint();
//	    }
//	    private void addPlobj(PlayletObjectInterface poi){
//	    	if (mouseInWindow){
//	    		addPlayletObject(poi);
//	    		testMouseOver();
//	    	}
//	    }

	    private Color getRandomColor(){
	    	int r = rnd.nextInt(255);
	    	int g = rnd.nextInt(255);
	    	int b = rnd.nextInt(255);
	    	return new Color(r, g, b);
	    }
	    private void addKeyListeners(){
	    	setFocusable(true);
	        addKeyListener(new java.awt.event.KeyAdapter() {
	            @Override
	            public void keyTyped(KeyEvent e) {
	                //parent.statusMessage("you typed a key");
	            	dealWithKeyTyped(e);
	            }
	            @Override
	            public void keyPressed(KeyEvent e) {
	            	//parent.statusMessage("you pressed a key");
	        		dealWithKeyPressed(e);
	            }
	            @Override
	            public void keyReleased(KeyEvent e) {
	            	//parent.statusMessage("you released a key");
	            	dealWithKeyReleased(e);
	            }
	        });
	    }
	    private void doDrawing(Graphics g){
			g2d = (Graphics2D) g;
			setAntiAliasing();
			setBackground(bgColor);
			//doTestRect();
			do2DObjectList();
			repaint();
	    }
	    private void do2DObjectList(){
	    	
	    	for (GUIObject gobj: gobjList){
	    		gobj.addShadow(g2d, bgColor, gobj.objParams);
	    	}
	    	for (Wire pw: wireList){
	    		pw.addObject(g2d);
	    	}
	    	for (GUIObject gobj: gobjList){
	    		gobj.addGhostConnector(g2d);
	    	}
	    	if (shiftWidget.shiftOn){
	    		addObjects();
	    		addGhosts();
	    	} else {
	    		addGhosts();
	    		addObjects();
	    	}
	    	
	    	shiftWidget.addObject(g2d);
	    }
	    private void addGhosts(){
	    	for (GUIObject gobj: gobjList){
	    		gobj.addGhost(g2d, shiftWidget.shiftOn);
	    	}
	    }
	    private void addObjects(){
	    	for (GUIObject gobj: gobjList){
	    		gobj.addObject(g2d, shiftWidget.shiftOn);
	    	}
	    }

	    private void setAntiAliasing(){
	    	g2d.setRenderingHint(
					RenderingHints.KEY_TEXT_ANTIALIASING, 
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(
					RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
	    }
	    
// wiring stuff ---------------------------------------------------------------------
	private void activateWires(){
		for (PlayletObjectInterface poi: bigPlobjList){
			if (poi.type() == PlayletObject.CHORD_PROGRESSION){
				doChordProgressionWires(poi);
			} else if (poi.type() == PlayletObject.RHYTHM_BUFFER){
				doRhythmBufferWires(poi);
			} else if (poi.type() == PlayletObject.CONTOUR){
				doContourWires(poi);
			} else if (poi.layer() == PlayletObject.PROCESSOR_OBJECT){
				doProcessorWire(poi);
			} else if (poi.layer() == PlayletObject.INSTRUMENT_OBJECT){
				doInstrumentWire(poi);
			}
		}
	}
	private void doInstrumentWire(PlayletObjectInterface poi){
		int proIndex = getIndexInPlobjMap(PlayletObject.PROCESSOR_OBJECT, poi.channel());
		if (proIndex > -1){
			if (!typeHasUpstreamConnection(poi.type())){
				connectLastProcessorToNearestInstrument(proIndex, poi.type());
			}			
		} else {
			int genIndex = getIndexInPlobjMap(PlayletObject.GENERATOR_OBJECT, poi.channel());
			if (genIndex > -1){
				if (!typeHasUpstreamConnection(poi.type())){
//					connectNearestGeneratorToInstrument(genIndex, poi.type());
					attachToNearestGenerator(poi);
				}
			}
		}
	}
	private void connectNearestGeneratorToInstrument(int genIndex, int instIndex){
		ArrayList<PlayletObjectInterface> genList = plobjMapByType.get(genIndex);
		Wire chosenWire = new Wire();
		for (PlayletObjectInterface gen: genList){
			for (Wire w: gen.outWireList()){
				if (w.destination.layer() == PlayletObject.INSTRUMENT_OBJECT 
						&& w.destination.channel() == gen.channel()
						&& w.length() < chosenWire.length()){
					chosenWire = w;
				}
			}
		}
		chosenWire.setOn(true);
	}
	private void connectLastProcessorToNearestInstrument(int proIndex, int instIndex){
		Wire chosenWire = new Wire();
		ArrayList<PlayletObjectInterface> proList = plobjMapByType.get(proIndex);
		Collections.sort(proList, GUIObject.yPosComparator);
		PlayletObjectInterface lastPro = proList.get(proList.size() - 1);
		for (Wire w: lastPro.outWireList()){
			if (w.destination.type() == instIndex && w.length() < chosenWire.length()){
				chosenWire = w;
			}
		}
		chosenWire.setOn(true);
	}
	private boolean typeHasUpstreamConnection(int index){
		ArrayList<PlayletObjectInterface> pList = plobjMapByType.get(index);
		for (PlayletObjectInterface poi: pList){
			for (Wire w: poi.inWireList()){
				if (w.on) return true;
			}
		}
		return false;
	}
	private int getIndexInPlobjMap(int layer, int channel){
		for (int pIndex: plobjMapByType.keySet()){
			PlayletObjectInterface plobj = plobjMapByType.get(pIndex).get(0);
			if (plobj.layer() == layer && plobj.channel() == channel){
				return pIndex;
			}
		}
		return -1;
	}
	private void doProcessorWire(PlayletObjectInterface poi){
		ArrayList<PlayletObjectInterface> pList = plobjMapByType.get(poi.type());
		int index = pList.indexOf(poi);
		if (index < 1){
			attachToNearestGenerator(poi);
		} else {
			attachUpstream(poi, pList, index);
		}
	}
	private void attachToNearestGenerator(PlayletObjectInterface poi){
		ArrayList<Wire> wList = new ArrayList<Wire>();
		for (Wire w: poi.inWireList()){
			if (w.source.layer() == PlayletObject.GENERATOR_OBJECT && w.source.channel() == poi.channel()){
				wList.add(w);
			}
		}
		if (wList.size() == 1){
			wList.get(0).setOn(true);
		} else if (wList.size() > 1){
			Collections.sort(wList, Wire.lengthComparator);
			wList.get(0).setOn(true);
		}
	}
	private void attachUpstream(PlayletObjectInterface poi, ArrayList<PlayletObjectInterface> pList, int index){
		PlayletObjectInterface upStreamItem = pList.get(index - 1);
		for (Wire w: poi.inWireList()){
			if (w.source == upStreamItem){
				w.setOn(true);
				break;
			}
		}
	}
	private void doContourWires(PlayletObjectInterface poi){
		for (PlayletObjectInterface gen: genList){
			Collections.sort(gen.inWireList(), Wire.lengthComparator);
			for (Wire w: gen.inWireList()){
				if (w.source.type() == PlayletObject.CONTOUR){
					w.setOn(true);
					break;
				}
			}
		}
	}
	private void doRhythmBufferWires(PlayletObjectInterface poi){
		for (PlayletObjectInterface gen: genList){
			Collections.sort(gen.inWireList(), Wire.lengthComparator);
			for (Wire w: gen.inWireList()){
				if (w.source.type() == PlayletObject.RHYTHM_BUFFER){
					w.setOn(true);
					break;
				}
			}
		}
	}
	private void doChordProgressionWires(PlayletObjectInterface poi){
		if (plobjMapByType.containsKey(PlayletObject.CHORD_PROGRESSION)){
			ArrayList<PlayletObjectInterface> pList = plobjMapByType.get(PlayletObject.CHORD_PROGRESSION);
			int index = pList.indexOf(poi);
			if (index == pList.size() - 1){
				poi.setAllOutWires(true);
			}
		}
	}
	private void prepPlobjMap(){
		// sorts all the stuff that depends on ypos for connection
		for (int key: sortByYPosKeys){
			if (plobjMapByType.containsKey(key)){
				Collections.sort(plobjMapByType.get(key), GUIObject.yPosComparator);
			}
		}
	}
	private void showAllWires(){
		for (Wire w: wireList){
			w.setOn(true);
		}
		repaint();
	}
	private void patchProcessors(){
		ArrayList<Wire> tempWireList = new ArrayList<Wire>();
		ArrayList<Wire> wiresToBeSwitchedOnList = new ArrayList<Wire>();
		PlayletObjectInterface testPoi;
		boolean flag = true;
//		System.out.println("----------------------------------");
		for (PlayletObjectInterface poi: proList){			
			flag = true;
			if (poi.hasOutputPatch()){
				testPoi = poi;
				while (flag){	
					tempWireList = getAvailableWires(testPoi);
					if (tempWireList.size() > 0){
						Collections.sort(tempWireList, Wire.yLengthComparator);
						wiresToBeSwitchedOnList.add(tempWireList.get(0));
						testPoi = tempWireList.get(0).source;
					} else {
						connectToNearestGenerator(testPoi);
						flag = false;
					}					
				}
			}			
		}
		for (Wire w: wiresToBeSwitchedOnList){
			w.setOn(true);
		}
	}
	private void patchGhostProcessors(){
		ArrayList<Wire> tempWireList = new ArrayList<Wire>();
		ArrayList<Wire> wiresToBeSwitchedOnList = new ArrayList<Wire>();
		PlayletObjectInterface testPoi;
		boolean flag = true;
//		System.out.println("----------------------------------");
		for (PlayletObjectInterface poi: proList){			
			flag = true;
			if (poi.hasOutputPatch()){
				testPoi = poi;
				while (flag){	
					tempWireList = getAvailableGhostWires(testPoi);
					if (tempWireList.size() > 0){
						Collections.sort(tempWireList, Wire.ghostYLengthComparator);
//						System.out.println("patchGhostProcessor for " + poi.toString() + "----------");
//						for (Wire w: tempWireList){
//							System.out.println(w.toString());
//						}
						wiresToBeSwitchedOnList.add(tempWireList.get(0));
						testPoi = tempWireList.get(0).source;
					} else {
						connectToNearestGhostGenerator(testPoi);
						flag = false;
					}					
				}
			}			
		}
		for (Wire w: wiresToBeSwitchedOnList){
			w.setGhostOn(true);			
		}
	}
//	below was an attempt to solve ghost wire switching but in the process i found the problem with the original, so it is incomplete
//	private void patchGhostProcessorsALT(){
//		ArrayList<PlayletObjectInterface> pList = findProcessorPatchedToInstrument();
//		makeSortedArrayOfUpstreamProcessors(pList);
//		switchOnWiresInWireLengthRange();
//	}
//	private ArrayList<PlayletObjectInterface> makeSortedArrayOfUpstreamProcessors(ArrayList<PlayletObjectInterface> pList){
//		ArrayList<PlayletObjectInterface> psList = new ArrayList<PlayletObjectInterface>();
//		for (PlayletObjectInterface poi: proList){
//			if (poi.)
//		}
//		return psList;
//	}
//	private ArrayList<PlayletObjectInterface> findProcessorPatchedToInstrument(){
//		ArrayList<PlayletObjectInterface> pList = new ArrayList<PlayletObjectInterface>();
//		for(PlayletObjectInterface poi: instList){
//			for (Wire w: poi.inWireList()){
//				if (w.ghostOn){
//					if (w.source != null) pList.add(w.source);
//				}
//			}
//		}
//		return pList;
//	}
	private void connectToNearestGenerator(PlayletObjectInterface poi){
//		System.out.println("--------------------------------------------------------");
//		System.out.println(poi.nameToString() + " is looking for a generator");
		ArrayList<Wire> wList = new ArrayList<Wire>();
		for (Wire w: poi.inWireList()){
//			System.out.println(poi.nameToString() + " has in wire from " + w.source.nameToString());
			if (w.source.layer() == PlayletObject.GENERATOR_OBJECT && w.source.channel() == poi.channel()){
				wList.add(w);
			}
		}	
//		System.out.println(poi.nameToString() + " has " + wList.size() + " options");
		if (wList.size() > 0){
			Collections.sort(wList, Wire.lengthComparator);
			wList.get(0).setOn(true);
			connectToResourceLayer(wList.get(0).source);
		}
	}
	private void connectToNearestGhostGenerator(PlayletObjectInterface poi){
//		System.out.println("--------------------------------------------------------");
//		System.out.println(poi.nameToString() + " is looking for a generator");
		ArrayList<Wire> wList = new ArrayList<Wire>();
		for (Wire w: poi.inWireList()){
//			System.out.println(poi.nameToString() + " has in wire from " + w.source.nameToString());
			if (w.source.layer() == PlayletObject.GENERATOR_OBJECT && w.source.channel() == poi.channel()){
				wList.add(w);
			}
		}	
//		System.out.println(poi.nameToString() + " has " + wList.size() + " options");
		if (wList.size() > 0){
			Collections.sort(wList, Wire.ghostLengthComparator);
			Wire wx = wList.get(0);
			wx.setGhostOn(true);
			connectGhostToResourceLayer(wx.source);
			
		}
	}
	private void connectToResourceLayer(PlayletObjectInterface poi){
		ArrayList<Wire> cpList = new ArrayList<Wire>();
		ArrayList<Wire> rbList = new ArrayList<Wire>();
		ArrayList<Wire> cnList = new ArrayList<Wire>();
		
		makeTypeList(cpList, PlayletObject.CHORD_PROGRESSION, poi);
		makeTypeList(rbList, PlayletObject.RHYTHM_BUFFER, poi);
		makeTypeList(cnList, PlayletObject.CONTOUR, poi);
		
//		System.out.println("cpList has " + cpList.size() + " for " + poi.nameToString());
		Collections.sort(cpList, Wire.sourceYposComparator);
		turnOnLastIndex(cpList);
		Collections.sort(rbList, Wire.lengthComparator);
		turnOnIndexZero(rbList);
		Collections.sort(cnList, Wire.lengthComparator);
		turnOnIndexZero(cnList);
	}
	private void connectGhostToResourceLayer(PlayletObjectInterface poi){
		ArrayList<Wire> cpList = new ArrayList<Wire>();
		ArrayList<Wire> rbList = new ArrayList<Wire>();
		ArrayList<Wire> cnList = new ArrayList<Wire>();
		
		makeTypeList(cpList, PlayletObject.CHORD_PROGRESSION, poi);
		makeTypeList(rbList, PlayletObject.RHYTHM_BUFFER, poi);
		makeTypeList(cnList, PlayletObject.CONTOUR, poi);
		
//		System.out.println("cpList has " + cpList.size() + " for " + poi.nameToString());
		Collections.sort(cpList, Wire.sourceGhostYposComparator);
		turnOnLastIndexGhost(cpList);
		Collections.sort(rbList, Wire.ghostLengthComparator);
		turnOnIndexZeroGhost(rbList);
		Collections.sort(cnList, Wire.ghostLengthComparator);
		turnOnIndexZeroGhost(cnList);
	}
	private void makeTypeList(ArrayList<Wire> wList, int type, PlayletObjectInterface poi){
//		System.out.println("testing for type " + type + " with " + poi.nameToString());
		for (Wire w: poi.inWireList()){
//			System.out.println(w.source.nameToString() + " of type " + w.source.type() + " tested against " + type);
			if (w.source.type() == type){
				wList.add(w);
			}
		}
	}
	private void turnOnIndexZero(ArrayList<Wire> wList){
		if (wList.size() > 0){
			wList.get(0).setOn(true);
		}
	}
	private void turnOnIndexZeroGhost(ArrayList<Wire> wList){
		if (wList.size() > 0){
			wList.get(0).setGhostOn(true);
		}
	}
	private void turnOnLastIndex(ArrayList<Wire> wList){
		if (wList.size() > 0){
			wList.get(wList.size() - 1).setOn(true);
		}
	}
	private void turnOnLastIndexGhost(ArrayList<Wire> wList){
		if (wList.size() > 0){
			wList.get(wList.size() - 1).setGhostOn(true);
		}
	}

	private ArrayList<Wire> getAvailableWires(PlayletObjectInterface poi){
		ArrayList<Wire> wList = new ArrayList<Wire>();
		for (Wire w: poi.inWireList()){
			if (w.source.type() == poi.type() &&
					w.source.guiObject().ypos() < poi.guiObject().ypos() &&
					w.length() < PlayletObject.maxProcessorOutWireLength){
				wList.add(w);
			}
		}
//		System.out.print("channel: " + poi.channel() + " has " + wList.size() + " avaialble processors for " + poi.nameToString());
		return wList;
	}
	private ArrayList<Wire> getAvailableGhostWires(PlayletObjectInterface poi){
		ArrayList<Wire> wList = new ArrayList<Wire>();
		for (Wire w: poi.inWireList()){
			if (w.source.type() == poi.type() &&
					w.source.guiObject().ghostYpos() < poi.guiObject().ghostYpos() &&
					w.ghostLength() < PlayletObject.maxProcessorOutWireLength){
				wList.add(w);
			}
		}
//		System.out.print("channel: " + poi.channel() + " has " + wList.size() + " avaialble processors for " + poi.nameToString());
		return wList;
	}
	private void makeProcessingChain(ArrayList<PlayletObjectInterface> loProList){
//		System.out.println("loProList size " + loProList.size());
		PlayletObjectInterface testPoi;
		PlayletObjectInterface nextPoi;
		ArrayList<PlayletObjectInterface> tempProList = new ArrayList<PlayletObjectInterface>();
		for (PlayletObjectInterface poi: loProList){
			tempProList.clear();
			for (PlayletObjectInterface nuPoi: proList){
				if (poi.channel() == nuPoi.channel()){
					tempProList.add(nuPoi);
				}
			}
			Collections.sort(tempProList, PlayletObject.xposComparator);
			testPoi = poi;
			int index = tempProList.indexOf(testPoi);
			while(true){
				if (index < 1){
					break;
				} else {
					index--;
					nextPoi = tempProList.get(index);
					Wire w = getWireLength(testPoi, nextPoi);
					if (w.length() <= PlayletObject.maxProcessorOutWireLength){
						w.setOn(true);
						testPoi = nextPoi;
					}
				}				
			}
			connectTestPoiToNearestIntrument(testPoi);
		}
		
	}
	private void connectTestPoiToNearestIntrument(PlayletObjectInterface testPoi){
		
	}
	private Wire getWireLength(PlayletObjectInterface dest, PlayletObjectInterface src){
		for (Wire w: dest.inWireList()){
			if (w.source == src){
				return w;
			}
		}
		return nullWire;			// arbitrarily large value
	}
//	private PlayletObjectInterface getNearestProcessor(PlayletObjectInterface poi){
//		
//		for (Wire w: poi.inWireList()){
//			
//		}
//	}
	private ArrayList<PlayletObjectInterface> makeOnlyShortestToInstrument(){
		// make map of shortest wire going to an instrument indexed by channel
		HashMap<Integer, Wire> shortWireList = new HashMap<Integer, Wire>();
		ArrayList<PlayletObjectInterface> loProList = new ArrayList<PlayletObjectInterface>();		// this will be a list of all the object attached to an instrument
		for (PlayletObjectInterface poi: instList){
//			System.out.println(poi.uniqueName() + " is being tested");
			if (!shortWireList.containsKey(poi.channel())){
//				System.out.println("adding channel " + poi.channel() + " to shortWireList");
				shortWireList.put(poi.channel(), nullWire);
			}
			// put the shortest wire per channel into hashmap
//			System.out.println(poi.inWireList().size() + " wires in inWireList");
			for (Wire w: poi.inWireList()){				
				if (w.source.channel() == poi.channel() && 
						w.length() < shortWireList.get(poi.channel()).length() && 
						w.length() < PlayletObject.maxProcessorOutWireLength){
					shortWireList.put(poi.channel(), w);
				}
			}
		}
		// switch on wires
		for (Integer index: shortWireList.keySet()){
			Wire w = shortWireList.get(index);
//			System.out.println("channel " + index + " wire " + w.toString() + " is to be set on");
			w.setOn(true);
			if (w.source != null && w.source.layer() == PlayletObject.GENERATOR_OBJECT){
				connectToResourceLayer(w.source);
			}
			loProList.add(w.source);
		}
//		System.out.println("done............................................");
		return loProList;
	}
	private ArrayList<PlayletObjectInterface> makeOnlyShortestGhostToInstrument(){
		// make map of shortest wire going to an instrument indexed by channel
		HashMap<Integer, Wire> shortWireList = new HashMap<Integer, Wire>();
		ArrayList<PlayletObjectInterface> loProList = new ArrayList<PlayletObjectInterface>();		// this will be a list of all the object attached to an instrument
		for (PlayletObjectInterface poi: instList){
//			System.out.println(poi.uniqueName() + " is being tested");
			if (!shortWireList.containsKey(poi.channel())){
//				System.out.println("adding channel " + poi.channel() + " to shortWireList");
				shortWireList.put(poi.channel(), nullWire);
			}
			// put the shortest wire per channel into hashmap
//			System.out.println(poi.inWireList().size() + " wires in inWireList");
			for (Wire w: poi.inWireList()){				
				if (w.source.channel() == poi.channel() && 
						w.ghostLength() < shortWireList.get(poi.channel()).ghostLength() && 
						w.ghostLength() < PlayletObject.maxProcessorOutWireLength){
					shortWireList.put(poi.channel(), w);
				}
			}
		}
		// switch on wires
		for (Integer index: shortWireList.keySet()){
			Wire w = shortWireList.get(index);
//			System.out.println("channel " + index + " wire " + w.toString() + " is to be set on");
			w.setGhostOn(true);
			if (w.source != null && w.source.layer() == PlayletObject.GENERATOR_OBJECT){
				connectGhostToResourceLayer(w.source);
			}
			loProList.add(w.source);
		}
//		System.out.println("done............................................");
		return loProList;
	}

	private void onlyOneInstrumentPerChannel(){
		ArrayList<PlayletObjectInterface> poiList = new ArrayList<PlayletObjectInterface>();
		for (PlayletObjectInterface poi: proList){
			if (poi.hasOutputPatch()){
				poiList.add(poi);
			}
		}
		//parent.statusPrint("poiList has " + poiList.size() + " items.");
		Wire tempWire;
		for (PlayletObjectInterface poi: poiList){
			tempWire = nullWire;
			for (Wire w: poi.outWireList()){
				if (w.on){
					if (w.length() < tempWire.length()){
						tempWire.setOn(false);
						tempWire = w;
					} else {
						w.setOn(false);
					}
				}
			}
		}
	}
	private void patchInstrumentToNearestProcessor(){
		Wire tempWire;
		for (PlayletObjectInterface poi: instList){
			tempWire = nullWire;
			for (Wire w: poi.inWireList()){
				if (w.source.channel() == poi.channel() 
						&& w.source.layer() == PlayletObject.PROCESSOR_OBJECT 
						&& w.length() < tempWire.length() 
						&& w.length() < PlayletObject.maxProcessorOutWireLength){
					tempWire = w;
				}
			}
			tempWire.setOn(true);
		}
	}
	private void switchOffAllWires(){
		for (Wire w: wireList){
			w.setOn(false);
		}
	}
	private void switchOffAllGhostWires(){
		for (Wire w: wireList){
			w.setGhostOn(false);
		}
	}
	private void doGeneratorAndInstrumentWires(){
		Collections.sort(proList, GUIObject.yPosComparator);
		HashMap<Integer, PlayletObjectInterface> firstMap = new HashMap<Integer, PlayletObjectInterface>();
		HashMap<Integer, PlayletObjectInterface> lastMap = new HashMap<Integer, PlayletObjectInterface>();
		HashMap<Integer, Wire> bestGen = new HashMap<Integer, Wire>();
		for (PlayletObjectInterface poi: proList){
			if (!firstMap.keySet().contains(poi.type())){
				firstMap.put(poi.type(), poi);
			}
			lastMap.put(poi.type(), poi);
		}
		for (PlayletObjectInterface poi: genList){
			// the closest generator to the top of the processor chain gets connected
			if (firstMap.containsKey(poi.type())){
				if (bestGen.containsKey(poi.type())){
					Wire wTemp =  switchOnCable(poi, firstMap.get(poi.type()));
					if (wTemp.length() < bestGen.get(poi.type()).length()){
						bestGen.get(poi.type()).setOn(false);
						wTemp.setOn(true);
						bestGen.put(poi.type(), wTemp);
					}
				} else {
					
				}
				
			}
		}
	}

	private void doLowestChordProgressionWires(){
		PlayletObjectInterface selectedPoi = new PlayletObject(-1000, -1000);
		boolean has = false;
		for (PlayletObjectInterface poi: resList){
			if (poi.type() == PlayletObject.CHORD_PROGRESSION){
				if (!has){
					selectedPoi = poi;
					selectedPoi.setAllOutWires(true);
					has = true;
				} else {
					if (poi.guiObject().ypos() > selectedPoi.guiObject().ypos()){
						selectedPoi.setAllOutWires(false);
						selectedPoi = poi;
						selectedPoi.setAllOutWires(true);
					}
				}
			}
		}
	}
	private void doProcessorChainWiresFromProList(){
		// attempts to do the same as doProcessorChainWires() but a different way......
		Collections.sort(proList, GUIObject.yPosComparator);
		HashMap<Integer, PlayletObjectInterface> poiMap = new HashMap<Integer, PlayletObjectInterface>();
		for (PlayletObjectInterface poi: proList){
			if (poiMap.keySet().contains(poi.type())){
				switchOnCable(poiMap.get(poi.type()), poi);
				poiMap.put(poi.type(), poi);
			} else {
				poiMap.put(poi.type(), poi);
			}
		} 		
	}
	private Wire switchOnCable(PlayletObjectInterface from, PlayletObjectInterface to){
		Wire xw = new Wire();
		for (Wire w: from.outWireList()){
			if (w.destination == to){
				w.setOn(true);
				return w;
			}
		}	
		return xw;
	}

	private void addWires(PlayletObjectInterface plobj){
		int[] connArr = PlayletObject.connList(plobj.layer());
		for (PlayletObjectInterface poi: bigPlobjList){
			if (isInArray(poi.layer(), connArr)){
				wireList.add(new Wire(plobj, poi));
			}
			if (isInArray(plobj.layer(), PlayletObject.connList(poi.layer()))){
				wireList.add(new Wire(poi, plobj));
			}
		}
	}
	private boolean isInArray(int i, int[] arr){
		for (int test: arr){
			if (i == test){
				return true;
			}
		}
		return false;
	}
	private void addPlobjToTheRightList(PlayletObjectInterface plobj){
		if (plobj.layer() == PlayletObject.RESOURCE_OBJECT){
			resList.add(plobj);
		} else if (plobj.layer() == PlayletObject.GENERATOR_OBJECT){
			genList.add(plobj);
		} else if (plobj.layer() == PlayletObject.PROCESSOR_OBJECT){
			proList.add(plobj);
		} else if (plobj.layer() == PlayletObject.INSTRUMENT_OBJECT){
			instList.add(plobj);
		}
	}

// toString methods ---------------------------------------------------------------------------
	public String toString(){
		String ret = "Surface toString ----------------------------------------------\n";
		return ret + resListToString() + genListToString() + proListToString() + instListToString() + wireListSeperatedToString() + gobjListToString();
	}
	public String resListToString(){
		String ret = "resList: " + resList.size() + " items.\n";
		for (PlayletObjectInterface r: resList){
			ret = ret + "    " + r.toString() + "\n";
		}
		return ret;
	}
	public String genListToString(){
		String ret = "genList: " + genList.size() + " items.\n";
		for (PlayletObjectInterface g: genList){
			ret = ret + "    " + g.toString() + "\n";
		}
		return ret;
	}
	public String proListToString(){
		String ret = "proList: " + proList.size() + " items.\n";
		for (PlayletObjectInterface p: proList){
			ret = ret + "    " + p.toString() + "\n";
		}
		return ret;
	}
	public String instListToString(){
		String ret = "instList: " + instList.size() + " items.\n";
		for (PlayletObjectInterface i: instList){
			ret = ret + "    " + i.toString() + "\n";
		}
		return ret;
	}
	public String wireListToString(){
		String ret = "wireList: " + wireList.size() + " items.\n";
		for (Wire w: wireList){
			ret = ret + "    " + w.toString() + "\n";
		}
		return ret;
	}
	public String wireListSeperatedToString(){
		String ret = "wireList: all ons\n";
		for (Wire w: wireList){
			if (w.on){
				ret = ret + "    " + w.toString() + "\n";
			}
		}
		ret = ret + "\n-- all offs ------------------------------\n";
		for (Wire w: wireList){
			if (!w.on){
				ret = ret + "    " + w.toString() + "\n";
			}
		}
		return ret;
	}
    private void newRandomBGColor(){
    	int r = rnd.nextInt(150) + 50;
    	int g = rnd.nextInt(150) + 50;
    	int b = rnd.nextInt(150) + 50;
    	parent.statusPrint("new BG color....." + r + ", " + g + ", " + b);
    	bgColor = new Color(r, g, b);
    	repaint(); 	
    }
	public String gobjListToString(){
		String ret = "gobjList: " + gobjList.size() + " items.\n";
		for (GUIObject g: gobjList){
			ret = ret + "    " + g.toString() + "\n";
		}
		return ret;
	}
}
