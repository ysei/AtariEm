package uk.org.wookey.atari.utils.assembler;

import java.util.Hashtable;
import java.util.List;

import com.loomcom.symon.InstructionTable;

import uk.org.wookey.atari.utils.Logger;
import uk.org.wookey.atari.utils.lexer.LexerToken;
import uk.org.wookey.atari.utils.lexer.LexerTokenType;

public class Parser extends SimpleParser {
	private final static Logger _logger = new Logger(Parser.class.getName());

	public final static String directives[] = {
		"org", "processor", "=",
		".word", ".byte"
	}; 
	
	private int errors;	
	private int pc;
	
	private Hashtable<String, Integer> instructions;	
	
	public Parser(List<LexerToken> tokens) {
		super(tokens);

		errors = 0;
		pc = 0;
		
		instructions = new Hashtable<String, Integer>();
		
		for (String op: InstructionTable.opcodeNames) {
			if (op != null) {
				op = op.toLowerCase();

				if (!instructions.containsKey(op)) {
					instructions.put(op, 0);
				}
			}
		}
	}
	
	public void pass() {
		LexerToken t = peekToken();
		errors = 0;
		pc = 0;
		
		try {
			while (t.type != LexerTokenType.EOF) {
				try {
					parseLine();
				} catch (SyntaxException e) {
					t = currentToken();
					
					_logger.logError("Syntax error on line " + t.lineNumber + ", column " + t.column + ": " + e.getMessage(), e);
					if (t.type != LexerTokenType.EOL) {
						gobble(LexerTokenType.EOL);
					}
					
					errors++;
				} catch (RuntimeAssemblyException e) {
					t = currentToken();
					
					_logger.logError("Runtime error on line " + t.lineNumber + ", column " + t.column + ": " + e.getMessage(), e);
					if (t.type != LexerTokenType.EOL) {
						gobble(LexerTokenType.EOL);
					}
					
					errors++;
				}

				t = peekToken();
			}
		}
		catch (EOFException e) {
			_logger.logInfo("End of file exception.");
		}
	}
	
	public void parseLine() throws SyntaxException, EOFException, RuntimeAssemblyException {
		LexerToken t = getToken(LexerTokenType.COMMENT);
		
		if (t.type == LexerTokenType.EOF) {
			throw new EOFException();
		}
		
		if (t.type == LexerTokenType.EOL) {
			return;
		}

		if (t.type == LexerTokenType.WHITESPACE) {
			instructionOrDirective(getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE));
			return;
		}
		
