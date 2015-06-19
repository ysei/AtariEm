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

import com.loomcom.symon.devices.Device;
import com.loomcom.symon.exceptions.MemoryAccessException;
import com.loomcom.symon.exceptions.MemoryRangeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import uk.org.wookey.atari.utils.Logger;

/**
 * The Bus ties the whole thing together, man.
 */
public class Bus {
	private final static Logger _logger = new Logger("Bus");
    private int startAddress = 0x0000;
    private int endAddress = 0xffff;

    // The CPU
    private Cpu cpu;
    
    private ArrayList<Device> deviceList;

    public Bus(int busWidth) {
    	deviceList = new ArrayList<Device>();
        startAddress = 0;
        endAddress = (1 << busWidth) - 1;
        cpu = null;
        
        _logger.logSuccess("bus created and initialesed.");
    }

    public int startAddress() {
        return startAddress;
    }

    public int endAddress() {
        return endAddress;
    }
    
    /**
     * Add a device to the bus.
     *
     * @param device
     * @param priority
     * @throws MemoryRangeException
     */
    public void addDevice(Device device, int startAddress) throws MemoryRangeException {    
        MemoryRange range = device.getMemoryRange();
        if(range.startAddress() < this.startAddress || range.startAddress() > this.endAddress) {
            throw new MemoryRangeException("start address of device " + device.getName() + " does not fall within the address range of the bus");
        }
        
        if(range.endAddress() < this.startAddress || range.endAddress() > this.endAddress) {
            throw new MemoryRangeException("end address of device " + device.getName() + " does not fall within the address range of the bus");
        }

        // Work out where to put it in the list
        device.setAddress(startAddress);
        device.setBus(this);

        if (deviceList.size() == 0) {
        	_logger.logInfo("Adding device to empty bus");
        	deviceList.add(device);
        }
        else {
        	boolean inserted = false;
        	int nDevs = deviceList.size();
        	
        	_logger.logInfo("Adding device into a bus with other devices");
        	for (int i=0; i<nDevs; i++) {
        		MemoryRange r = deviceList.get(i).getMemoryRange();
        	
        		if (!inserted && (r.compareTo(range) > 0)) {
        			_logger.logInfo("Inserting device at position " + i + " in bus");
        			deviceList.add(i, device);
        			inserted = true;
        		}
        	}
        	
        	if (!inserted) {
        		_logger.logInfo("Adding device at end of bus");
        		deviceList.add(device);
        	}
        }
    }
    
    public void addCpu(Cpu cpu) {
        this.cpu = cpu;
        cpu.setBus(this);
    }

    public int read(int address) throws MemoryAccessException {
        Device d = deviceAt(address);
        
        if (d != null) {
        	//_logger.logInfo("Rd " + String.format("%04X", address) + " - its in device '" + d.toString() + "'.");
            return d.read(address) & 0xff;
        }
        else {
        	_logger.logWarn("Rd " + String.format("%04X", address) + " - no device.");
        }
        
        throw new MemoryAccessException("Bus read failed. No device at address " + String.format("$%04X", address));
    }

    public void write(int address, int value) throws MemoryAccessException {
        Device d = deviceAt(address);
        
        if (d != null) {
            d.write(address, value);
            return;
        }
        
        throw new MemoryAccessException("Bus write failed. No device at address " + String.format("$%04X", address));
    }

    public void assertIrq() {
        if (cpu != null) {
            cpu.assertIrq();
        }
    }

    public void clearIrq() {
        if (cpu != null) {
            cpu.clearIrq();
        }
    }

    public void assertNmi() {
        if (cpu != null) {
            cpu.assertNmi();
        }
    }

    public void clearNmi() {
        if (cpu != null) {
            cpu.clearNmi();
        }
    }

    public Cpu getCpu() {
        return cpu;
    }

    public void loadProgram(int... program) throws MemoryAccessException {
        int address = getCpu().getProgramCounter();
        int i = 0;
        for (int d : program) {
            write(address + i++, d);
        }
    }
    
    public void dumpState() {
    	_logger.logInfo("BUS DUMP:");
    	_logger.logInfo("  Start address: " + startAddress + ", End address: " + endAddress);
    	
    	if (cpu == null) {
    		_logger.logInfo("  No CPU");
    	}
    	else {
    		_logger.logInfo("  CPU: " + cpu.toString());
    	}
    	
    	if (deviceList.size() != 0) {
    		_logger.logInfo("  Devices:");

    		for (Device dev : deviceList) {
    			_logger.logInfo("    " + dev.toString());
    		}
    	}
    	else {
    		_logger.logInfo("  No devices attached");
    	}
    }
    
    private Device deviceAt(int address) {
    	for (Device dev : deviceList) {
    		if (dev.handlesAddress(address)) {
    			return dev;
    		}
    	}
    	
    	return null;
    }
    
    public void constructUI(JPanel panel) {
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	
    	for (Device dev: deviceList) {
    		if (dev.hasUI()) {
    			panel.add(dev.getUI());
    		}
    	}
    }
}