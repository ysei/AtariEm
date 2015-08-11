package uk.org.wookey.atari.assembler;

public class ExprNode {
	public ExprNode lhs;
	public ExprNode rhs;

	public ExprNode() {
		lhs = rhs = null;
	}
	
	public int eval() throws NoValueException {
		return 0;
	}
	
	public String toString() {
		return "()";
	}
}
