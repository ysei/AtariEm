package uk.org.wookey.atari.utils.assembler;

public class RuntimeAssemblyException extends Exception {
	private static final long serialVersionUID = 1L;

	public RuntimeAssemblyException() {}

    public RuntimeAssemblyException(String message) {
       super(message);
    }
}
