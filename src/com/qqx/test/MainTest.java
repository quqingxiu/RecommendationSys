package com.qqx.test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.qqx.data.impl.DataSetImpl;
import com.qqx.matrix.ItemBasedSimilarityMatrix;
import com.qqx.matrix.KNearestNeighbor;
import com.qqx.model.Item;
import com.qqx.model.PredictedItemRating;
import com.qqx.recommend.Delphi;
import com.qqx.similarity.CosineSimilarity;

public class MainTest {
	private static String basePath = System.getProperty("user.dir");
	private static String userPath = basePath+"/data/u.user";
	private static String itemPath = basePath+"/data/u.item";
	private static String ratingPath = basePath+"/data/u.test";
	
	public static void main(String[] args) {
		DataSetImpl ds = new DataSetImpl();
		
		ds.load(userPath, itemPath, ratingPath);
		ds.printRatingMatrix();
		
		ItemBasedSimilarityMatrix similarity = new ItemBasedSimilarityMatrix(ds,new CosineSimilarity());
//		ItemBasedSimilarityMatrix similarity = new ItemBasedSimilarityMatrix(ds,new JaccardSimilarity());
		similarity.printSimilarityMatrix();
		
		Map<String,Item> map = similarity.getItemSimilarity(4);
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			Item item = map.get(key);
			System.out.println(key + " : " + item.toString());
		}

		KNearestNeighbor knn = new KNearestNeighbor(similarity);
//		Set<Integer> set = knn.getKNearestNeigtbor(4);
//		for(int i : set){
//			System.out.print(i + " ");
//		}
		
		System.out.println("\n======¿ªÊ¼ÍÆ¼ö===============");
		Delphi delphi = new Delphi(ds, similarity, knn);
		List<PredictedItemRating> list = delphi.recommend(ds.getUser(2), 2);
		for(PredictedItemRating p : list){
			System.out.println(p.toString());
		}
		
	}

}
