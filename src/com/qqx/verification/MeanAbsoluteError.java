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
 * ����ƽ�����������
 * @author qqx
 *
 */
public class MeanAbsoluteError {
	private static final int topN = 20;
	
	public static void main(String[] args) {
		//����ѵ������
		DataSetImpl trainDataSet = new DataSetImpl();
		trainDataSet.load(DataConfig.getUserDataFilePath(), DataConfig.getItemDataFilePath(), 
				DataConfig.getRatingTrainDataFilePath());
		//�������ƶ�
		ItemBasedSimilarityMatrix similarity = new ItemBasedSimilarityMatrix(trainDataSet,
				new PearsonCorrelationCoefficient());
		//����k����
		KNearestNeighbor knn = new KNearestNeighbor(similarity);
		//�����Ƽ�����
		Delphi delphi = new Delphi(trainDataSet, similarity, knn);
		
		//���ز�������
		DataSetImpl testDataSet = new DataSetImpl();
		testDataSet.load(DataConfig.getUserDataFilePath(), DataConfig.getItemDataFilePath(), 
				DataConfig.getRatingTestDataFilePath());
		getMeanAbsoluteError(testDataSet.getUsers(),delphi);
	}
	
	/**
	 * �����ܵ�ƽ���������
	 * @param users
	 * @param delphi
	 * @return
	 */
	public static void getMeanAbsoluteError(Collection<User> users,Delphi delphi){
		long begin = System.currentTimeMillis();
    	System.out.println("\n================��ʼ����ƽ���������======================");
		double maueSum = 0.0;
		double userRatingSize = 0.0;
		double insertionItemSize = 0.0;
		for(User user:users){
			List<PredictedItemRating> predRatingList = delphi.recommend2(user, topN);
			//�����û�ƽ���������
			double maue = getMeanAbsoluteUserError(user, predRatingList);
			maueSum += maue;
			
			//����coverage
			userRatingSize += user.getRatingVector().keySet().size();
			insertionItemSize += getInsertionItemSize(user, predRatingList);
		}
		long end = System.currentTimeMillis();
    	System.out.println("================���������,��ʱ:"+(end-begin)+" ms===================");
    	System.out.println("\n����õ���ƽ���������Ϊ��"+maueSum/users.size());
    	System.out.println("����õ��ĸ�����Ϊ��"+insertionItemSize/userRatingSize);
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
	 * ��ȡ�û�ƽ���������
	 * @param user	�û�
	 * @param predRatingList	�Ƽ���Ŀ����
	 * @return
	 */
	public static double getMeanAbsoluteUserError(User user,List<PredictedItemRating> predRatingList){
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
