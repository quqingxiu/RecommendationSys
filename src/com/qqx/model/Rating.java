package com.qqx.model;

/**
 * �û�����ʵ��
 * @author qqx
 *
 */
public class Rating {
	private int userId;
	private int itemId;
	private int rating;
	
	public Rating(String ratingStr,String splite) {
		String[] ss = ratingStr.split(splite);
		if(ss.length < 3){			//��������
			System.out.println("�����д���");
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
