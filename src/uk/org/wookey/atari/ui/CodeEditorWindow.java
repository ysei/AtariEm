package uk.org.wookey.atari.ui;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.org.wookey.atari.assembler.Assembler;
import uk.org.wookey.atari.editor.AssemblyCodeEditor;
import uk.org.wookey.atari.editor.TextLineNumber;
import uk.org.wookey.atari.machines.Machine;
import uk.org.wookey.atari.utils.Logger;

public class CodeEditorWindow extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger("CodeEditorWindow");

	private AssemblyCodeEditor editor;
	private File currentFile;
	
	private Machine machine;
	
	public CodeEditorWindow(Machine m) {
		super("Code Editor");
		
		machine = m;
		currentFile = null;
		
		setSize(400, 300);
		setLayout(new BorderLayout());
		
		setJMenuBar(new MenuBar(this));		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
		editor = new AssemblyCodeEditor();
		
		JScrollPane scroller = new JScrollPane(editor);
		scroller.setRowHeaderView(new TextLineNumber(editor));
		add(scroller, BorderLayout.CENTER);

		JPanel buttonBar = new JPanel();
		JButton assembleButton = new JButton("Assemble");
		assembleButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	        	doAssemble();
	          }
	        });
		
		buttonBar.add(assembleButton);
		add(buttonBar, BorderLayout.PAGE_END);

		setVisible(true);
	}
	
	private void doAssemble() {
		Assembler asm = new Assembler(machine, editor.getText());	
	}
	
	private void openFile() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		    "Assembler source files", "a65", "asm", "inc");
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(new File("./code"));
		
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		   System.out.println("You chose to open this file: " +
		        chooser.getSelectedFile().getName());
		   
		   try {
			editor.openFile(chooser.getSelectedFile());
			currentFile = chooser.getSelectedFile();
		   } catch (IOException e) {
			   e.printStackTrace();
			   System.out.println("Failed to open file: " + chooser.getSelectedFile().getName());
		   }
		}
	}
	
	private void saveFile() {
		if (currentFile == null) {
			saveFileAs();
		}
		else {
			try {
				_logger.logInfo("Save file: " + currentFile.getCanonicalPath());
				FileWriter out = new FileWriter(currentFile.getCanonicalPath());
				editor.write(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void saveFileAs() {
		_logger.logError("Need to implement 'SaveAs'");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		_logger.logInfo("Main menu click: '" + cmd + "'");
		
		if (cmd.equalsIgnoreCase(MenuBar.EXIT_TEXT)) {
			dispose();
		}
		else if (cmd.equalsIgnoreCase(MenuBar.OPEN_FILE_TEXT)) {
			openFile();
		}
		else if (cmd.equalsIgnoreCase(MenuBar.SAVE_FILE_TEXT)) {
			saveFile();
		}
	}

	private class MenuBar extends JMenuBar {
		private static final String FILE_TEXT = "File";
		public static final String OPEN_FILE_TEXT = "Open";
		public static final String SAVE_FILE_TEXT = "Save";
		public static final String SAVE_FILE_AS_TEXT = "Save As";
		public static final String EXIT_TEXT = "Exit";
		
		public MenuBar(ActionListener app) {
			JMenu fileMenu = new JMenu(FILE_TEXT);
			fileMenu.setMnemonic(KeyEvent.VK_F);
			
			JMenuItem item = new JMenuItem(EXIT_TEXT);
			item.setMnemonic(KeyEvent.VK_X);
			item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
			item.addActionListener(app);
			fileMenu.add(item);

			item = new JMenuItem(OPEN_FILE_TEXT);
			item.setMnemonic(KeyEvent.VK_O);
			item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
			item.addActionListener(app);
			fileMenu.add(item);

			item = new JMenuItem(SAVE_FILE_TEXT);
			item.setMnemonic(KeyEvent.VK_S);
			item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
			item.addActionListener(app);
			fileMenu.add(item);

			item = new JMenuItem(SAVE_FILE_AS_TEXT);
			item.addActionListener(app);
			fileMenu.add(item);

			add(fileMenu);
		}
	}
}
