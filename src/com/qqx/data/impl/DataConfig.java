package com.qqx.data.impl;

public class DataConfig {
	/**
	 * 用户信息数据
	 */
	private static String userDataFilePath = "/data/u.user";
	/**
	 * 项目信息数据
	 */
	private static String itemDataFilePath = "/data/u.item";
	/**
	 * 评分训练数据
	 */
	private static String ratingTrainDataFilePath = "/data/u1.base";
	/**
	 * 评分测试数据
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
