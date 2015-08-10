package uk.org.wookey.atari.exceptions;

public class FatalException extends WookeyException {
	private static final long serialVersionUID = 1L;

    public FatalException(String message) {
       super(message);
    }
}
