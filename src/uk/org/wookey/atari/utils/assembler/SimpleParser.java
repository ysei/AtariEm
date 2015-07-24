package uk.org.wookey.atari.utils.assembler;

import java.util.List;

import uk.org.wookey.atari.utils.Logger;
import uk.org.wookey.atari.utils.lexer.LexerToken;
import uk.org.wookey.atari.utils.lexer.LexerTokenType;

public class SimpleParser {
	private final static Logger _logger = new Logger(Parser.class.getName());

	private List<LexerToken> tokens;
	private int tokenIndex;

	public SimpleParser(List<LexerToken> tokList) {
		tokens = tokList;
		tokenIndex = 0;
	}
	
	protected void gobble(LexerTokenType type) {
		LexerToken t = getToken();
		
		while (t.type != type) {
			t = getToken();
			if (t.type == LexerTokenType.EOF) {
				_logger.logError("Unexpected EOF while gobbling up to " + type);
			}
		}
	}
	
	protected LexerToken peekToken() {
		if (tokenIndex >= tokens.size()) {
			_logger.logInfo("Out of LexerTokens!");
			return new LexerToken(LexerTokenType.EOF);
		}
		
		return tokens.get(tokenIndex);
	}
	
	protected LexerToken currentToken() {
		if (tokenIndex == 0) {
			_logger.logError("currentToken(); called before getToken()");
			return null;
		}
		
		return tokens.get(tokenIndex-1);
	}

	protected LexerToken getToken() {
		if (tokenIndex >= tokens.size()) {
			_logger.logInfo("Out of LexerTokens!");
			return new LexerToken(LexerTokenType.EOF);
		}
		
		LexerToken t = tokens.get(tokenIndex);
		tokenIndex++;
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return t;
	}
}
