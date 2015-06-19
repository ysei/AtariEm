package uk.org.wookey.atari.ui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Bit extends JLabel {
	int val;
	
	public Bit() {
		super();
		val = 0;
		
		setVal(val);
		setBorder(BorderFactory.createLineBorder(Color.RED));
		this.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public void setVal(int newVal) {
		val = newVal & 1;
		
		this.setText("" + val);
	}
}
