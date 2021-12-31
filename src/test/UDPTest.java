package test;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;

import com.illposed.osc.OSCPacket;




import acm.program.ConsoleProgram;


public class UDPTest extends ConsoleProgram{

	public void run(){
		setSize(500, 500);
		getLocalHost();
		
		sendUDPMessage("/", ",sss", new String[]{"hey", "jude", "jazzz"});
//		test1();
//		OSCMessMaker mess = new OSCMessMaker();
//		mess.addItem(1);
//		println(mess.toString());
//		UDPConnection conn = new UDPConnection(7800);
//		conn.sendUDPMessage(mess);
		
				
	}
	private void makePacket(String str){
//		OSCPacket op = 
	}
	private void test1(){
		int i = 1000;
//		BigInteger bi = BigInteger.valueOf(i);
		byte[] bArr = intToByteArray(i);
		for (byte b: bArr){
			println(b);
		}
	}
	private byte[] intToByteArray(int value){
		return new byte[]{
				(byte)(value >>> 24),
				(byte)(value >>> 16),
				(byte)(value >>> 8),
				(byte)value};

	}
	private void getLocalHost(){
		try {
			String ia = Inet4Address.getLocalHost().getHostAddress();
			println(ia);
		} catch (Exception e){
			
		}
	}
	private void sendOSCMessage(String str){
//		int port = 7800;
//		OSCPort sender = new OSCPort();
	}
	private void sendUDPMessage(String addr, String type, String[] strArr){
		try {
			int port = 7800;
			InetAddress add = Inet4Address.getLocalHost();
//			println("add: " + add);
			InetAddress address = InetAddress.getByName("127.0.0.1");
//			println(address);
			byte[] message = get32bitByteArray(addr, type, strArr);
			for (byte b: message) println(b);
			DatagramPacket dp = new DatagramPacket(message, message.length, add, port);
			DatagramSocket dsocket = new DatagramSocket();
			//dsocket.setInterface(InetAddress.getByName(<localhost>));
			dsocket.send(dp);
			dsocket.close();
		} catch (Exception e){
			
		}
	}
	private byte[] get32bitByteArray(String addr, String type, String[] strArr){
		int len = 0;
		byte[] byteAddr = getOSCByteArray(addr);
		len += byteAddr.length;
		byte[] byteType = getOSCByteArray(type);
		len += byteType.length;
		byte[][] argArr = new byte[strArr.length][];
		for (int i = 0; i < strArr.length; i++){
			argArr[i] = getOSCByteArray(strArr[i]);
			len += argArr[i].length;
		}
		byte[] bigArr = new byte[len];
		int index = 0;
		for (byte b: byteAddr){
			bigArr[index] = b;
			index++;
		}
		for (byte b: byteType){
			bigArr[index] = b;
			index++;
		}
		for (byte[] ba: argArr){
			for (byte b: ba){
				bigArr[index] = b;
				index++;
			}
		}
		return bigArr;
	}
	private byte[] getOSCByteArray(String str){
		int len = str.length();
		int remainder = len % 4;
		byte[] mess = str.getBytes();
		byte[] bArr = new byte[len + 4 - remainder];
//		bArr[0] = 47;
		for (int i = 0; i < mess.length; i++){
			bArr[i] = mess[i];
		}
		return bArr;
	}
}
