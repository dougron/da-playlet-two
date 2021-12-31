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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import DataObjects.ableton_device_control_utils.DeviceParamInfo;
import DataObjects.ableton_device_control_utils.controller.ControllerInfo;
import UDPUtils.OSCAtom;
import UDPUtils.OSCMessMaker;
import UDPUtils.UDPConnection;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.BooleanGUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.GUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.ShiftWidget;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.TimeDisplayWidget;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.number_things.StaticNumber;
import main.java.com.dougronthebold.DAPlayletTwo.patching.PatchingManager;
import main.java.com.dougronthebold.DAPlayletTwo.patching.Wire;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumAddKik;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumAddSnare;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.drums.DrumInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.Contour;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic.RhythmBuffer;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody.MelodyEscapeNote;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody.MelodyGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody.MelodyInstrument;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody.MelodySizeAndLengthContextEmbellisher;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.melody.MelodySyncopator;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.resource.ChordProgression;
import main.java.com.dougronthebold.DAPlayletTwo.upd_utils.ReceiveSocketThread;


/*
 * a version of Surface that uses a thread based game loop 
 */
public class SurfaceGame extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, ConsoleInterface, Runnable, PlobjParent{
	
	private ArrayList<PlayletObjectInterface> resList = new ArrayList<PlayletObjectInterface>();
	public ArrayList<PlayletObjectInterface> genList = new ArrayList<PlayletObjectInterface>();
	private ArrayList<PlayletObjectInterface> proList = new ArrayList<PlayletObjectInterface>();
	public ArrayList<PlayletObjectInterface> instList = new ArrayList<PlayletObjectInterface>();
	
	public ArrayList<PlayletObjectInterface> testList = new ArrayList<PlayletObjectInterface>();
	public int testListIndex = 0;
	
	private HashMap<Integer, ArrayList<PlayletObjectInterface>> plobjMapByType = new HashMap<Integer, ArrayList<PlayletObjectInterface>>();
	private ArrayList<Wire> wireList = new ArrayList<Wire>();
	private CopyOnWriteArrayList<GUIObject> gobjList = new CopyOnWriteArrayList<GUIObject>();
	private CopyOnWriteArrayList<PlayletObjectInterface> plobjToLoadList = new CopyOnWriteArrayList<PlayletObjectInterface>();
	private CopyOnWriteArrayList<ArrayList<OSCAtom>> udpRecList= new CopyOnWriteArrayList<ArrayList<OSCAtom>>();
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
	public int mouseX = 0;
	public int mouseY = 0;
	private Graphics2D g2d;
	private Color bgColor = new Color(100, 100, 100);
	private boolean mouseInWindow = false;
	private String keyInBuffer = "";
	
	public static final Wire nullWire = new Wire();		// a monstrously long wire for comparisons.....
	public static final GUIObject nullGobj = new GUIObject(0, 0);		// null GUIObject for placeholding
	private UDPConnection conn; 
	
	private boolean wireHasChanged = false;
	private boolean isPlaying = false;
	private ShiftWidget shiftWidget = new ShiftWidget(50.0, 50.0, 35.0, 35.0);
	private TimeDisplayWidget tdWidget = new TimeDisplayWidget(200, 50);
	
	private PatchingManager pm = new PatchingManager();
	private PlobjAdder pa = new PlobjAdder(this);
	
	private final int DELAY = 25;
	private Thread animator;
	private Thread udpReceiveThread;
	private PlayletObjectInterface testPlobj;
	//physics testing stuff -----------
	private double dx = 0.0;
	private double dy = 0.0;
	private boolean upIsOn = false;
	private boolean downIsOn = false;
	private boolean leftIsOn = false;
	private boolean rightIsOn = false;
	private boolean brakeIsOn = false;
	
