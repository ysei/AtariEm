package uk.org.wookey.atari.exceptions;

public class NosuchLabelException extends WookeyException {
	private static final long serialVersionUID = 1L;

    public NosuchLabelException(String message) {
       super(message);
    }
}
