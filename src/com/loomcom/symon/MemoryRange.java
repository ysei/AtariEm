/*
 * Copyright (c) 2014 Seth J. Morabito <web@loomcom.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.loomcom.symon;

import com.loomcom.symon.exceptions.*;

/**
 * MemoryRange is a simple container class representing a literal
 * range of memory, with a staraddress, and an end address.  It has
 * guards against creating impossible memory ranges, and implements
 * some useful methods for checking address inclusion and range
 * overlaps.
 */
public class MemoryRange implements Comparable<MemoryRange> {

  /** The starting address of the memory range. */
  private int startAddress;
  
  /** The ending address of the memory range. */
  private int endAddress;
  
  private int length;
  
  public MemoryRange(int startAddress, int endAddress)
		throws MemoryRangeException {
    if (startAddress < 0 || endAddress < 0) {
      throw new MemoryRangeException("Addresses cannot be less than 0.");
    }
  
    if (startAddress >= endAddress) {
      throw new MemoryRangeException("End address must be greater " +
                                     "than start address.");
    }

    this.startAddress = startAddress;
    this.endAddress = endAddress;
    this.length = endAddress - startAddress + 1;
  }
  
  public MemoryRange(int size) throws MemoryRangeException {
	  this(0, size-1);
  }

  /**
   * @returns the starting address.
   */
  public int startAddress() {
    return startAddress;
  }

  public void setStartAddress(int addr) throws MemoryRangeException {
	  if (startAddress < 0) {
		  throw new MemoryRangeException("Addresses cannot be less than 0.");
	  }

	  startAddress = addr;
	  length = endAddress - startAddress + 1;
	  endAddress = addr+length;
  }

  /**
   * @returns the ending address.
   */
  public int endAddress() {
    return endAddress;
  }
  
  public void setEndAddress(int addr) throws MemoryRangeException {
	  if (endAddress < 0) {
		  throw new MemoryRangeException("Addresses cannot be less than 0.");
	  }
	    
	  endAddress = addr;
	  length = endAddress - startAddress + 1;
  }

  /**
   * @returns the length of the range
   */
  public int length() {
	  return endAddress - startAddress + 1;
  }

  /**
   * Checks for address inclusion in the range.
   *
   * @returns true if the address is included within this range,
   *          false otherwise.
   */
  public boolean includes(int address) {
    return (address <= endAddress &&
            address >= startAddress);
  }

  /**
   * Checks for overlapping memory ranges.
   *
   * @returns true if this range overlaps in any way with the other.
   */
  public boolean overlaps(MemoryRange other) {
    return (this.includes(other.startAddress()) ||
            other.includes(this.startAddress()));
  }
  
  public void set(MemoryRange other) {
	  if (this != other) {
		  startAddress = other.startAddress;
		  endAddress = other.endAddress;
		  length = other.length;
	  }
  }

  // Implementation of Comparable interface
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