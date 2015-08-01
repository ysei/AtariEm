package uk.org.wookey.atari.utils.assembler;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.loomcom.symon.InstructionTable;

import uk.org.wookey.atari.exceptions.EOFException;
import uk.org.wookey.atari.exceptions.LabelExistsException;
import uk.org.wookey.atari.exceptions.NosuchLabelException;
import uk.org.wookey.atari.exceptions.RuntimeAssemblyException;
import uk.org.wookey.atari.exceptions.SyntaxException;
import uk.org.wookey.atari.utils.Formatter;
import uk.org.wookey.atari.utils.LabelTable;
import uk.org.wookey.atari.utils.Logger;

public class Parser extends SimpleParser {
	//private final static Logger _logger = new Logger(Parser.class.getName());
	private final static Logger _logger = new Logger("Parser");

	public final static String directives[] = {
		"org", "processor", "=", "equ",
		".word", ".byte", ".byt"
	}; 
	
	private int pc;
	
	private int passNumber;
	private boolean generatingCode;
	private boolean silentReplace;
	private int numHardErrors;
	private int numSoftErrors;
	private int anonLabelCounter;
	
	private Hashtable<String, Integer> instructions;
	
	private LabelTable labels;
	
	public Parser(List<LexerToken> tokens) {
		super(tokens);

		numHardErrors = 0;
		numSoftErrors = 0;
		pc = 0;
		
		labels = new LabelTable();
		instructions = new Hashtable<String, Integer>();
		
		passNumber = 0;
		silentReplace = false;
		generatingCode = false;
		anonLabelCounter = 0;
		
		for (String op: InstructionTable.opcodeNames) {
			if (op != null) {
				op = op.toLowerCase();

				if (!instructions.containsKey(op)) {
					instructions.put(op, 0);
				}
			}
		}
	}
	
	public void pass(boolean generate) {
		generatingCode = generate;
		
		pass();
	}
	
	public void pass() {
		boolean eof = false;
		
		passNumber++;
		rewindTokens();
		
		_logger.logInfo("Pass " + passNumber);
		
		silentReplace = (passNumber > 1);
		
		LexerToken t = peekToken();
		pc = 0;
		anonLabelCounter = 0;
		
		try {
			while (!eof) {
				try {
					parseLine();
				} catch (SyntaxException e) {
					t = currentToken();
					
					_logger.logError("Syntax error on line " + t.lineNumber + ", column " + t.column + ": " + e.getMessage(), e);
					skipTo(LexerTokenType.EOL);
					
					numHardErrors++;
				} catch (RuntimeAssemblyException e) {
					t = currentToken();
					
					_logger.logError("Runtime error on line " + t.lineNumber + ", column " + t.column + ": " + e.getMessage(), e);
					skipTo(LexerTokenType.EOL);
					
					numSoftErrors++;
				}

				t = peekToken();
				if (t.type == LexerTokenType.EOF) {
					eof = true;
				}
			}
		}
		catch (EOFException e) {
			_logger.logInfo("End of file.");
		}
	}
	
	public void parseLine() throws SyntaxException, EOFException, RuntimeAssemblyException {
		LexerToken t = getToken();
		
		if (t.type == LexerTokenType.EOL) {
			return;
		}
		else if (t.type == LexerTokenType.WHITESPACE) {
			instructionOrDirective(getToken());
		}
		else if (t.type == LexerTokenType.ATOM) {
			instructionOrDirective(getToken(), t);
		}
		else {
			throw new SyntaxException("Unexpected token: " + t.toString());
		}
		
		expectEOL("Unexpected token found - was expecting EOL");
	}
	
	public void instructionOrDirective(LexerToken t) throws SyntaxException, RuntimeAssemblyException, EOFException {
		instructionOrDirective(t, null);	
	}

	public void instructionOrDirective(LexerToken t, LexerToken label) throws SyntaxException, RuntimeAssemblyException, EOFException {
		if (isDirective(t) || (t.type == LexerTokenType.EQUALS)) {
			directive(t, label);	
		}
		else if (t.type == LexerTokenType.EOL) {
			// just a blank line - or maybe a label all by itself
			if (label != null) {
				try {
					labels.add(label.value, pc, silentReplace);
				} catch (LabelExistsException e) {
					throw new RuntimeAssemblyException("Label '" + label.value + "' already defined");
				}				
			}
			return;
		}
		else {
			// Assume its an instruction
			instruction(t, label);
		}
	}
	
