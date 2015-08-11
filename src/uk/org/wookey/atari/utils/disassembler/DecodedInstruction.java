package uk.org.wookey.atari.utils.disassembler;

public class DecodedInstruction {
	private String opcode;
	private String operand;
	
	public DecodedInstruction() {
		this("?", "");
	}
	
	public DecodedInstruction(String op, String opnd) {
		opcode = op;
		operand = opnd;
	}
	
	public void setOpcode(String op) {
		opcode = op;
	}
	
	public void setOperand(String opnd) {
		operand = opnd;
	}
}
