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
	 * 项目相似度矩阵
	 */
	private double[][] similarityMatrix = null;
	/**
	 * 相似度计算实例
	 */
	private Similarity similarityInter = null;
	/**
	 * 数据集实例
	 */
	private Dataset dataSet = null;
	/**
	 * 矩阵行数
	 */
	private int nRows;
	
	public ItemBasedSimilarityMatrix(Dataset dataSet,Similarity similarity) {
		long begin = System.currentTimeMillis();
		System.out.println("================开始初始化相似度矩阵======================");
		this.similarityInter = similarity;
		this.dataSet = dataSet;
		this.nRows = dataSet.getItemCount();
		calculate();
		long end = System.currentTimeMillis();
    	System.out.println("==============相似度矩阵初始化完成,用时:"+(end-begin)+" ms=========");
	}

	/**
	 * 计算相似度矩阵
	 * @param dataSet
	 */
	private void calculate() {
		int nItems = dataSet.getItemCount();	//项目数
		similarityMatrix = new double[nItems][nItems];
		
		for(int u = 0;u<nItems;u++){
			int itemAId = getObjIdFromIndex(u);
			Item itemA = dataSet.getItem(itemAId);	//获取项目A
			for(int v=u+1; v<nItems; v++){
				int itemBId = getObjIdFromIndex(v);
				Item itemB = dataSet.getItem(itemBId);
				//计算相似度
				similarityMatrix[u][v] = similarityInter.calculateSimilarity(itemA, itemB);
			}
			//主对角线上的元素设为1
			similarityMatrix[u][u] = 1.0;
		}
		
	}
	
	/**
	 * 打印相似度矩阵
	 */
	public void printSimilarityMatrix(){
		DecimalFormat df = new DecimalFormat("######0.00");
		
		if(similarityMatrix == null){
			System.out.println("相似度矩阵为空！");
		}else{
			System.out.println("==============相似度矩阵===============");
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
	 * 获取项目与其他项目的相似度集合,并按相似度降序排列
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
			if(i == itemId) continue;	//过滤掉自身
			double similarity = 0.0;
			if(i > itemId){		//因为只初始化了上三角矩阵，所以此处需要做处理
				similarity = similarityMatrix[itemId][i];
			}else{
				similarity = similarityMatrix[i][itemId];
			}
			
			//防止相同的相似度造成键值重复，因此作如下处理
			String key = Math.round(similarity*1000000)+""+i;
			map.put(key, dataSet.getItem(getObjIdFromIndex(i)));
		}

		return map;
	}
	
	/**
	 * 获取两个项目之间的相似度
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
        
        //因为只初始化了上三角形矩阵，所以需要进行index变换
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
	 * 将矩阵index转换为item ID
	 * @param index
	 * @return
	 */
	public static int getObjIdFromIndex(int index){
		return index+1;
	}
	
	/**
	 * 将item ID转换为矩阵index
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
