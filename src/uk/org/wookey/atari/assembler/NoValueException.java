package uk.org.wookey.atari.assembler;

public class NoValueException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoValueException() {}

    public NoValueException(String message) {
       super(message);
    }
}
