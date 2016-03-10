package com.qqx.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
	/**
	 * �û�ID
	 */
	private int userId;
	/**
	 * ����
	 */
	private String userName;
	/**
	 * �û���������,��¼itemId->rating
	 */
	private Map<Integer,Rating> ratingVector;
	
	/**
	 * �����û��������ݴ���User����
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
	 * ���캯��
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
	 * �����û���ƽ������
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
	 * ��ȡ�û�����Ŀ������
	 * @param itemId
	 * @return
	 */
	public Rating getItemRating(int itemId){
		return ratingVector.get(itemId);
	}
	
	/**
	 * ������Ŀ������
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
