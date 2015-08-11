package uk.org.wookey.atari.architecture;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import uk.org.wookey.atari.exceptions.MemoryAccessException;
import uk.org.wookey.atari.exceptions.MemoryRangeException;
import uk.org.wookey.atari.labels.LabelTable;
import uk.org.wookey.atari.ui.CollapsablePanel;
import uk.org.wookey.atari.utils.Logger;

public class Bus {
	private final static Logger _logger = new Logger("Bus");
    private int startAddress = 0x0000;
    private int endAddress = 0xffff;

    private Cpu cpu;
    private LabelTable labels;
    
    private ArrayList<Device> deviceList;
    
    private JPanel ui;

    public Bus(int busWidth, LabelTable labs) {
    	deviceList = new ArrayList<Device>();
        startAddress = 0;
        endAddress = (1 << busWidth) - 1;
        cpu = null;
        labels = labs;
        
        ui = new JPanel();
    	ui.setLayout(new BoxLayout(ui, BoxLayout.Y_AXIS));
    	ui.setAlignmentX(Component.LEFT_ALIGNMENT); 	
       
        _logger.logSuccess("bus created and initialesed.");
    }

    public int startAddress() {
        return startAddress;
    }

    public int endAddress() {
        return endAddress; 
    }
    
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
    
    public String getLabel(int address) {
    	// The global label table takes priority
    	String res = labels.addressToLabelString(address);
    	
    	if (res != "") {
    		return res;
    	}
    	
        Device d = deviceAt(address);
        
        if (d != null) {
            return d.getLabel(address);
        }

        return String.format("$$$%04X", address);
    }

    public int readWord(int address) throws MemoryAccessException {
    	return read(address) | (read(address+1)>>8);
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

    public void writeWord(int address, int wordVal) throws MemoryAccessException {
    	write(address, wordVal & 0xff);
    	write(address+1, (wordVal<<8) & 0xff);
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
    
    public void constructUI() {
    	for (Device dev: deviceList) {
    		if (dev.hasUI()) {
    			CollapsablePanel coll = new CollapsablePanel(dev.getName(), dev.getUI());
    			ui.add(coll);
    		}
    	}
    }
    
    public JPanel getUI() {
    	return ui;
    }
}