	private boolean forceReRender = false;
	

	
	public SurfaceGame(SurfaceParent p){
		parent = p;	
		conn = new UDPConnection(7800);
		udpReceiveThread = new Thread(new ReceiveSocketThread(udpRecList));
		udpReceiveThread.start();
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
// Runnable methods ------------------------------------------------------------
	@Override
	public void run() {
		long beforeTime, timeDiff, sleep;
		beforeTime = System.currentTimeMillis();
//		addTestPlobj();
		loadTestPlobjArrayList();

		while (true) {		
		    cycle();
		    repaint();
		    timeDiff = System.currentTimeMillis() - beforeTime;
		    sleep = DELAY - timeDiff;
		
		    if (sleep < 0) {
		        sleep = 2;
		    }
		
		    try {
		        Thread.sleep(sleep);
		    } catch (InterruptedException e) {
		        System.out.println("Interrupted: " + e.getMessage());
		    }
		
		    beforeTime = System.currentTimeMillis();
		}
	}
// this could possibly be a JPanel thing after all.............
	@Override
	public void addNotify() {
		super.addNotify();
		animator = new Thread(this);
		animator.start();
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
				//testForReRender();
			}		
			//sortGobjAndRepaint();
		}		
	}
	
// KeyListener methods ---------------------------------------------------------

	public void dealWithKeyTyped(KeyEvent e){
		
	}
	public void dealWithKeyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W){
			upIsOn = true;
		} else if(key == KeyEvent.VK_S){
			downIsOn = true;
		} else if(key == KeyEvent.VK_A){
			leftIsOn = true;
		} else if (key == KeyEvent.VK_D){
			rightIsOn = true;
		} else if (key == KeyEvent.VK_Z){
			brakeIsOn = true;
		} else if (key == KeyEvent.VK_C){
			dx -= 1;
			dy -= 1;
		} else if (key == KeyEvent.VK_SHIFT){
			if (!shiftWidget.shiftOn){
				shiftKey = true;
				shiftWidget.setShift(true);
				testMouseOver();
				//parent.statusPrint("shiftKey true");
			}
		} else if (key == KeyEvent.VK_G){
			if (shiftWidget.shiftOn){
				cancelGhosts();
			} else {
				moveToGhosts();		
			}
			doWireSwitching();
		} else if (key == KeyEvent.VK_H){
			swopObjectsAndGhosts();	
		} else if (key == KeyEvent.VK_T){
			//System.out.println("Adding from testList");
			addFromTestList();	
		}
		pa.testKeyInBuffer(e.getKeyText(key));
	}
	public void dealWithKeyReleased(KeyEvent e){
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W){
			upIsOn = false;
		} else if(key == KeyEvent.VK_S){
			downIsOn = false;
		} else if(key == KeyEvent.VK_A){
			leftIsOn = false;
		} else if (key == KeyEvent.VK_D){
			rightIsOn = false;
		} else if (key == KeyEvent.VK_Z){
			brakeIsOn = false;
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
//    		repaint();
//    		testMouseOver();
    	}
    	if (shiftWidget.isMouseOvered(e.getX(), e.getY())){
    		shiftWidget.toggleShift();
    	}
    	testMouseOverButton(e);
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
	    		//doWireSwitching();
	    		//repaint();
    		} else {
    			double x = e.getX() - deltaX;
	    		double y = e.getY() - deltaY;
	    		selectedGobj.setGhostPos(x, y);
	    		//System.out.println("setting pos to: " + x + ", " + y);
	    		//doWireSwitching();
	    		//repaint();
    		}	    		
    	} else {
    		double xdiff = e.getX() - dragPoint.getX();
    		double ydiff = e.getY() - dragPoint.getY();
//    		pLoz.changeShadowOffset(xdiff * changeShadowFactor * -1, ydiff * changeShadowFactor * -1);
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
// ConsoleInterface methods --------------------------------------------------
    public void consolePrint(String str){
    	System.out.println(str);
    }
// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	forceReRender = true;
    }
	
