package uk.org.wookey.atari.utils.assembler;

public class SyntaxException extends Exception {
	private static final long serialVersionUID = 1L;

	public SyntaxException() {}

    public SyntaxException(String message) {
       super(message);
    }
}
