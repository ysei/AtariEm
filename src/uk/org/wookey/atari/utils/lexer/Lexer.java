package uk.org.wookey.atari.utils.lexer;

import java.util.List;
import java.util.ArrayList;

import uk.org.wookey.atari.utils.Logger;

/*
 * Lexical analyzer for Scheme-like minilanguage:
 * (define (foo x) (bar (baz x)))
 */
public class Lexer {
	private final static Logger _logger = new Logger(Lexer.class.getName());

    public List<LexerToken> lex(String input) {
        List<LexerToken> result = new ArrayList<LexerToken>();
        int lineNum = 1;
        int col = 0;
        
        for (int i = 0; i < input.length(); ) {
        	col++;
        	//_logger.logInfo("Examining '" + input.charAt(i) + "'");
            switch (input.charAt(i)) {
            case '\n':
                result.add(new LexerToken(LexerTokenType.EOL, lineNum, col));
                i++;
                lineNum++;
                col = 0;
                break;
                
            case '(':
                result.add(new LexerToken(LexerTokenType.LPAREN, lineNum, col));
                i++;
                break;
                
            case ')':
                result.add(new LexerToken(LexerTokenType.RPAREN, lineNum, col));
                i++;
                break;
                
            case ',':
                result.add(new LexerToken(LexerTokenType.COMMA, lineNum, col));
                i++;
                break;
                
            case '+':
                result.add(new LexerToken(LexerTokenType.PLUS, lineNum, col));
                i++;
                break;
                
            case '-':
                result.add(new LexerToken(LexerTokenType.MINUS, lineNum, col));
                i++;
                break;
                
            case '<':
                result.add(new LexerToken(LexerTokenType.LSBOF, lineNum, col));
                i++;
                break;
                
            case '>':
                result.add(new LexerToken(LexerTokenType.MSBOF, lineNum, col));
                i++;
                break;
                
            case '$':
            	{
            		String num = getHexNumber(input, i+1);
            		_logger.logInfo("Hex: " + num);
            		i += num.length() + 1;
            		col += num.length() + 1;
            		result.add(new LexerToken(LexerTokenType.HEX, num, lineNum, col));          	
            	}
                break;
                
            case '#':
                result.add(new LexerToken(LexerTokenType.HASH, lineNum, col));
                i++;
                break;
                
            case '%':
                result.add(new LexerToken(LexerTokenType.PERCENT, lineNum, col));
                i++;
                break;
                
            case ';':
            	String comment = gobbleLine(input, i);
            	i += comment.length();
            	col += comment.length();
            	
            	comment = comment.substring(1);
            	
            	result.add(new LexerToken(LexerTokenType.COMMENT, comment, lineNum, col));
            	break;
            	
            case ' ':
            case '\t':
            	String white = gobbleWhitespace(input, i);
            	i += white.length();
            	col += white.length();
            	result.add(new LexerToken(LexerTokenType.WHITESPACE, white, lineNum, col));
            	break;
            	
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            	{
            		String num = getNumber(input, i);
            		i += num.length();
            		col += num.length();
            		result.add(new LexerToken(LexerTokenType.DECIMAL, num, lineNum, col));
            	}
                break;
                
            default:
            	String atom = getAtom(input, i);
            	i += atom.length();
            	col += atom.length();
            	result.add(new LexerToken(LexerTokenType.ATOM, atom, lineNum, col));
                break;
            }
        }
        
        // Make sure that the last token is an EOL token
        LexerToken lastTok = result.get(result.size()-1);
        if (lastTok.type != LexerTokenType.EOL) {
            result.add(new LexerToken(LexerTokenType.EOL, "<EOL>"));
        }
        
        return result;
    }

    private String getAtom(String s, int i) {
        int j = i;
        
        while (j < s.length()) {
            if (isAtomChar(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }
    
    private String getNumber(String s, int i) {
        int j = i;
        
        while (j < s.length()) {
            if (isDecimalChar(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }
    
    private String getHexNumber(String s, int i) {
        int j = i;
        
        while (j < s.length()) {
            if (isHexChar(s.charAt(j))) {
                j++;
            } else {
                return s.substring(i, j);
            }
        }
        return s.substring(i, j);
    }
    
    private String gobbleLine(String s, int i) {
    	int eol = i;
    	
    	while (eol < s.length()) {
    		if (s.charAt(eol) == '\n') {
    			return s.substring(i,  eol);
    		}
    		eol++;
    	}
    	
    	return s.substring(i, eol);
    }
    
    private String gobbleWhitespace(String s, int i) {
    	int end = i;
    	
    	while (end < s.length()) {
    		if (!(s.charAt(end) == ' ' || s.charAt(end) == '\t')) {
    			return s.substring(i,  end);
    		}
    		end++;
    	}
    	
    	return s.substring(i, end);
    }
    
    private boolean isAtomChar(char c) {
    	if (c <= ' ') {
    		return false;
    	}

    	return true;
    }
    
    private boolean isDecimalChar(char c) {
    	if (c >= '0' && c <= '9') {
    		return true;
    	}

    	return false;
    }

    private boolean isHexChar(char c) {
    	if (c >= '0' && c <= '9') {
    		return true;
    	}
    	
    	if (c >= 'a' && c <= 'f') {
    		return true;
    	}
    	
    	if (c >= 'A' && c <= 'F') {
    		return true;
    	}

    	return false;
    }
}