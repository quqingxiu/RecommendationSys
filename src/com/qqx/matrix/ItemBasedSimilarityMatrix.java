package com.qqx.matrix;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import com.qqx.data.Dataset;
import com.qqx.model.Item;
import com.qqx.similarity.Similarity;


public class ItemBasedSimilarityMatrix {
	/**
	 * ��Ŀ���ƶȾ���
	 */
	private double[][] similarityMatrix = null;
	/**
	 * ���ƶȼ���ʵ��
	 */
	private Similarity similarityInter = null;
	/**
	 * ���ݼ�ʵ��
	 */
	private Dataset dataSet = null;
	/**
	 * ��������
	 */
	private int nRows;
	
	public ItemBasedSimilarityMatrix(Dataset dataSet,Similarity similarity) {
		long begin = System.currentTimeMillis();
		System.out.println("================��ʼ��ʼ�����ƶȾ���======================");
		this.similarityInter = similarity;
		this.dataSet = dataSet;
		this.nRows = dataSet.getItemCount();
		calculate();
		long end = System.currentTimeMillis();
    	System.out.println("==============���ƶȾ����ʼ�����,��ʱ:"+(end-begin)+" ms=========");
	}

	/**
	 * �������ƶȾ���
	 * @param dataSet
	 */
	private void calculate() {
		int nItems = dataSet.getItemCount();	//��Ŀ��
		similarityMatrix = new double[nItems][nItems];
		
		for(int u = 0;u<nItems;u++){
			int itemAId = getObjIdFromIndex(u);
			Item itemA = dataSet.getItem(itemAId);	//��ȡ��ĿA
			for(int v=u+1; v<nItems; v++){
				int itemBId = getObjIdFromIndex(v);
				Item itemB = dataSet.getItem(itemBId);
				//�������ƶ�
				similarityMatrix[u][v] = similarityInter.calculateSimilarity(itemA, itemB);
			}
			//���Խ����ϵ�Ԫ����Ϊ1
			similarityMatrix[u][u] = 1.0;
		}
		
	}
	
	/**
	 * ��ӡ���ƶȾ���
	 */
	public void printSimilarityMatrix(){
		DecimalFormat df = new DecimalFormat("######0.00");
		
		if(similarityMatrix == null){
			System.out.println("���ƶȾ���Ϊ�գ�");
		}else{
			System.out.println("==============���ƶȾ���===============");
//			for(int i=0,len=similarityMatrix.length;i<len;i++){
			for(int i=6,len=11;i<len;i++){
//				for(int j=0;j<similarityMatrix[i].length;j++){
				for(int j=0;j<similarityMatrix[i].length;j++){
					System.out.print(df.format(similarityMatrix[i][j])+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
	/**
	 * ��ȡ��Ŀ��������Ŀ�����ƶȼ���,�������ƶȽ�������
	 * @param itemId
	 */
	public Map<String, Item> getItemSimilarity(int objId) {
		Map<String, Item> map = new TreeMap<String, Item>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s2.compareTo(s1);
			}
		});
		
		int nItems = dataSet.getItemCount();
		int itemId = getIndexFromObjId(objId);
		for(int i=0; i<nItems; i++){
			if(i == itemId) continue;	//���˵�����
			double similarity = 0.0;
			if(i > itemId){		//��Ϊֻ��ʼ���������Ǿ������Դ˴���Ҫ������
				similarity = similarityMatrix[itemId][i];
			}else{
				similarity = similarityMatrix[i][itemId];
			}
			
			//��ֹ��ͬ�����ƶ���ɼ�ֵ�ظ�����������´���
			String key = Math.round(similarity*1000000)+""+i;
			map.put(key, dataSet.getItem(getObjIdFromIndex(i)));
		}

		return map;
	}
	
	/**
	 * ��ȡ������Ŀ֮������ƶ�
	 * @param idX
	 * @param idY
	 * @return
	 */
	public double getSimilarityValues(int idX,int idY) {
		if (similarityMatrix == null) {
            throw new IllegalStateException(
                    "You have to calculate similarities first.");
        }

        int x = getIndexFromObjId(idX);
        int y = getIndexFromObjId(idY);
        
        //��Ϊֻ��ʼ�����������ξ���������Ҫ����index�任
        int i, j;
        if (x <= y) {	
            i = x;
            j = y;
        } else {
            i = y;
            j = x;
        }
        return similarityMatrix[i][j];
	}

	/**
	 * ������indexת��Ϊitem ID
	 * @param index
	 * @return
	 */
	public static int getObjIdFromIndex(int index){
		return index+1;
	}
	
	/**
	 * ��item IDת��Ϊ����index
	 * @param index
	 * @return
	 */
	public static int getIndexFromObjId(int objId){
		return objId - 1;
	}

	public int getnRows() {
		return nRows;
	}
}
