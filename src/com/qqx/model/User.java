package com.qqx.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
	/**
	 * 用户ID
	 */
	private int userId;
	/**
	 * 名称
	 */
	private String userName;
	/**
	 * 用户评分向量,记录itemId->rating
	 */
	private Map<Integer,Rating> ratingVector;
	
	/**
	 * 根据用户评分数据创建User对象
	 * @param userStr
	 * @param splite
	 */
	public User(String userStr,String splite){
		String[] ss = userStr.split(splite);
		this.userId = Integer.valueOf(ss[0]);
		if(ratingVector == null){
			ratingVector = new HashMap<Integer, Rating>();
		}
	}
	
	/**
	 * 构造函数
	 * @param userId
	 * @param userName
	 * @param ratingList
	 */
	public User(int userId,String userName,List<Rating> ratingList){
		this.userId = userId;
		this.userName = userName;
		if(ratingVector == null){
			ratingVector = new HashMap<Integer, Rating>(ratingList.size());
		}
		for(Rating r:ratingList){
			ratingVector.put(r.getUserId(), r);
		}
	}
	
	/**
	 * 计算用户的平均评分
	 * @return
	 */
	public double getAverageRating(){
		if(ratingVector == null || ratingVector.keySet().size() == 0){
			return 0.0;
		}
		
		int totalRatings = 0;
		for(int userId : ratingVector.keySet()){
			int rating = ratingVector.get(userId).getRating();
			totalRatings += rating;
		}
		return totalRatings/ratingVector.keySet().size();
	}
	
	/**
	 * 获取用户对项目的评分
	 * @param itemId
	 * @return
	 */
	public Rating getItemRating(int itemId){
		return ratingVector.get(itemId);
	}
	
	/**
	 * 设置项目的评分
	 * @param r
	 */
	public void setItemRating(Rating r){
		ratingVector.put(r.getItemId(), r);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Map<Integer, Rating> getRatingVector() {
		return ratingVector;
	}

	public void setRatingVector(Map<Integer, Rating> ratingVector) {
		this.ratingVector = ratingVector;
	}
}
