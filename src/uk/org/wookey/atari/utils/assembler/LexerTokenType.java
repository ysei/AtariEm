package uk.org.wookey.atari.utils.assembler;

public enum LexerTokenType {
	EOL,
	EOF,
	WHITESPACE,
	DECIMAL,
	LPAREN, 
	RPAREN,
	COMMA,
	PLUS,
	MINUS,
	HEX,
	HASH,
	BINARY,
	COMMENT,
	LSBOF,
	MSBOF,
	ATOM,
	EQUALS,
	PLABEL,
	NLABEL,
	UNKNOWN
}
