package uk.org.wookey.atari.exceptions;

public class NosuchLabelException extends Exception {
	private static final long serialVersionUID = 1L;

	public NosuchLabelException() {}

    public NosuchLabelException(String message) {
       super(message);
    }
}
