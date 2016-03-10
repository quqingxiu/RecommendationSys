package com.qqx.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {
	public final static String CHARACTER_CODE_GBK = "gbk";

	public final static String CHARACTER_CODE_ISO = "iso-8859-1";

	public final static String CHARACTER_CODE_UTF = "UTF-8";

	public static boolean isNullOrEmpty(String strToBeChecked) {
		return strToBeChecked == null || "".equals(strToBeChecked.trim());
	}
	
	public static boolean isDBCcase(String str) {
		if (str.matches("[\\w]")) {
			return true;
		} else {
			return str.matches("/[^\\uFF00-\\uFFFF]/g");
		}
	}

	public static String getSubString(String str, int length) {
		int count = 0;
		StringBuffer temp1 = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (count < length) {
				if (StringUtil.isDBCcase(String.valueOf(str.charAt(i)))) {
					temp1.append(str.charAt(i));
					count++;
				} else {
					temp1.append(str.charAt(i));
					count = count + 2;
				}
			}
			if (count >= 38) {
				break;
			}
		}
		return temp1.toString();
	}

	public static int getStringLenght(String str) {
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if (StringUtil.isDBCcase(String.valueOf(str.charAt(i)))) {
				count++;
			} else {
				count = count + 2;
			}
		}
		return count;
	}

	public static String ISO2UTF8(String str) {

		if (str == null) {

			return "";
		}

		try {
			str = new String(str.getBytes(StringUtil.CHARACTER_CODE_ISO),
					StringUtil.CHARACTER_CODE_UTF);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public static String ISO2GBK(String str) {

		if (str == null) {

			return "";
		}

		try {
			str = new String(str.getBytes(StringUtil.CHARACTER_CODE_ISO),
					StringUtil.CHARACTER_CODE_GBK);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	public static boolean isEmpty(String str)
	{
	   return ((str == null) || (str.length() == 0));
	}

}
