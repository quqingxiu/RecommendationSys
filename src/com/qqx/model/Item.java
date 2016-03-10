package com.qqx.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目实体
 * @author qqx
 *
 */
public class Item {
	/**
	 * 项目ID
	 */
	private int itemId;
	/**
	 * 名称
	 */
	private String itemName;
	/**
	 * 项目评分向量,记录userId->rating
	 */
	private Map<Integer,Rating> ratingVector;
	
	/**
	 * 根据项目数据字符串生成项目对象
	 * @param itemStr
	 * @param splite
	 */
	public Item(String itemStr,String splite){
		String[] ss = itemStr.split(splite);
		this.itemId = Integer.valueOf(ss[0]);
		this.itemName = ss[1];
		if(ratingVector == null){
			ratingVector = new HashMap<Integer, Rating>();
		}
	}
	
	/**
	 * 构造函数
	 * @param itemId
	 * @param itemName
	 * @param ratingList
	 */
	public Item(int itemId,String itemName,List<Rating> ratingList){
		this.itemId = itemId;
		this.itemName = itemName;
		if(ratingVector == null){
			ratingVector = new HashMap<Integer, Rating>(ratingList.size());
		}
		for(Rating r:ratingList){
			ratingVector.put(r.getUserId(), r);
		}
	}
	
	/**
	 * 计算项目的平均评分
	 * @return
	 */
	public double getAverageRating(){
		if(ratingVector == null || ratingVector.keySet().size() == 0){
			return 0.0;
		}
		
		double totalRatings = 0;
		for(int userId : ratingVector.keySet()){
			int rating = ratingVector.get(userId).getRating();
			totalRatings += rating;
		}
		return totalRatings/ratingVector.keySet().size();
	}
	
	/**
	 * 获取项目的所有评分
	 * @return
	 */
	public Collection<Rating> getAllRatings(){
		return ratingVector.values();
	}
	
	/**
	 * 获取用户评分
	 * @param userId
	 * @return
	 */
	public Rating getUserRating(int userId){
		return ratingVector.get(userId);
	}
	
	public String toString(){
		return "{itemId:"+itemId+",itemName:"+itemName+"}";
	}
	
	/**
	 * 添加用户对项目的评分
	 * @param r
	 */
	public void setUserRating(Rating r){
		ratingVector.put(r.getUserId(), r);
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public void setRatingVector(Map<Integer, Rating> ratingVector) {
		this.ratingVector = ratingVector;
	}

	public int getItemId() {
		return itemId;
	}
}
