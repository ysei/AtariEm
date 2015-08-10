package uk.org.wookey.atari.architecture;

import uk.org.wookey.atari.utils.assembler.Instruction;

public interface InstructionData {
    public enum CpuBehavior {
        NMOS_WITH_ROR_BUG,
        NMOS_WITH_INDIRECT_JMP_BUG,
        NMOS_WITHOUT_INDIRECT_JMP_BUG,
        CMOS
    }

    public enum Mode {
        ACC {
            public String toString() {
                return "Accumulator";
            }
        },
        ABS {
            public String toString() {
                return "Absolute";
            }
        },
        ABX {
            public String toString() {
                return "Absolute, X-indexed";
            }
        },
        ABY {
            public String toString() {
                return "Absolute, Y-indexed";
            }
        },
        IMM {
            public String toString() {
                return "Immediate";
            }
        },
        IMP {
            public String toString() {
                return "Implied";
            }
        },
        IND {
            public String toString() {
                return "Indirect";
            }
        },
        XIN {
            public String toString() {
                return "X-indexed Indirect";
            }
        },
        INY {
            public String toString() {
                return "Indirect, Y-indexed";
            }
        },
        REL {
            public String toString() {
                return "Relative";
            }
        },
        ZPG {
            public String toString() {
                return "Zeropage";
            }
        },
        ZPX {
            public String toString() {
                return "Zeropage, X-indexed";
            }
        },
        ZPY {
            public String toString() {
                return "Zeropage, Y-indexed";
            }
        },
        NUL {
            public String toString() {
                return "NULL";
            }
        }
    }

    public static final String[] opcodeNames = {
        "BRK", "ORA",  null,  null,  null, "ORA", "ASL",  null,
        "PHP", "ORA", "ASL",  null,  null, "ORA", "ASL",  null,
        "BPL", "ORA",  null,  null,  null, "ORA", "ASL",  null,
        "CLC", "ORA",  null,  null,  null, "ORA", "ASL",  null,
        "JSR", "AND",  null,  null, "BIT", "AND", "ROL",  null,
        "PLP", "AND", "ROL",  null, "BIT", "AND", "ROL",  null,
        "BMI", "AND",  null,  null,  null, "AND", "ROL",  null,
        "SEC", "AND",  null,  null,  null, "AND", "ROL",  null,
        "RTI", "EOR",  null,  null,  null, "EOR", "LSR",  null,
        "PHA", "EOR", "LSR",  null, "JMP", "EOR", "LSR",  null,
        "BVC", "EOR",  null,  null,  null, "EOR", "LSR",  null,
        "CLI", "EOR",  null,  null,  null, "EOR", "LSR",  null,
        "RTS", "ADC",  null,  null,  null, "ADC", "ROR",  null,
        "PLA", "ADC", "ROR",  null, "JMP", "ADC", "ROR",  null,
        "BVS", "ADC",  null,  null,  null, "ADC", "ROR",  null,
        "SEI", "ADC",  null,  null,  null, "ADC", "ROR",  null,
        "BCS", "STA",  null,  null, "STY", "STA", "STX",  null,
        "DEY",  null, "TXA",  null, "STY", "STA", "STX",  null,
        "BCC", "STA",  null,  null, "STY", "STA", "STX",  null,
        "TYA", "STA", "TXS",  null,  null, "STA",  null,  null,
        "LDY", "LDA", "LDX",  null, "LDY", "LDA", "LDX",  null,
        "TAY", "LDA", "TAX",  null, "LDY", "LDA", "LDX",  null,
        "BCS", "LDA",  null,  null, "LDY", "LDA", "LDX",  null,
        "CLV", "LDA", "TSX",  null, "LDY", "LDA", "LDX",  null,
        "CPY", "CMP",  null,  null, "CPY", "CMP", "DEC",  null,
        "INY", "CMP", "DEX",  null, "CPY", "CMP", "DEC",  null,
        "BNE", "CMP",  null,  null,  null, "CMP", "DEC",  null,
        "CLD", "CMP",  null,  null,  null, "CMP", "DEC",  null,
        "CPX", "SBC",  null,  null, "CPX", "SBC", "INC",  null,
        "INX", "SBC", "NOP",  null, "CPX", "SBC", "INC",  null,
        "BEQ", "SBC",  null,  null,  null, "SBC", "INC",  null,
        "SED", "SBC",  null,  null,  null, "SBC", "INC",  null
    };

