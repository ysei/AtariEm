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
	//private final static Logger _logger = new Logger(Parser.class.getName());
	private final static Logger _logger = new Logger("Parser");

	public final static String directives[] = {
		"org", "processor", "=", "equ",
		".word", ".byte", ".byt"
	}; 
	
	private int errors;	
	private int pc;
	
	private int passNumber;
	private boolean silentReplace;
	
	private Hashtable<String, Integer> instructions;
	
	private LabelTable labels;
	
	public Parser(List<LexerToken> tokens) {
		super(tokens);

		errors = 0;
		pc = 0;
		
		labels = new LabelTable();
		instructions = new Hashtable<String, Integer>();
		
		passNumber = 0;
		silentReplace = false;
		
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
		passNumber++;
		rewindTokens();
		
		_logger.logInfo("Pass " + passNumber);
		
		silentReplace = (passNumber > 1);
		
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
		LexerToken t = getToken();
		
		if (t.type == LexerTokenType.EOF) {
			throw new EOFException();
		}
		
		if (t.type == LexerTokenType.EOL) {
			return;
		}

		if (t.type == LexerTokenType.WHITESPACE) {
			instructionOrDirective(getToken());
		}
		else if (t.type == LexerTokenType.ATOM) {
			instructionOrDirective(getToken(), t);
		}
		else {
			errors++;
			throw new SyntaxException("Unexpected token: " + t.toString());
		}
		
		gobble(LexerTokenType.EOL);
	}
	
	public void instructionOrDirective(LexerToken t) throws SyntaxException, RuntimeAssemblyException {
		instructionOrDirective(t, null);	
	}

	public void instructionOrDirective(LexerToken t, LexerToken label) throws SyntaxException, RuntimeAssemblyException {
		if (isInstruction(t)) {
			instruction(t, label);
		}
		else if (isDirective(t) || (t.type == LexerTokenType.EQUALS)) {
			directive(t, label);	
		}
		else if (t.type == LexerTokenType.EOL) {
			// just another blank line;
			return;
		}
		else {
			_logger.logInfo("Unexpected token: " + t.toString());
			t = skipUpto(LexerTokenType.EOL, LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
			errors++;
		}
	}
	
	private void instruction(LexerToken instruction, LexerToken label) throws SyntaxException, RuntimeAssemblyException {
		if (label != null) {
			try {
				labels.add(label.value, pc, silentReplace);
			} catch (LabelExistsException e) {
				throw new RuntimeAssemblyException("Label '" + label.value + "' already defined");
			}
		}
		
		_logger.logInfo("Instruction: " + instruction.value);
		
		Instruction inst = lookup(instruction.value);
		if (inst == null) {
			throw new SyntaxException("Unknown instruction '" + instruction.value + "'");
		}

		// Make some simple choices about what sort of instruction it is
		LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
		
		if (t.type == LexerTokenType.EOL) {
			implicitOrAccumulatorInstruction(inst);
		}
		else if (t.type == LexerTokenType.HASH) {
			immediateInstruction(inst);
		}
		else if (t.type == LexerTokenType.LPAREN) {
			indirectTypeInstruction(inst);
		}
		else {
			if (inst.relative != -1) {
				relativeInstruction(inst);
			}
			else {
				// Might be accumulator
				if (t.type == LexerTokenType.ATOM && t.value.equalsIgnoreCase("a")) {
					// Accumulator
					if (inst.accumulator == -1) {
						throw new SyntaxException("Instruction does not support Accumulator addressing mode.");
					}
					
					emit(inst.accumulator);
					
					getToken();
				}
				else {		
					unGetToken();
					
					int val = evalExp();
					
					LexerToken after = peekToken();
					_logger.logSuccess("Token after expression: " + after.toString());
					
					if (tokensAre(LexerTokenType.COMMA, LexerTokenType.ATOM)) {
						LexerToken atom = peekToken(2);
						
						if (atom.value.equalsIgnoreCase("x")) {
							_logger.logInfo("X-VAL=" + val);
							if (val > 255) {
								// Non zero page
								if (inst.absoluteX == -1) {
									throw new SyntaxException("Instruction doesn't supprt Absolute,X addressing mode.");
								}
								
								emit(inst.absoluteX, lsb(val), msb(val));
							}
							else {
								// zero page
								if (inst.zeroPageX == -1) {
									if (inst.absoluteX == -1) {
										throw new SyntaxException("Instruction doesn't supprt Absolute,X or ZeroPage,X addressing mode.");
									}
									
									emit(inst.absoluteX, val, 0);
								}
								else {								
									emit(inst.zeroPageX, val);
								}
							}
							
							t = getToken(2);
						}
						else if (atom.value.equalsIgnoreCase("y")) {
							_logger.logInfo("Y-VAL=" + val);
							if (val > 255) {
								// Non zero page
								if (inst.absoluteY == -1) {
									throw new SyntaxException("Instruction doesn't supprt Absolute,Y addressing mode.");
								}
									
								emit(inst.absoluteY, lsb(val), msb(val));
							}
							else {
								// zero page
								if (inst.zeroPageY == -1) {
									if (inst.absoluteY == -1) {
										throw new SyntaxException("Instruction doesn't supprt Absolute,Y or ZeroPage,Y addressing mode.");
									}
										
									emit(inst.absoluteY, val, 0);
								}
								else {								
									emit(inst.zeroPageY, val);
								}
							}	
							
							t = getToken(2);
						}
					}
					else {
						_logger.logInfo("VAL=" + val);

						if (val > 255) {
							// absolute
							if (inst.absolute == -1) {
								throw new SyntaxException("Instruction doesn't supprt Absolute addressing mode.");
							}
							
							emit(inst.absolute, lsb(val), msb(val));
						}
						else {
							// zero page
							if (inst.zeroPage == -1) {
								if (inst.absolute == -1) {
									throw new SyntaxException("Instruction doesn't supprt Absolute or ZeroPage addressing mode.");
								}
								
								emit(inst.absolute, val, 0);
							}
							else {
								emit(inst.zeroPage, val);
							}
						}
					}
				}
			}
		}
		
		t = skipUpto(LexerTokenType.EOL, LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
/*		if (currentToken().type != LexerTokenType.EOL) {
			gobble(LexerTokenType.EOL);
		} */
	}
	
	private void implicitOrAccumulatorInstruction(Instruction inst) throws SyntaxException {
		if ((inst.implicit == -1) && (inst.accumulator == -1)){
			throw new SyntaxException("Missing operand from non implicit/accumulator instruction.");
		}

		if (inst.implicit != -1) {
			emit(inst.implicit);
		}
		else {
			emit(inst.accumulator);
		}
	}
	
	private void immediateInstruction(Instruction inst) throws SyntaxException, RuntimeAssemblyException {
		if (inst.immediate == -1) {
			throw new SyntaxException("Immediate mode not supported by this instruction");
		}
		
		int val = evalExp();
		if ((val & 0xff) != val) {
			throw new RuntimeAssemblyException("Immediate value " + val + " too big.");
		}
	
		emit(inst.immediate, val);
	}
	
	private void relativeInstruction(Instruction inst) throws SyntaxException {
		_logger.logSuccess("RELATIVE. Token is " + currentToken().toString());
		unGetToken();
		
		int val = evalExp();

		int displacement = val - pc;
		emit(inst.relative, displacement);
	}
	
	private void indirectTypeInstruction(Instruction inst) throws SyntaxException, RuntimeAssemblyException {
		_logger.logWarn("An indirect instruction of some sort");
		
		int val = evalExp();
		LexerToken t = peekToken();
		
		if (tokensAre(LexerTokenType.COMMA, LexerTokenType.ATOM, LexerTokenType.RPAREN)) {
			LexerToken atom = peekToken(2);
			
			if (!atom.value.equalsIgnoreCase("x")) {
				throw new SyntaxException("Unknown indirection");
			}
			
			// the expression must refer to page zero
			if (val > 255) {
				throw new RuntimeAssemblyException("Indirect X expression must refer to page zero");
			}
			
			if (inst.indirectX == -1) {
				throw new SyntaxException(inst.name + " does not support (indirect,X) addressing.");
			}
			
			emit(inst.indirectX, val);
			
			getToken(3);		
		}
		else if (tokensAre(LexerTokenType.RPAREN, LexerTokenType.COMMA, LexerTokenType.ATOM)) {
			LexerToken atom = peekToken(3);
			
			if (!atom.value.equalsIgnoreCase("y")) {
				throw new SyntaxException("Unknown indirection");
			}

			// the expression must refer to page zero
			if (val > 255) {
				throw new RuntimeAssemblyException("Indirect Y expression must refer to page zero");
			}
			
			if (inst.indirectY == -1) {
				throw new SyntaxException(inst.name + " does not support (indirect),Y addressing.");
			}

			emit(inst.indirectY, val);
			
			getToken(3);
		}
		else if (tokensAre(LexerTokenType.RPAREN)) {
			if (inst.indirect == -1) {
				throw new SyntaxException(inst.name + " does not support indirect addressing.");
			}

			emit(inst.indirect, lsb(val), msb(val));
			
			getToken();
		}
		else {
			_logger.logError("unexpected token: " + t.toString());
		}
		
		t = skipUpto(LexerTokenType.EOL, LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
	}
	
	private void directive(LexerToken directive, LexerToken label) throws SyntaxException, RuntimeAssemblyException {
		if (directive.value.equalsIgnoreCase("org")) {
			orgDirective(directive.value);
		}
		else if (directive.value.equalsIgnoreCase("processor")) {
			processorDirective(directive.value);
		}
		else if (directive.type == LexerTokenType.EQUALS || directive.value.equalsIgnoreCase("equ")) {
			equalsDirective(directive.value, label);
		}
		else if (directive.value.equalsIgnoreCase(".byte") || directive.value.equalsIgnoreCase(".byt")) {
			byteDirective(directive.value);
		}
		else if (directive.value.equalsIgnoreCase(".word")) {
			wordDirective(directive.value);
		}
		else {
			throw new SyntaxException("unimplemented directive: '" + directive.value.toLowerCase() + "'");
		}
		
		LexerToken t = skipUpto(LexerTokenType.EOL, LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
	}
	
	private void processorDirective(String dName) throws SyntaxException {
		LexerToken t = getToken(LexerTokenType.WHITESPACE);
		_logger.logInfo("PROCESSOR set to " + t.value);

		if ((t.type != LexerTokenType.ATOM) || (!t.value.equals("6502"))) {
			throw new SyntaxException("Unknown processor type: " + t.value);
		}
		
		t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
		if (t.type != LexerTokenType.EOL) {
			throw new SyntaxException("Badly formed PROCESSOR directive");
		}
	}
	
	private void equalsDirective(String dName, LexerToken label) throws SyntaxException, RuntimeAssemblyException {
		if (label == null) {
			throw new SyntaxException(dName + " directive without label");
		}
		
		int val = evalExp();
		
		_logger.logSuccess("EQU: " + label.value + "=" + val);
		
		if (label.value.equals("*")) {
			pc = val;
		}
		else {
			try {
				labels.add(label.value, val, silentReplace);
			} catch (LabelExistsException e) {
				throw new RuntimeAssemblyException("Label '" + label.value + "' already defined");
			}
		}

		LexerToken t = skipUpto(LexerTokenType.EOL, LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
	}
	
	private void orgDirective(String dName) throws SyntaxException {
		pc = evalExp();
		_logger.logSuccess("ORG set to " + pc);
		
		LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
		if (t.type != LexerTokenType.EOL) {
			throw new SyntaxException("Unexpected token found: " + t.toString());
		}
	}
	
	private void byteDirective(String dName) throws SyntaxException, RuntimeAssemblyException {
		boolean scanning = true;
		
		while (scanning) {
			int byt = evalExp();
		
			if ((byt & 0xff) != byt) {
				throw new RuntimeAssemblyException("Overflow when processing " + dName + " directive");
			}
		
			emit(byt);
		
			LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
			
			if (t.type != LexerTokenType.COMMA) {
				scanning = false;
			}
		}
		
		if (currentToken().type != LexerTokenType.EOL) {
			throw new SyntaxException("Unexpected token found: " + currentToken().toString());
		}
	}

	private void wordDirective(String dName) throws SyntaxException, RuntimeAssemblyException {
		boolean scanning = true;
		
		while (scanning) {
			int wrd = evalExp();
		
			if ((wrd & 0xffff) != wrd) {
				throw new RuntimeAssemblyException("Overflow while processing " + dName + " directive");
			}
		
			emit(lsb(wrd), msb(wrd));
		
			LexerToken t = getToken(LexerTokenType.COMMENT, LexerTokenType.WHITESPACE);
			
			if (t.type != LexerTokenType.COMMA) {
				scanning = false;
			}
		}
		
		if (currentToken().type != LexerTokenType.EOL) {
			throw new SyntaxException("Unexpected token found: " + currentToken().toString());
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
	
	public void dumpLabels() {
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
	
	private void emit(int... bytes) {
		for (int b: bytes) {
			b = b & 0xff;
			
			_logger.logSuccess(Integer.toHexString(pc) + ": " + Integer.toHexString(b));
			pc++;
		}
	}
	
	private int lsb(int wrd) {
		return wrd & 0xff;
	}
	
	private int msb(int wrd) {
		return (wrd>>8) & 0xff;
	}
}
