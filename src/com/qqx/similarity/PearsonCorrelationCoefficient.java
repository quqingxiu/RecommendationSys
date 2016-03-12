package com.qqx.similarity;

import java.util.Collection;

import com.qqx.model.Item;
import com.qqx.model.Rating;

/**
 * pearson相关系数计算实现类
 * @author qqx
 *
 */
public class PearsonCorrelationCoefficient implements Similarity {

	@Override
	public double calculateSimilarity(Item itemA, Item itemB) {
		Collection<Rating> ratings = itemA.getAllRatings();
		
		double dotProductSum = 0.0;
		double vProductSumA = 0.0;
		double vProductSumB = 0.0;
		
		double aveRA = itemA.getAverageRating();	//项目A的平均评分
		double aveRB = itemB.getAverageRating();	//项目B的平均评分
		for(Rating ra : ratings){
			int userId = ra.getUserId();	//用户id
			Rating rb = itemB.getUserRating(userId);	//判断该用户是否对项目b进行评分
			if(rb != null){
				dotProductSum += (ra.getRating() - aveRA)*(rb.getRating() - aveRB);
				vProductSumA += Math.pow((ra.getRating() - aveRA), 2);
				vProductSumB += Math.pow((rb.getRating() - aveRB), 2);
			}
		}
		if(vProductSumA == 0 || vProductSumB == 0){
			return 0.0;
		}
		
		return dotProductSum / (Math.sqrt(vProductSumA) * Math.sqrt(vProductSumB));
	}

}
