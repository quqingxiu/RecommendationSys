package com.qqx.verification;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.qqx.data.impl.DataConfig;
import com.qqx.data.impl.DataSetImpl;
import com.qqx.matrix.ItemBasedSimilarityMatrix;
import com.qqx.matrix.KNearestNeighbor;
import com.qqx.model.PredictedItemRating;
import com.qqx.model.Rating;
import com.qqx.model.User;
import com.qqx.recommend.Delphi;
import com.qqx.similarity.PearsonCorrelationCoefficient;

/**
 * 计算平均绝对误差类
 * @author qqx
 *
 */
public class MeanAbsoluteError {
	private static final int topN = 20;
	
	public static void main(String[] args) {
		//记载训练数据
		DataSetImpl trainDataSet = new DataSetImpl();
		trainDataSet.load(DataConfig.getUserDataFilePath(), DataConfig.getItemDataFilePath(), 
				DataConfig.getRatingTrainDataFilePath());
		//计算相似度
		ItemBasedSimilarityMatrix similarity = new ItemBasedSimilarityMatrix(trainDataSet,
				new PearsonCorrelationCoefficient());
		//计算k近邻
		KNearestNeighbor knn = new KNearestNeighbor(similarity);
		//生成推荐对象
		Delphi delphi = new Delphi(trainDataSet, similarity, knn);
		
		//加载测试数据
		DataSetImpl testDataSet = new DataSetImpl();
		testDataSet.load(DataConfig.getUserDataFilePath(), DataConfig.getItemDataFilePath(), 
				DataConfig.getRatingTestDataFilePath());
		getMeanAbsoluteError(testDataSet.getUsers(),delphi);
	}
	
	/**
	 * 计算总的平均绝对误差
	 * @param users
	 * @param delphi
	 * @return
	 */
	public static void getMeanAbsoluteError(Collection<User> users,Delphi delphi){
		long begin = System.currentTimeMillis();
    	System.out.println("\n================开始计算平均绝对误差======================");
		double maueSum = 0.0;
		double userRatingSize = 0.0;
		double insertionItemSize = 0.0;
		for(User user:users){
			List<PredictedItemRating> predRatingList = delphi.recommend2(user, topN);
			//计算用户平均绝对误差
			double maue = getMeanAbsoluteUserError(user, predRatingList);
			maueSum += maue;
			
			//计算coverage
			userRatingSize += user.getRatingVector().keySet().size();
			insertionItemSize += getInsertionItemSize(user, predRatingList);
		}
		long end = System.currentTimeMillis();
    	System.out.println("================误差计算完成,用时:"+(end-begin)+" ms===================");
    	System.out.println("\n计算得到的平均绝对误差为："+maueSum/users.size());
    	System.out.println("计算得到的覆盖率为："+insertionItemSize/userRatingSize);
	}
	
	public static int getInsertionItemSize(User user,List<PredictedItemRating> predRatingList){
		int insertionSum = 0;
		for(PredictedItemRating p : predRatingList){
			if(user.getRatingVector().keySet().contains(p.getItemId())){
				insertionSum ++;
			}
		}
		
		return insertionSum;
	}
	
	
	/**
	 * 获取用户平均绝对误差
	 * @param user	用户
	 * @param predRatingList	推荐项目集合
	 * @return
	 */
	public static double getMeanAbsoluteUserError(User user,List<PredictedItemRating> predRatingList){
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
