package uk.org.wookey.atari.editor;

public class LineInfo {
	public int start;
	public int length;
	
	public LineInfo() {
		start = 0;
		length = 0;
	}
	
	public LineInfo(int s, int l) {
		start = s;
		length = l;
	}
}
