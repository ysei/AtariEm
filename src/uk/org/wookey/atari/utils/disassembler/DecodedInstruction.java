package uk.org.wookey.atari.utils.disassembler;

public class DecodedInstruction {
	private String opcode;
	
	public DecodedInstruction() {
		this("?");
	}
	
	public DecodedInstruction(String op) {
		opcode = op;
	}
	
	public void setOpcode(String op) {
		opcode = op;
	}
}
