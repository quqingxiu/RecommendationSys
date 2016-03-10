package com.qqx.matrix;

import com.qqx.model.Item;
import com.qqx.model.Rating;

/**
 * ���־�����
 * ���ã���һ��������һ��������ֹ�ϵ�洢�ɱ����ʽ��
 * ����ƽ���ļ������ƶ�
 * @author qqx
 *
 */
public class RatingCountMatrix {
	private int[][] matrix = null;
	
	/**
	 * 
	 * @param itemA
	 * @param itemB
	 * @param nRatingValues ���ֵ�����ֵ
	 */
	public RatingCountMatrix(Item itemA,Item itemB,int nRatingValues) {
		init(nRatingValues);
		calculate(itemA,itemB);
	}

	/**
	 * ��ʼ�����󣬴洢�����û������ϵ�һ���ԡ�
	 * �����û���ͬһ��Ŀ���֣����ڷ�ֵ��Ӧ���������м�1
	 * @param itemA
	 * @param itemB
	 */
	public void calculate(Item itemA,Item itemB){
		for(Rating ratingForA : itemA.getAllRatings()){
			//����Ƿ�����ͬ�û���A��B��Ŀ���
			Rating ratingForB = itemB.getUserRating(ratingForA.getUserId());
			if(ratingForB != null){
				int i = ratingForA.getRating() - 1;
				int j = ratingForB.getRating() - 1;
				matrix[i][j] ++;
			}
		}
	}
	
	/**
	 * ��ȡ�û�����һ�µ���Ŀ��
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
	 * ��ȡ�����û���ͬ���ֹ�����Ŀ��
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
