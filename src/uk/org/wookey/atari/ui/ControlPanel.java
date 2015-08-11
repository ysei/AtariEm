package uk.org.wookey.atari.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	public ControlPanel() {
		setSize(new Dimension(250, 80));
		
		add(new GameButton());
		add(new GameButton());
		add(new GameButton());
		add(new GameButton());
		add(new GameButton());
	}
}
