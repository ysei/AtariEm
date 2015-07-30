package uk.org.wookey.atari.utils;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import uk.org.wookey.atari.exceptions.LabelExistsException;
import uk.org.wookey.atari.exceptions.NosuchLabelException;

public class LabelTable {
	private final static Logger _logger = new Logger(LabelTable.class.getName());

	TreeMap<String,Integer> labels;
	
	public LabelTable() {
		labels = new TreeMap<String, Integer>();
	}
	
	public void add(String label, int val, boolean silentReplace) throws LabelExistsException {
		// Do we already have a label with that name?
		if (labels.containsKey(label) && !silentReplace) {
			throw new LabelExistsException("Label '" + label + "' already in table");
		}
		else {
			//_logger.logInfo("add label '" + label + "'=" + val);
			labels.put(label, val);
		}
	}
	
	public boolean labelExists(String label) {
		return labels.containsKey(label);
	}
	
	public int get(String label) throws NosuchLabelException {
		if (labels.containsKey(label)) {
			return labels.get(label);
		}
		
		throw new NosuchLabelException("No label '" + label + "' in label table");
	}
	
	public int getWithDefault(String label, int deflt) {
		int val;
		try {
			val = get(label);
		} catch (NosuchLabelException e) {
			val = deflt;
		}
		
		return val;
	}
	
	public Set<String> getSet() {
		return labels.keySet();
	}
}
