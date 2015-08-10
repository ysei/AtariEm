package uk.org.wookey.atari.exceptions;

public class WookeyException extends Exception {
	private static final long serialVersionUID = 1L;

	public WookeyException(String msg) {
        super(msg);
    }
    
    public WookeyException() {
        super();
    }

}
