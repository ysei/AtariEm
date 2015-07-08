package uk.org.wookey.assembler;

import java.io.IOException;

public interface AssemblerInput {
	public void reset();
	public String getLine(int lineNum);
	public char getChar();
	public void unGetChar() throws IOException;
	public boolean atEOF();
	public String getName();
	public int getPosition();
	public void setPosition(int position) throws IOException;
}
