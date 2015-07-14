package uk.org.wookey.assembler;

import java.io.InputStream;

import com.gmail.sherl.asm816.AddressMode;
import com.gmail.sherl.asm816.AsmException;
import com.gmail.sherl.asm816.Error;
import com.gmail.sherl.asm816.INSTR;
import com.gmail.sherl.asm816.JunkPile;
import com.gmail.sherl.asm816.Lexer;
import com.gmail.sherl.asm816.Parser;
import com.gmail.sherl.asm816.Token;
import com.gmail.sherl.asm816.__TokenIterator;
import com.gmail.sherl.asm816.ctype;
import com.gmail.sherl.expression.ComplexExpression;
import com.gmail.sherl.expression.Expression;
import com.gmail.sherl.expression.RelExpression;
import com.gmail.sherl.expression.__Expression;
import com.gmail.sherl.mpw.MPWExpression;
import com.gmail.sherl.mpw.MPW_SymbolTable;
import com.gmail.sherl.omf.OMF;
import com.gmail.sherl.omf.OMF_Opcode;

import uk.org.wookey.atari.utils.Logger;

public class Wookey_Parser extends Parser {
	private final static Logger _logger = new Logger(Wookey_Parser.class.getName());

	private boolean finished;
	private int line;
	private String fOpcode;
	
	private JunkPile fData;
	private MPW_SymbolTable fSymbols;
	private Wookey_Lexer fLexer;
	
	@Override
    public void ParseFile(InputStream stream) {
        Wookey_Lexer lex = new Wookey_Lexer(stream);
        Parse(lex);
    }   
    
    private void Parse(Wookey_Lexer lex) {
    	finished = false;
        while (!finished) {
        	_logger.logInfo("ParseLine");
            ParseLine(lex);
        }  	
    }
    
    protected void ParseLine(Lexer lex) {
        line = lex.Line();
        
        try {         
            Line l = GetLine(lex);
            
            if (l == null) return;
            DoLine(l.lab, l.opcode, l.operand);
        }
        catch (AsmException error) {
            error.SetLine(line);
            error.print(System.err);
            lex.SkipLine();
        }      
    }
    
    private Line GetLine(Lexer lex) throws AsmException {
       Line l = new Line();
        
       Token t;
       String s;
       int type;
        
       t = lex.Expect(Token.EOF, Token.EOL, 
    		   Token.SPACE, Token.SYMBOL);
        
       type = t.Type();
        
       if (type == Token.EOL) {
    	   _logger.logInfo("EOL");
    	   return null;
       }
        
       if (type == Token.EOF) {
    	   _logger.logInfo("EOF");
    	   finished = true;
    	   return null;
       }
        
       if (type == Token.SYMBOL) {
    	   _logger.logInfo("SYMBOL");
    	   l.lab = t.toString();
    	   t = lex.Expect(Token.SPACE, Token.EOL);
            
    	   if (t.Type() == Token.EOL) {
    		   return l;
    	   }
       }
        
       t = lex.Expect(Token.SYMBOL);
	   _logger.logInfo("SYMBOL?");
        
       l.opcode = t.toString().toUpperCase();
       l.operand = lex.Arguments(true);

       return l;
    }
    
    void DoLine(String lab, String opcode, __TokenIterator ti) throws AsmException
    {
        fPC = fData == null ? 0 : fData.Size();
                
        this.fOpcode = opcode;
        Wookey_Directive d;
        if (opcode == null) {
            d = Wookey_Directive.IMPLIED_ANOP;
        }
        else { 
        	d = (Wookey_Directive)fDirectives.get(opcode);
        }
        
        if (d != null)
        {
            boolean tf = ParseDirective(lab, d, ti);
            if (!tf)
                throw new AsmException(Error.E_UNEXPECTED, opcode);
           return;
        }
                    
        INSTR instr = fOpcodes.get(opcode);
        if (instr != null) {
        	ParseInstruction(lab, instr, ti);
                
        	return;
        }
            
        throw new AsmException(Error.E_UNEXPECTED, opcode);        
    }
    
    protected boolean ParseDirective(String lab, Enum d, __TokenIterator ti) throws AsmException {
        __Expression e = null;
        Wookey_Directive dir = (Wookey_Directive)d;
              
        switch(dir)
        {
        case IMPLIED_ANOP:
            // EOL already processed.
            break;
        
        case EQU: {
        	e = ParseExpression(ti);    
        	
        	if (lab != null) {
        		e = e.Simplify(fSymbols, false);
        		fSymbols.Put(lab, e);
                    
        		Expression ex = new Expression(e);
        		ex.SetName(lab);
        		ex.SetType(OMF.OMF_GEQU);
        		ex.SetSize(0);
        		fData.add(ex);
        	}
        	e = null;
        	lab = null;
        }
        break;
        
        default:
            return false;
        }        

        return true;
    }

