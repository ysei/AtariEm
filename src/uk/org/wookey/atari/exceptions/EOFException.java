package uk.org.wookey.atari.exceptions;

public class EOFException extends WookeyException {
	private static final long serialVersionUID = 1L;

	public EOFException(String message) {
       super(message);
    }
}
