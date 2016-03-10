package com.qqx.matrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.qqx.model.Item;

/**
 * K近邻实现类
 * @author qqx
 *
 */
public class KNearestNeighbor {
	/**
	 * 定义k的取值
	 */
	private static final int K = 2;
	/**
	 * 存储项目的k近邻,ItemId->ItemId集合
	 */
	private Map<Integer,Set<Integer>> knn = null;
	/**
	 * 相似度矩阵
	 */
	private ItemBasedSimilarityMatrix ibsMatrix = null;
	
	public KNearestNeighbor(ItemBasedSimilarityMatrix ibsMatrix) {
		knn = new HashMap<Integer, Set<Integer>>(ibsMatrix.getnRows());
		this.ibsMatrix = ibsMatrix;
		
		initKNearestNeighbor();
	}
	
	/**
	 * 初始化k近邻
	 */
	private void initKNearestNeighbor(){
		int nItems = ibsMatrix.getnRows();
		for(int i=0; i<nItems ;i++){
			int objId = ItemBasedSimilarityMatrix.getObjIdFromIndex(i);
			Map<String,Item> map = ibsMatrix.getItemSimilarity(objId);		//获取项目objId的相似度集合
			
			//获取与项目i相似度最高的前k个项目
			Set<String> keySet = map.keySet();
			Iterator<String> iter = keySet.iterator();
			int count = 0;
			while (iter.hasNext()) {
				if(count >= K){
					break;
				}
				count++;
				Item item = map.get(iter.next());
				
				Set<Integer> set = knn.get(objId);
				if(set == null){
					set = new HashSet<Integer>();
				}
				set.add(item.getItemId());
				knn.put(objId, set);
			}
		}
	}
	
	/**
	 * 获取项目itemId的k近邻
	 * @param itemId
	 * @return
	 */
	public Set<Integer> getKNearestNeigtbor(int itemId){
		return knn.get(itemId);
	}
	
	
	
}
