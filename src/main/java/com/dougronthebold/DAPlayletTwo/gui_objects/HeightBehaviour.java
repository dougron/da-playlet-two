package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;

/*
 * class for wrapping behaviour parameters for when a GUIObject height is changed
 */
public class HeightBehaviour {

	public int min;
	public int max;
	public int increment = 3;
	public int fineIncrement = 1;
	public boolean useHeightValueForOutput = true;
	public int behaviourModel = DEFAULT_BEHAVIOUR;
	public String[] guiValueArray;
//	public int guiValueArrayIndex;
//	public int height;
	
	public HeightBehaviour(int min, int max){
		// basic behaviour that sets a minimum and maximum height
		this.min = min;
		this.max = max;
	}
	public HeightBehaviour(int behave){
		if (behave == INSTRUMENT_BEHAVIOUR){
			setupForInstrument();
		} else {
			setDefaults();
		}
	}
	public HeightBehaviour(){
		setDefaults();
	}
	public HeightBehaviour(String[] strArr, int min, int stepSize){
		guiValueArray = strArr;
		fineIncrement = stepSize;
		increment = stepSize;
		this.min = min;
		max = min + (strArr.length - 1) * stepSize;
		behaviourModel = CUSTOM_ARRAY_BEHAVIOUR;
//		guiValueArrayIndex = 0;
	}
	public boolean testHeight(GUIObjectParams params){
		boolean heightNeededConstraining = false;
		if (params.height < min){
			params.height = min;
			heightNeededConstraining = true;
		} else if (params.height > max){
			params.height = max;
			heightNeededConstraining = true;
		}
		
		return heightNeededConstraining;
	}
	public double heightForUDPMessage(GUIObjectParams params){
		// returns the height in range 0.0 - 1.0 where 0.0 represents height min and 1.0 represents height max
		return (double)(params.height - min) / (max - min); 
	}
	public String heightForGUI(GUIObjectParams params){
		if (behaviourModel == INSTRUMENT_BEHAVIOUR){
			return Integer.toString((int)(heightForUDPMessage(params) * 66) -60) + "db";
		} else if (behaviourModel == CUSTOM_ARRAY_BEHAVIOUR){
			int index = (params.height - min) / increment;
			if (index < 0){
				index = 0;
			} else if (index > guiValueArray.length - 1){
				index = guiValueArray.length - 1;
			}
			return guiValueArray[index];
		}
		return Integer.toString(params.height);
	}
	public int getIncrement(int notches, boolean fineControl){
		if (fineControl){
			return notches * fineIncrement;
		} else {
			return notches * increment;
		}
	}
//	public void doHeightbehaviour(){
//		if (behaviourModel == INSTRUMENT_BEHAVIOUR){
//			sendVolumeInformationViaUDP();
//		}
//	}
// privates -----------------------------------------------------------------
	private void sendVolumeInformationViaUDP(){

	}
	private void setDefaults(){
		this.min = defaultMin;
		this.max = defaultMax;
		behaviourModel = DEFAULT_BEHAVIOUR;
	}
	private void setupForInstrument(){
		this.min = instrumentMin;
		this.max = instrumentMax;
		behaviourModel = INSTRUMENT_BEHAVIOUR;
	}
	
	
	
	public static final int DEFAULT_BEHAVIOUR = 0;
	public static final int INSTRUMENT_BEHAVIOUR = 1;
	public static final int CUSTOM_ARRAY_BEHAVIOUR = 2;
	
	public static final int instrumentMin = 6;
	public static final int instrumentMax = 24;
	
	public static final int defaultMin = 6;
	public static final int defaultMax = 50;
}
