package uk.org.wookey.atari.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class CoinDoor extends JPanel {
	public CoinDoor() {
		super();
		
		Dimension d = new Dimension(120, 120);
		
		setSize(d);
		setPreferredSize(d);
		
		setBackground(Color.BLACK);
	}
}
