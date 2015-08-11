package uk.org.wookey.atari.assembler;

import java.util.List;

import uk.org.wookey.atari.exceptions.EOFException;
import uk.org.wookey.atari.exceptions.SyntaxException;
import uk.org.wookey.atari.utils.Logger;

public class SimpleParser {
	private final static Logger _logger = new Logger("SimpleParser");

	private List<LexerToken> tokens;
	private int tokenIndex;

	public SimpleParser(List<LexerToken> tokList) {
		tokens = tokList;
		tokenIndex = 0;
	}

	protected void rewindTokens() {
		tokenIndex = 0;
	}

	protected LexerToken peekToken() {
		return peekToken(1);
	}
	
	protected LexerToken peekToken(int numAhead) {
		if (tokenIndex+numAhead-1 >= tokens.size()) {
			_logger.logInfo("Out of LexerTokens!");
			return new LexerToken(LexerTokenType.EOF);
		}
		
		return tokens.get(tokenIndex+numAhead-1);
	}
	
	protected LexerToken currentToken() {
		if (tokenIndex == 0) {
			_logger.logError("currentToken(); called before getToken()");
			return null;
		}
		
		return tokens.get(tokenIndex-1);
	}

	protected LexerToken getToken() throws EOFException {
		if (tokenIndex >= tokens.size()) {
			throw new EOFException("Ran out of tokens");
		}
			
		LexerToken t = tokens.get(tokenIndex);
		tokenIndex++;
				
		return t;
	}
	
	protected LexerToken getToken(int skip) throws EOFException {
		LexerToken res = currentToken();
		
		for (int i=0; i<skip; i++) {
			res = getToken();
			//_logger.logSuccess("Got token: " + res.toString());
		}
		
		return res;
	}
	
	protected boolean unGetToken() {
		if (tokenIndex == 0) {
			_logger.logError("Call to unGetToken() but no token to unget!");
			return false;
		}
		
		tokenIndex--;
		return true;
	}
	
	protected LexerToken skipTo(LexerTokenType wanted) throws EOFException {
		if (currentToken().type == wanted) {
			return currentToken();
		}
		
		LexerToken t = getToken();
		
		while (true) {
			if (t.type == wanted) {
				return t;
			}
			else if (t.type == LexerTokenType.EOF) {
				throw new EOFException("Hit EOF while skipping to " + wanted.toString());
			}

			t = getToken();
		}
	}
	
	protected void expectEOL(String exceptionMessage) throws SyntaxException {
		if (currentToken().type != LexerTokenType.EOL) {
			throw new SyntaxException(exceptionMessage + " [" + currentToken().toString() + "]");
		}
	}
	
	protected boolean tokensAre(LexerTokenType... expected) {
		return expect(0, expected);
	}
	
	protected boolean expect(int lookAhead, LexerTokenType... expected) {
		for (LexerTokenType t: expected) {
			LexerToken la = peekToken(lookAhead);
			
			//_logger.logInfo("Looking for: " + t.toString() + ", got " + la.toString());
			
			if (t != la.type) {
				return false;
			}

			lookAhead++;
		}
		
		return true;
	}
	
	protected void lookAround() {
		int start = tokenIndex - 5;
		int end = tokenIndex + 8;
		
		if (start < 0) {
			start = 0;
		}
		
		if (end > tokens.size()) {
			end = tokens.size() - 1;
		}
		
		for (int i=start; i<end; i++) {
			if (i == tokenIndex) {
				System.out.print("*");
			}
			System.out.print(tokens.get(i).toString() + "   ");
		}
		System.out.println();
	}
}
