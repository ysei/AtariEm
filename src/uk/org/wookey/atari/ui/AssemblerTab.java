package uk.org.wookey.atari.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import uk.org.wookey.atari.editor.CodeEditor;

public class AssemblerTab extends JPanel {
	private static final long serialVersionUID = 1L;

	public AssemblerTab() {
		super();
		
		setBackground(new Color(200, 100, 50));
		setLayout(new BorderLayout());
		
		CodeEditor textArea = new CodeEditor();
		add(textArea, BorderLayout.CENTER);
	}
}