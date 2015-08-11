package uk.org.wookey.atari.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import uk.org.wookey.atari.utils.Logger;

public class MainMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	private static final String FILE_TEXT = "File";
	public static final String EXIT_TEXT = "Exit";
	
	private static final String TOOLS_TEXT = "Tools";
	public static final String EDITOR_TEXT = "Code Editor";
	public static final String SETTINGS_TEXT = "Settings";
	public static final String LOGSESSION_TEXT = "Log Session";
	
	private static final String HELP_TEXT = "Help";
	public static final String ABOUT_TEXT = "About";
	
	private Logger _logger = new Logger("MainMenu");

	public MainMenuBar(ActionListener app) {
		JMenu fileMenu = new JMenu(FILE_TEXT);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem exitItem = new JMenuItem(EXIT_TEXT);
		exitItem.setMnemonic(KeyEvent.VK_X);
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
		exitItem.addActionListener(app);
		fileMenu.add(exitItem);
		add(fileMenu);
				
		JMenu toolMenu = new JMenu(TOOLS_TEXT);
		
		JMenuItem editorItem = new JMenuItem(EDITOR_TEXT);
		editorItem.setMnemonic(KeyEvent.VK_E);
		editorItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
		editorItem.addActionListener(app);
		toolMenu.add(editorItem);
		
		JMenuItem settingsItem = new JMenuItem(SETTINGS_TEXT);
		settingsItem.setMnemonic(KeyEvent.VK_S);
		settingsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		settingsItem.addActionListener(app);
		toolMenu.add(settingsItem);
		
		JCheckBoxMenuItem logSession = new JCheckBoxMenuItem(LOGSESSION_TEXT);
		logSession.setSelected(false);
		logSession.addActionListener(app);
		toolMenu.add(logSession);

		add(toolMenu);
		
		JMenu helpMenu = new JMenu(HELP_TEXT);
		helpMenu.setMnemonic(KeyEvent.VK_H);

		JMenuItem aboutItem = new JMenuItem(ABOUT_TEXT);
		aboutItem.addActionListener(app);
		helpMenu.add(aboutItem);
		
		add(helpMenu);
	}
}