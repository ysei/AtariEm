package uk.org.wookey.atari.utils.disassembler;

import uk.org.wookey.atari.architecture.Bus;
import uk.org.wookey.atari.architecture.Cpu;
import uk.org.wookey.atari.architecture.InstructionData;
import uk.org.wookey.atari.exceptions.MemoryAccessException;

import com.loomcom.symon.machines.Machine;
import com.loomcom.symon.util.HexUtil;

public class Disassembler implements InstructionData {
	private Machine machine;
	private Cpu cpu;
	private Bus bus;
	
	public Disassembler(Machine m) {
		machine = m;
		cpu = machine.getCpu();
		bus = machine.getBus();
	}
	
	public DecodedInstruction disassemble(int addr) throws MemoryAccessException {
		DecodedInstruction inst = new DecodedInstruction();
        String mnemonic = opcodeNames[addr];

        if (mnemonic == null) {
            return inst;
        }
        
        inst.setOpcode(mnemonic);

        StringBuilder sb = new StringBuilder(mnemonic);

        switch (instructionModes[addr]) {
            case ABS:
                inst.setOperand(bus.getLabel(bus.readWord(addr+1)), 3);
                break;
                
            case ABX:
                inst.setOperand(bus.getLabel(bus.readWord(addr+1)) + ",X", 3);
                break;
                
            case ABY:
                inst.setOperand(bus.getLabel(bus.readWord(addr+1)) + ",Y", 3);
                break;
                
            case IMM:
            	inst.setOperand("#$" + HexUtil.byteToHex(bus.read(addr+1)), 2);
                break;
                
            case IND:
                inst.setOperand("(" + bus.getLabel(bus.readWord(addr+1)) + ")", 3);
                break;
                
            case XIN:
            	inst.setOperand("($" + HexUtil.byteToHex(bus.read(addr+1)) + ",X)", 2);
                break;
                
            case INY:
            	inst.setOperand("($" + HexUtil.byteToHex(bus.read(addr+1)) + "),Y", 2);
                break;
                
            case REL:
            case ZPG:
            	inst.setOperand("$" + HexUtil.byteToHex(bus.read(addr+1)), 2);
                break;
                
            case ZPX:
            	inst.setOperand("$" + HexUtil.byteToHex(bus.read(addr+1)) + ",X", 2);
                break;
                
            case ZPY:
            	inst.setOperand("$" + HexUtil.byteToHex(bus.read(addr+1)) + ",Y", 2);
                break;
        }

        return inst;
	}
}
