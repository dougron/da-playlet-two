package test;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.ConsoleInterface;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.bass.BassProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysGeneratorOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysInstrumentOne;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys.KeysProcessorOne;
import main.java.com.dougronthebold.DAPlayletTwo.ui.Surface;
import main.java.com.dougronthebold.DAPlayletTwo.ui.SurfaceParent;


public class SurfaceConsoleTest extends ConsoleProgram implements SurfaceParent, ConsoleInterface, PlobjParent{

	
	public void run(){
		setSize(700, 700);
		Surface s = new Surface(this);
		addResources(s);
		addGenerators(s);
		addProcessors(s);
		addInstruments(s);
		s.doWireSwitching();
		println(s.toString());
		s.render();
		printAllPNOs(s);
	}
	public void consolePrint(String str){
		println(str);
	}
	public void statusPrint(String str){
		println("statusLine: " + str);
	}
	public int xSize(){
		return 0;
	}
	public int ySize(){
		return 0;
	}
	
// privates c-----------------------------------------------------------------
	private void printAllPNOs(Surface s){
		for (PlayletObjectInterface poi: s.bigPlobjList){
			println("==============================================");
			println(poi.toString());
			println("receivedPNO:---------------------------------");
			println(poi.recievedPNORenderStory());
			println("processedPNO:---------------------------------");
			println(poi.processedPNORenderStory());
		}
	}
	
	private void addInstruments(Surface s){
		s.addPlayletObject(new BassInstrumentOne(40.0, 300, "_1", this, this));
		s.addPlayletObject(new BassInstrumentOne(100.0, 300, "_2", this, this));
		s.addPlayletObject(new KeysInstrumentOne(100.0, 200, "_1", this, this));
		s.addPlayletObject(new KeysInstrumentOne(100.0, 300, "_2", this, this));
	}
	private void addProcessors(Surface s){
		s.addPlayletObject(new BassProcessorOne(40.0, 230.0, "_1", this));
		s.addPlayletObject(new BassProcessorOne(40.0, 220.0, "_2", this));
		s.addPlayletObject(new BassProcessorOne(40.0, 240.0, "_3", this));
		s.addPlayletObject(new KeysProcessorOne(100.0, 200.0, "_1", this));
	}
	private void addGenerators(Surface s){
		s.addPlayletObject(new BassGeneratorOne(40.0, 100.0, "_1", this));
		s.addPlayletObject(new BassGeneratorOne(200.0, 100.0, "_2", this));
		s.addPlayletObject(new KeysGeneratorOne(70.0, 100.0, "_1", this));
		s.addPlayletObject(new KeysGeneratorOne(100.0, 105.0, "_2", this));
	}
	private void addResources(Surface s){
//		s.addPlayletObject(new ChordProgression(30.0, 30.0, "_1"));
//		s.addPlayletObject(new ChordProgression(100.0, 35.0, "_2"));
//		s.addPlayletObject(new RhythmBuffer(20.0, 80.0, "_1"));
//		s.addPlayletObject(new RhythmBuffer(100.0, 80.0, "_2"));
//		s.addPlayletObject(new Contour(40.0, 40.0, "_1"));
//		s.addPlayletObject(new Contour(300.0, 40.0, "_2"));
	}
// PlobjParent methods ---------------------------------------------------------
    public void setForceReRenderOn(){
    	
    }
   
}
