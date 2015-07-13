package uk.org.wookey.assembler;

import java.io.InputStream;

import com.gmail.ksherl.asm816.AddressMode;
import com.gmail.ksherl.asm816.AsmException;
import com.gmail.ksherl.asm816.Error;
import com.gmail.ksherl.asm816.INSTR;
import com.gmail.ksherl.asm816.JunkPile;
import com.gmail.ksherl.asm816.Lexer;
import com.gmail.ksherl.asm816.Parser;
import com.gmail.ksherl.asm816.SymbolTable;
import com.gmail.ksherl.asm816.Token;
import com.gmail.ksherl.asm816.__TokenIterator;
import com.gmail.ksherl.asm816.ctype;
import com.gmail.ksherl.expression.ComplexExpression;
import com.gmail.ksherl.expression.Expression;
import com.gmail.ksherl.expression.RelExpression;
import com.gmail.ksherl.expression.__Expression;
import com.gmail.ksherl.mpw.MPWExpression;
import com.gmail.ksherl.omf.OMF;
import com.gmail.ksherl.omf.OMF_Opcode;
import com.gmail.ksherl.orca.Orca_Directive;

import uk.org.wookey.atari.utils.Logger;

public class WookeyParser extends Parser {
	private final static Logger _logger = new Logger(WookeyParser.class.getName());

	private boolean finished;
	private int lineNumber;
	private String fOpcode;
	
	private JunkPile fData;
	private SymbolTable fSymbols;
	private WookeyLexer fLexer;
	
	public WookeyParser() {
		super();
		fSymbols = new SymbolTable();
	}
	
	@Override
    public void ParseFile(InputStream stream) {
        WookeyLexer lex = new WookeyLexer(stream);
        Parse(lex);
    }   
    
    private void Parse(WookeyLexer lex) {
    	Token t;
        String lab;
        
        for(;;) {           
            try {
                t = lex.NextToken();
                lab = null;
                
                switch (t.Type()) {
                case Token.EOF:
                    return;
                    
                case Token.EOL:
                    continue;
                    
                case Token.SPACE:
                    break;
                    
                case Token.SYMBOL:
                    lab = t.toString();
                    
                    lex.Expect(Token.SPACE);
                    break;
                    
                default:
                    throw new AsmException(Error.E_UNEXPECTED, t);
                }
                
                // We should get a SYMBOL that's either an instruction or a directive
                t = lex.Expect(Token.SYMBOL);
                String s = t.toString().toUpperCase();
                INSTR instr = fOpcodes.get(s);
                if (instr != null) {
                	// It's an instruction
                	_logger.logInfo("Instruction");
                    ParseInstruction(lab, lex, instr);                    
                }
                else {
                	// It had better be a directive
                	_logger.logInfo("Directive?");
                }

            }
            catch (AsmException e)
            {
                // TODO -- check if fatal, update error count.
                e.print(System.err);
                lex.SkipLine();
            }

        }
   }
    
    protected void ParseLine(Lexer lex) {
        lineNumber = lex.Line();
        
        try {         
            _logger.logInfo("Parsing line #" + lineNumber);
            
            Token t = lex.Expect(Token.EOF, Token.EOL, Token.SPACE, Token.SYMBOL);
        
            switch (t.Type()) {
            case Token.EOF:
            	finished = true;
            	break;
            	
            case Token.EOL:
            	break;
            	
            case Token.SPACE:
            	break;
            	
            case Token.SYMBOL:
            	break;
            }
            
            _logger.logInfo("First token ok: " + t.detailString());
            lex.SkipLine();
        } catch (AsmException error) {
            error.SetLine(lineNumber);
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
    	   return null;
       }
        
       if (type == Token.EOF) {
    	   finished = true;
    	   return null;
       }
        
       if (type == Token.SYMBOL) {
    	   l.lab = t.toString();
    	   t = lex.Expect(Token.SPACE, Token.EOL);
            
    	   if (t.Type() == Token.EOL) {
    		   return l;
    	   }
       }
        
       t = lex.Expect(Token.SYMBOL);
        
       l.opcode = t.toString().toUpperCase();
       l.operand = lex.Arguments(true);

       return l;
    }
    
    void DoLine(String lab, String opcode, __TokenIterator ti) throws AsmException
    {
        fPC = fData == null ? 0 : fData.Size();
                
        this.fOpcode = opcode;
        WookeyDirective d;
        if (opcode == null) {
            d = WookeyDirective.IMPLIED_ANOP;
        }
        else { 
        	d = (WookeyDirective)fDirectives.get(opcode);
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
        WookeyDirective dir = (WookeyDirective)d;
              
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
        
            
        case DC:
            ParseDC(ti, lab, DotSize('w'));
            break;
            
        case DCB:
            ParseDCB(ti, lab, DotSize('w'));
            break;
            
            
        case MACHINE:
            {              
                int i = ti.ExpectSymbol("M65816", "M65C02", "M6502");
                ti.Expect(Token.EOL);
                
                switch(i)
                {
                case 1:
                    fMachine = INSTR.m65816;
                    fM = fX = true;
                    break;
                case 2:
                    fMachine = INSTR.m65c02;
                    fM = fX = false;
                    break;
                case 3:
                    fMachine = INSTR.m6502;
                    fM = fX = false;
                    break;
                }          
            }
            break;
            
        case INCLUDE:
            ParseInclude(ti);
            break;
            
        default:
            return false;
        }        

        return true;
    }

    protected boolean ParseInstruction(String lab, INSTR instr, __TokenIterator ti) throws AsmException {
        int opcode = 0x00;
        int size;
        
        if (lab != null) {
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
            
            if (e != null) {
                e = e.Simplify(fSymbols, false);
            }
            
            if (check_a) {
                String oplab = e.toString();
                if (oplab != null 
                        && oplab.compareToIgnoreCase("a") == 0)
                    mode = AddressMode.IMPLIED;
            }
            opcode = -1;
            
            // TODO -- warn if we need to truncate a number.
            if (mode == AddressMode.ASSUMED_ABS) {
                Integer v = e.Value();
                if (v == null) {
                    opcode = instr.find_opcode(fMachine, AddressMode.ABS, AddressMode.ABSLONG, AddressMode.DP);
                }
                else {
                    int i = v.intValue();
                    if (i <= 0xff) {
                        opcode = instr.find_opcode(fMachine, AddressMode.DP, AddressMode.ABS, AddressMode.ABSLONG);
                    }
                    else if (i > 0xffff) {                       
                        opcode = instr.find_opcode(fMachine, AddressMode.ABSLONG, AddressMode.ABS, AddressMode.DP);
                    }
                    else { 
                        opcode = instr.find_opcode(fMachine, AddressMode.ABS, AddressMode.ABSLONG, AddressMode.DP);
                    }
                }
            }
            else if (mode == AddressMode.ASSUMED_ABS_X) {
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
            else if (mode == AddressMode.ASSUMED_ABS_Y) {
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
            else {
                opcode = instr.opcode(mode, fMachine);
            }
            
            if (opcode == -1) {
                throw new AsmException(Error.E_OPCODE, instr.toString());
            }
            size = INSTR.Size(opcode, fM, fX);

            fData.add8(opcode, 0);
            if (size > 1) {
                Expression ex = new Expression(e);
                ex.SetSize(size - 1);
                //TODO -- set type.
                if (INSTR.isBranch(opcode))
                    ex.SetType(OMF.OMF_RELEXPR);
                
                if (opcode == 0x22)
                    ex.SetType(OMF.OMF_LEXPR);
                
                fData.add(ex);
            }
        }
                
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

    private void AddLabel(String lab) {
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
