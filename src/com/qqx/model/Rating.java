package com.qqx.model;

/**
 * 用户评分实体
 * @author qqx
 *
 */
public class Rating {
	private int userId;
	private int itemId;
	private int rating;
	
	public Rating(String ratingStr,String splite) {
		String[] ss = ratingStr.split(splite);
		if(ss.length < 3){			//数据有误
			System.out.println("数据有错误");
		}else{
			userId = Integer.valueOf(ss[0].trim());
			itemId = Integer.valueOf(ss[1].trim());
			rating = Integer.valueOf(ss[2].trim());
		}
	}
	
	public Rating(int userId, int itemId, int rating) {
		super();
		this.userId = userId;
		this.itemId = itemId;
		this.rating = rating;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
}
