package uk.org.wookey.atari.exceptions;

public class LabelExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public LabelExistsException() {}

    public LabelExistsException(String message) {
       super(message);
    }
}
