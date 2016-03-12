package com.qqx.verification;

import java.util.List;
import java.util.Map;

import com.qqx.model.PredictedItemRating;
import com.qqx.model.Rating;
import com.qqx.model.User;
import com.qqx.recommend.Delphi;

/**
 * ƽ���������
 * @author qqx
 *
 */
public class MeanAbsoluteError {
	
	/**
	 * 
	 * @param users
	 * @param delphi
	 * @return
	 */
	public double getMeanAbsoluteError(List<User> users,Delphi delphi){
		for(User user:users){
			
		}
		return 0.0;
	}
	
	
	/**
	 * ��ȡ�û�ƽ���������
	 * @param user	�û�
	 * @param predRatingList	�Ƽ���Ŀ����
	 * @return
	 */
	public double getMeanAbsoluteUserError(User user,List<PredictedItemRating> predRatingList){
		double differenceSum = 0.0;
		int commonItemSum = 0;
		Map<Integer,Rating> uRatings = user.getRatingVector();	//�û�ʵ�����ּ���
		for(PredictedItemRating p: predRatingList){
			int pItemId = p.getItemId();	//�Ƽ�����Ŀid
			if(uRatings.keySet().contains(pItemId)){
				Rating uRating = uRatings.get(pItemId);	//ʵ���û�����
				differenceSum += Math.abs(p.getRating() - uRating.getRating());
				commonItemSum++;
			}
		}
		
		if(commonItemSum == 0){
			commonItemSum =1;
		}
		
		return differenceSum/commonItemSum;
	}
}
