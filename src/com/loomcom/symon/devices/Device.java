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

package com.loomcom.symon.devices;

import com.loomcom.symon.Bus;
import com.loomcom.symon.MemoryRange;
import com.loomcom.symon.exceptions.MemoryAccessException;
import com.loomcom.symon.exceptions.MemoryRangeException;

import java.util.HashSet;
import java.util.Set;

/**
 * A memory-mapped IO Device.
 */

public abstract class Device implements Comparable<Device> {
    private MemoryRange range;
    private String name;
    private String labelPrefix;
    private Bus bus;

    /**
     * Listeners to notify on update.
     */
    private Set<DeviceChangeListener> deviceChangeListeners;

    public Device(MemoryRange range, String name) {
    	this.range = range;
    	this.name = labelPrefix = name;
    }
    
    public Device(MemoryRange range) {
    	this(range, null);
    }
    
    public Device(String name)
            throws MemoryRangeException {
    	this.range = new MemoryRange(0, 0);
        this.name = name;
        this.deviceChangeListeners = new HashSet<DeviceChangeListener>();
    }

    public Device() throws MemoryRangeException {
        this((String)null);
    }

    /* Methods required to be implemented by inheriting classes. */
    public abstract void write(int address, int data) throws MemoryAccessException;

    public abstract int read(int address) throws MemoryAccessException;

    public String toString() {
        return name + "@" + String.format("%04X-%04X", 
        			getMemoryRange().startAddress(),
        			getMemoryRange().endAddress());
    }

    public Bus getBus() {
        return this.bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getLabelPrefix() {
    	return labelPrefix;
    }
    
    public void setLabelPrefix(String newPrefix) {
    	labelPrefix = newPrefix;
    }

    public void registerListener(DeviceChangeListener listener) {
        deviceChangeListeners.add(listener);
    }

    public void notifyListeners() {
        for (DeviceChangeListener l : deviceChangeListeners) {
            l.deviceStateChanged();
        }
    }
    
    public void setRange(MemoryRange r) {
    	range.set(r);
    }
    
    public void setAddress(int addr) {
    	try {
			range.setStartAddress(addr);
		} catch (MemoryRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public int compareTo(Device other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare to null.");
        }
        if (this == other) {
            return 0;
        }

        return range.compareTo(other.range);
    }
    
    protected int addressOffset(int absoluteAddress) {
    	return absoluteAddress - range.startAddress();
    }

	public MemoryRange getMemoryRange() {
		return range;
	}
	
	public boolean handlesAddress(int address) {
		return this.getMemoryRange().includes(address);
	}
	
	public String getLabel(int address) {
       	address = addressOffset(address);

       	return labelPrefix + "+" + String.format("$%04X", address);
	}
}