    protected boolean ParseInstruction(String lab, INSTR instr, __TokenIterator ti) 
    throws AsmException
    {
        int opcode = 0x00;
        int size;
        
        if (lab != null)
        {
            AddLabel(lab);
            fSymbols.Put(lab, new RelExpression(fPC));
        }

        boolean check_a = false;
        
        //__TokenIterator ti = lex.Arguments(true);
        
        // special case mvn/mvp
        switch (instr)
        {
        case MVN:
        case MVP:
            {   
                __Expression e1, e2;
                Expression ex1, ex2;
                
                e1 = ParseExpression(ti);
                ti.Expect(',');
                e2 = ParseExpression(ti);
                ti.Expect(Token.EOL);

                e1 = e1.Simplify(fSymbols, false);
                e2 = e2.Simplify(fSymbols, false);
                switch (instr)
                {
                case MVN:
                    opcode = 0x54;
                    break;
                case MVP:
                    opcode = 0x44;
                    break;
                }
                fData.add8(opcode, 0);
                
                ex1 = new Expression(e1);
                ex2 = new Expression(e2);
                ex1.SetSize(1);
                ex2.SetSize(1);
                
                fData.add(ex1);
                fData.add(ex2);
                
                break;
            }
            
            /*
             *TODO -- perhaps these should be macros ?
             * special case -- a --> implied
             */
        case ASL:
        case DEC:
        case INC:
        case LSR:
        case ROL:
        case ROR:
            check_a = true;
            
        default:
            
            operand oper = ParseOperand(ti);
            AddressMode mode = oper.mode;
            __Expression e = oper.expression;
            if (e != null)
                e = e.Simplify(fSymbols, false);
            
            if (check_a)
            {
                String oplab = e.toString();
                if (oplab != null 
                        && oplab.compareToIgnoreCase("a") == 0)
                    mode = AddressMode.IMPLIED;
            }
            opcode = -1;
            
            // TODO -- warn if we need to truncate a number.
            if (mode == AddressMode.ASSUMED_ABS)
            {
                Integer v = e.Value();
                if (v == null)
                    opcode = instr.find_opcode(fMachine, AddressMode.ABS, AddressMode.ABSLONG, AddressMode.DP);

                else
                {
                    int i = v.intValue();
                    if (i <= 0xff)
                        opcode = instr.find_opcode(fMachine, AddressMode.DP, AddressMode.ABS, AddressMode.ABSLONG);

                    else if (i > 0xffff)                        
                        opcode = instr.find_opcode(fMachine, AddressMode.ABSLONG, AddressMode.ABS, AddressMode.DP);

                    else 
                        opcode = instr.find_opcode(fMachine, AddressMode.ABS, AddressMode.ABSLONG, AddressMode.DP); 
                }
            }
            else if (mode == AddressMode.ASSUMED_ABS_X)
            {
                Integer v = e.Value();
                if (v == null)                     
                    opcode = instr.find_opcode(fMachine, AddressMode.ABS_X, AddressMode.ABSLONG_X, AddressMode.DP_X);

                else
                {
                    int i = v.intValue();
                    if (i <= 0xff)
                        opcode = instr.find_opcode(fMachine, AddressMode.DP_X, AddressMode.ABS_X, AddressMode.ABSLONG_X);

                    else if (i > 0xffff)                        
                        opcode = instr.find_opcode(fMachine, AddressMode.ABSLONG_X, AddressMode.ABS_X, AddressMode.DP_X);

                    else 
                        opcode = instr.find_opcode(fMachine, AddressMode.ABS_X, AddressMode.ABSLONG_X, AddressMode.DP_X); 
                }            
            }
            else if (mode == AddressMode.ASSUMED_ABS_Y)
            {
                Integer v = e.Value();
                if (v == null) 
                    opcode = instr.find_opcode(fMachine, AddressMode.ABS_Y, AddressMode.DP_Y);
                else
                {
                    int i = v.intValue();
                    if (i <= 0xff)
                        opcode = instr.find_opcode(fMachine, AddressMode.DP_Y, AddressMode.ABS_Y);

                    else                     
                        opcode = instr.find_opcode(fMachine, AddressMode.ABS_Y, AddressMode.DP_Y);
                }                  
            }
            else
                opcode = instr.opcode(mode, fMachine);
            if (opcode == -1)
            {
                throw new AsmException(Error.E_OPCODE, instr.toString());
            }
            size = INSTR.Size(opcode, fM, fX);

            fData.add8(opcode, 0);
            if (size > 1)
            {
                Expression ex = new Expression(e);
                ex.SetSize(size - 1);
                //TODO -- set type.
                if (INSTR.isBranch(opcode))
                    ex.SetType(OMF.OMF_RELEXPR);
                
                if (opcode == 0x22)
                    ex.SetType(OMF.OMF_LEXPR);
                
                
                fData.add(ex);
            }

        } /* switch (instr) */
                
        return true;
    }
    
