package com.mirhoseini.utils.encrypt;

public class SimpleCoder {

	public static String code(String str, int key) {
		if (str != null) {
			String result = "";
			for (char ch : str.toCharArray()) {
				ch += key;
				result += Character.toString(ch);
			}
			return result;
		}
		return null;
	}

	public static String decode(String str, int key) {
		if (str != null) {
			String result = "";
			for (char ch : str.toCharArray()) {
				ch -= key;
				result += Character.toString(ch);
			}
			return result;
		}
		return null;
	}
}
