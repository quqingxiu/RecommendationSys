package com.qqx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 项目预测评分类
 * @author qqx
 *
 */
public class PredictedItemRating {
	private int userId;
	private int itemId;
	private double rating;
	
	public PredictedItemRating(int userId, int itemId, double rating) {
		this.userId = userId;
		this.itemId = itemId;
		this.rating = rating;
	}

	@Override
	public String toString() {
        return this.getClass().getSimpleName() + "[userId: " + userId
                + ", itemId: " + itemId + ", rating: " + rating + "]";
    }
	
	/**
	 * 获取估计评分的前n项
	 * @param values
	 * @param topN 前n项值
	 * @return
	 */
	public static List<PredictedItemRating> getTopNRecommendations(List<PredictedItemRating> values,int topN){
		PredictedItemRating.sort(values);
		List<PredictedItemRating> topRecommendations = new ArrayList<PredictedItemRating>();
		for(PredictedItemRating r : values){
			if(topRecommendations.size() >= topN){
				break;
			}
			topRecommendations.add(r);
		}
		return topRecommendations;
	}
	
	/**
	 * 对PredictedItemRating列表进行排序
	 * @param values
	 */
	public static void sort(List<PredictedItemRating> values){
		Collections.sort(values, new Comparator<PredictedItemRating>() {
			@Override
			public int compare(PredictedItemRating o1, PredictedItemRating o2) {
				int result = 0;
                if( o1.getRating() < o2.getRating() ) {
                    result = 1; // reverse order
                }
                else if( o1.getRating() > o2.getRating() ) {
                    result = -1;
                }
                else {
                    result = 0;
                }
                return result;
			}
		});
		
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

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
}
