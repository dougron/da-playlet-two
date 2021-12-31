package main.java.com.dougronthebold.DAPlayletTwo.gui_objects;
/*
 * wrapper for communication between a button and its parent, to avoid having endless 
 * methods in the PlayletObjectInterface that mean nothing to most classes implementing it
 */
public class ButtonInstruction {
	
	public int instruction;
	public int intParam;
	public double doubleParam;

	public ButtonInstruction(int instruction){
		this.instruction = instruction;
	}
}