    @Override
	protected void AddDirectives() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ComplexExpression ParseExpression(__TokenIterator ti)
			throws AsmException {
		// TODO Auto-generated method stub
		return null;
	}

    private void AddLabel(String lab)
    {
        if (lab == null) {
        	return;
        }
        
        if (lab.indexOf('@') != -1) {
        	return; // local label.
        }
        
        OMF_Opcode op = null;
        
        fLexer.SetLocalLabel(lab);
        
        fData.add(op);
    }
    
    private int DotSize(int blank) {
        if (fOpcode == null) {
        	return blank;
        }
        
        if (fOpcode.indexOf('.') > 0) { 
            return ctype.tolower(fOpcode.charAt(fOpcode.length() -1));
        }
        
        return blank;
    }

    private void ParseDCB(__TokenIterator ti, String lab, int q) throws AsmException {
        int size = QualifierToInt(q);
        __Expression e;
        String s;
        Token t;
        
        int repeat = ParseIntExpression(ti);
        ti.Expect(',');
        
        t = ti.Peek();
        if (t.Type() == Token.STRING) {
            t = ti.Expect(Token.STRING);
            s = t.toString();
            
            byte[] data = StringToByte(s);
            for (int i = 0; i < repeat; i++) {
                fData.add(data);       
            }
        }
        else {
            e = ParseExpression(ti);
            e = e.Simplify(fSymbols, false);
            Expression ex = new Expression(e);
            ex.SetSize(QualifierToInt(size));
            for (int i = 0; i < repeat; i++) {
                fData.add(ex);          
            }
        }
        
        ti.Expect(Token.EOL);
    }
    
    private void ParseDC(__TokenIterator ti, String lab, int q) throws AsmException {
        int size = QualifierToInt(q);

        __Expression e;
        String s;
        Token t;
        
        for (;;) {
            t = ti.Peek();
            if (t.Type() == Token.STRING) {
                t = ti.Expect(Token.STRING);
                s = t.toString();
                // TODO -- check if a float.
                fData.add(StringToByte(s));
                // TODO -- if size == 2 or 4, align.
            }
            else {
                e = ParseExpression(ti);
                e = e.Simplify(fSymbols, false);
                Expression ex = new Expression(e);
                ex.SetSize(size);
                fData.add(ex); 
            }
            
            t = ti.Expect(Token.EOL, (int)',');
            if (t.Type() == Token.EOL) break;
        }
    }

    private void ParseInclude(__TokenIterator ti) throws AsmException {
        Token t;
        String s;
        t = ti.Expect(Token.STRING);
        ti.Expect(Token.EOL);
        
        s = t.toString();
        
        Include(s);
    }
    
    private static final boolean QualifierIsInt(int q) {
        switch(q)
        {
        case 'b':
        case 'w':
        case 'l':
            return true;
        }    
        
        return false;
    }
    
    private static final int QualifierToInt(int q) {
        switch(q)
        {
        case 'b':
            return 1;
        case 'w':
            return 2;
        case 'l':
            return 4;
        case 's':
            return 4;
        case 'd':
            return 8;
        case 'x':
            return 12;
        case 'p':
            return 12;
        }
        
        return 0;
    }
    
    private byte[] StringToByte(String s)
    {
        int length; 
        byte[] data;
        
        /*
         * TODO -- macro replacement within string.
         */
        
        length = s.length();
        data = new byte[length];
        
        for (int i = 0; i < length; i++)
        {
            int c = s.charAt(i);
            data[i] = (byte)c;
        }
        
        return data;
    }
    
    private int ParseIntExpression(__TokenIterator ti) throws AsmException {      
        ComplexExpression ce;
        __Expression e;
        
        ce = MPWExpression.Parse(ti, fPC); 
        e = ce.Simplify(fSymbols, true);
        Integer v = e.Value();
        
        if (v == null) {
            throw new AsmException(Error.E_EXPRESSION);
        }
        
        return v.intValue();
    }    

    class Line {
        public String lab;
        public String opcode;
        public __TokenIterator operand;

        public Line() {
            lab = null;
            opcode = null;
            operand = null;
        }
    }
}
