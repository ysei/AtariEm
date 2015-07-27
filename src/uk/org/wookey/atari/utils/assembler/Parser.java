package uk.org.wookey.atari.utils.assembler;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.loomcom.symon.InstructionTable;

import uk.org.wookey.atari.exceptions.EOFException;
import uk.org.wookey.atari.exceptions.LabelExistsException;
import uk.org.wookey.atari.exceptions.RuntimeAssemblyException;
import uk.org.wookey.atari.exceptions.SyntaxException;
import uk.org.wookey.atari.utils.LabelTable;
import uk.org.wookey.atari.utils.Logger;
import uk.org.wookey.atari.utils.lexer.LexerToken;
import uk.org.wookey.atari.utils.lexer.LexerTokenType;

public class Parser extends SimpleParser {
	private final static Logger _logger = new Logger(Parser.class.getName());

	public final static String directives[] = {
		"org", "processor", "=",
		".word", ".byte", ".byt"
	}; 
	
	private int errors;	
	private int pc;
	
	private Hashtable<String, Integer> instructions;
	
	private LabelTable labels;
	
	public Parser(List<LexerToken> tokens) {
		super(tokens);

		errors = 0;
		pc = 0;
		
		labels = new LabelTable();
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
		
		dumpLabels();
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
			instruction(t, label);
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
	
	private void instruction(LexerToken instruction, LexerToken label) throws SyntaxException, RuntimeAssemblyException {
		if (label != null) {
			try {
				labels.add(label.value, pc);
			} catch (LabelExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		_logger.logInfo("Instruction: " + instruction.value);
		
		Instruction inst = lookup(instruction.value);
		if (inst == null) {
			throw new SyntaxException("Unknown instruction '" + instruction.value + "'");
		}
		
		LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
		if (t.type == LexerTokenType.EOL) {
			if (inst.implicit != -1) {
				_logger.logSuccess("CODE: $" + Integer.toHexString(inst.implicit));
				pc++;
			}
			else if (inst.accumulator != -1) {
				_logger.logSuccess("CODE: $" + Integer.toHexString(inst.accumulator));
				pc++;
			}
			else {
				throw new SyntaxException("Missing operand.");
			}
		}
		else if (t.type == LexerTokenType.HASH) {
			if (inst.immediate != -1) {
				int val = evalExp();
				if ((val & 0xff) != val) {
					throw new SyntaxException("Immediate value " + val + " too big.");
				}
				_logger.logSuccess("CODE $" + Integer.toHexString(inst.immediate) + " $" + Integer.toHexString(val));
				pc += 2;
			}
			else {
				throw new SyntaxException("Immediate mode not supported by this instruction");
			}
		}
		else {
			_logger.logInfo("Token is: " + t.toString());
			unGetToken();
			if (inst.relative != -1) {
				int val = evalExp();

				pc += 2;
				int displacement = val - pc;
				_logger.logSuccess("CODE $" + Integer.toHexString(inst.relative) + " $" + Integer.toHexString(displacement));
			}
			else {
				if (t.type == LexerTokenType.LPAREN) {
					_logger.logInfo("Some kind of indirect");

					t = getToken();
					
					int val = evalExp();
					LexerToken after = peekToken();
					
					_logger.logInfo("Token after expression: " + after.toString());

					pc += 3;
				}
				else {
					int val = evalExp();
					int bytes = 3;
					
					LexerToken after = peekToken();
					_logger.logInfo("Token after expression: " + after.toString());
					
					if (val < 256) {
						// zero page - if instruction supports it
						if ((inst.zeroPage != -1) || 
								(inst.zeroPageX != -1) || 
								(inst.zeroPageY != -1)) {
							bytes = 2;
						}
					}
					/*
					public int accumulator;
					public int zeroPage;
					public int zeroPageX;
					public int zeroPageY;
					public int absolute;
					public int absoluteX;
					public int absoluteY;
					*/
					
					pc += bytes;
				}
			}
		}
		
		if (currentToken().type != LexerTokenType.EOL) {
			gobble(LexerTokenType.EOL);
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
			
			if (label.value.equals("*")) {
				pc = val;
				_logger.logInfo("PC set to " + val + " via '=' directive");
			}
			else {
				try {
					labels.add(label.value, val);
				} catch (LabelExistsException e) {
					// TODO Auto-generated catch block
					_logger.logError("Failed to add label cos one already exists!", e);
				}
				_logger.logInfo("LABEL '" + label.value + "' set to " + val);
			}
			
			LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
			if (t.type != LexerTokenType.EOL) {
				throw new SyntaxException("Badly formed = directive");
			}
		}
		else if (directive.value.equalsIgnoreCase(".byte") || directive.value.equalsIgnoreCase(".byt")) {
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
			
			t = getToken(LexerTokenType.WHITESPACE, LexerTokenType.COMMENT);
			
			if (isOper(t)) {
				return new OpNode(t.type, res, expr());
			}
			else {
				unGetToken();
				return res;
			}
		}
		else {
			_logger.logInfo("Was expecting a Simple() - got: " + t.toString());
		}
		
		throw new SyntaxException("At bottom of Expr() - token is: " + t.toString());
	}

	private ExprNode simple() throws SyntaxException {
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
			exp = new LabelNode(t.value, labels);
		}
		else {
			throw new SyntaxException("Unexpected item in Simple() - " + t.toString());
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
	
	private void dumpLabels() {
		Iterator<Map.Entry<String, Integer>> it = labels.getSet().iterator();

		_logger.logInfo("Labels:");
		
		while (it.hasNext()) {
			Map.Entry<String, Integer> entry = it.next();

			_logger.logInfo(entry.getKey() + " -> " + Integer.toHexString(entry.getValue()));
		}
	}
	
	private Instruction lookup(String inst) {
		for (Instruction i: InstructionTable.instructions) {
			if (inst.equalsIgnoreCase(i.name)) {
				return i;
			}
		}
		
		return null;
	}
}
