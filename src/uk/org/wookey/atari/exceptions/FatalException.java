package uk.org.wookey.atari.exceptions;

public class FatalException extends Exception {
	private static final long serialVersionUID = 1L;

	public FatalException() {}

    public FatalException(String message) {
       super(message);
    }
}
