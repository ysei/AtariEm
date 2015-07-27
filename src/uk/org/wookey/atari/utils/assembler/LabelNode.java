package uk.org.wookey.atari.utils.assembler;

import uk.org.wookey.atari.exceptions.NosuchLabelException;
import uk.org.wookey.atari.utils.LabelTable;
import uk.org.wookey.atari.utils.Logger;

public class LabelNode extends ExprNode {
	private final static Logger _logger = new Logger(LabelNode.class.getName());
	
	private LabelTable labels;
	private String labelName;
	
	public LabelNode(String l, LabelTable t) {
		super();
		
		labelName = l;
		labels = t;
	}
	
	public int eval() {
		int val = 0;
		try {
			val = labels.get(labelName);
			_logger.logInfo("Label '" + labelName + "' => " + val);
		} catch (NosuchLabelException e) {
			_logger.logError("No such label '" + labelName + "' assuming value of 0");
			val = 0;
		}
		
		return val;
	}
	
	public String toString() {
		return "(LABEL '" + labelName + "')";
	}
}
