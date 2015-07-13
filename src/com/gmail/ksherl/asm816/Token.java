package com.gmail.ksherl.asm816;

/*
 * Created on Feb 24, 2006
 * Feb 24, 2006 2:31:33 AM
 */

public final class Token
{
    public static final int EOF = -1;
    public static final int SPACE = 256;
    public static final int NUMBER = 257;
    public static final int SYMBOL = 258;
    public static final int EOL = 259;
    public static final int STRING = 260;
    
    // multi-byte expression operators
    public static final int LOGICAL_AND = 261;
    public static final int LOGICAL_OR = 262;
    public static final int LOGICAL_NOT = 263;
    public static final int LOGICAL_EOR = 264;
    
    public static final int LE = 265;
    public static final int GE = 266;
    public static final int GT = '>';
    public static final int LT = '<';
    
    public static final int LEFT_SHIFT = 267;
    public static final int RIGHT_SHIFT = 268;
    
    public static final int EQ = '=';
    public static final int NE = 269;
    
    public static final int MOD = 270;
        
    public static final int MACRO_PARM = 300;
    public static final int MACRO_LAB = 301;
    
    private int tokenType;
    private int tokenValue;
    private String tokenString;
    private int tokenLineNumber;

    public Token(int type) {
        tokenType = type;
        tokenValue = 0;
        tokenString = null;
        tokenLineNumber = 0;
        if (ctype.isprint(type)) {
            tokenString = new String( new byte[] {(byte)type});
        }
    }
    
    public Token(int type, Lexer lex) {
        tokenType = type;
        tokenValue = 0;
        tokenString = null;
        tokenLineNumber = lex == null ? 0 : lex.Line();
    }
    
    public Token(int type, String string, Lexer lex) {
        this(type, lex);
        tokenString = string;
    }
    
    public Token(int type, int value, Lexer lex) {
        this(type, lex);
        tokenValue = value;
    }  
    
    public String toString() {
        return tokenString;
    }
    
    public int Line() {
        return tokenLineNumber;
    }
    
    public int Value() {
        if (tokenType == STRING)
        {
            int l = tokenString.length();
            int v = 0;
            for (int i = 0; i < l; i++)
            {
                v = v << 8;
                v |= (tokenString.charAt(i) & 0xff);
            }
            return v;
        }
        return tokenValue;
    }
    
    public int Type() {
        return tokenType;
    }
    
    /*
     * checks if a token is a register
     * returns 'a', 'x', 'y', 's', or 0.
     */
    public int Register() {
        if (tokenType != SYMBOL) return 0;
        if (tokenString.length() != 1) return 0;
        int c = ctype.tolower(tokenString.charAt(0));
        if (c == 'a' || c == 'x' || c == 'y' || c == 's') return c;
        return 0;
    }
    
    /*
     * throw an error if this isn't the expected token type.
     */
    public void Expect(Integer... arg) throws AsmException {
        for (int i = 0; i < arg.length; i++) {
            int type = arg[i].intValue();
            
            if (type == tokenType) {
            	return;
            }
        }
        
        throw new AsmException(Error.E_UNEXPECTED, this);
    }
    
    public void Expect(int t1) throws AsmException {
        if (tokenType != t1) 
            throw new AsmException(Error.E_UNEXPECTED, this);
    }
    
    public void Expect(int t1, int t2) throws AsmException {
        if (tokenType != t1 && tokenType != t2)
            throw new AsmException(Error.E_UNEXPECTED, this);
    }
    
    public void Expect(int t1, int t2, int t3) throws AsmException {
        if (tokenType != t1 && tokenType != t2 && tokenType != t3)
            throw new AsmException(Error.E_UNEXPECTED, this);
    }
    
    public void Expect(int[] arg) throws AsmException {
        for(int type: arg)
        {
            if (type == tokenType) return;
        }
       throw new AsmException(Error.E_UNEXPECTED, this);
    }
    
    public int ExpectSymbol(String name) throws AsmException {
        if (tokenType == SYMBOL)
        {
            if (tokenString.compareToIgnoreCase(name) == 0)
                return 1;
        }
        throw new AsmException(Error.E_UNEXPECTED, this);
    }
    
    public int ExpectSymbol(String... names) throws AsmException {
        if (tokenType == SYMBOL) {
            for (int i = 0; i < names.length; i++) {
                if (tokenString.compareToIgnoreCase(names[i]) == 0)
                    return i + 1;
            }
        }
        
        throw new AsmException(Error.E_UNEXPECTED, this);
    }

    public String detailString() {
    	String res = "Line #" + tokenLineNumber + ": " + tokenName();
    	
    	return res;
    }
    
    private String tokenName() {
    	String name = "T_" + tokenType;
    	
    	switch (tokenType) {
    	case EOF:
    		name = "T_EOF";
    		break;

    	case SPACE:
    		name = "T_SPACE";
    		break;

    	case NUMBER:
    		name = "T_NUMBER";
    		break;

    	case SYMBOL:
    		name = "T_SYMBOL=";
    		int c = Register();
    		if (c == 0) {
    			name = name + "'" + tokenString + "'";
    		}
    		else {
    			name = name + tokenString.toUpperCase() + "_Reg";
    		}
    		break;

    	case EOL:
    		name = "T_EOL";
    		break;

    	case STRING:
    		name = "T_STRING='" + tokenString + "'";
    		break;

    	case LOGICAL_AND:
    		name = "T_AND";
    		break;

    	case LOGICAL_NOT:
    		name = "T_NOT";
    		break;

    	case LOGICAL_OR:
    		name = "T_OR";
    		break;

    	case LOGICAL_EOR:
    		name = "T_EOR";
    		break;

    	case LE:
    		name = "T_LE";
    		break;

    	case GE:
    		name = "T_GE";
    		break;

    	case GT:
    		name = "T_GT";
    		break;

    	case LT:
    		name = "T_LT";
    		break;

    	case LEFT_SHIFT:
    		name = "T_<<";
    		break;

    	case RIGHT_SHIFT:
    		name = "T_>>";
    		break;

    	case EQ:
    		name = "T_EQ";
    		break;

    	case NE:
    		name = "T_NE";
    		break;

    	case MOD:
    		name = "T_MOD";
    		break;
    	}
    	
    	return name;
    }
}
