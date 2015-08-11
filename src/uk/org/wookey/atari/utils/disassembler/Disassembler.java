package uk.org.wookey.atari.utils.disassembler;

import uk.org.wookey.atari.architecture.Bus;
import uk.org.wookey.atari.architecture.Cpu;
import uk.org.wookey.atari.architecture.InstructionData;

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
	
	public DecodedInstruction disassemble(int addr) {
		DecodedInstruction inst = new DecodedInstruction();
        String mnemonic = opcodeNames[addr];

        if (mnemonic == null) {
            return inst;
        }
        
        inst.setOpcode(mnemonic);

        StringBuilder sb = new StringBuilder(mnemonic);

        switch (instructionModes[addr]) {
            case ABS:
                inst.setOperand(bus.getLabel(bus.readWord(addr+1)));
                break;
            case ABX:
                inst.setOperand(bus.getLabel(bus.readWord(addr+1)) + ",X");
                break;
            case ABY:
                inst.setOperand(bus.getLabel(bus.readWord(addr+1)) + ",Y");
                break;
            case IMM:
                sb.append(" #$" + HexUtil.byteToHex(args[0]));
                break;
            case IND:
                inst.setOperand("(" + bus.getLabel(bus.readWord(addr+1)) + ")");
                break;
            case XIN:
                sb.append(" ($" + HexUtil.byteToHex(args[0]) + ",X)");
                break;
            case INY:
                sb.append(" ($" + HexUtil.byteToHex(args[0]) + "),Y");
                break;
            case REL:
            case ZPG:
                sb.append(" $" + HexUtil.byteToHex(args[0]));
                break;
            case ZPX:
                sb.append(" $" + HexUtil.byteToHex(args[0]) + ",X");
                break;
            case ZPY:
                sb.append(" $" + HexUtil.byteToHex(args[0]) + ",Y");
                break;
        }

        return inst;
	}
}
