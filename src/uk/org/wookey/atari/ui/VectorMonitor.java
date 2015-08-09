package uk.org.wookey.atari.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class VectorMonitor extends JPanel {
	public VectorMonitor() {
		setDoubleBuffered(true);
		
		Dimension d = new Dimension(250, 150);
		
		setSize(d);
		setPreferredSize(d);

		setBackground(Color.YELLOW);
		
		setBorder(new LineBorder(Color.BLACK, 4, true));
	}
}
