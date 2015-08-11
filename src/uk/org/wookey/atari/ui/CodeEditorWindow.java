package uk.org.wookey.atari.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import uk.org.wookey.atari.editor.CodeEditor;
import uk.org.wookey.atari.machines.Machine;
import uk.org.wookey.atari.utils.Logger;

public class CodeEditorWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger("CodeEditorWindow");

	private Machine machine;
	
	public CodeEditorWindow(Machine m) {
		super("Code Editor");
		
		machine = m;
		
		setSize(400, 300);
		setLayout(new BorderLayout());
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		CodeEditor editor = new CodeEditor();
		
		add(editor, BorderLayout.CENTER);
		
		setVisible(true);
	}
}
