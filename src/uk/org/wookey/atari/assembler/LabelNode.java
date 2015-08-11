package uk.org.wookey.atari.assembler;

import uk.org.wookey.atari.exceptions.NosuchLabelException;
import uk.org.wookey.atari.labels.LabelTable;
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
	
	public int eval() throws NoValueException {
		int val = 0;
		try {
			val = labels.get(labelName);
			//_logger.logInfo("Label '" + labelName + "' => " + val);
		} catch (NosuchLabelException e) {
			//_logger.logError("No such label '" + labelName + "' assuming value of 0");
			throw new NoValueException("Unknown label '" + labelName + "' in expression");
		}
		
		return val;
	}
	
	public String toString() {
		return "(LABEL '" + labelName + "')";
	}
}
