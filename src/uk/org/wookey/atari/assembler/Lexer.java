package uk.org.wookey.atari.assembler;

import java.util.List;
import java.util.ArrayList;

import uk.org.wookey.atari.utils.Logger;

/*
 * Lexical analyzer for Scheme-like minilanguage:
 * (define (foo x) (bar (baz x)))
 */
public class Lexer {
	private final static Logger _logger = new Logger(Lexer.class.getName());
	
	private boolean startOfLine;

    public List<LexerToken> lex(String input) {
        List<LexerToken> result = new ArrayList<LexerToken>();
        int lineNum = 1;
        int col = 0;
        
        startOfLine = true;
        
        for (int i = 0; i < input.length(); ) {
        	//_logger.logInfo("Examining '" + input.charAt(i) + "'");
            switch (input.charAt(i)) {
            case '\n':
                add(new LexerToken(LexerTokenType.EOL, lineNum, col), result);
                i++;
                lineNum++;
                col = 0;
                startOfLine = true;
                break;
                
            case '(':
                add(new LexerToken(LexerTokenType.LPAREN, lineNum, col), result);
               
                i++;
                col++;
                break;
                
            case ')':
                add(new LexerToken(LexerTokenType.RPAREN, lineNum, col), result);
                i++;
                col++;
                break;
                
            case ',':
                add(new LexerToken(LexerTokenType.COMMA, lineNum, col), result);
                i++;
                col++;
                break;
                
            case '+':
                add(new LexerToken(LexerTokenType.PLUS, lineNum, col), result);
                i++;
                col++;
                break;
                
            case '-':
                add(new LexerToken(LexerTokenType.MINUS, lineNum, col), result);
                i++;
                col++;
                break;
                 
            case '<':
                i++;
                col++;
            	if (i < input.length() && input.charAt(i) == '@') {
            		add(new LexerToken(LexerTokenType.PLABEL, lineNum, col), result);
            		i++;
            		col++;
            	}
            	else {
                    add(new LexerToken(LexerTokenType.LSBOF, lineNum, col), result);
            	}            	
                break;
                
            case '>':
                i++;
                col++;
            	if (i < input.length() && input.charAt(i) == '@') {
            		add(new LexerToken(LexerTokenType.NLABEL, lineNum, col), result);
            		i++;
            		col++;
            	}
            	else {
                    add(new LexerToken(LexerTokenType.MSBOF, lineNum, col), result);
            	}            	
                break;
                
            case '$':
        	{
        		String num = getHexNumber(input, i+1);
        		i += num.length() + 1;
        		col += num.length() + 1;
        		add(new LexerToken(LexerTokenType.HEX, num, lineNum, col), result);          	
        	}
            break;
            
            case '%':
        	{
        		String num = getBinNumber(input, i+1);
        		_logger.logInfo("Bin: " + num);
        		i += num.length() + 1;
        		col += num.length() + 1;
        		add(new LexerToken(LexerTokenType.BINARY, num, lineNum, col), result);          	
        	}
            break;
            
            case '#':
                add(new LexerToken(LexerTokenType.HASH, lineNum, col), result);
                i++;
                col++;
                break;
                
            case '=':
                add(new LexerToken(LexerTokenType.EQUALS, lineNum, col), result);
                i++;
                col++;
                break;
                
            case ';':
            	String comment = gobbleLine(input, i);
            	i += comment.length();
            	col += comment.length();
            	
            	comment = comment.substring(1);
            	
            	add(new LexerToken(LexerTokenType.COMMENT, comment, lineNum, col), result);
            	break;
            	
            case ' ':
            case '\t':
            	String white = gobbleWhitespace(input, i);
            	i += white.length();
            	col += white.length();
            	add(new LexerToken(LexerTokenType.WHITESPACE, white, lineNum, col), result);
            	break;
            	
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
            	{
            		String num = getNumber(input, i);
            		i += num.length();
            		col += num.length();
            		add(new LexerToken(LexerTokenType.DECIMAL, num, lineNum, col), result);
            	}
                break;
                
            default:
            	if (isAtomChar(input.charAt(i))) {
            		String atom = getAtom(input, i);
            		i += atom.length();
            		col += atom.length();
            		add(new LexerToken(LexerTokenType.ATOM, atom, lineNum, col), result);
            	}
            	else {
            		add(new LexerToken(LexerTokenType.UNKNOWN, input.substring(i,  i), lineNum, col), result);
            		i++;
            		col++;
            	}
                break;
            }
        }
        
        // Make sure that the last token is an EOL token
        LexerToken lastTok = result.get(result.size()-1);
        if (lastTok.type != LexerTokenType.EOL) {
            add(new LexerToken(LexerTokenType.EOL, "<EOL>"), result);
        }
        
        return result;
    }
    
    private void add(LexerToken t, List<LexerToken> result) {
    	if ((t.type == LexerTokenType.WHITESPACE) && !startOfLine) {
    		return;
    	}
    	
    	startOfLine = false;

    	if (t.type == LexerTokenType.COMMENT) {
    		return;
    	}
    	
    	//_logger.logInfo("Add tok " + t.toString());
    	
    	result.add(t);
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
    
    private String getBinNumber(String s, int i) {
        int j = i;
        
        while (j < s.length()) {
            if (isBinChar(s.charAt(j))) {
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

    	if ((c >= 'a') && (c <= 'z')) {
    		return true;
    	}

    	if ((c >= 'A') && (c <= 'Z')) {
    		return true;
    	}

    	if (isDecimalChar(c)) {
    		return true;
    	}
    	
    	if (c == '_' || c == '.' || c == '*' || c == ':' || c == '@') {
    		return true;
    	}
    	
    	return false;
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

    private boolean isBinChar(char c) {
    	if (c >= '0' && c <= '1') {
    		return true;
    	}

    	return false;
    }
}