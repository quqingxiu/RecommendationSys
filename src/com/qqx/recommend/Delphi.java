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
 * �Ƽ���
 * �ṩ����Ԥ�⣬�Լ�k���Ƽ�
 * @author qqx
 *
 */
public class Delphi {
	/**
	 * ���ݼ�����
	 */
	private Dataset dataset = null;
	/**
	 * ���ƶȾ���ʵ��
	 */
	private ItemBasedSimilarityMatrix similarity;
	/**
	 * ���ƶȷ�ֵ
	 */
	private final double similarityThreshold;
	/**
	 * k����ʵ��
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
			if(!skipItem(user, item)){			//�����û�û�������ֵ���Ŀ
				int itemId = item.getItemId();
				Set<Integer> knnSet = knn.getKNearestNeigtbor(itemId);	//��ȡitemId��k����
				
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
	 * �����û�user����Ŀitem�Ĺ���
	 * @param user
	 * @param item	��������Ŀ
	 * @param knnSet	item��k���ڼ���
	 * @return
	 */
	public double estimateItemBasedRating(User user,Item item,Set<Integer> knnSet){
		Map<Integer,Rating> map = user.getRatingVector();
		Set<Integer> userRatedSet = map.keySet();	//�û������ֵ���Ŀ����
		
		double weightSimilaritySum = 0.0;
		double SimilaritySum = 0.0;
		for(int itemId: knnSet){
			if(userRatedSet.contains(itemId)){	
				//��Ŀi,j�����ƶ�
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
	 * Ϊ�û��Ƽ�ǰtopN��
	 * @param user
	 * @param topN
	 * @return
	 */
	public List<PredictedItemRating> recommend(User user, int topN) {
		List<PredictedItemRating> recommendations = new ArrayList<PredictedItemRating>();
//		double maxRating = -1.0d;
		
		for(Item item : dataset.getItems()){
			if(!skipItem(user, item)){
				//Ԥ���û��Ը���Ŀ������
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
	 * �ж��û��Ƿ����Ŀ���ֹ�
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
	 * ����ĳ�û���ĳ��Ŀ������
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
		
		//�жϸ��û��Ƿ������Ŀ������
		Rating existingRatingByUser = item.getUserRating(user.getUserId());
		if(existingRatingByUser != null){
			estimateRating = existingRatingByUser.getRating();
		}else{	//���û�δ������Ŀ���й�ֵ
			double similarityBetweenItems = 0.0;	//��Ŀ֮������ƶ�
			double weightedRating = 0.0;	
			
			for(Item anotherItem : dataset.getItems()){
				//ֻ���Ǹ��û�����ֵ���Ŀ
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
