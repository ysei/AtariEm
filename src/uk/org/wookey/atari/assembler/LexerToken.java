package uk.org.wookey.atari.assembler;



public class LexerToken {
	public final LexerTokenType type;
    public final String value;
    
    public int lineNumber;
    public int column;
    
    // could have column and line number fields too, for reporting errors later

    public LexerToken(LexerTokenType t) {
    	this(t, "");
    }

    public LexerToken(LexerTokenType t, String c) {
    	this(t, c, 0, 0);
    }

    public LexerToken(LexerTokenType t, int lineNum, int col) {
    	this(t, "", lineNum, col);
    }

    public LexerToken(LexerTokenType t, String c, int lineNum, int col) {
        type = t;
        value = c;
        
        lineNumber = lineNum;
        column = col;
    }

    public String toString() {
    	String inf = "(" + lineNumber + ":" + column + ") ";
    	
        if (type == LexerTokenType.ATOM) {
            return inf + "ATOM<" + value + ">";
        }
        else if (type == LexerTokenType.COMMENT) {
        	return inf + "COMMENT<" + value + ">";
        }
        else if (type == LexerTokenType.DECIMAL) {
        	return inf + "DECIMAL<" + value + ">";
        }
        else if (type == LexerTokenType.HEX) {
        	return inf + "HEX<" + value + ">";
        }
        else if (type == LexerTokenType.BINARY) {
        	return inf + "BIN<" + value + ">";
        }
        
        return inf + type.toString();
    }
}
