package uk.org.wookey.atari.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import uk.org.wookey.atari.utils.Logger;

public class AssemblyCodeEditor extends GenericEditor {
	private static final long serialVersionUID = 1L;
	private final static Logger _logger = new Logger(AssemblyCodeEditor.class.getName());
	
	private final static int COMMENT = 1;
	private final static int LABEL = 2;
	private final static int TEXT = 3;
	private final static int INSTRUCTION = 4;
	private final static int DIRECTIVE = 5;

	private final static String instructions[] = {
		"bcc", "bcs", "beq", "bmi", "bne", "bpl", "bvc", "bvs",
		"adc", "and", "asl", "bit", "brk", "clc", "cld", "cli",
		"clv", "cmp", "cpx", "cpy", "dec", "dex", "dey", "eor",
		"inc", "inx", "iny", "jmp", "jsr", "lda", "ldx", "ldy",
		"lsr", "nop", "ora", "pha", "php", "pla", "plp", "rol",
		"ror", "rti", "rts", "sbc", "sec", "sed", "sei", "sta", 
		"stx", "sty", "tax", "tay", "tsx", "txa", "txs", "tya"
	};
	
	private final static String directives[] = {
		".org", "org", ".byt", "byt", ".byte", "byte", ".asc", "asc", ".db", "db", 
		".word", "word", ".dw", "dw", ".proc", "proc", ".scope", "scope", 
		".endproc", "endproc", ".endscope", "endscope", "include", "include", "#include"
	}; 
	
	private final static int TAB_COLUMN = 24;

	protected SimpleAttributeSet labelAttributes;
	protected SimpleAttributeSet commentAttributes;
	protected SimpleAttributeSet normalAttributes;
	protected SimpleAttributeSet reservedWordAttributes;
	protected SimpleAttributeSet seperatorAttributes;
	protected SimpleAttributeSet stringAttributes;
	protected ReservedWordList reservedWords;
	protected String wordSeperators;
	protected String quoteCharacters;
	protected String myName;

	private boolean startOfLine;
	private boolean inComment;
	
	private int lineNumber;
	
	EditorStatusBar statusBar;
	
	public AssemblyCodeEditor(EditorStatusBar sb) {
		super();
		
		statusBar = sb;
		lineNumber = 1;
		
		myName = "Code";		
		
		wordSeperators = " ;.+-*/=!\n";
		quoteCharacters = "\"'";
		
		normalAttributes = new SimpleAttributeSet();
		StyleConstants.setForeground(normalAttributes, Color.black);
		
		reservedWordAttributes = new SimpleAttributeSet();
		StyleConstants.setForeground(reservedWordAttributes, new Color(0x73, 0x04, 0x73));
		StyleConstants.setBold(reservedWordAttributes, true);
		
		seperatorAttributes = new SimpleAttributeSet();
		StyleConstants.setForeground(seperatorAttributes, Color.red);
		
		stringAttributes = new SimpleAttributeSet();
		StyleConstants.setForeground(stringAttributes, Color.blue);
		StyleConstants.setItalic(stringAttributes, true);
		
		labelAttributes = new SimpleAttributeSet();
		StyleConstants.setForeground(labelAttributes, new Color(0xdf, 0x01, 0x3a));
		StyleConstants.setBold(labelAttributes, true);
		
		commentAttributes = new SimpleAttributeSet();
		StyleConstants.setForeground(commentAttributes, new Color(0xff, 0x00, 0x00));
		StyleConstants.setItalic(commentAttributes, true);
		
		reservedWords = new ReservedWordList(reservedWordAttributes);

		wordSeperators = " \n";
		quoteCharacters = "";

		setBackground(new Color(0xe0, 0xff, 0xf0));
		setFont(new Font("Courier", Font.PLAIN, 12));
		
	    InputMap im = getInputMap();
	    KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
	    KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
	    
	    TextHandler textHandler = new TextHandler(this);
	    
	    ActionMap aMap = getActionMap();
	    aMap.put(im.get(enter), new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				GenericEditor editor = (GenericEditor) e.getSource();
				
				_logger.logInfo("Action (Enter);");
				
				insertText("\n", editor.getCaretPosition(), normalAttributes);
				statusBar.debugMessage("LABEL");
				try {
					statusBar.setLineNum(editor.getLineOfOffset(editor.getCaretPosition()));
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
	    });
	    
	    aMap.put(im.get(tab), textHandler);
	    
	    addKeyListener(textHandler);
	    getDocument().addDocumentListener(textHandler);
	    
	    startOfLine = true;
	    inComment = false;
	    
	    statusBar.setLineNum(lineNumber);
	    this.setText("");
	}

	private void appendText(String s) {
		appendText(s, normalAttributes);
	}
	
	private void appendText(String s, AttributeSet attributes) {
		Document doc = getDocument();
		
		insertText(s, doc.getLength(), attributes);
	}
	
	private void insertText(String s, int pos, AttributeSet attributes) {
		Document doc = getDocument();
		
		try {
			doc.insertString(pos, s, attributes);
			setCaretPosition(pos+s.length());		
		} catch (BadLocationException e) {
			_logger.logError("Failed to insert into code doc", e);
		}		
	}
	
	private class TextHandler extends AbstractAction implements KeyListener, DocumentListener, ActionListener {
		private static final long serialVersionUID = 1L;
		
		public TextHandler(AssemblyCodeEditor editor) {
			super();
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			_logger.logInfo("Release");
		}

		@Override
		public void keyPressed(KeyEvent e) {
			_logger.logInfo("Press");
		}

		@Override
		public void keyTyped(KeyEvent e) {
			AssemblyCodeEditor editor = (AssemblyCodeEditor) e.getSource();
			char c = e.getKeyChar();
						
			_logger.logInfo("Typed");

			if (c == ';') {
				if (!inComment) {
					inComment = true;
					
					editor.insertText(";", editor.getCaretPosition(), commentAttributes);
					e.consume();
				}
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			_logger.logInfo("Action");
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			_logger.logInfo("Changed");
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			_logger.logInfo("Inserted");
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			_logger.logInfo("Removed");
		}
	}
}
