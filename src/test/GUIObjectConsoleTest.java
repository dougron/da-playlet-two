package test;
import java.util.ArrayList;

import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.GUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.gui_objects.HexagonGUIObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;


public class GUIObjectConsoleTest extends ConsoleProgram{

	public void run(){
		setSize(700, 700);
//		gobjTest();
		plobjTest();
	} 
	private void plobjTest(){
		ArrayList<PlayletObject> plobjList = new ArrayList<PlayletObject>();
//		plobjList.add(new ChordProgression());
//		plobjList.add(new BassGeneratorOne());
		for (PlayletObject plobj: plobjList){
			println(plobj.toString());
		}
	}
	private void gobjTest(){
		ArrayList<GUIObject> gobjList = new ArrayList<GUIObject>();
//		gobjList.add(new RoundRectangleGUIObject());		// creates error if unchecked, but not important as this is old code
		gobjList.add(new HexagonGUIObject(50.0, 50.0));
		for (GUIObject gobj: gobjList){
			println(gobj.toString());
		}
	}
}
