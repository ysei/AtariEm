package uk.org.wookey.atari.labels;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import uk.org.wookey.atari.exceptions.LabelExistsException;
import uk.org.wookey.atari.exceptions.NosuchLabelException;
import uk.org.wookey.atari.utils.Logger;

public class LabelTable {
	private final static Logger _logger = new Logger(LabelTable.class.getName());

	private TreeMap<String,Integer> labels;
	
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
	
	public boolean addressHasLabel(int address) {
		return false;
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

	public String addressToLabel(int addr) {
		return addressToLabel(addr, false);
	}

	public String addressToLabel(int addr, boolean zp) {
		for (String lab: labels.keySet()) {
			if (labels.get(lab) == addr) {
				return lab;
			}
		}
		
		if (zp) {
			return String.format("$%02x", addr);
		}

		return String.format("$%04x", addr);
	}
	
	public String addressToLabelString(int addr) {
		for (String lab: labels.keySet()) {
			if (labels.get(lab) == addr) {
				return lab;
			}
		}
		
		return "";
	}
	
	public Set<String> getSet() {
		return labels.keySet();
	}
}
