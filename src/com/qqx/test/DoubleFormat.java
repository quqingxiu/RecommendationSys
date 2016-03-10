package com.qqx.test;

public class DoubleFormat {
	public static void main(String[] args) {
		double similarity = 0.123456789;
		int i = 8;
		String key = Math.round(similarity*1000000)+""+i;
		System.out.println(key);
	}

}
