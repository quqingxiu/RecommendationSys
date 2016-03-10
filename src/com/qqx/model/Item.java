package com.qqx.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��Ŀʵ��
 * @author qqx
 *
 */
public class Item {
	/**
	 * ��ĿID
	 */
	private int itemId;
	/**
	 * ����
	 */
	private String itemName;
	/**
	 * ��Ŀ��������,��¼userId->rating
	 */
	private Map<Integer,Rating> ratingVector;
	
	/**
	 * ������Ŀ�����ַ���������Ŀ����
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
	 * ���캯��
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
	 * ������Ŀ��ƽ������
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
	 * ��ȡ��Ŀ����������
	 * @return
	 */
	public Collection<Rating> getAllRatings(){
		return ratingVector.values();
	}
	
	/**
	 * ��ȡ�û�����
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
	 * ����û�����Ŀ������
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
