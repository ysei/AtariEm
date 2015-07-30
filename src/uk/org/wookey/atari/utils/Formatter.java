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
}
