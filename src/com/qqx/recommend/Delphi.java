package com.qqx.recommend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qqx.data.Dataset;
import com.qqx.matrix.ItemBasedSimilarityMatrix;
import com.qqx.matrix.KNearestNeighbor;
import com.qqx.model.Item;
import com.qqx.model.PredictedItemRating;
import com.qqx.model.Rating;
import com.qqx.model.User;

/**
 * 推荐类
 * 提供评分预测，以及k项推荐
 * @author qqx
 *
 */
public class Delphi {
	/**
	 * 数据集变量
	 */
	private Dataset dataset = null;
	/**
	 * 相似度矩阵实例
	 */
	private ItemBasedSimilarityMatrix similarity;
	/**
	 * 相似度阀值
	 */
	private final double similarityThreshold;
	/**
	 * k近邻实例
	 */
	private KNearestNeighbor knn = null;
	
	public Delphi(Dataset dataset, ItemBasedSimilarityMatrix similarity,KNearestNeighbor knn,double similarityThreshold) {
		this.dataset = dataset;
		this.similarity = similarity;
		this.knn = knn;
		this.similarityThreshold = similarityThreshold;
	}
	
	public Delphi(Dataset dataset, ItemBasedSimilarityMatrix similarity,KNearestNeighbor knn) {
		this(dataset, similarity, knn, 0.0);
	}
	
	public List<PredictedItemRating> recommend2(User user, int topN) {
		List<PredictedItemRating> recommendations = new ArrayList<PredictedItemRating>();
		
		for(Item item : dataset.getItems()){
			if(!skipItem(user, item)){			//评估用户没有评过分的项目
				int itemId = item.getItemId();
				Set<Integer> knnSet = knn.getKNearestNeigtbor(itemId);	//获取itemId的k近邻
				
				double predictedRating = estimateItemBasedRating(user, item, knnSet);
				if(!Double.isNaN(predictedRating)){
					recommendations.add(new PredictedItemRating(user.getUserId(), item.getItemId(), predictedRating));
				}
			}else{
				System.out.println("skip Item : "+item.getItemName());
			}
		}
		
		List<PredictedItemRating> topRecommendations = PredictedItemRating.getTopNRecommendations(recommendations, topN);
		return topRecommendations;
	}
	
	/**
	 * 计算用户user对项目item的估分
	 * @param user
	 * @param item	待估分项目
	 * @param knnSet	item的k近邻集合
	 * @return
	 */
	public double estimateItemBasedRating(User user,Item item,Set<Integer> knnSet){
		Map<Integer,Rating> map = user.getRatingVector();
		Set<Integer> userRatedSet = map.keySet();	//用户评过分的项目集合
		
		double weightSimilaritySum = 0.0;
		double SimilaritySum = 0.0;
		for(int itemId: knnSet){
			if(userRatedSet.contains(itemId)){	
				//项目i,j的相似度
				double simValues = similarity.getSimilarityValues(itemId, item.getItemId());
				int rating = user.getItemRating(itemId).getRating();
				
				weightSimilaritySum += (simValues * rating);
				SimilaritySum += simValues;
			}
		}
		
		if(SimilaritySum == 0){
			return 0.0;
		}
		return weightSimilaritySum / SimilaritySum;
	}
	
	
	/**
	 * 为用户推荐前topN项
	 * @param user
	 * @param topN
	 * @return
	 */
	public List<PredictedItemRating> recommend(User user, int topN) {
		List<PredictedItemRating> recommendations = new ArrayList<PredictedItemRating>();
//		double maxRating = -1.0d;
		
		for(Item item : dataset.getItems()){
			if(!skipItem(user, item)){
				//预测用户对该项目的评分
				double predictedRating = estimateItemBasedRating(user,item);
//				if(predictedRating > maxRating){
//					maxRating = predictedRating;
//				}
				
				if(!Double.isNaN(predictedRating)){
					recommendations.add(new PredictedItemRating(user.getUserId(), item.getItemId(), predictedRating));
				}
				else{
					System.out.println("the estimate value is Nan : "+item.getItemName());
				}
				
			}else{
				System.out.println("skip Item : "+item.getItemName());
			}
		}
		
		List<PredictedItemRating> topRecommendations = PredictedItemRating.getTopNRecommendations(recommendations, topN);
		
		return topRecommendations;
	}
	
	/**
	 * 判断用户是否对项目评分过
	 * @param user
	 * @param item
	 * @return
	 */
	private boolean skipItem(User user, Item item) {
        boolean skipItem = true;
        if( user.getItemRating(item.getItemId()) == null ) {
            skipItem = false;
        }
        return skipItem;
    }

	/**
	 * 估计某用户对某项目的评分
	 * @param user
	 * @param item
	 * @return
	 */
	public double estimateItemBasedRating(User user,Item item){
		double estimateRating = Double.NaN;
		int itemId = item.getItemId();
		int userId = user.getUserId();
		double weightedRatingSum = 0.0;	
		double similaritySum = 0.0;
		
		//判断该用户是否给该项目评过分
		Rating existingRatingByUser = item.getUserRating(user.getUserId());
		if(existingRatingByUser != null){
			estimateRating = existingRatingByUser.getRating();
		}else{	//对用户未评分项目进行估值
			double similarityBetweenItems = 0.0;	//项目之间的相似度
			double weightedRating = 0.0;	
			
			for(Item anotherItem : dataset.getItems()){
				//只考虑该用户打过分的条目
				Rating anotherItemRating = anotherItem.getUserRating(userId);
				if(anotherItemRating != null){
					similarityBetweenItems = similarity.getSimilarityValues(itemId, anotherItem.getItemId());
					if(similarityBetweenItems > similarityThreshold){
						weightedRating += similarityBetweenItems * anotherItemRating.getRating();
						weightedRatingSum += weightedRating;
						similaritySum += similarityBetweenItems;
					}
				}
			}
			
			if(similaritySum > 0.0){
				estimateRating = weightedRatingSum / similaritySum;
			}
		}
		
		return estimateRating;
	}

}
