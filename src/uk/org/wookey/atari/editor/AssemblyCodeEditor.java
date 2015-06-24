package uk.org.wookey.atari.editor;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
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
		".org", ".byt", ".word", ".proc", ".endproc"
	}; 

	protected SimpleAttributeSet labelAttributes;
	
	public AssemblyCodeEditor() {
		super();
		
		myName = "Code";		
		
		wordSeperators = " ;.+-*/=!\n";
		quoteCharacters = "\"'";
		reservedWords.add(instructions);
		
		labelAttributes = new SimpleAttributeSet();
		StyleConstants.setForeground(labelAttributes, new Color(0xdf, 0x01, 0x3a));
		StyleConstants.setBold(labelAttributes, true);
		
	    InputMap im = getInputMap();
	    KeyStroke tab = KeyStroke.getKeyStroke("TAB");
	    
	    TextHandler textHandler = new TextHandler(this);
	    
	    getActionMap().put(im.get(tab), textHandler);
	    
	    this.addKeyListener(textHandler);
	}
	
	private class TextHandler extends AbstractAction implements KeyListener {
		private static final long serialVersionUID = 1L;
		private AssemblyCodeEditor editor;
		
		public TextHandler(AssemblyCodeEditor editor) {
			super();
			
			this.editor = editor;
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			char c = e.getKeyChar();
			
			_logger.logInfo("Pressed '" + c + "'");
			
			int keyCode = e.getKeyCode();
			_logger.logInfo(KeyEvent.getKeyText(keyCode));
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
		}
	}
}
