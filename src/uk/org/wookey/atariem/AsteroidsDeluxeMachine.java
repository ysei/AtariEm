package uk.org.wookey.atariem;

import java.io.File;
import java.util.logging.Logger;

import com.loomcom.symon.Bus;
import com.loomcom.symon.Cpu;
import com.loomcom.symon.devices.Acia;
import com.loomcom.symon.devices.Acia6551;
import com.loomcom.symon.devices.Crtc;
import com.loomcom.symon.devices.Memory;
import com.loomcom.symon.devices.Pia;
import com.loomcom.symon.devices.Via6522;
import com.loomcom.symon.exceptions.MemoryRangeException;
import com.loomcom.symon.machines.Machine;
import com.loomcom.symon.machines.SymonMachine;

public class AsteroidsDeluxeMachine implements Machine { 
    private final static Logger logger = Logger.getLogger(SymonMachine.class.getName());
    
    // Constants used by the simulated system. These define the memory map.
    private static final int BUS_BOTTOM = 0x0000;
    private static final int BUS_TOP    = 0xffff;

    // 32K of RAM from $0000 - $7FFF
    private static final int MEMORY_BASE = 0x0000;
    private static final int MEMORY_SIZE = 0x8000;

    // PIA at $8000-$800F

    private static final int PIA_BASE = 0x8000;

    // ACIA at $8800-$8803
    private static final int ACIA_BASE = 0x8800;

    // CRTC at $9000-$9001
    private static final int CRTC_BASE = 0x9000;

    // 16KB ROM at $C000-$FFFF
    private static final int ROM_BASE = 0xC000;
    private static final int ROM_SIZE = 0x4000;


        // The simulated peripherals
    private final Bus    bus;
    private final Cpu    cpu;
    private final Acia   acia;
    private final Pia    pia;
    private final Crtc   crtc;
    private final Memory ram;
    private       Memory rom;


    public AsteroidsDeluxeMachine() throws Exception {
        this.bus = new Bus(BUS_BOTTOM, BUS_TOP);
        this.cpu = new Cpu();
        this.ram = new Memory(MEMORY_BASE, MEMORY_BASE + MEMORY_SIZE - 1, false);
        this.pia = new Via6522(PIA_BASE);
        this.acia = new Acia6551(ACIA_BASE);
        this.crtc = new Crtc(CRTC_BASE, ram);

        bus.addCpu(cpu);
        bus.addDevice(ram);
        bus.addDevice(pia);
        bus.addDevice(acia);
        bus.addDevice(crtc);
        
        // TODO: Make this configurable, of course.
        File romImage = new File("rom.bin");
        if (romImage.canRead()) {
            logger.info("Loading ROM image from file " + romImage);
            this.rom = Memory.makeROM(ROM_BASE, ROM_BASE + ROM_SIZE - 1, romImage);
        } else {
            logger.info("Default ROM file " + romImage +
                        " not found, loading empty R/W memory image.");
            this.rom = Memory.makeRAM(ROM_BASE, ROM_BASE + ROM_SIZE - 1);
        }

        bus.addDevice(rom);
        
    }

    @Override
    public Bus getBus() {
        return bus;
    }

    @Override
    public Cpu getCpu() {
        return cpu;
    }

    @Override
    public Memory getRam() {
        return ram;
    }

    @Override
    public Acia getAcia() {
        return acia;
    }

    @Override
    public Pia getPia() {
        return pia;
    }

    @Override
    public Crtc getCrtc() {
        return crtc;
    }

    @Override
    public Memory getRom() {
        return rom;
    }
    
    public void setRom(Memory rom) throws MemoryRangeException {
        if(this.rom != null) {
            bus.removeDevice(this.rom);
        }
        this.rom = rom;
        bus.addDevice(this.rom);
    }

    @Override
    public int getRomBase() {
        return ROM_BASE;
    }

    @Override
    public int getRomSize() {
        return ROM_SIZE;
    }

    @Override
    public int getMemorySize() {
        return MEMORY_SIZE;
    }

    @Override
    public String getName() {
        return "Symon";
    }
}
