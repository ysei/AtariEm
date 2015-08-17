package uk.org.wookey.atari.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import uk.org.wookey.atari.machines.AsteroidsDeluxeMachine;
import uk.org.wookey.atari.machines.Machine;
import uk.org.wookey.atari.sim.Simulator;
import uk.org.wookey.atari.utils.Logger;

public class ApplicationWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger("ApplicationWindow");
	protected Simulator sim;
	protected Machine machine;
	private MainStatusBar statusBar; 
	private StatusPanel statusPane;
	
	public ApplicationWindow() {
		super("Atari Emulator");
		
		machine = new AsteroidsDeluxeMachine();
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = tk.getScreenSize().width;
		int ySize = tk.getScreenSize().height;
		
		if (xSize > 1360) {
			xSize = 1360;
		}
		
		if (ySize > 768) {
			ySize = 768;
		}
		
		setSize(xSize, ySize);
		setLocation((tk.getScreenSize().width - xSize) / 2, (tk.getScreenSize().height - ySize) / 2);

		setLayout(new BorderLayout());
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		MenuBar menu = new MenuBar(this);
		setJMenuBar(menu);
		
        addMouseListener(new PopupListener());
        
        statusPane = new StatusPanel(machine);
        statusPane.updateState();

		sim = new Simulator(machine, statusPane);
		add(sim, BorderLayout.CENTER);
		
		add(new Cabinet(), BorderLayout.LINE_START);
		add(statusPane, BorderLayout.LINE_END);
		
		statusBar = MainStatusBar.getMainStatusBar(); 
		
		add(statusBar, BorderLayout.PAGE_END);
		pack();
		setVisible(true);
		
		sim.getConsole().requestFocusInWindow();
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		_logger.logInfo("Main menu click: '" + cmd + "'");
		
		if (cmd.equalsIgnoreCase(MenuBar.EXIT_TEXT)) {
			System.exit(0);
		}
		else if (cmd.equalsIgnoreCase(MenuBar.EDITOR_TEXT)) {
			 SwingUtilities.invokeLater(new Runnable() {
				 @Override
				 public void run() {
					 @SuppressWarnings("unused")
					CodeEditorWindow win = new CodeEditorWindow(machine);
				 }
			 });

		}
	}
	
	private class PopupListener extends MouseAdapter implements ActionListener {
		private static final String SETTINGS_TEXT = "Settings";
		private static final String MACROS_TEXT = "Macros";
		private static final String KEYMAP_TEXT = "Key Map";

		public void mouseClicked(MouseEvent e) {			
			int button = e.getButton();
			
			if (button == MouseEvent.BUTTON3) {
				//_logger.logInfo("Button3 - do popup");
				handlePopup(e.getX(), e.getY());
			}
		}
		
		private void handlePopup(int x, int y) {
			_logger.logInfo("WorldTab popup");
				
			JPopupMenu popup = new JPopupMenu();
				
			JMenuItem keys = new JMenuItem(KEYMAP_TEXT);
			keys.addActionListener(this);
			popup.add(keys);
				
			JMenuItem macros = new JMenuItem(MACROS_TEXT);
			macros.addActionListener(this);
			popup.add(macros);
				
			JMenuItem settings = new JMenuItem(SETTINGS_TEXT);
			settings.addActionListener(this);
			popup.add(settings);

			popup.show(sim, x, y-20);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			
			_logger.logInfo("Popup menu click: '" + cmd + "'");			
		}
	}
	
	private class MenuBar extends JMenuBar {
		private static final long serialVersionUID = 1L;
		private static final String FILE_TEXT = "File";
		public static final String EXIT_TEXT = "Exit";
		
		private static final String TOOLS_TEXT = "Tools";
		public static final String EDITOR_TEXT = "Code Editor";
		public static final String SETTINGS_TEXT = "Settings";
		public static final String LOGSESSION_TEXT = "Log Session";
		
		private static final String HELP_TEXT = "Help";
		public static final String ABOUT_TEXT = "About";
		
		public MenuBar(ActionListener app) {
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
}
