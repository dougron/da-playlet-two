package main.java.com.dougronthebold.DAPlayletTwo.number_things;

public class SineWaveGraph {

	public double waveLength = 4.0;		// default values wavelength in quarter notes
	public double amplitude = 1.0;		// pulse width in percentage
	public double offset = 0.0;			// offset in percentage. Limited to 0. - 1. from panel but could arguably be -1. - 1.
	
	public boolean modulateWaveLength = false;
	public SineWaveGraph waveLengthModulator;
	public double waveLengthModDepth = 0.0;
	
	public boolean modulateAmplitude = false;
	public SineWaveGraph amplitudeModulator;
	public double amplitudeModDepth = 0.0;
	
	public boolean modulateOffset = false;
	public SineWaveGraph offsetModulator;
	public double offsetModDepth = 0.0;
	
	public boolean isOutput = true;
	public String name;
	
	
//	public SineWaveGraph(String name, double waveLength, double amp, double offset){
//		this.waveLength = waveLength;
//		this.amplitude = amp;
//		this.offset = offset;
//		this.name = name;
//	}
//	public SineWaveGraph(String name, double waveLength){
//		this.waveLength = waveLength;
//		this.name = name;
//	}
	public SineWaveGraph(String name, boolean isOutput){
		this.name = name;
		this.isOutput = isOutput;
	}
	public double getValue(double pos){
		double wl;
		double amp;
		double off;
		if (modulateWaveLength){
			wl = waveLength * (waveLengthModulator.getValue(pos) * waveLengthModDepth + 1.0);	// 1 - gets sine wave into range 0.0 - 2.0
		} else {
			wl = waveLength;
		}
		if (modulateAmplitude){
			amp = amplitude * (amplitudeModulator.getValue(pos) * amplitudeModDepth + 1.0); 	// ditto
		} else {
			amp = amplitude;
		}
		if (modulateOffset){
			off = offset + offsetModulator.getValue(pos) * offsetModDepth;
		} else {
			off = offset;
		}
		double d = Math.sin((pos / wl + off) * Math.PI * 2) * amp;
		
		return d;
	}
	public String toString(){
		String ret = "SineWaveGraph: " + name + " ---------------------------\n";
		ret += "waveLength/amplitude/offset:      " + String.format("%.2f", waveLength) + "/" + String.format("%.2f", amplitude) + "/" + String.format("%.2f", offset) + "\n";
		ret += "isOutput:  " + isOutput;
		ret += "modulate?: " + modulateWaveLength + "/" + modulateAmplitude + "/" + modulateOffset + "\n";
		ret += "source:    ";
		if (modulateWaveLength){
			ret += waveLengthModulator.name;
		} else {
			ret += "none";
		}
		ret += "/";
		if (modulateAmplitude){
			ret += amplitudeModulator.name;
		} else {
			ret += "none";
		}
		ret += "/";
		if (modulateOffset){
			ret += offsetModulator.name;
		} else {
			ret += "none";
		}
		ret += "\n";
		ret += "modDepth:    " + waveLengthModDepth + "/" + amplitudeModDepth + "/" + offsetModDepth + "\n";
		return ret;
	}
}
