package uk.org.wookey.atari.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class VectorMonitor extends JPanel {
	public VectorMonitor() {
		setDoubleBuffered(true);
		
		setSize(new Dimension(250, 200));
	}
}
