package uk.org.wookey.assembler;

import java.io.*;
/**
*	An interface describing an assembler
*	@author David Schager 2006
*/
public interface AbstractAssembler {
	/**
	*	Returns the Lexer object that this assembler is using
	*	@return The @see AbstractLexer
	*/
	public abstract AbstractLexer getLexer ();
	
	/**
	*	Returns the parser object that this assembler is using
	*	@return The @see AbstractParser
	*/
	public abstract AbstractParser getParser ();
	
	/**
	*	Assembles a source file
	*	@param source object to get source code lines from
	*   @param output object to write assembled code to
	*	@return Error code indicating level of success of assembly. 
	*	A 1 indicates error, and a 0 indicates success.
	*	@throws IOException on error while reading source
	*/
	public abstract int assemble (AssemblerInput source, AssemblerOutput output) throws IOException;
}
