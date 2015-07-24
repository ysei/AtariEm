package uk.org.wookey.atari.utils.assembler;

import uk.org.wookey.atari.utils.Logger;
import uk.org.wookey.atari.utils.lexer.LexerTokenType;

public class OpNode extends ExprNode {
	private final static Logger _logger = new Logger(OpNode.class.getName());
	
	private char op;
	
	public OpNode(LexerTokenType type, ExprNode l, ExprNode r) {
		super();
		
		if (type == LexerTokenType.PLUS) {
			op = '+';
		}
		else if (type == LexerTokenType.MINUS) {
			op = '-';
		}
		
		lhs = l;
		rhs = r;
	}
	
	public OpNode(char op, ExprNode l, ExprNode r) {
		this.op = op;
		
		lhs = l;
		rhs = r;
	}
	
	public int eval() {
		switch (op) {
		case '+':
			return lhs.eval() + rhs.eval();
			
		case '-':
			return lhs.eval() - rhs.eval();
		}
		
		_logger.logError("Unknown operator '" + op + "' when evaluating expression");
		return 0;
	}
	
	
	public String toString() {
		return "(OP " + lhs.toString() + " " + op + " " + rhs.toString() + ")";
	}

}
