package main.java.com.dougronthebold.DAPlayletTwo.upd_utils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import acm.program.ConsoleProgram;

public class UDPReceive{

	public UDPReceive(){
		//System.out.println("receiver running......");

		try {
			int port = 7800;
			
			DatagramSocket dsocket = new DatagramSocket(port);
			
			byte[] buffer = new byte[2048];
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			while (true){
				dsocket.receive(packet);
				String msg = new String(buffer, 0, packet.getLength());
				//System.out.println(packet.getAddress().getHostName() + ": " + msg);
				System.out.println(packet.getAddress().getHostName() + ": " + msg);
				packet.setLength(buffer.length);
			}
			
			
		} catch (Exception e) {
			
			System.err.println(e);
		}
	}
	
	public static void main (String[] args)
	{
		new UDPReceive();
	}
}
