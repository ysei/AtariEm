package uk.org.wookey.atari.utils.disassembler;

public class DecodedInstruction {
	private String opcode;
	private String operand;
	private int length;
	
	public DecodedInstruction() {
		this("?", "");
	}
	
	public DecodedInstruction(String op, String opnd) {
		opcode = op;
		operand = opnd;
		length = 1;
	}
	
	public void setOpcode(String op) {
		opcode = op;
	}
	
	public void setOperand(String opnd) {
		operand = opnd;
	}
	
	public void setOperand(String opnd, int l) {
		operand = opnd;
		length = l;
	}
	
	public void setLength(int l) {
		length = l;
	}
}
