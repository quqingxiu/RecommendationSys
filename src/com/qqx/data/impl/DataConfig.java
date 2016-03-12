package com.qqx.data.impl;

public class DataConfig {
	/**
	 * �û���Ϣ����
	 */
	private static String userDataFilePath = "/data/u.user";
	/**
	 * ��Ŀ��Ϣ����
	 */
	private static String itemDataFilePath = "/data/u.item";
	/**
	 * ����ѵ������
	 */
	private static String ratingTrainDataFilePath = "/data/u1.base";
	/**
	 * ���ֲ�������
	 */
	private static String ratingTestDataFilePath = "/data/u1.test";
	
	private static String basePath = System.getProperty("user.dir");

	public static String getUserDataFilePath() {
		return basePath+userDataFilePath;
	}

	public static String getItemDataFilePath() {
		return basePath+itemDataFilePath;
	}

	public static String getRatingTrainDataFilePath() {
		return basePath+ratingTrainDataFilePath;
	}

	public static String getRatingTestDataFilePath() {
		return basePath+ratingTestDataFilePath;
	}
}
