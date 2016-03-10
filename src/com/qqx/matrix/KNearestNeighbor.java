package com.qqx.matrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.qqx.model.Item;

/**
 * K����ʵ����
 * @author qqx
 *
 */
public class KNearestNeighbor {
	/**
	 * ����k��ȡֵ
	 */
	private static final int K = 2;
	/**
	 * �洢��Ŀ��k����,ItemId->ItemId����
	 */
	private Map<Integer,Set<Integer>> knn = null;
	/**
	 * ���ƶȾ���
	 */
	private ItemBasedSimilarityMatrix ibsMatrix = null;
	
	public KNearestNeighbor(ItemBasedSimilarityMatrix ibsMatrix) {
		knn = new HashMap<Integer, Set<Integer>>(ibsMatrix.getnRows());
		this.ibsMatrix = ibsMatrix;
		
		initKNearestNeighbor();
	}
	
	/**
	 * ��ʼ��k����
	 */
	private void initKNearestNeighbor(){
		int nItems = ibsMatrix.getnRows();
		for(int i=0; i<nItems ;i++){
			int objId = ItemBasedSimilarityMatrix.getObjIdFromIndex(i);
			Map<String,Item> map = ibsMatrix.getItemSimilarity(objId);		//��ȡ��ĿobjId�����ƶȼ���
			
			//��ȡ����Ŀi���ƶ���ߵ�ǰk����Ŀ
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
	 * ��ȡ��ĿitemId��k����
	 * @param itemId
	 * @return
	 */
	public Set<Integer> getKNearestNeigtbor(int itemId){
		return knn.get(itemId);
	}
	
	
	
}
