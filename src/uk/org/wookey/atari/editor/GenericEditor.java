package uk.org.wookey.atari.editor;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import uk.org.wookey.atari.utils.Logger;

public class GenericEditor extends JTextPane {
	private static final long serialVersionUID = 1L;
	private static Logger _logger = new Logger(GenericEditor.class.getName());

	public GenericEditor() {
		super();

		setBackground(new Color(0xe0, 0xff, 0xf0));
		setFont(new Font("Courier", Font.PLAIN, 12));
	}
	
	public int getLineOfOffset(int offset) throws BadLocationException {
		// TODO: Write some code
		return 0;
	}
	
	public int getLineEndOffset(int line) throws BadLocationException {
		// TODO: Write some code
		return 0;
	}
	
	public int getLineStartOffset(int line) throws BadLocationException {
		// TODO: Write some code
		return 0;
	}
	
	public int getLineCount() {
		// TODO: Write some code
		return 0;
	}
}