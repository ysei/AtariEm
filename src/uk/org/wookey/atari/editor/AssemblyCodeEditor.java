package uk.org.wookey.atari.editor;

public class AssemblyCodeEditor extends GenericEditor {
	public AssemblyCodeEditor() {
		super();
		
		myName = "Code";
		
		String instructions[] = {"bcc", "bcs", "beq", "bmi", "bne", "bpl", "bvc", "bvs",
									"adc", "and", "asl", "bit", "brk", "clc", "cld", "cli",
									"clv", "cmp", "cpx", "cpy", "dec", "dex", "dey", "eor",
									"inc", "inx", "iny", "jmp", "jsr", "lda", "ldx", "ldy",
									"lsr", "nop", "ora", "pha", "php", "pla", "plp", "rol",
									"ror", "rti", "rts", "sbc", "sec", "sed", "sei", "sta", 
									"stx", "sty", "tax", "tay", "tsx", "txa", "txs", "tya"
		};

		String words[] = {".org", ".byt"};
		
		wordSeperators = " ;.+-*/=!\n";
		quoteCharacters = "\"'";
		reservedWords.add(instructions);
	}
}
