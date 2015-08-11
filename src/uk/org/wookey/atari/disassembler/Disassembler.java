package uk.org.wookey.atari.disassembler;

import uk.org.wookey.atari.architecture.Bus;
import uk.org.wookey.atari.architecture.InstructionData;
import uk.org.wookey.atari.exceptions.MemoryAccessException;
import uk.org.wookey.atari.utils.Formatter;

public class Disassembler implements InstructionData {
	private Bus bus;
	
	public Disassembler(Bus b) {
		bus = b;
	}
	
	public DecodedInstruction disassemble(int addr) throws MemoryAccessException {
		DecodedInstruction inst = new DecodedInstruction();
		int op = bus.read(addr);
        String mnemonic = opcodeNames[op];

        if (mnemonic == null) {
            return inst;
        }
        
        inst.setOpcode(mnemonic);

        StringBuilder sb = new StringBuilder(mnemonic);

        switch (instructionModes[op]) {
        	case ACC:
        		break;
        		
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
            	inst.setOperand("#$" + Formatter.toHexString(bus.read(addr+1), 2), 2);
                break;
                
            case IND:
                inst.setOperand("(" + bus.getLabel(bus.readWord(addr+1)) + ")", 3);
                break;
                
            case XIN:
            	inst.setOperand("($" + Formatter.toHexString(bus.read(addr+1), 2) + ",X)", 2);
                break;
                
            case INY:
            	inst.setOperand("($" + Formatter.toHexString(bus.read(addr+1), 2) + "),Y", 2);
                break;
                
            case REL:
            case ZPG:
            	inst.setOperand("$" + Formatter.toHexString(bus.read(addr+1), 2), 2);
                break;
                
            case ZPX:
            	inst.setOperand("$" + Formatter.toHexString(bus.read(addr+1), 2) + ",X", 2);
                break;
                
            case ZPY:
            	inst.setOperand("$" + Formatter.toHexString(bus.read(addr+1), 2) + ",Y", 2);
                break;
        }

        return inst;
	}
}
