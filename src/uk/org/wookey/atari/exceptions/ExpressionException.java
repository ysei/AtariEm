package uk.org.wookey.atari.exceptions;

public class ExpressionException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExpressionException() {}

    public ExpressionException(String message) {
       super(message);
    }
}