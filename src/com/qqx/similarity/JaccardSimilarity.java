package com.qqx.similarity;

import com.qqx.matrix.RatingCountMatrix;
import com.qqx.model.Item;

/**
 * Jaccard���ƶȼ���ʵ����
 * @author qqx
 *
 */
public class JaccardSimilarity implements Similarity {
	/**
	 * ���ּ���
	 */
	private static final int RATING_LEVEL_VALUE = 5;
	
	@Override
	public double calculateSimilarity(Item itemA, Item itemB) {
		double similarity = 0.0;
		//���ƶȼ���
		RatingCountMatrix rcm = new RatingCountMatrix(itemA, itemB, RATING_LEVEL_VALUE);
		int totalCount = rcm.getTotalCount();
		int agreementCount = rcm.getAgreementCount();
		
		if(agreementCount > 0){
			similarity = (double)agreementCount/(double)totalCount;
		}
		return similarity;
	}

}
