package uk.org.wookey.atari.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import com.loomcom.symon.Bus;
import com.loomcom.symon.Cpu;
import com.loomcom.symon.devices.Acia;
import com.loomcom.symon.devices.Acia6850;
import com.loomcom.symon.devices.Via6522;
import com.loomcom.symon.exceptions.MemoryAccessException;
import com.loomcom.symon.exceptions.MemoryRangeException;
import com.loomcom.symon.machines.Machine;
import com.loomcom.symon.machines.SymonMachine;

import uk.org.wookey.atari.sim.Simulator;
import uk.org.wookey.atari.utils.Logger;

public class ApplicationWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger("ApplicationWindow");
	protected JTabbedPane tabs;
	private MainStatusBar statusBar;
	
	public ApplicationWindow() {
		super("IC");
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		
		gbc.insets = new Insets(2, 2, 2, 2);
		
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.anchor = GridBagConstraints.PAGE_START;
		
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

		setLayout(new GridBagLayout());
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new MainWindowListener());
		
		MainMenuBar menu = new MainMenuBar();
		setJMenuBar(menu);
		
		tabs = new JTabbedPane();
		
        tabs.addMouseListener(new PopupListener());
        
		tabs.add("Console", new DebugTab());
		tabs.add("Simulator", new Simulator(new SymonMachine()));
		tabs.addTab("Assembler",  new AssemblerTab());
		
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		add(tabs, gbc);
		
		statusBar = MainStatusBar.getMainStatusBar(); 
		JPanel outer = new JPanel();
		outer.setLayout(new BorderLayout());
		outer.add(statusBar, BorderLayout.EAST);
		outer.setBackground(new Color(0xd0, 0xd0, 0xd0));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		//gbc.gridwidth = 3;
		add(outer, gbc);
		
		setVisible(true);
	}	
	public void addTab(JPanel tab) {
		String title;
		
		title = tab.getName();
		tabs.add(title, tab);
		tabs.setSelectedComponent(tab);
	}
	
	class FocusHandler implements FocusListener {
		@Override
		public void focusGained(FocusEvent fev) {
			Component c = fev.getComponent();
			c.setBackground(c.getBackground().darker());
		}

		@Override
		public void focusLost(FocusEvent fev) {
			Component c = fev.getComponent();
			c.setBackground(c.getBackground().brighter());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		_logger.logInfo("Got a close tab click");
		Component x = (Component) event.getSource();
		
		x = x.getParent();
		_logger.logInfo("Not sure what to do with this type of tab: " + x.toString());
	}
	
	public JTabbedPane getTabbedPane() {
		return tabs;
	}
	
	private class MainWindowListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			_logger.logInfo("Main window closed");
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			_logger.logInfo("Main window closing");
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
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

			popup.show(tabs.getSelectedComponent(), x, y-20);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			
			_logger.logInfo("Popup menu click: '" + cmd + "'");			
		}
	}
}
