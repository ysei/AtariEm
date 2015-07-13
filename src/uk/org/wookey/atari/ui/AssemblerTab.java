package uk.org.wookey.atari.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.gmail.ksherl.asm816.Parser;

import uk.org.wookey.assembler.WookeyParser;
import uk.org.wookey.atari.editor.CodeEditor;

public class AssemblerTab extends JPanel {
	private static final long serialVersionUID = 1L;

	private CodeEditor editor;
	private JButton assembleButton;
	
	public AssemblerTab() {
		super();
		
		setBackground(new Color(200, 100, 50));
		setLayout(new BorderLayout());
		
		editor = new CodeEditor();
		add(editor, BorderLayout.CENTER);
		
		JPanel buttonContainer = new JPanel();
		assembleButton = new JButton("Assemble");
		
		buttonContainer.add(assembleButton);
		
		assembleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
        		Parser p = new WookeyParser();
        		
        		p.SetOutFile("zz.o");
        		p.ParseFile(editor.toInputStream());
            }
        });
		
		add(buttonContainer, BorderLayout.PAGE_END);
	}
}