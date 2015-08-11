package uk.org.wookey.atari.labels;

public class AddressLabel {
	private int address;
	private String readLabel;
	private String writeLabel;
	
	public AddressLabel(int address, String readLabel, String writeLabel) {
		this.address = address;
		this.readLabel = readLabel;
		this.writeLabel = writeLabel;
	}
	
	public AddressLabel(int address, String label) {
		this(address, label, label);
	}
	
	public int getAddress() {
		return address;
	}
	
	public String getReadLabel() {
		return readLabel;
	}
	
	public String getWriteLabel() {
		return writeLabel;
	}
}
