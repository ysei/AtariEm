package uk.org.wookey.atari.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainStatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	private static MainStatusBar _sb = new MainStatusBar();
	
	private JLabel timerLabel;

	public MainStatusBar() {
		super();
		
		this.setBackground(new Color(0xd0, 0xd0, 0xd0));
		
		timerLabel = new JLabel("X");
		timerLabel.setMinimumSize(new Dimension(170, timerLabel.getMinimumSize().height));
		add(timerLabel);
		
		setTimerMessage(0);
	}
	
	public void addSpacer() {
		ImagePanel spacer = new ImagePanel(new File("images/h-spacer.png"));
		spacer.setOpaque(false);
		add(spacer);
	}
	
	public void setTimerMessage(int queueLength) {
		timerLabel.setText("Timer Queue: " + queueLength);
	}

	public static MainStatusBar getMainStatusBar() {
		return _sb;
	}
}
