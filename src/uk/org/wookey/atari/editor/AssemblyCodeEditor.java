package uk.org.wookey.atari.editor;

import java.io.BufferedReader;
import java.io.FileReader;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
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
		".org", "org", ".byt", "byt", ".byte", "byte", ".asc", "asc", ".db", "db", 
		".word", "word", ".dw", "dw", ".proc", "proc", ".scope", "scope", 
		".endproc", "endproc", ".endscope", "endscope", "include", "include", "#include"
	}; 
	
	public AssemblyCodeEditor(EditorStatusBar sb) {
		super();
		
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/6502", "uk.org.wookey.atari.editor.MOS6502TokenMaker");
		
		setSyntaxEditingStyle("text/6502");
		
		openFile("testprog.asm");
	}
	
	public void openFile(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("code/" + filename));
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
			
			while(line != null) {
				sb.append(line + "\n");
				line = reader.readLine();
			}
			
			// Put the text into the RSyntaxTextArea
			setText(sb.toString());
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
}
