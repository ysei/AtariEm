package uk.org.wookey.atari.utils;

public class Formatter {
	public static String toHexString(int val, int digits) {
		String res = Integer.toHexString(val);
		int len = res.length();
		
		if (len < digits) {
			int extra = digits-len;
			
			res = "00000000".substring(0,  extra) + res;
		}
		
		return res;
	}
	
	public static String padString(String s, int places) {
		return padString(s, places, ' ');
	}

	public static String padString(String s, int places, char padding) {
		String pad = "";
		
		for (int i=0; i<Math.abs(places)-s.length(); i++) {
			pad += " ";
		}
		
		if (places > 0) {
			return pad + s;
		}

		return s + pad;
	}
}
