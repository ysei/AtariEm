package uk.org.wookey.atari.utils.lexer;


public class LexerToken {
	public final LexerTokenType t;
    public final String c;
    
    // could have column and line number fields too, for reporting errors later

    public LexerToken(LexerTokenType t, String c) {
        this.t = t;
        this.c = c;
    }

    public String toString() {
        if (t == LexerTokenType.ATOM) {
            return "ATOM<" + c + ">";
        }
        else if (t == LexerTokenType.COMMENT) {
        	return "COMMENT<" + c + ">";
        }
        
        return t.toString();
    }
}
