package uk.org.wookey.atari.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class RegisterPanel extends JPanel {
	private int width;
	private boolean lsbFirst;
	private ArrayList<Bit> bits;
	private int val;
	
	public RegisterPanel(int width) {
		this.width = width;
		lsbFirst = false;
		
		setLayout(new GridBagLayout());

		createBits();
	}

	public RegisterPanel(int width, boolean lsbFirst) {
		this.width = width;
		this.lsbFirst = lsbFirst;

		createBits();
		set(0);
	}
	
	private void createBits() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridy = 0;
		gbc.gridx = 1;
		
		bits = new ArrayList<Bit>();
		for (int i=0; i<width; i++) {
			bits.add(new Bit());
		}
		
		if (lsbFirst) {
			for (int i=0; i<width; i++) {
				add(bits.get(i), gbc);
				gbc.gridx++;
			}
		}
		else {
			for (int i=width-1; i>=0; i--) {
				add(bits.get(i), gbc);
				gbc.gridx++;
			}
		}
	}
	
	public void set(int val) {
		this.val = val;
		
		for (int i=0; i<width; i++) {
			bits.get(i).setVal(val >> i);
		}
	}
}
