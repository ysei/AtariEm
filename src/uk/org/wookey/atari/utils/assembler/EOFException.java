package uk.org.wookey.atari.utils.assembler;

public class EOFException extends Exception {
	private static final long serialVersionUID = 1L;

	public EOFException() {}

    public EOFException(String message) {
       super(message);
    }
}