    public static final Mode[] instructionModes = {
        Mode.IMP, Mode.XIN, Mode.NUL, Mode.NUL,   // 0x00-0x03
        Mode.NUL, Mode.ZPG, Mode.ZPG, Mode.NUL,   // 0x04-0x07
        Mode.IMP, Mode.IMM, Mode.ACC, Mode.NUL,   // 0x08-0x0b
        Mode.NUL, Mode.ABS, Mode.ABS, Mode.NUL,   // 0x0c-0x0f
        Mode.REL, Mode.INY, Mode.NUL, Mode.NUL,   // 0x10-0x13
        Mode.NUL, Mode.ZPX, Mode.ZPX, Mode.NUL,   // 0x14-0x17
        Mode.IMP, Mode.ABY, Mode.NUL, Mode.NUL,   // 0x18-0x1b
        Mode.NUL, Mode.ABX, Mode.ABX, Mode.NUL,   // 0x1c-0x1f
        Mode.ABS, Mode.XIN, Mode.NUL, Mode.NUL,   // 0x20-0x23
        Mode.ZPG, Mode.ZPG, Mode.ZPG, Mode.NUL,   // 0x24-0x27
        Mode.IMP, Mode.IMM, Mode.ACC, Mode.NUL,   // 0x28-0x2b
        Mode.ABS, Mode.ABS, Mode.ABS, Mode.NUL,   // 0x2c-0x2f
        Mode.REL, Mode.INY, Mode.NUL, Mode.NUL,   // 0x30-0x33
        Mode.NUL, Mode.ZPX, Mode.ZPX, Mode.NUL,   // 0x34-0x37
        Mode.IMP, Mode.ABY, Mode.NUL, Mode.NUL,   // 0x38-0x3b
        Mode.NUL, Mode.ABX, Mode.ABX, Mode.NUL,   // 0x3c-0x3f
        Mode.IMP, Mode.XIN, Mode.NUL, Mode.NUL,   // 0x40-0x43
        Mode.NUL, Mode.ZPG, Mode.ZPG, Mode.NUL,   // 0x44-0x47
        Mode.IMP, Mode.IMM, Mode.ACC, Mode.NUL,   // 0x48-0x4b
        Mode.ABS, Mode.ABS, Mode.ABS, Mode.NUL,   // 0x4c-0x4f
        Mode.REL, Mode.INY, Mode.NUL, Mode.NUL,   // 0x50-0x53
        Mode.NUL, Mode.ZPX, Mode.ZPX, Mode.NUL,   // 0x54-0x57
        Mode.IMP, Mode.ABY, Mode.NUL, Mode.NUL,   // 0x58-0x5b
        Mode.NUL, Mode.ABX, Mode.ABX, Mode.NUL,   // 0x5c-0x5f
        Mode.IMP, Mode.XIN, Mode.NUL, Mode.NUL,   // 0x60-0x63
        Mode.NUL, Mode.ZPG, Mode.ZPG, Mode.NUL,   // 0x64-0x67
        Mode.IMP, Mode.IMM, Mode.ACC, Mode.NUL,   // 0x68-0x6b
        Mode.IND, Mode.ABS, Mode.ABS, Mode.NUL,   // 0x6c-0x6f
        Mode.REL, Mode.INY, Mode.NUL, Mode.NUL,   // 0x70-0x73
        Mode.NUL, Mode.ZPX, Mode.ZPX, Mode.NUL,   // 0x74-0x77
        Mode.IMP, Mode.ABY, Mode.NUL, Mode.NUL,   // 0x78-0x7b
        Mode.NUL, Mode.ABX, Mode.ABX, Mode.NUL,   // 0x7c-0x7f
        Mode.REL, Mode.XIN, Mode.NUL, Mode.NUL,   // 0x80-0x83
        Mode.ZPG, Mode.ZPG, Mode.ZPG, Mode.NUL,   // 0x84-0x87
        Mode.IMP, Mode.NUL, Mode.IMP, Mode.NUL,   // 0x88-0x8b
        Mode.ABS, Mode.ABS, Mode.ABS, Mode.NUL,   // 0x8c-0x8f
        Mode.REL, Mode.INY, Mode.NUL, Mode.NUL,   // 0x90-0x93
        Mode.ZPX, Mode.ZPX, Mode.ZPY, Mode.NUL,   // 0x94-0x97
        Mode.IMP, Mode.ABY, Mode.IMP, Mode.NUL,   // 0x98-0x9b
        Mode.NUL, Mode.ABX, Mode.NUL, Mode.NUL,   // 0x9c-0x9f
        Mode.IMM, Mode.XIN, Mode.IMM, Mode.NUL,   // 0xa0-0xa3
        Mode.ZPG, Mode.ZPG, Mode.ZPG, Mode.NUL,   // 0xa4-0xa7
        Mode.IMP, Mode.IMM, Mode.IMP, Mode.NUL,   // 0xa8-0xab
        Mode.ABS, Mode.ABS, Mode.ABS, Mode.NUL,   // 0xac-0xaf
        Mode.REL, Mode.INY, Mode.NUL, Mode.NUL,   // 0xb0-0xb3
        Mode.ZPX, Mode.ZPX, Mode.ZPY, Mode.NUL,   // 0xb4-0xb7
        Mode.IMP, Mode.ABY, Mode.IMP, Mode.NUL,   // 0xb8-0xbb
        Mode.ABX, Mode.ABX, Mode.ABY, Mode.NUL,   // 0xbc-0xbf
        Mode.IMM, Mode.XIN, Mode.NUL, Mode.NUL,   // 0xc0-0xc3
        Mode.ZPG, Mode.ZPG, Mode.ZPG, Mode.NUL,   // 0xc4-0xc7
        Mode.IMP, Mode.IMM, Mode.IMP, Mode.NUL,   // 0xc8-0xcb
        Mode.ABS, Mode.ABS, Mode.ABS, Mode.NUL,   // 0xcc-0xcf
        Mode.REL, Mode.INY, Mode.NUL, Mode.NUL,   // 0xd0-0xd3
        Mode.NUL, Mode.ZPX, Mode.ZPX, Mode.NUL,   // 0xd4-0xd7
        Mode.IMP, Mode.ABY, Mode.NUL, Mode.NUL,   // 0xd8-0xdb
        Mode.NUL, Mode.ABX, Mode.ABX, Mode.NUL,   // 0xdc-0xdf
        Mode.IMM, Mode.XIN, Mode.NUL, Mode.NUL,   // 0xe0-0xe3
        Mode.ZPG, Mode.ZPG, Mode.ZPG, Mode.NUL,   // 0xe4-0xe7
        Mode.IMP, Mode.IMM, Mode.IMP, Mode.NUL,   // 0xe8-0xeb
        Mode.ABS, Mode.ABS, Mode.ABS, Mode.NUL,   // 0xec-0xef
        Mode.REL, Mode.INY, Mode.NUL, Mode.NUL,   // 0xf0-0xf3
        Mode.NUL, Mode.ZPX, Mode.ZPX, Mode.NUL,   // 0xf4-0xf7
        Mode.IMP, Mode.ABY, Mode.NUL, Mode.NUL,   // 0xf8-0xfb
        Mode.NUL, Mode.ABX, Mode.ABX, Mode.NUL    // 0xfc-0xff
    };

