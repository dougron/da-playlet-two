package test;
import acm.program.ConsoleProgram;
import main.java.com.dougronthebold.DAPlayletTwo.ui.PlobjAdder;

public class PlobjAdderConsoleTest extends ConsoleProgram{

	
	public void run(){
		setSize(700, 700);
		PlobjAdder pa = new PlobjAdder(this);
		pa.testKeyInBuffer("M");
		pa.testKeyInBuffer("P");
		pa.testKeyInBuffer("S");
	}
}
