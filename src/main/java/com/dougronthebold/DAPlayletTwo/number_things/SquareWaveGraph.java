package main.java.com.dougronthebold.DAPlayletTwo.number_things;
import java.awt.EventQueue;

public class SquareWaveGraph {

	public double waveLength = 4.0;		// default values wavelength in quarter notes
	public double pulseWidth = 0.5;		// pulse width in percentage
	public double offset = 0.0;			// offset in percentage
	
	public SquareWaveGraph(double waveLength, double pulseWidth, double offset){
		this.waveLength = waveLength;
		this.pulseWidth = pulseWidth;
		this.offset = offset;
	}
	public SquareWaveGraph(double waveLength){
		this.waveLength = waveLength;
	}
	public SquareWaveGraph(){
		
	}
	public int getValue(double pos){
		double modPos = pos % waveLength;
		if (modPos >= offset * waveLength && modPos <= waveLength * (pulseWidth + offset)){
			return 1;
		} else {
			return 0;
		}
	}
	public void setWaveLength(double wl){
		waveLength = wl;
	}
	public void setPulseWidth(double pw){
		pulseWidth = pw;
	}
	public void setOffset(double of){
		offset = of;
	}
	public String toString(){
		String ret = "SquareWaveGraph:----\n";
		ret += "  waveLength:   " + waveLength + "\n";
		ret += "  pulseWidth:   " + pulseWidth + "\n";
		ret += "  offset:       " + offset + "\n";
		return ret;
	}
	
	
	
// static methods ------------------------------------------------------------------------------------	
	public static void main(String[] args) {
        SquareWaveGraph swg = new SquareWaveGraph(4.0, 0.25, 0.25);
        for (double d = 0.0; d < 20.0; d += 0.5){
        	System.out.println(d + ": " + swg.getValue(d));
        }
    }
}