    public static final int[] instructionSizes = {
        1, 2, 0, 0, 0, 2, 2, 0, 1, 2, 1, 0, 0, 3, 3, 0,
        2, 2, 0, 0, 0, 2, 2, 0, 1, 3, 0, 0, 0, 3, 3, 0,
        3, 2, 0, 0, 2, 2, 2, 0, 1, 2, 1, 0, 3, 3, 3, 0,
        2, 2, 0, 0, 0, 2, 2, 0, 1, 3, 0, 0, 0, 3, 3, 0,
        1, 2, 0, 0, 0, 2, 2, 0, 1, 2, 1, 0, 3, 3, 3, 0,
        2, 2, 0, 0, 0, 2, 2, 0, 1, 3, 0, 0, 0, 3, 3, 0,
        1, 2, 0, 0, 0, 2, 2, 0, 1, 2, 1, 0, 3, 3, 3, 0,
        2, 2, 0, 0, 0, 2, 2, 0, 1, 3, 0, 0, 0, 3, 3, 0,
        2, 2, 0, 0, 2, 2, 2, 0, 1, 0, 1, 0, 3, 3, 3, 0,
        2, 2, 0, 0, 2, 2, 2, 0, 1, 3, 1, 0, 0, 3, 0, 0,
        2, 2, 2, 0, 2, 2, 2, 0, 1, 2, 1, 0, 3, 3, 3, 0,
        2, 2, 0, 0, 2, 2, 2, 0, 1, 3, 1, 0, 3, 3, 3, 0,
        2, 2, 0, 0, 2, 2, 2, 0, 1, 2, 1, 0, 3, 3, 3, 0,
        2, 2, 0, 0, 0, 2, 2, 0, 1, 3, 0, 0, 0, 3, 3, 0,
        2, 2, 0, 0, 2, 2, 2, 0, 1, 2, 1, 0, 3, 3, 3, 0,
        2, 2, 0, 0, 0, 2, 2, 0, 1, 3, 0, 0, 0, 3, 3, 0
    };

