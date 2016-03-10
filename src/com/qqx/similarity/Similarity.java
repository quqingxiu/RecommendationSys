package com.qqx.similarity;

import com.qqx.model.Item;

/**
 * ��Ŀ���ƶȼ���ӿ�
 * @author qqx
 *
 */
public interface Similarity {
	/**
	 * ������Ŀ֮������ƶ�
	 * @param itemA
	 * @param itemB
	 * @return
	 */
	public double calculateSimilarity(Item itemA,Item itemB);

}
