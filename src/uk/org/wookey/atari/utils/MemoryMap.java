package uk.org.wookey.atari.utils;

import java.util.HashMap;

import com.loomcom.symon.util.HexUtil;

public class MemoryMap {
	private HashMap<Integer, AddressLabel> memoryMap;
	
	public MemoryMap() {
		memoryMap = new HashMap<Integer, AddressLabel>();
	}
	
	public String getReadLabel(int address) {
		if (memoryMap.containsKey(address)) {
			return memoryMap.get(address).getReadLabel();
		}

		return "$" + HexUtil.wordToHex(address);
	}
	
	public String getWriteLabel(int address) {
		if (memoryMap.containsKey(address)) {
			return memoryMap.get(address).getWriteLabel();
		}

		return "$" + HexUtil.wordToHex(address);
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
			res = "$" + HexUtil.wordToHex(address);
		}
		
		return res;
	}
}
