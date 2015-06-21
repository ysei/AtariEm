package uk.org.wookey.atari.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RegisterPanel extends JPanel {
	private int width;
	private boolean lsbFirst;
	private ArrayList<Bit> bits;
	private JLabel regName;
	private int val;
	
	public RegisterPanel(int width) {
		this("", width);
	}
	
	public RegisterPanel(String registerName, int width) {
		this(registerName, width, false);
	}

	public RegisterPanel(String registerName, int width, boolean lsbFirst) {
		this.width = width;
		this.lsbFirst = lsbFirst;

		setLayout(new GridBagLayout());

		regName = new JLabel(registerName);
		Dimension ms = regName.getMinimumSize();
		ms.width = 100;
		regName.setMinimumSize(ms);
		regName.setPreferredSize(ms);
		regName.setMaximumSize(ms);
		
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
		
		add(regName);
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
