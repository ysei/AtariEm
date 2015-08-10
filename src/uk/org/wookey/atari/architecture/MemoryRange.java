package uk.org.wookey.atari.architecture;

import uk.org.wookey.atari.exceptions.MemoryRangeException;

public class MemoryRange implements Comparable<MemoryRange> {
	private int startAddress;
	private int endAddress;
	  
	private int length;
	  
	public MemoryRange(int startAddress, int endAddress) throws MemoryRangeException {
		if ((startAddress < 0) || (endAddress < 0)) {
			throw new MemoryRangeException("Addresses must be positive");
	    }
	  
	    if (startAddress >= endAddress) {
	    	throw new MemoryRangeException("End address must be greater than start address.");
	    }

	    this.startAddress = startAddress;
	    this.endAddress = endAddress;
	    this.length = endAddress - startAddress + 1;
	}
	  
	public MemoryRange(int size) throws MemoryRangeException {
		this(0, size-1);
	}

	public int startAddress() {
	    return startAddress;
	}

	public void setStartAddress(int addr) throws MemoryRangeException {
		if (startAddress < 0) {
			throw new MemoryRangeException("Addresses cannot be less than 0.");
		}

		startAddress = addr;
		endAddress = addr+length-1;
	}

	public int endAddress() {
	    return endAddress;
	}
	  
	public int length() {
		return length;
	}

	public boolean includes(int address) {
	    return ((address <= endAddress) && (address >= startAddress));
	}

	public boolean overlaps(MemoryRange other) {
	    return (this.includes(other.startAddress()) || other.includes(this.startAddress()));
	}
	  
	public void set(MemoryRange other) {
		if (this != other) {
			startAddress = other.startAddress;
			endAddress = other.endAddress;
			length = other.length;
		}
	}

	public int compareTo(MemoryRange other) {
		if (other == null) {
			throw new NullPointerException("Cannot compare to null.");
		}
	    
	    if (this == other) {
	      return 0;
	    }
	    
	    Integer thisStartAddr = new Integer(this.startAddress());
	    Integer thatStartAddr = new Integer(other.startAddress());
	    
	    return thisStartAddr.compareTo(thatStartAddr);
	}

	public String toString() {
	    StringBuffer desc = new StringBuffer("@");
	    desc.append(String.format("0x%04x", startAddress));
	    desc.append("-");
	    desc.append(String.format("0x%04x", endAddress));
	    return desc.toString();
	}
}