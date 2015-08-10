package uk.org.wookey.atari.exceptions;

public class LabelExistsException extends WookeyException {
	private static final long serialVersionUID = 1L;

    public LabelExistsException(String message) {
       super(message);
    }
}