// privates -------------------------------------------------------------------------------------
    private void addFromTestList(){
    	if (testListIndex < testList.size()){
    		PlayletObjectInterface plobj = testList.get(testListIndex);
    		plobjToLoadList(plobj);
    		testListIndex++;
    	}
    }
    private void testMouseOverButton(MouseEvent e){
    	int x = e.getX();
    	int y = e.getY();
    	//System.out.println("testMouseOverButton----------------------------");
    	boolean flag = false;
    	for (GUIObject gobj: gobjList){
    		//System.out.println(gobj.parent().nameToString() + " tested");
    		if (shiftWidget.shiftOn){
    			flag = gobj.testForGhostButtonPress(x, y);
        		if (flag) break;
    		} else {
    			flag = gobj.testForButtonPress(x, y);
        		if (flag) break;
    		}   		
    	}
    }
// test stuff -----------------------------------------------------------
    private void addTestPlobj(){		// potentially causes problems with game loop
    	
//    	testPlobj = new DrumGeneratorOne(40, 300, "DG_1");
//    	plobjToLoadList(testPlobj);
    	plobjToLoadList(new DrumGeneratorOne(50, 300, "DG_1", this));
    	plobjToLoadList(new DrumAddKik(50, 450, "KIK", this));
    	plobjToLoadList(new DrumAddSnare(50, 550, "SNR", this));
    	plobjToLoadList(new DrumInstrumentOne(50, 720, "DRUM", 0, 0, this, this));
//    	BassGeneratorOne bg1 = new BassGeneratorOne(50, 300, "BG_1");
//    	plobjToLoadList(bg1);
//    	bg1.guiObject().setHasGhost(true);
//    	bg1.guiObject().setGhostPos(30, 280);
//   	BassInstrumentOne bs1 = new BassInstrumentOne(80, 720, "BassInst1", 1, 0, this, this);
//    	plobjToLoadList(bs1);
//    	for (Wire w: bs1.inWireList()){
//    		w.setOn(true);
//    		w.setGhostOn(true);
//    	}
    	plobjToLoadList(new BassInstrumentOne(100, 720, "BassInst2", 2, 0, this, this));
//    	plobjToLoadList(new MasterInstrumentObject(400, 720, "Master"));					//out
//    	plobjToLoadList(new MasterSloWahProcessor(400, 450, "MPS"));						//out
//    	plobjToLoadList(new MasterGenerator(400, 300, "MG"));								//out
    	plobjToLoadList(new BassProcessorOne(250, 450, "BP_2", this));
//    	plobjToLoadList(new KeysGeneratorOne(250, 300, "KeysGenPad", this));
//    	plobjToLoadList(new KeysProcessorOne(250, 400, "KP1", this));
//    	plobjToLoadList(new KeysProcessorOne(250, 500, "KP2", this));							//out
//    	plobjToLoadList(new KeysInstrumentOne(350, 720, "xxxKeysInst3", 3, 0, this, this));
    	plobjToLoadList(new BassGeneratorOne(150, 300, "BG_1", this));
    	
    	plobjToLoadList(new MelodyGeneratorOne(400, 300, "MG_1", this));
    	plobjToLoadList(new MelodyInstrument(400, 720, "MInst1", 4, 0, this, this));

    	plobjToLoadList(new ChordProgression(50, 150, "CP_1", this));
    	plobjToLoadList(new ChordProgression(200, 150, "CP_2", this));
    	plobjToLoadList(new RhythmBuffer(100, 100, "RB_1", this));
    	plobjToLoadList(new Contour(200, 100, "CN_1", this));
    	doWireSwitching();
    	sendInstrumentInitializationMessage();
    	clearAllClips();
    }
    private void loadTestPlobjArrayList(){
    	testList.add(new ChordProgression(50, 150, "CP_1", this));
    	testList.add(new ChordProgression(200, 150, "CP_2", this));
    	testList.add(new RhythmBuffer(120, 100, "RB_1", this));
    	testList.add(new Contour(180, 100, "CN_1", this));
    	testList.add(new DrumGeneratorOne(50, 300, "DG_1", this));
    	testList.add(new DrumAddKik(50, 450, "KIK", this));
    	testList.add(new DrumAddSnare(50, 550, "SNR", this));
    	testList.add(new DrumInstrumentOne(50, 720, "DRUM", 0, 0, this, this));
//    	BassGeneratorOne bg1 = new BassGeneratorOne(50, 300, "BG_1");
//    	testList.add(bg1);
//    	bg1.guiObject().setHasGhost(true);
//    	bg1.guiObject().setGhostPos(30, 280);
    	BassInstrumentOne bs1 = new BassInstrumentOne(120, 720, "BassInst1", 1, 0, this, this);
    	testList.add(bs1);
//    	for (Wire w: bs1.inWireList()){
//    		w.setOn(true);
//    		w.setGhostOn(true);
//    	}
    	testList.add(new BassInstrumentOne(170, 720, "BassInst2", 2, 0, this, this));
//    	testList.add(new MasterInstrumentObject(400, 720, "Master"));					//out
//    	testList.add(new MasterSloWahProcessor(400, 450, "MPS"));						//out
//    	testList.add(new MasterGenerator(400, 300, "MG"));								//out
    	testList.add(new BassProcessorOne(150, 450, "BP_2", this));
    	testList.add(new BassGeneratorOne(150, 300, "BG_1", this));
    	
    	testList.add(new KeysGeneratorOne(250, 300, "KeysGenPad", this));
    	testList.add(new KeysProcessorOne(250, 400, "KP1", this));
    	testList.add(new KeysProcessorOne(250, 500, "KP2", this));							//out
    	testList.add(new KeysInstrumentOne(250, 720, "xxxKeysInst3", 3, 0, this, this));
    	
    	
    	
    	testList.add(new MelodyInstrument(350, 720, "MInst1", 4, 0, this, this));
    	testList.add(new MelodyGeneratorOne(350, 300, "MG_1", this));
    	testList.add(new MelodySyncopator(400, 450, "MSync", this));
    	testList.add(new MelodyEscapeNote(400, 450, "ESC", this));
    	testList.add(new MelodySizeAndLengthContextEmbellisher(350, 450, "ConEmb", this));
    	
    }
   
