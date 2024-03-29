package uk.org.wookey.atari.ui;

import java.awt.*;

import javax.swing.*;

import uk.org.wookey.atari.utils.Logger;

public class DebugTab extends JPanel {
	private static final long serialVersionUID = 6325314968517017778L;
	private static JTextPane _log;

	public DebugTab() {
		super();
		
		setLayout(new BorderLayout());
		
		_log = new JTextPane();
		_log.setEditable(false);
		_log.setBackground(new Color(0, 0, 0));
		_log.setFont(new Font("Courier", Font.PLAIN, 12));

		JScrollPane scroller = new JScrollPane(_log);
		add(scroller, BorderLayout.CENTER);
		new Logger(_log);
	}
}