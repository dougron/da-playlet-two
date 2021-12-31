package test;


import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import UDPUtils.OSCAtom;
import UDPUtils.OSCUtils;
import acm.program.ConsoleProgram;

public class UDPReceiveTest extends ConsoleProgram{
	
	public double delay = 1000;

	public void run(){
		setSize(500, 1000);
		double originalTime = System.currentTimeMillis();
		int port = 7802;
		byte[] buffer = new byte[50];
		DatagramSocket dsocket;
		try {
			dsocket = new DatagramSocket(port);
			dsocket.setSoTimeout((int)delay);
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			while (true){
				if (System.currentTimeMillis() - originalTime > 20000){
					break;
				}
				try {
					double currentTime = System.currentTimeMillis();
					println("currentTime: " + (currentTime - originalTime));
//					while (System.currentTimeMillis() < currentTime + delay){				
//					}
//					dsocket.setSoTimeout((int)delay);
					dsocket.receive(packet);

					
					String msg = new String(buffer, 0, packet.getLength());
//					println(packet.getLength());
					println(packet.getAddress().getHostName() + ": " + msg);
//					for (byte b: buffer){
//						println(b + ", " + (char)b);
//					}
//					println("\n");
					ArrayList<OSCAtom> atList = OSCUtils.makeAtomList(buffer);
					for (OSCAtom at: atList){
						println(at.toString());
					}
					packet.setLength(buffer.length);
				} catch (InterruptedIOException iioe){
					println("InterruptedIOException");
				} catch (IOException ioe){
					println("IOException");
				} catch (Exception e){
					
				}
			}
		} catch (Exception e){
			
		}		
		
		
	}
	private void receivePacket(DatagramSocket dsocket, DatagramPacket packet, byte[] buffer){
		
	}
	private ArrayList<OSCAtom> makeAtomList(byte[] barr){
//		ArrayList<OSCAtom> atList = new ArrayList<OSCAtom>();
		ArrayList<Byte> typeList = new ArrayList<Byte>();
		boolean foundTypeList = false;
		int typeListEndIndex = 0;
		int nextItemIndex = 0;
		for (int i = 0; i < barr.length; i++){
			if (barr[i] == byte_comma){   		// looking for 1st comma, which indicates beginning of typelist
				foundTypeList = true;
			}
			if (foundTypeList){
				if (barr[i] == byte_s || barr[i] == byte_i || barr[i] == byte_f){
					typeList.add(barr[i]);
				} else if (barr[i] == 0){
					typeListEndIndex = i;
					break;
				}
			}
		}
		nextItemIndex = ((typeListEndIndex + 4) / 4) * 4;
		println("typeList----------------------------");
		for (byte b: typeList){
			println(b);
		}
		println("typeListEndIndex: " + typeListEndIndex);
		println("nextItemIndex: " + nextItemIndex);
		ArrayList<OSCAtom> atList = addAtomsToList(barr, nextItemIndex, typeList);
		 
		
		return atList;
	}
	private ArrayList<OSCAtom> addAtomsToList(byte[] barr, int startIndex, ArrayList<Byte> typeList){
		ArrayList<OSCAtom> atList = new ArrayList<OSCAtom>();
		byte[] tempByteArr;
		for (byte type: typeList){
			if (type == byte_i){
				tempByteArr = new byte[4];
				for (int i = 0; i < 4; i++){
					tempByteArr[i] = barr[startIndex + i];
				}
				atList.add(makeIntOSCAtom(tempByteArr));
				startIndex += 4;
			} else if (type == byte_f){
				tempByteArr = new byte[4];
				for (int i = 0; i < 4; i++){
					tempByteArr[i] = barr[startIndex + i];
				}
				atList.add(makeFloatOSCAtom(tempByteArr));
				startIndex += 4;
			} else if (type == byte_s){
				boolean loop = true;
				int tIndex = startIndex;
				while(loop){
					if (barr[tIndex] == 0){
						loop = false;
					} else {
						tIndex++;
					}
				}
				tempByteArr = new byte[tIndex - startIndex];
				for (int i = 0; i < tempByteArr.length; i++){
					tempByteArr[i] = barr[startIndex + i];
				}
				String decoded = "";
				try {
					decoded = new String(tempByteArr, "UTF-8");
				} catch (Exception e){
					
				}
				atList.add(new OSCAtom(decoded));
				startIndex = ((startIndex + tempByteArr.length + 4) / 4) * 4;
			}
		}
		
		return atList;
	}
	private OSCAtom makeFloatOSCAtom(byte[] tbar){
		println("makeFloat-------------");
		for (byte b: tbar){
			print(b + ",");
		}
		println("\n");
		float x = java.nio.ByteBuffer.wrap(tbar).getFloat();
		return new OSCAtom(x);
	}
	private OSCAtom makeIntOSCAtom(byte[] tbar){
		println("makeInt-------------");
		for (byte b: tbar){
			print(b + ",");
		}
		println("\n");
		int x = java.nio.ByteBuffer.wrap(tbar).getInt();
		
//		int value = ((tbar[3] & 0xFF) << 0) |
//				((tbar[2] & 0xFF) << 8) |
//				((tbar[1] & 0xFF) << 16) |
//				((tbar[0] & 0xFF) << 32);
		return new OSCAtom(x);
	}
	
	private static final int byte_f = 102;
	private static final int byte_i = 105;
	private static final int byte_s = 115;
	private static final int byte_comma = 44;

}
