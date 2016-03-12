package com.qqx.verification;

import java.util.List;
import java.util.Map;

import com.qqx.model.PredictedItemRating;
import com.qqx.model.Rating;
import com.qqx.model.User;
import com.qqx.recommend.Delphi;

/**
 * 平均绝对误差
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
	 * 获取用户平均绝对误差
	 * @param user	用户
	 * @param predRatingList	推荐项目集合
	 * @return
	 */
	public double getMeanAbsoluteUserError(User user,List<PredictedItemRating> predRatingList){
		double differenceSum = 0.0;
		int commonItemSum = 0;
		Map<Integer,Rating> uRatings = user.getRatingVector();	//用户实际评分集合
		for(PredictedItemRating p: predRatingList){
			int pItemId = p.getItemId();	//推荐的项目id
			if(uRatings.keySet().contains(pItemId)){
				Rating uRating = uRatings.get(pItemId);	//实际用户评分
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
