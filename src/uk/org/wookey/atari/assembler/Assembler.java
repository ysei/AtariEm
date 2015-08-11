package uk.org.wookey.atari.assembler;

import java.util.List;

import uk.org.wookey.atari.editor.CodeEditor;
import uk.org.wookey.atari.utils.Logger;

public class Assembler {
	private final static Logger _logger = new Logger("Assembler");
	
	public Assembler(String sourceCode) {
		Lexer lex = new Lexer();
		
		_logger.logInfo("Assembling...");
		
		List<LexerToken> tokens = lex.lex(sourceCode);

		/*
		for (LexerToken tok: tokens) {
			_logger.logInfo(tok.toString());
		}
		*/
		
		Parser p = new Parser(tokens);
		
		boolean assembling = true;
		int passNum = 1;
		
		while (assembling) {
			p.pass(false);
			
			if (p.hardErrors() > 0) {
				_logger.logError("Giving up after pass " + passNum + " due to syntax errors.");
				assembling = false;
			}
			
			passNum++;
			if (passNum > 5 || p.errors() == 0) {
				assembling = false;
			}
		}
		
		if (p.errors() > 0) {
			_logger.logError("Errors prevent code generation.");
		}
		else {
			p.pass(true);
		}
		
		//p.dumpLabels();
		
		_logger.logInfo("Errors: " + p.errors() + ", hard: " + p.hardErrors() + ", soft: " + p.softErrors());
	}
}
