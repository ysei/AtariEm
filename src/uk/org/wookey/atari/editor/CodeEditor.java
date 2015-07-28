package uk.org.wookey.atari.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import uk.org.wookey.atari.utils.Logger;
import uk.org.wookey.atari.utils.assembler.Parser;
import uk.org.wookey.atari.utils.lexer.Lexer;
import uk.org.wookey.atari.utils.lexer.LexerToken;

public class CodeEditor extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger(CodeEditor.class.getName());

	private AssemblyCodeEditor editor;
	private EditorStatusBar statusBar;
	
	public CodeEditor() {
		super();
		
		setLayout(new BorderLayout());
		
		JPanel editorPanel = new JPanel();
		editorPanel.setLayout(new BorderLayout());
		
		statusBar = new EditorStatusBar();
		editor = new AssemblyCodeEditor();
		
		JScrollPane scroller = new JScrollPane(editor);
		scroller.setRowHeaderView(new TextLineNumber(editor));
		//scroller.setRowHeaderView(new AssemblerOutput(editor));

		editorPanel.add(scroller, BorderLayout.CENTER);
		
		JTextPane logPane = new JTextPane();
		logPane.setBackground(Color.BLACK);
		//new Logger(logPane);
		
		//Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
		                           editorPanel, new JScrollPane(logPane));
		
		splitPane.setOneTouchExpandable(true);
		
		JPanel buttonBar = new JPanel();
		JButton assembleButton = new JButton("Assemble");
		assembleButton.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent arg0) {
	        	doAssemble();
	          }
	        });
		
		buttonBar.add(assembleButton);

		add(splitPane, BorderLayout.CENTER);
		add(buttonBar, BorderLayout.PAGE_END);
		splitPane.setDividerLocation(500);
	}
	
	private void doAssemble() {
		Lexer lex = new Lexer();
		
		_logger.logInfo("Assembling...");
		List<LexerToken> tokens = lex.lex(editor.getText());
				
		/*for (LexerToken tok: tokens) {
			_logger.logInfo(tok.toString());
		}*/
		
		Parser p = new Parser(tokens);
		p.pass();
		p.pass();
		p.dumpLabels();
		
		_logger.logInfo("Errors: " + p.errors());
	}
}
