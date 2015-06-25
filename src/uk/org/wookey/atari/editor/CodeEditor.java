package uk.org.wookey.atari.editor;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class CodeEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	private AssemblyCodeEditor editor;
	private EditorStatusBar statusBar;
	
	public CodeEditor() {
		super();
		
		setLayout(new BorderLayout());
		
		statusBar = new EditorStatusBar();
		editor = new AssemblyCodeEditor(statusBar);
		
		JScrollPane scroller = new JScrollPane(editor);
		scroller.setRowHeaderView(new TextLineNumber(editor));

		add(statusBar, BorderLayout.SOUTH);
		add(scroller, BorderLayout.CENTER);
	}
}