    public static final int[] instructionClocks = {
        7, 6, 0, 0, 0, 3, 5, 0, 3, 2, 2, 0, 0, 4, 6, 0,
        2, 5, 0, 0, 0, 4, 6, 0, 2, 4, 0, 0, 0, 4, 7, 0,
        6, 6, 0, 0, 3, 3, 5, 0, 4, 2, 2, 0, 4, 4, 6, 0,
        2, 5, 0, 0, 0, 4, 6, 0, 2, 4, 0, 0, 0, 4, 7, 0,
        6, 6, 0, 0, 0, 3, 5, 0, 3, 2, 2, 0, 3, 4, 6, 0,
        2, 5, 0, 0, 0, 4, 6, 0, 2, 4, 0, 0, 0, 4, 7, 0,
        6, 6, 0, 0, 0, 3, 5, 0, 4, 2, 2, 0, 5, 4, 6, 0,
        2, 5, 0, 0, 0, 4, 6, 0, 2, 4, 0, 0, 0, 4, 7, 0,
        2, 6, 0, 0, 3, 3, 3, 0, 2, 0, 2, 0, 4, 4, 4, 0,
        2, 6, 0, 0, 4, 4, 4, 0, 2, 5, 2, 0, 0, 5, 0, 0,
        2, 6, 2, 0, 3, 3, 3, 0, 2, 2, 2, 0, 4, 4, 4, 0,
        2, 5, 0, 0, 4, 4, 4, 0, 2, 4, 2, 0, 4, 4, 4, 0,
        2, 6, 0, 0, 3, 3, 5, 0, 2, 2, 2, 0, 4, 4, 6, 0,
        2, 5, 0, 0, 0, 4, 6, 0, 2, 4, 0, 0, 0, 4, 7, 0,
        2, 6, 0, 0, 3, 3, 5, 0, 2, 2, 2, 0, 4, 4, 6, 0,
        2, 5, 0, 0, 0, 4, 6, 0, 2, 4, 0, 0, 0, 4, 7, 0
    };

