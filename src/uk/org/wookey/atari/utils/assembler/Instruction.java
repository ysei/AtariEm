package uk.org.wookey.atari.utils.assembler;

public class Instruction {
	public String name;
	
	public int implicit;
	public int accumulator;
	public int immediate;
	public int zeroPage;
	public int zeroPageX;
	public int zeroPageY;
	public int relative;
	public int absolute;
	public int absoluteX;
	public int absoluteY;
	public int indirect;
	public int indirectX;
	public int indirectY;
	
	public Instruction() {
		name = "???";
		
		implicit = 0;
		accumulator = 0;
		immediate = 0;
		zeroPage = 0;
		zeroPageX = 0;
		zeroPageY = 0;
		relative = 0;
		absolute = 0;
		absoluteX = 0;
		absoluteY = 0;
		indirect = 0;
		indirectX = 0;
		indirectY = 0;
	}
	
	public Instruction(String name, int imp, int acc, int imm, int zp, int zpx, int zpy, int rel, int abs, int absx, int absy, int ind, int indx, int indy) {
		this.name = name;
		
		implicit = imp;
		accumulator = acc;
		immediate = imm;
		zeroPage = zp;
		zeroPageX = zpx;
		zeroPageY = zpy;
		relative = rel;
		absolute = abs;
		absoluteX = absx;
		absoluteY = absy;
		indirect = ind;
		indirectX = indx;
		indirectY = indy;
	}
	
	public Instruction(String name, int imm, int zp, int zpx, int abs, int absx, int absy, int indx, int indy) {
		this();
		
		this.name = name;
		immediate = imm;
		zeroPage = zp;
		zeroPageX = zpx;
		absolute = abs;
		absoluteX = absx;
		absoluteY = absy;
		indirectX = indx;
		indirectY = indy;
	}

	public Instruction(String name, int acc, int zp, int zpx, int abs, int absx) {
		this();
		
		this.name = name;
		accumulator = acc;
		zeroPage = zp;
		zeroPageX = zpx;
		absolute = abs;
		absoluteX = absx;
	}
	
	public Instruction(String name, int zp, int zpx, int abs, int absx) {
		this();
		
		this.name = name;
		zeroPage = zp;
		zeroPageX = zpx;
		absolute = abs;
		absoluteX = absx;
	}

	public Instruction(String name, int imm, int zp, int abs) {
		this();
		
		this.name = name;
		immediate = imm;
		zeroPage = zp;
		absolute = abs;
	}

	public Instruction(String name, int zp, int abs) {
		this();
		
		this.name = name;
		zeroPage = zp;
		absolute = abs;
	}

	public Instruction(String name, int rel) {
		this();
		
		this.name = name;
		relative = rel;
	}
}
