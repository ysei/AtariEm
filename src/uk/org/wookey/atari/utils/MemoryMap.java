package uk.org.wookey.atari.utils;

import java.util.HashMap;

public class MemoryMap {
	private HashMap<Integer, AddressLabel> memoryMap;
	
	public MemoryMap() {
		memoryMap = new HashMap<Integer, AddressLabel>();
	}
	
	public String getReadLabel(int address) {
		if (memoryMap.containsKey(address)) {
			return memoryMap.get(address).getReadLabel();
		}

		return "$" + Formatter.toHexString(address, 4);
	}
	
	public String getWriteLabel(int address) {
		if (memoryMap.containsKey(address)) {
			return memoryMap.get(address).getWriteLabel();
		}

		return "$" + Formatter.toHexString(address, 4);
	}
	
	public String getLabel(int address, boolean checkRead, boolean checkWrite) {
		String res = null;

		if (memoryMap.containsKey(address)) {
			AddressLabel lab = memoryMap.get(address);
			
			if (checkWrite) {
				res = lab.getWriteLabel();
			}
			
			if (checkRead && (res == null)) {
				res = lab.getReadLabel();
			}
		}
		
		if (res == null) {
			res = "$" + Formatter.toHexString(address, 4);
		}
		
		return res;
	}
}
