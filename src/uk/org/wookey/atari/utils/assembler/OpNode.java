package uk.org.wookey.atari.utils.assembler;

import uk.org.wookey.atari.utils.Logger;

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
	
	public int eval() throws NoValueException {
		int res = 0;
		
		switch (op) {
		case '+':
			res = lhs.eval() + rhs.eval();
			break;
			
		case '-':
			res = lhs.eval() - rhs.eval();
			break;
			
		default:
			throw new NoValueException("Unknown operator '" + op + "' when evaluating expression");
		}

		return res;
	}
	
	
	public String toString() {
		return "(OP " + lhs.toString() + " " + op + " " + rhs.toString() + ")";
	}

}
