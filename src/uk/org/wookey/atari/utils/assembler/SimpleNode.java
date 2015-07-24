package uk.org.wookey.atari.utils.assembler;

public class SimpleNode extends ExprNode {
	private int val;
	
	public SimpleNode(int val) {
		super();
		this.val = val;
	}
	
	public int eval() {
		return val;
	}
	
	public String toString() {
		return "(SIMPLE '" + val + "')";
	}
}
