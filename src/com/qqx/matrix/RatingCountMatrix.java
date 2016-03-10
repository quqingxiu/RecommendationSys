package com.qqx.matrix;

import com.qqx.model.Item;
import com.qqx.model.Rating;

/**
 * 评分矩阵类
 * 作用：将一个类与另一个类的评分关系存储成表格形式，
 * 便于平滑的计算相似度
 * @author qqx
 *
 */
public class RatingCountMatrix {
	private int[][] matrix = null;
	
	/**
	 * 
	 * @param itemA
	 * @param itemB
	 * @param nRatingValues 评分的上限值
	 */
	public RatingCountMatrix(Item itemA,Item itemB,int nRatingValues) {
		init(nRatingValues);
		calculate(itemA,itemB);
	}

	/**
	 * 初始化矩阵，存储两个用户评分上的一致性。
	 * 两个用户对同一条目评分，则在分值对应的行与列中加1
	 * @param itemA
	 * @param itemB
	 */
	public void calculate(Item itemA,Item itemB){
		for(Rating ratingForA : itemA.getAllRatings()){
			//检查是否有相同用户给A、B条目打分
			Rating ratingForB = itemB.getUserRating(ratingForA.getUserId());
			if(ratingForB != null){
				int i = ratingForA.getRating() - 1;
				int j = ratingForB.getRating() - 1;
				matrix[i][j] ++;
			}
		}
	}
	
	/**
	 * 获取用户评分一致的项目数
	 * @return
	 */
	public int getAgreementCount(){
		int ratingCount = 0;
		for(int i=0,len=matrix.length;i<len;i++){
			ratingCount += matrix[i][i];
		}
		return ratingCount;
	}
	
	/**
	 * 获取两个用户共同评分过的项目数
	 * @return
	 */
	public int getTotalCount(){
		int total = 0;
		int len = matrix.length;
		for(int i=0;i<len;i++){
			for(int j=0;j<len;j++){
				total += matrix[i][j];
			}
		}
		return total;
	}

	private void init(int nSize){
		matrix = new int[nSize][nSize];
	}
}
