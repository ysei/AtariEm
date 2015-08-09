package uk.org.wookey.atari.ui;

import java.awt.Color;
import java.awt.Dimension;

public class PlayerSelectButton extends PushButton {
	private static final long serialVersionUID = 1L;

	public PlayerSelectButton() {
		super();
		
		Dimension d = new Dimension(10, 10);
		
		setSize(d);
		setPreferredSize(d);
		
		setBackground(new Color(32, 32, 128));
	}
}
