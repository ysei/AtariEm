package uk.org.wookey.atari.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import uk.org.wookey.atari.utils.Logger;

public class DebugWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger(DebugWindow.class.getName());
	protected JTabbedPane tabs;
	
	public DebugWindow() {
		super("Debug");
		
		setLayout(new BorderLayout());
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		add(new DebugTab(), BorderLayout.CENTER);
		
		setVisible(true);
	}	
}
