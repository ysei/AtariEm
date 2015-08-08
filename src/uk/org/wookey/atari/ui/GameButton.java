package uk.org.wookey.atari.ui;

import java.awt.Color;
import java.awt.Dimension;

public class GameButton extends PushButton {
	public GameButton() {
		super();
		
		Dimension d = new Dimension(16, 16);
		
		setSize(d);
		setPreferredSize(d);
		
		setBackground(new Color(32, 32, 128));
	}
}
