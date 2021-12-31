package main.java.com.dougronthebold.DAPlayletTwo.playlet_object.generic;
import PlugIns.PlugInLegato;
import main.java.com.dougronthebold.DAPlayletTwo.interfaces.PlobjParent;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PNO;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObject;
import main.java.com.dougronthebold.DAPlayletTwo.playlet_object.PlayletObjectInterface;

public class LegatoProcessor  extends PlayletObject implements PlayletObjectInterface{

	private PlugInLegato plug = new PlugInLegato(new double[]{1.0});
	
	public LegatoProcessor(double x, double y, String uniqueName, PlobjParent pp){
		super(PlayletObject.ROUND_RECTANGLE, PlayletObject.BASS_PROCESSOR, x, y, uniqueName, pp);
	}
	
	public void in(PNO pno){
		receivedPNO = pno;
		processedPNO = pno.copy();
//		ppa.rnd.reset();
		plug.process(processedPNO.pnl(), ppa);
		sendPNOCopyToOuts(processedPNO.copy());
	}
}
