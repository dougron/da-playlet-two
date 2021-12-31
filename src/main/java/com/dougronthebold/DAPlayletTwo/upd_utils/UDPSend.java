package main.java.com.dougronthebold.DAPlayletTwo.upd_utils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSend {
	public static void main(String args[]){
		try {
			String host = "localhost";
			int port = 7800;
			
			byte[] message = "flamjangle....".getBytes();
			
			InetAddress address = InetAddress.getByName(host);
			System.out.println(address);
			
			DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
			
			DatagramSocket dsocket = new DatagramSocket();
			dsocket.send(packet);
			dsocket.close();
			
		} catch (Exception e){
			System.err.println(e);
		}
		
		
	}

}
