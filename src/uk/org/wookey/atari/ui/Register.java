package uk.org.wookey.atari.ui;

import javax.swing.JPanel;

public class Register extends JPanel {
	int readReg;
	int writeReg;
	boolean split;
	
	public Register (boolean split) {
		super();
		
		this.split = split; 
		
		readReg = writeReg = 0;
	}
	
	public void setReadReg(int reg) {
		if (split) {
			readReg = reg;
		}
		else {
			writeReg = reg;
		}
	}
	
	public void setWriteReg(int reg) {
		writeReg = reg;
	}
}
