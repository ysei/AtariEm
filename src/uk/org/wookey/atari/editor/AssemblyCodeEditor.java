package uk.org.wookey.atari.editor;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import uk.org.wookey.atari.utils.Logger;

public class AssemblyCodeEditor extends RSyntaxTextArea {
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
		"org", "byt", "byte", "asc", "db", 
		"word", "dw", "include"
	}; 
	
	public AssemblyCodeEditor(EditorStatusBar sb) {
		super();
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		
		atmf.putMapping("text/as65", "uk.org.wookey.atari.editor.AssemblerTokenMaker");
		
		setSyntaxEditingStyle("text/as65");
	}
}
