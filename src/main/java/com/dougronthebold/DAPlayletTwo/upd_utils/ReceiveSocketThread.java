package main.java.com.dougronthebold.DAPlayletTwo.upd_utils;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import UDPUtils.OSCAtom;
import UDPUtils.OSCUtils;



public class ReceiveSocketThread implements Runnable{
	
	private CopyOnWriteArrayList<ArrayList<OSCAtom>> oscReceiveBuffer;
	private Thread portThread;

	public ReceiveSocketThread(CopyOnWriteArrayList<ArrayList<OSCAtom>> atList){
		this.oscReceiveBuffer = atList;
	}
	
	
// Runnable methods-------------------------------------------
	@Override
	public void run(){
		try {
			int port = 7802;
			
			DatagramSocket dsocket = new DatagramSocket(port);
			
			byte[] buffer = new byte[2048];
			
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			while (true){
				dsocket.receive(packet);
				String msg = new String(buffer, 0, packet.getLength());

				//System.out.println(packet.getAddress().getHostName() + ": " + msg);
				ArrayList<OSCAtom> atList = OSCUtils.makeAtomList(buffer);
				oscReceiveBuffer.add(atList);
				//System.out.println(makeStringFromAtomList(atList));
				packet.setLength(buffer.length);
			}
			
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	private String makeStringFromAtomList(ArrayList<OSCAtom> atList){
		String ret = "";
		for (OSCAtom atom: atList){
			ret = ret + atom.itemAsString() + ", ";
		}
		return ret;
	}
	
}
