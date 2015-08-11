package uk.org.wookey.atari.machines;

import java.io.File;
import java.io.IOException;

import uk.org.wookey.atari.devices.Memory;
import uk.org.wookey.atari.exceptions.MemoryRangeException;
import uk.org.wookey.atari.utils.Logger;

public class AsteroidsDeluxeMachine extends Machine {
    private final static Logger _logger = new Logger("AD Machine");
    
    // 1K of RAM from $0000 - $03FF
    private static final int RAM_BASE = 0x0000;
    private static final int RAM_SIZE = 0x0400;

    // 8KB ROM at $6000-$7FFF
    // Rom is ghosted as $E000-$FFFF
    private static final int ROM_BASE = 0x6000;
    private static final int ROM_SIZE = 0x2000;
    
    // The simulated peripherals
    private Memory ram;
    private Memory rom;

    public AsteroidsDeluxeMachine() {
    	super("Asteroids Deluxe");
    }

	public void addDevices() {
        try {
			this.ram = new Memory(RAM_SIZE, false);

	        bus.addCpu(cpu);
	        bus.addDevice(ram, RAM_BASE);

	        File romImage = new File("machines/" + this.getName() + "/rom.bin");
	        if (romImage.canRead()) {
	            _logger.logInfo("Loading ROM image from file " + romImage);
	            this.rom = Memory.makeROM(ROM_SIZE, romImage);
	        } else {
	            _logger.logInfo("Default ROM file " + romImage +
	                        " not found, loading empty R/W memory image.");
	            this.rom = Memory.makeRAM(ROM_SIZE);
	        }

	        bus.addDevice(rom, ROM_BASE);
	        bus.addDevice(rom, ROM_BASE+0x8000);
		} catch (MemoryRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
