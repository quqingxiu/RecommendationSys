package com.qqx.similarity;

import com.qqx.model.Item;

/**
 * 项目相似度计算接口
 * @author qqx
 *
 */
public interface Similarity {
	/**
	 * 计算项目之间的相似度
	 * @param itemA
	 * @param itemB
	 * @return
	 */
	public double calculateSimilarity(Item itemA,Item itemB);

}