// plobj managementg stuff ---------------------------------------------
    public void plobjToLoadList(PlayletObjectInterface plobj){
    	plobjToLoadList.add(plobj);
    }
    public void addPlayletObjectWithoutWireSwitching(PlayletObjectInterface plobj){
		
		addPlobjToTheRightList(plobj);
		gobjList.add(plobj.guiObject());
		sortCopyOnWriteArray(gobjList, GUIObject.heightComparator);
		addWires(plobj);
		bigPlobjList.add(plobj);
		if (plobjMapByType.containsKey(plobj.type())){
			plobjMapByType.get(plobj.type()).add(plobj);
		} else {
			plobjMapByType.put(plobj.type(), new ArrayList<PlayletObjectInterface>());
			plobjMapByType.get(plobj.type()).add(plobj);
		}
		if (plobj.layer() == PlayletObject.INSTRUMENT_OBJECT){
			sendInstrumentInitializationMessage();
		}
	}
    private void sortCopyOnWriteArray(CopyOnWriteArrayList list, Comparator<GUIObject> comp){
    	List tempList = Arrays.asList(gobjList.toArray());
    	Collections.sort(tempList, comp);
    	gobjList.clear();
    	gobjList.addAll(tempList);
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
    private void cancelGhosts(){
    	for (GUIObject gobj: gobjList){
    		gobj.setHasGhost(false);
    	}
    }
    private void moveToGhosts(){
    	for (GUIObject gobj: gobjList){
    		gobj.manifestGhost();
    	}
    }
    private void swopObjectsAndGhosts(){
    	for (GUIObject gobj: gobjList){
    		gobj.swopObjAndGhost();
    	}
    	//doWireSwitching();
    }
// run() methods dependants --------------------------------------------
    private void cycle(){
//    	testWASD();
    	dealWithPlobjToLoadList();
    	dealWithUDPRecList();
    	doWireSwitching();
    	testForReRender();
		testInstrumentsForStopPlay();
    	sortGobjList();
    }
    private void dealWithPlobjToLoadList(){
    	for (PlayletObjectInterface plobj: plobjToLoadList){
    		addPlayletObjectWithoutWireSwitching(plobj);
    	}
    	plobjToLoadList.clear();
    }
    private void dealWithUDPRecList(){
    	for (ArrayList<OSCAtom> atList: udpRecList){
    		String firstArg = atList.get(0).itemAsString();
    		if (firstArg.equals("position")){
    			setPositionFromAtomList(atList);
    		}
    	}
    	udpRecList.clear();
    }
    private void setPositionFromAtomList(ArrayList<OSCAtom> atList){
    	// atList is assumed to have 3 items index 0 = "position", index 1 = bar as int, index 2 = beat as int
    	tdWidget.setBarsAndBeats(atList.get(1).intValue, atList.get(2).intValue);
    }
    private void sortGobjList(){
    	sortCopyOnWriteArray(gobjList, GUIObject.heightComparator);
    }
    private void testWASD(){
    	if (upIsOn) dy -= 1;
    	if (downIsOn) dy += 1;
    	if (leftIsOn) dx -= 1;
    	if (rightIsOn) dx += 1;
    	if (brakeIsOn){
    		dx = dx * 0.95;
    		dy = dy * 0.95;
    	}
    	double xp = testPlobj.guiObject().xpos() + dx;
    	double yp = testPlobj.guiObject().ypos() + dy;
    	testPlobj.guiObject().setPos(xp, yp);
    	if (xp < 0 || xp > parent.xSize()){
    		dx = dx * -1;
    	} 
    	if (yp < 0 || yp > parent.ySize()){
    		dy = dy * -1;
    	}
    }
// wire switching stuff -------------------------------------------------
    public void doWireSwitching(){
		switchOffAllWires();
		switchOffAllGhostWires();
		pm.repatch(instList, proList);
		
	}
    private void testForReRender(){
    	boolean rerender = false;
    	if (forceReRender){
    		rerender = true;
    	} else if (testWiresForReRender()){
    		rerender = true;	    		
    	} else if (testForHeightChange()){
    		rerender = true;
    	}
    	if (rerender){
    		//System.out.println("Status has changed --- RERENDER!!!!!!");
	    	newRandomBGColor();
	    	renderAll();
	    	forceReRender = false;
    	}
    	
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
// music rendering and messaging stuff -----------------------------------------------
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
    private void renderAll(){
//    	parent.statusPrint("rendering");
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
    private void clearAllClips(){
    	for (PlayletObjectInterface poi: instList){
    		poi.in(PlayletObject.nullPNO);
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
//    	mess.addItem("Hello World.....");
//    	for (int i = 0; i < 13095; i++){
//    		mess.addItem(i);
//    	}   	
    }
// misc-----------------------------------------------------------------
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
    private void newRandomBGColor(){
    	int r = rnd.nextInt(150) + 50;
    	int g = rnd.nextInt(150) + 50;
    	int b = rnd.nextInt(150) + 50;
    	parent.statusPrint("new BG color....." + r + ", " + g + ", " + b);
    	bgColor = new Color(r, g, b);
//    	repaint(); 	
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
    	//repaint();
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
// init stuff ----------------------------------------------------------
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
// drawing stuff ------------------------------------------------------------
    private void doDrawing(Graphics g){
		g2d = (Graphics2D) g;
		setAntiAliasing();
		setBackground(bgColor);
		//doTestRect();
		do2DObjectList();
		//repaint();
    }
    private void do2DObjectList(){
    	
//    	Iterator<GUIObject> git = gobjList.iterator();
//    	GUIObject gobj;
//    	while (git.hasNext()){
//    		gobj = git.next();
//   		gobj.addShadow(g2d, bgColor, gobj.objParams);
//    	}
    	for (GUIObject gobj: gobjList){
    		gobj.addShadow(g2d, bgColor, gobj.objParams);
    	}
    	for (Wire pw: wireList){
    		pw.addObject(g2d);
    	}
    	for (GUIObject gobj1: gobjList){
    		gobj1.addGhostConnector(g2d);
    	}
    	if (shiftWidget.shiftOn){
    		addObjects();
    		addGhosts();
    	} else {
    		addGhosts();
    		addObjects();
    	}
    	tdWidget.addObject(g2d);
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
}
