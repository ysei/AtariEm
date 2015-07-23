package uk.org.wookey.atari.utils.lexer;

import java.util.List;
import java.util.ArrayList;

import uk.org.wookey.atari.editor.CodeEditor;
import uk.org.wookey.atari.utils.Logger;

/*
 * Lexical analyzer for Scheme-like minilanguage:
 * (define (foo x) (bar (baz x)))
 */
public class Lexer {
	private final static Logger _logger = new Logger(Lexer.class.getName());

    public List<LexerToken> lex(String input) {
        List<LexerToken> result = new ArrayList<LexerToken>();
        
        for (int i = 0; i < input.length(); ) {
        	_logger.logInfo("Examining '" + input.charAt(i) + "'");
            switch (input.charAt(i)) {
            case '\n':
                result.add(new LexerToken(LexerTokenType.EOL, "<EOL>"));
                i++;
                break;
                
            case '(':
                result.add(new LexerToken(LexerTokenType.LPAREN, "("));
                i++;
                break;
                
            case ')':
                result.add(new LexerToken(LexerTokenType.RPAREN, ")"));
                i++;
                break;
                
            case ',':
                result.add(new LexerToken(LexerTokenType.COMMA, ","));
                i++;
                break;
                
            case '+':
                result.add(new LexerToken(LexerTokenType.PLUS, "+"));
                i++;
                break;
                
            case '-':
                result.add(new LexerToken(LexerTokenType.MINUS, "-"));
                i++;
                break;
                
            case ';':
            	String comment = gobbleLine(input, i);
            	i += comment.length();
            	
            	comment = comment.substring(1);
            	
            	result.add(new LexerToken(LexerTokenType.COMMENT, comment));
            	break;
            	
            case ' ':
            case '\t':
            	i++;
            	break;
                
            default:
            	String atom = getAtom(input, i);
            	i += atom.length();
            	result.add(new LexerToken(LexerTokenType.ATOM, atom));
                break;
            }
        }
        
        // Make sure that the last token is an EOL token
        LexerToken lastTok = result.get(result.size()-1);
        if (lastTok.t != LexerTokenType.EOL) {
            result.add(new LexerToken(LexerTokenType.EOL, "<EOL>"));
        }
        
        return result;
    }

    /*
     * Given a String, and an index, get the atom starting at that index
     */
    private String getAtom(String s, int i) {
        int j = i;
        
        for ( ; j < s.length(); ) {
            if (Character.isLetter(s.charAt(j))) {
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
}