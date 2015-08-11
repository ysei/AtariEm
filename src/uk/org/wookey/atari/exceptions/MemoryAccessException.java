package uk.org.wookey.atari.exceptions;

public class MemoryAccessException extends WookeyException {
	private static final long serialVersionUID = 1L;

	public MemoryAccessException(String message) {
		super(message);
	}
}