		if (t.type == LexerTokenType.ATOM) {
			instructionOrDirective(getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE), t);
			return;
		}
		
		errors++;
		throw new SyntaxException("Unexpected token: " + t.toString());
	}
	
	public void instructionOrDirective(LexerToken t) throws SyntaxException, RuntimeAssemblyException {
		instructionOrDirective(t, null);	
	}

	public void instructionOrDirective(LexerToken t, LexerToken label) throws SyntaxException, RuntimeAssemblyException {
		if (isInstruction(t)) {
			_logger.logInfo("Instruction: " + t.value);
			gobble(LexerTokenType.EOL);
		}
		else if (isDirective(t)) {
			directive(t, label);	
		}
		else if (t.type == LexerTokenType.EOL) {
			// just another blank line;
			return;
		}
		else {
			_logger.logInfo("Unexpected token: " + t.toString());
			gobble(LexerTokenType.EOL);
			errors++;
		}
	}
	
	private void directive(LexerToken directive, LexerToken label) throws SyntaxException, RuntimeAssemblyException {
		if (directive.value.equalsIgnoreCase("org")) {
			pc = evalExp();
			_logger.logSuccess("ORG set to " + pc);
			
			LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
			if (t.type != LexerTokenType.EOL) {
				throw new SyntaxException("Unexpected token found: " + t.toString());
			}
		}
		else if (directive.value.equalsIgnoreCase("processor")) {
			LexerToken t = getToken(LexerTokenType.WHITESPACE);
			_logger.logInfo("PROCESSOR set to " + t.value);
			
			t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
			if (t.type != LexerTokenType.EOL) {
				throw new SyntaxException("Badly formed PROCESSOR directive");
			}
		}
		else if (directive.value.equalsIgnoreCase("=")) {
			if (label == null) {
				throw new SyntaxException("= directive without label");
			}
			
			int val = evalExp();
			
			_logger.logInfo("LABEL '" + label.value + "' set to " + val);
			
			LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
			if (t.type != LexerTokenType.EOL) {
				throw new SyntaxException("Badly formed = directive");
			}
		}
		else if (directive.value.equalsIgnoreCase(".byte")) {
			boolean scanning = true;
			
			while (scanning) {
				int byt = evalExp();
			
				if ((byt & 0xff) != byt) {
					throw new RuntimeAssemblyException("Overflow in .BYTE directive");
				}
			
				_logger.logSuccess(".BYTE " + byt);
			
				LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
				
				if (t.type != LexerTokenType.COMMA) {
					scanning = false;
				}
			}
			
			if (currentToken().type != LexerTokenType.EOL) {
				throw new SyntaxException("Unexpected token found: " + currentToken().toString());
			}
		}
		else if (directive.value.equalsIgnoreCase(".word")) {
			boolean scanning = true;
			
			while (scanning) {
				int byt = evalExp();
			
				if ((byt & 0xffff) != byt) {
					throw new RuntimeAssemblyException("Overflow in .WORD directive");
				}
			
				_logger.logSuccess(".WORD " + byt);
			
				LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
				
				if (t.type != LexerTokenType.COMMA) {
					scanning = false;
				}
			}
			
			if (currentToken().type != LexerTokenType.EOL) {
				throw new SyntaxException("Unexpected token found: " + currentToken().toString());
			}
		}
		else {
			throw new SyntaxException("unimplemented directive: '" + directive.value.toLowerCase() + "'");
		}
	}
	
	private boolean isInstruction(LexerToken t) {
		return instructions.containsKey(t.value.toLowerCase());
	}
	
	private boolean isDirective(LexerToken t) {
		for (String directive: directives) {
			if (directive.equalsIgnoreCase(t.value)) {
				return  true;
			}
		}
		
		return false;
	}	
	
	public int errors() {
		return errors;
	}
	
	private int evalExp() throws SyntaxException {
		ExprNode exp = expr();
		
		if (exp == null) {
			throw new SyntaxException("Null (empty); expression");
		}
		
		return exp.eval();
	}

	private ExprNode expr() throws SyntaxException {
		ExprNode res = null;
		
		LexerToken t = peekToken();
		
		while (t.type == LexerTokenType.WHITESPACE) {
			t = getToken();
			t = peekToken();
		}
			
		if (isSimple(t)) {
			res = simple();
			
			if (res == null) {
				_logger.logError("Simple(); returned null");
				return null;
			}
			
			t = getToken(LexerTokenType.WHITESPACE);
			
			if (isOper(t)) {
				return new OpNode(t.type, res, expr());
			}
			else {
				unGetToken();
				return res;
			}
		}
		
		throw new SyntaxException("THING HAPPENED");
	}

	private ExprNode simple() {
		ExprNode exp = null;
		
		LexerToken t = getToken();
		
		if (t.type == LexerTokenType.DECIMAL) {
			exp = new SimpleNode(Integer.parseInt(t.value));
		}
		else if (t.type == LexerTokenType.HEX) {
			exp = new SimpleNode(Integer.parseInt(t.value, 16));
		}
		else if (t.type == LexerTokenType.BINARY) {
			exp = new SimpleNode(Integer.parseInt(t.value, 2));
		}
		else if (t.type == LexerTokenType.ATOM) {
			exp = new LabelNode(t.value);
		}
		else {
			_logger.logError("Unexpected item in Simple() - " + t.toString());
		}
		
		return exp;
	}
	
	private boolean isSimple(LexerToken t) {
		return typeIs(t, LexerTokenType.DECIMAL, LexerTokenType.HEX, LexerTokenType.BINARY, LexerTokenType.ATOM);
	}
	
	private boolean isOper(LexerToken t) {
		return typeIs(t, LexerTokenType.PLUS, LexerTokenType.MINUS);
	}
	
	private boolean typeIs(LexerToken t, LexerTokenType... types) {
		for (LexerTokenType type: types) {
			if (t.type == type) {
				return true;
			}
		}
		
		return false;
	}
}
