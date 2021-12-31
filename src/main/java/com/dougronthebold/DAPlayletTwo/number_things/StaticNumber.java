package main.java.com.dougronthebold.DAPlayletTwo.number_things;

public class StaticNumber {

	private static int number = -1;
	
	public static int nextInt(){
		number += 1;
		return number;
	}
	
}
