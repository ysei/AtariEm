/*
 * Copyright (c) 2014 Seth J. Morabito <web@loomcom.com>
 *                    Maik Merten <maikmerten@googlemail.com>
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

package com.loomcom.symon.machines;

import com.loomcom.symon.devices.Acia;
import com.loomcom.symon.devices.Acia6850;
import com.loomcom.symon.devices.Memory;
import com.loomcom.symon.devices.SdController;
import com.loomcom.symon.exceptions.MemoryRangeException;

import java.io.File;
import java.io.IOException;

import uk.org.wookey.atari.utils.Logger;


public class MulticompMachine extends Machine {    
    private final static Logger _logger = new Logger(MulticompMachine.class.getName());
    
    // 56K of RAM from $0000 - $DFFF
    private static final int RAM_BASE = 0x0000;
    private static final int RAM_SIZE = 0xE000;

    // ACIA at $FFD0-$FFD1
    private static final int ACIA_BASE = 0xFFD0;

    // SD controller at $FFD8-$FFDF
    private static final int SD_BASE = 0xFFD8;

    // 8KB ROM at $E000-$FFFF
    private static final int ROM_BASE = 0xE000;
    private static final int ROM_SIZE = 0x2000;

    // The simulated peripherals	
    private Acia   acia;
    private Memory ram;
    private SdController sdController;
    private Memory rom;

    public MulticompMachine() {
    	super("Multicomp");	
    }
    
    public void addDevices() {
    	try {
    	this.ram = new Memory(RAM_SIZE, false);
        this.acia = new Acia6850();
        this.acia.setBaudRate(0);
        this.sdController = new SdController();

        bus.addCpu(cpu);
        bus.addDevice(ram, RAM_BASE);
        bus.addDevice(acia, ACIA_BASE);
        bus.addDevice(sdController, SD_BASE);
        
        // TODO: Make this configurable, of course.
        File romImage = new File("rom.bin");
        if (romImage.canRead()) {
            _logger.logInfo("Loading ROM image from file " + romImage);
            this.rom = Memory.makeROM(ROM_SIZE, romImage);
        } else {
            _logger.logInfo("Default ROM file " + romImage +
                        " not found, loading empty R/W memory image.");
            this.rom = Memory.makeRAM(ROM_SIZE - 1);
        }

        bus.addDevice(rom, ROM_BASE);
    	} catch (MemoryRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}