package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.keys;
import java.awt.Color;

import PlugIns.PlugInRandomBendOnLongNote;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class KeysRandomBend extends PlayletObject implements PlayletObjectInterface{
	
	private PlugInRandomBendOnLongNote plug = new PlugInRandomBendOnLongNote(0.5, 0.75);

	public KeysRandomBend(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.KEYS_PROCESSOR, x, y, uniqueName, pp);
		guiObject.setBGColor(new Color(100, 50, 150, 255));
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
		plug.process(processedPNO.pnl(), ppa);
		sendPNOCopyToOuts(processedPNO.copy());
	}
	
	
}
