package uk.org.wookey.atari.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
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

	public AssemblyCodeEditor(EditorStatusBar sb) {
		super(sb);
		
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
		
	}
}
