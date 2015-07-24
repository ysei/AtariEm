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
		"org", "byt", "byte", "asc", "db", 
		"word", "dw", "include", "processor"
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
		_logger.logInfo("Down to the real work");
		
		LexerToken t = peekToken();
		errors = 0;
		pc = 0;
		
		while (t.type != LexerTokenType.EOF) {
			parseLine();
			_logger.logInfo("On to the next line");
			t = peekToken();
	    }
		
		_logger.logInfo("All done.");
	}
	
	public void parseLine() {
		LexerToken t = getToken();
		
		_logger.logInfo("Processing " + t.toString());
		
		if (t.type == LexerTokenType.EOF) {
			_logger.logInfo("End of file - stop parsing.");
		}
		else if (t.type == LexerTokenType.EOL) {
			_logger.logInfo("Blank line.");
		}
		else if (t.type == LexerTokenType.COMMENT) {
			_logger.logInfo("Comment - skip to end of line.");
			t = getToken();
			if (t.type != LexerTokenType.EOL) {
				_logger.logError("Expected next token to be an EOL - got: " + t.toString());
				errors++;
				gobble(LexerTokenType.EOL);
			}
		}		
		else if (t.type == LexerTokenType.WHITESPACE) {
			_logger.logInfo("Something without a label");
			//gobble(LexerTokenType.EOL);
			instructionOrDirective(getToken());
		}
		else if (t.type == LexerTokenType.ATOM) {
			_logger.logInfo("An atom of some sort: " + t.toString());

			t = getToken();
			if (t.type != LexerTokenType.WHITESPACE) {
				_logger.logError("Unexpected token: " + t.toString());
				gobble(LexerTokenType.EOL);
				errors++;
			}
			else {
				instructionOrDirective(getToken(), t);
			}
		}
		else {
			_logger.logError("Unexpected token: " + t.toString());
			gobble(LexerTokenType.EOL);
			errors++;
		}
	}
	
	public void instructionOrDirective(LexerToken t, LexerToken label) {
		instructionOrDirective(t);	
	}

	public void instructionOrDirective(LexerToken t) {
		if (isInstruction(t)) {
			_logger.logInfo("Instruction: " + t.value);
			gobble(LexerTokenType.EOL);
		}
		else if (isDirective(t)) {
			_logger.logInfo("Directive: " + t.value);
			directive(t);	
		}
		else {
			_logger.logInfo("Unexpected token: " + t.toString());
			gobble(LexerTokenType.EOL);
			errors++;
		}
	}
	
	private void directive(LexerToken directive) {
		if (directive.value.equalsIgnoreCase("org")) {
			int org = evalExp();
			_logger.logSuccess("ORG set to " + org);
		}
		else {
			_logger.logError("unimplemented directive: '" + directive.value.toLowerCase() + "'");
			gobble(LexerTokenType.EOL);
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
	
	private int evalExp() {
		_logger.logInfo("evalExp();");
		
		ExprNode exp = expr();
		
		if (exp == null) {
			_logger.logError("Null (empty); expression");
			return 0;
		}
		
		return exp.eval();
	}

	private ExprNode expr() {
		ExprNode res = null;
		boolean parsing = true;
		
		LexerToken t = peekToken();
		
		_logger.logInfo("Parsing expression");
		
		while (parsing) {
			_logger.logInfo("Look at " + t.toString());
			
			if (isSimple(t)) {
				res = simple();
				
				if (res == null) {
					_logger.logError("Simple(); returned null");
					return null;
				}
			}
			else if (isOper(t)) {
				t = getToken();
				_logger.logInfo("Parsing operator: " + t.toString());
				res = new OpNode(t.type, res, expr());
				parsing = false;
			}
			else if (t.type == LexerTokenType.WHITESPACE) {
				// ignore it
				_logger.logInfo("Skipping whitespace");
				t = getToken();
			}
			else {
				_logger.logInfo("Non expression token found: " + t.toString());
				parsing = false;
			}
			
			t = peekToken();
			_logger.logInfo("Next token is: " + t.toString());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		t = currentToken();
		if (typeIs(t, LexerTokenType.COMMENT, LexerTokenType.EOL)) {
			_logger.logInfo("Finished parsing expression successfully:" + res.toString());
		}
		else {
			_logger.logError("Unexpected token found: " + t.toString());
			res = null;
		}

		gobble(LexerTokenType.EOL);
		
		return res;
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
		else if (t.type == LexerTokenType.ATOM) {
			exp = new LabelNode(t.value);
		}
		else {
			_logger.logError("Unexpected item in Simple() - " + t.toString());
		}
		
		return exp;
	}
	
	private boolean isSimple(LexerToken t) {
		return typeIs(t, LexerTokenType.DECIMAL, LexerTokenType.HEX, LexerTokenType.ATOM);
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