	private void addLabel(String name, int val) throws RuntimeAssemblyException {
		if (name.equals("@")) {
			anonLabelCounter++;
			name = String.format("@%04d", anonLabelCounter);
		}
		
		try {
			labels.add(name, pc, silentReplace);
		} catch (LabelExistsException e) {
			throw new RuntimeAssemblyException("Label '" + name + "' already defined");
		}
	}
	
	private void instruction(LexerToken instruction, LexerToken label) throws SyntaxException, RuntimeAssemblyException, EOFException {
		if (label != null) {
			addLabel(label.value, pc);
		}
		
		//_logger.logInfo("Instruction: " + instruction.value);
		
		Instruction inst = lookup(instruction.value);
		if (inst == null) {
			throw new SyntaxException("Unknown instruction or directive '" + instruction.value + "'");
		}

		// Make some simple choices about what sort of instruction it is
		LexerToken t = getToken();
		
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
					logf(inst.name, inst.accumulator, "A");
					
					getToken(2);
				}
				else {		
					unGetToken();
					
					int val = evalExp();
					
					if (tokensAre(LexerTokenType.COMMA, LexerTokenType.ATOM)) {
						LexerToken atom = peekToken();
						
						if (atom.value.equalsIgnoreCase("x")) {
							//_logger.logInfo("X-VAL=" + val);
							if (val > 255) {
								// Non zero page
								if (inst.absoluteX == -1) {
									throw new SyntaxException("Instruction doesn't supprt Absolute,X addressing mode.");
								}
								
								emit(inst.absoluteX, lsb(val), msb(val));
								logf(inst.name, inst.absoluteX, String.format("%s,X", labels.addressToLabel(val)), lsb(val), msb(val));
							}
							else {
								// zero page
								if (inst.zeroPageX == -1) {
									if (inst.absoluteX == -1) {
										throw new SyntaxException("Instruction doesn't supprt Absolute,X or ZeroPage,X addressing mode.");
									}
									
									emit(inst.absoluteX, val, 0);
									logf(inst.name, inst.absoluteX, String.format("%s,X", labels.addressToLabel(val)), lsb(val), msb(val));
								}
								else {								
									emit(inst.zeroPageX, val);
									logf(inst.name, inst.zeroPageX, String.format("%s,X", labels.addressToLabel(val, true)), val);
								}
							}
							
							t = getToken(2);
						}
						else if (atom.value.equalsIgnoreCase("y")) {
							//_logger.logInfo("Y-VAL=" + val);
							if (val > 255) {
								// Non zero page
								if (inst.absoluteY == -1) {
									throw new SyntaxException("Instruction doesn't supprt Absolute,Y addressing mode.");
								}
									
								emit(inst.absoluteY, lsb(val), msb(val));
								logf(inst.name, inst.absoluteY, String.format("%s,Y", labels.addressToLabel(val)), lsb(val), msb(val));
							}
							else {
								// zero page
								if (inst.zeroPageY == -1) {
									if (inst.absoluteY == -1) {
										throw new SyntaxException("Instruction doesn't supprt Absolute,Y or ZeroPage,Y addressing mode.");
									}
										
									emit(inst.absoluteY, val, 0);
									logf(inst.name, inst.absoluteY, String.format("%s,Y", labels.addressToLabel(val)), lsb(val));
								}
								else {								
									emit(inst.zeroPageY, val);
									logf(inst.name, inst.zeroPageY, String.format("%s,Y", labels.addressToLabel(val, true)), val);
								}
							}	
							
							t = getToken(2);
						}
					}
					else {
						if (val > 255) {
							// absolute
							if (inst.absolute == -1) {
								throw new SyntaxException("Instruction doesn't supprt Absolute addressing mode.");
							}
							
							emit(inst.absolute, lsb(val), msb(val));
							logf(inst.name, inst.absolute, labels.addressToLabel(val), lsb(val), msb(val));
						}
						else {
							// zero page
							if (inst.zeroPage == -1) {
								if (inst.absolute == -1) {
									throw new SyntaxException("Instruction doesn't supprt Absolute or ZeroPage addressing mode.");
								}
								
								emit(inst.absolute, val, 0);
								logf(inst.name, inst.absolute, labels.addressToLabel(val), lsb(val), 0);
							}
							else {
								emit(inst.zeroPage, val);
								logf(inst.name, inst.zeroPage, labels.addressToLabel(val, true), val);
							}
						}
					}
				}
			}
		}
	}
	
	private void implicitOrAccumulatorInstruction(Instruction inst) throws SyntaxException {
		if ((inst.implicit == -1) && (inst.accumulator == -1)){
			throw new SyntaxException("Missing operand from non implicit/accumulator instruction.");
		}

		if (inst.implicit != -1) {
			emit(inst.implicit);
			logf(inst.name, inst.implicit);
		}
		else {
			emit(inst.accumulator);
			logf(inst.name, inst.accumulator, "A");
		}
	}

	
	private void logf(String name, int opcode, int... opnd) {
		logf(name, opcode, null, opnd);
	}
	
	private void logf(String name, int opcode, String operand, int... opnd) {
		System.out.printf("%04x: ", pc);
		
		System.out.printf("%02x ", opcode);
		for (int op: opnd) {
			System.out.printf("%02x ", op);
		}
		
		for (int i=opnd.length; i < 4; i++) {
			System.out.print("   ");
		}
		
		System.out.format("%-16s ", labels.addressToLabelString(pc));
		System.out.print(" " + name);
		
		if (operand != null) {
			System.out.print(" " + operand);
		}
		
		System.out.println();
	}
	
	private void immediateInstruction(Instruction inst) throws SyntaxException, RuntimeAssemblyException, EOFException {
		if (inst.immediate == -1) {
			throw new SyntaxException("Immediate mode not supported by this instruction");
		}
		
		int val = evalExp();
		if ((val & 0xff) != val) {
			throw new RuntimeAssemblyException("Immediate value " + val + " too big.");
		}
	
		emit(inst.immediate, val);
		logf(inst.name, inst.immediate, String.format("#%s", labels.addressToLabel(val)), val);
	}
	
	private void relativeInstruction(Instruction inst) throws SyntaxException, EOFException, RuntimeAssemblyException {
		unGetToken();
		
		int val = evalExp();
		int delta = 0;
		int nextpc = pc+2;
		
		//_logger.logInfo("RELATIVE. PC=" + pc + ", VAL=" + val);
		
		if (val > nextpc) {
			delta = val - nextpc;
			//_logger.logInfo("Forwards displacement: " + delta);
		}
		else {
			delta = -(nextpc - val);
			//_logger.logInfo("Backwards displacement: " + delta);
		}
		
		emit(inst.relative, delta);
		logf(inst.name, inst.relative, String.format("%s", labels.addressToLabel(val)), delta & 0xff);
	}
	
	private void indirectTypeInstruction(Instruction inst) throws SyntaxException, RuntimeAssemblyException, EOFException {
		int val = evalExp();
		LexerToken t = currentToken();
		
		if (tokensAre(LexerTokenType.COMMA, LexerTokenType.ATOM, LexerTokenType.RPAREN)) {
			LexerToken atom = peekToken(1);
			
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
			logf(inst.name, inst.indirectX, String.format("(%s,X)", labels.addressToLabel(val, true)), val);
			
			getToken(3);		
		}
		else if (tokensAre(LexerTokenType.RPAREN, LexerTokenType.COMMA, LexerTokenType.ATOM)) {
			LexerToken atom = peekToken(2);
			
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
			logf(inst.name, inst.indirectY, String.format("(%s),Y", labels.addressToLabel(val)), val);
			
			getToken(3);
		}
		else if (tokensAre(LexerTokenType.RPAREN)) {
			if (inst.indirect == -1) {
				throw new SyntaxException(inst.name + " does not support indirect addressing.");
			}

			emit(inst.indirect, lsb(val), msb(val));
			logf(inst.name, inst.indirect, String.format("(%s)", labels.addressToLabel(val)), lsb(val), msb(val));

			getToken();
		}
		else {
			throw new SyntaxException("unexpected token: " + t.toString());
		}
	}
	
	private void directive(LexerToken directive, LexerToken label) throws SyntaxException, RuntimeAssemblyException, EOFException {
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
			byteDirective(directive.value, label);
		}
		else if (directive.value.equalsIgnoreCase(".word")) {
			wordDirective(directive.value, label);
		}
		else {
			throw new SyntaxException("unimplemented directive: '" + directive.value.toLowerCase() + "'");
		}
	}
	
	private void processorDirective(String dName) throws SyntaxException, EOFException {
		LexerToken t = getToken();
		_logger.logInfo("PROCESSOR set to " + t.value);

		if ((t.type != LexerTokenType.ATOM) || (!t.value.equals("6502"))) {
			throw new SyntaxException("Unknown processor type: " + t.value);
		}
		
		t = getToken();
		if (t.type != LexerTokenType.EOL) {
			throw new SyntaxException("Badly formed PROCESSOR directive");
		}
	}
	
	private void equalsDirective(String dName, LexerToken label) throws SyntaxException, RuntimeAssemblyException, EOFException {
		if (label == null) {
			throw new SyntaxException(dName + " directive without label");
		}
		
		int val = evalExp();
		
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
	}
	
	private void orgDirective(String dName) throws SyntaxException, EOFException, RuntimeAssemblyException {
		pc = evalExp();
		
		LexerToken t = getToken();
		if (t.type != LexerTokenType.EOL) {
			throw new SyntaxException("Unexpected token found: " + t.toString());
		}
	}
	
	private void byteDirective(String dName, LexerToken label) throws SyntaxException, RuntimeAssemblyException, EOFException {
		boolean scanning = true;
		
		//lookAround();
		if (label != null) {
			try {
				labels.add(label.value, pc, silentReplace);
			} catch (LabelExistsException e) {
				throw new RuntimeAssemblyException("Label '" + label.value + "' already defined");
			}
		}
		
		while (scanning) {
			int byt = evalExp();
		
			if ((byt & 0xff) != byt) {
				throw new RuntimeAssemblyException("Overflow when processing " + dName + " directive");
			}
		
			emit(byt);
		
			//lookAround();
			if (currentToken().type != LexerTokenType.COMMA) {
				scanning = false;
			}
		}
		
		//lookAround();
	}

	private void wordDirective(String dName, LexerToken label) throws SyntaxException, RuntimeAssemblyException, EOFException {
		boolean scanning = true;
		
		if (label != null) {
			try {
				labels.add(label.value, pc, silentReplace);
			} catch (LabelExistsException e) {
				throw new RuntimeAssemblyException("Label '" + label.value + "' already defined");
			}
		}
		
		while (scanning) {
			int wrd = evalExp();
		
			if ((wrd & 0xffff) != wrd) {
				throw new RuntimeAssemblyException("Overflow while processing " + dName + " directive");
			}
		
			emit(lsb(wrd), msb(wrd));
		
			if (currentToken().type != LexerTokenType.COMMA) {
				scanning = false;
			}
		}
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
		return numHardErrors + numSoftErrors;
	}
	
	public int hardErrors() {
		return numHardErrors;
	}
	
	public int softErrors() {
		return numSoftErrors;
	}
	
	private int evalExp() throws SyntaxException, EOFException, RuntimeAssemblyException {
		ExprNode exp = expr();
		int res = 0;
		
		if (exp == null) {
			throw new SyntaxException("Null (empty); expression");
		}
		
		try {
			res = exp.eval();
		} catch (NoValueException e) {
			if (generatingCode) {
				throw new RuntimeAssemblyException(e.getMessage());
			}
		}
		
		return res;
	}

	private ExprNode expr() throws SyntaxException, EOFException {
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
			
			t = getToken();
			
			if (isOper(t)) {
				return new OpNode(t.type, res, expr());
			}
			else {
				return res;
			}
		}
		else {
			_logger.logInfo("Was expecting a Simple() - got: " + t.toString());
		}
		
		throw new SyntaxException("At bottom of Expr() - token is: " + t.toString());
	}

	private ExprNode simple() throws SyntaxException, EOFException {
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
		else if (t.type == LexerTokenType.PLABEL) {
			String label = String.format("@%04d", anonLabelCounter);
			exp = new LabelNode(label, labels);
		}
		else if (t.type == LexerTokenType.NLABEL) {
			String label = String.format("@%04d", anonLabelCounter+1);
			exp = new LabelNode(label, labels);
		}		
		else {
			throw new SyntaxException("Unexpected item in Simple() - " + t.toString());
		}
		
		return exp;
	}
	
	private boolean isSimple(LexerToken t) {
		return typeIs(t, LexerTokenType.DECIMAL, LexerTokenType.HEX, LexerTokenType.BINARY, 
				LexerTokenType.ATOM, LexerTokenType.PLABEL, LexerTokenType.NLABEL);
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
		_logger.logInfo("Labels:");
		
		for (String lab: labels.getSet()) {
			try {
				_logger.logInfo(Formatter.padString(lab,  24) + " -> " + Formatter.toHexString(labels.get(lab), 4));
			} catch (NosuchLabelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			
			if (generatingCode) {
				//_logger.logSuccess(Integer.toHexString(pc) + ": " + Formatter.toHexString(b,  2));
			}
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
