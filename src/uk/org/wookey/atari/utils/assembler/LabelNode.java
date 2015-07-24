package uk.org.wookey.atari.utils.assembler;

import uk.org.wookey.atari.utils.Logger;

public class LabelNode extends ExprNode {
	private final static Logger _logger = new Logger(LabelNode.class.getName());
	
	private String labelName;
	
	public LabelNode(String l) {
		super();
		
		labelName = l;
	}
	
	public int eval() {
		_logger.logError("Don't know the value of label '" + labelName + "'");
		return 0;
	}
	
	public String toString() {
		return "(LABEL '" + labelName + "')";
	}
}