    public static Instruction[] instructions = {
    	new Instruction("adc", 0x69, 0x65, 0x75, 0x6d, 0x7d, 0x79, 0x61, 0x71),
    	new Instruction("and", 0x29, 0x25, 0x35, 0x2d, 0x3d, 0x39, 0x21, 0x31),
    	
    	new Instruction("asl", 0x0a, 0x06, 0x16, 0x0e, 0x1e),
    	
    	new Instruction("bcc", 0x90),
    	new Instruction("bcs", 0xb0),
    	new Instruction("beq", 0xf0),
    	
    	new Instruction("bit", 0x24, 0x2c),
    	
    	new Instruction("bmi", 0x30),
    	new Instruction("bne", 0xd0),
    	new Instruction("bpl", 0x10),
    	
    	new Instruction("brk", 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	
    	new Instruction("bvc", 0x50),
    	new Instruction("bvs", 0x70),

    	new Instruction("clc", 0x18, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("cld", 0xd8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("cli", 0x58, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("clv", 0xb8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	
    	new Instruction("cmp", 0xc9, 0xc5, 0xd5, 0xcd, 0xdd, 0xd9, 0xc1, 0xd1),
    	
    	new Instruction("cpx", 0xe0, 0xe4, 0xec),
    	new Instruction("cpy", 0xc0, 0xc4, 0xcc),
    	
    	new Instruction("dec", 0xc6, 0xd6, 0xce, 0xde),
    	
    	new Instruction("dex", 0xca, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("dey", 0x88, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	
    	new Instruction("eor", 0x49, 0x45, 0x55, 0x4d, 0x5d, 0x59, 0x41, 0x51),

    	new Instruction("inc", 0xe6, 0xf6, 0xee, 0xfe),

    	new Instruction("inx", 0xe8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("iny", 0xc8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("jmp", -1, -1, -1, -1, -1, -1, -1, 0x4c, -1, -1, 0x6c, -1, -1),
    	new Instruction("jsr", -1, -1, -1, -1, -1, -1, -1, 0x20, -1, -1, -1, -1, -1),

    	new Instruction("lda", 0xa9, 0xa5, 0xb5, 0xad, 0xbd, 0xb9, 0xa1, 0xb1),
    	
    	// int imp, int acc, int imm, int zp, int zpx, int zpy, int rel, int abs, int absx, int absy, int ind, int indx, int indy
    	
    	new Instruction("ldx", -1, -1, 0xa2, 0xa6, -1, 0xb6, -1, 0xae, -1, 0xbe, -1, -1, -1),
    	new Instruction("ldy", -1, -1, 0xa0, 0xa4, 0xb4, -1, -1, 0xac, 0xbc, -1, -1, -1, -1),
    	
    	new Instruction("lsr", 0x4a, 0x46, 0x56, 0x4e, 0x5e),
    	
    	new Instruction("nop", 0xea, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	
    	new Instruction("ora", 0x09, 0x05, 0x15, 0x0d, 0x1d, 0x19, 0x01, 0x11),
    	
    	new Instruction("pha", 0x48, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("php", 0x88, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("pla", 0x68, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("plp", 0x28, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	
    	new Instruction("rol", 0x2a, 0x26, 0x36, 0x2e, 0x3e),
    	new Instruction("ror", 0x6a, 0x66, 0x76, 0x6e, 0x7e),

    	new Instruction("rti", 0x40, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("rts", 0x60, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),

    	new Instruction("sbc", 0xe9, 0xe5, 0xf5, 0xed, 0xfd, 0xf9, 0xe1, 0xf1),
    	
    	new Instruction("sec", 0x38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("sed", 0xf8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("sei", 0x78, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	
    	new Instruction("sta", -1, -1, -1, 0x85, 0x95, -1, -1, 0x8d, 0x9d, 0x99, -1, 0x81, 0x91),
    	new Instruction("stx", -1, -1, -1, 0x86, -1, 0x96, -1, 0x8e, -1, -1, -1, -1, -1),
    	new Instruction("sty", -1, -1, -1, 0x84, 0x94, -1, -1, 0x8c, -1, -1, -1, -1, -1),

    	new Instruction("tax", 0xaa, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("tay", 0xa8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("tsx", 0xba, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("txa", 0x8a, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("txs", 0x9a, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
    	new Instruction("tya", 0x98, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1)
    };
}