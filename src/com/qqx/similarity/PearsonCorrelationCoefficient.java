package com.qqx.similarity;

import java.util.Collection;

import com.qqx.model.Item;
import com.qqx.model.Rating;

/**
 * pearson���ϵ������ʵ����
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
		
		double aveRA = itemA.getAverageRating();	//��ĿA��ƽ������
		double aveRB = itemB.getAverageRating();	//��ĿB��ƽ������
		for(Rating ra : ratings){
			int userId = ra.getUserId();	//�û�id
			Rating rb = itemB.getUserRating(userId);	//�жϸ��û��Ƿ����Ŀb��������
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
