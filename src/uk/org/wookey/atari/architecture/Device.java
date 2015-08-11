package uk.org.wookey.atari.architecture;

import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import uk.org.wookey.atari.exceptions.MemoryAccessException;
import uk.org.wookey.atari.exceptions.MemoryRangeException;

public abstract class Device implements Comparable<Device> {
    private MemoryRange range;
    private String name;
    private String labelPrefix;
    private Bus bus;
    
    protected JPanel ui;

    /**
     * Listeners to notify on update.
     */
    private Set<DeviceChangeListener> deviceChangeListeners;

    public Device(MemoryRange range, String name) {
    	this.range = range;
    	this.name = labelPrefix = name;
    	
    	ui = null;
    	if (hasUI()) {
            ui = new JPanel();
            //ui.setBorder(new TitledBorder(getName()));
            
            ui.setLayout(new BoxLayout(ui, BoxLayout.Y_AXIS));
            ui.setAlignmentX(0);
    	}
    }
    
    public Device(MemoryRange range) {
    	this(range, null);
    }
    
    public Device(String name) throws MemoryRangeException {
    	this.range = new MemoryRange(0, 0);
        this.name = name;
        this.deviceChangeListeners = new HashSet<DeviceChangeListener>();
    }

    public Device() throws MemoryRangeException {
        this((String)null);
    }
    
    public Device(Device src) {
    	name = src.name;
    	labelPrefix = src.labelPrefix;
    	bus = src.bus;

    	range = new MemoryRange(src.range);
    	ui = null;
    	if (hasUI()) {
            ui = new JPanel();
            //ui.setBorder(new TitledBorder(getName()));
            
            ui.setLayout(new BoxLayout(ui, BoxLayout.Y_AXIS));
            ui.setAlignmentX(0);
    	}
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
	
	public boolean hasUI() {
		return false;
	}
	
	public JPanel getUI() {
		return ui;
	}
}