package main.java.com.dougronthebold.DAPlayletTwo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import com.cycling74.max.Atom;


public class DAPlayletTwo extends JFrame implements SurfaceParent{
	
	private JLabel statusLine;
	private SurfaceGame ui;
	private int xsize = 500;
	private int ysize = 825;

	
	public DAPlayletTwo(){
		initUI();
		uiTest1();
	}
	public void statusMessage(String str){
		statusLine.setText(str);
	}
// SurfaceParent methods ---------------------------------------------------------
	public int xSize(){
		return (int)this.getContentPane().getSize().getWidth();
	}
	public int ySize(){
		return (int)this.getContentPane().getSize().getHeight();
	}
//	public int xSize(){
//		return getSize().width;
//	}
//	public int ySize(){
//		return getSize().height;
//	}
//	public Dimension size(){
//		return getSize();
//	}
// ChordProgrammer2Parent methods -------------------------------------------------------------
//	public void sendChordProgrammerMessage(Atom[] atArr){
		
//	}
	public void consolePrint(String str){
		
	}
	public void statusPrint(String str){
		statusLine.setText(str);
	}
	
// privates ===========================================================================
	private void uiTest1(){
		// bassmaker plus two chord progressions
//		ui.addNewPlayletObject(new PlayletObject(PlayletObject.ROUND_RECTANGLE, makeProgression1(), 100, 100, 35, new Color(127, 0, 0), "CP_1"));
//		ui.addNewPlayletObject(new PlayletObject(PlayletObject.ROUND_RECTANGLE, makeProgression2(), 300, 100, 35, new Color(127, 0, 0), "CP_2"));

	}
	private void initUI(){				
		setTitle("PlayletSurfaceTest");
		setSize(xsize, ysize);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		statusLine = addStatusBar();
		ui = new SurfaceGame(this);
		add(ui);
		//statusMessage("urg...");
	}
	private JLabel addStatusBar(){
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		return statusLabel;
	}
	

// runnable =-=-=-=--=-====--------------------------------------------------
	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	DAPlayletTwo ex = new DAPlayletTwo();
                ex.setVisible(true);
            }
        });
    }
}
