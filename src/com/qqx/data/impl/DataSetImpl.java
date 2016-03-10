package com.qqx.data.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qqx.data.Dataset;
import com.qqx.model.Item;
import com.qqx.model.Rating;
import com.qqx.model.User;
import com.qqx.util.FileUtils;

/**
 * ��������
 * @author qqx
 *
 */
public class DataSetImpl implements Dataset {
	/**
	 * �������ݷָ��
	 */
	private static final String RATING_DATA_SPLITE = "	";
	/**
	 * �û���Ϣ���ݷָ��
	 */
	private static final String USER_DATA_SPLITE = "\\|";
	/**
	 * ��Ŀ��Ϣ���ݷָ��
	 */
	private static final String ITEM_DATA_SPLITE = "\\|";
	
	/*
     * ���ּ���
     */
    private List<Rating> allRatings = new ArrayList<Rating>();
    
    /*
     * �û�����
     */
    private Map<Integer, User> allUsers = new HashMap<Integer, User>();
    
    /*
     * ��Ŀ����
     */
    private Map<Integer, Item> allItems = new HashMap<Integer, Item>(); 
    
    /*
     * �û������ּ��ϣ�userId->rating
     */
    Map<Integer, List<Rating>> ratingsByUserId = new HashMap<Integer, List<Rating>>();
    
    /**
     * ��������
     * @param userPath
     * @param itemPath
     * @param ratingPath
     * @return
     */
    public boolean load(String userPath,String itemPath,String ratingPath){
    	//�����û���Ϣ����
    	List<String> dataList = FileUtils.readFileByLine(new File(userPath));
    	for(String line : dataList){
    		User u = new User(line, USER_DATA_SPLITE);
    		allUsers.put(u.getUserId(), u);
    	}
    	
    	//������Ŀ��Ϣ����
    	dataList = FileUtils.readFileByLine(new File(itemPath));
    	for(String line : dataList){
    		Item i = new Item(line, ITEM_DATA_SPLITE);
    		allItems.put(i.getItemId(), i);
    	}
    	
    	//�����û���������
    	dataList = FileUtils.readFileByLine(new File(ratingPath));
    	for(String line : dataList){
    		Rating r = new Rating(line,RATING_DATA_SPLITE);
    		List<Rating> list;
    		if(ratingsByUserId.keySet().contains(r.getUserId())){
    			list = ratingsByUserId.get(r.getUserId());
    		}else{
    			list = new ArrayList<Rating>();
    		}
    		list.add(r);
			ratingsByUserId.put(r.getUserId(), list);
			allRatings.add(r);
			
			//��������ӵ���Ŀ������
			Item item = allItems.get(r.getItemId());
			item.setUserRating(r);
			
			User user = allUsers.get(r.getUserId());
			user.setItemRating(r);
    	}
    	
    	return true;
    }
    
    /**
     * ����û����־���
     */
    public void printRatingMatrix(){
    	int nUser = allUsers.size();
    	int nItem = allItems.size();
    	int[][] ratingMatrix = new int[nUser][nItem];
    	
    	for(int i=1,len=ratingsByUserId.size(); i<=len; i++){
    		for(Rating r : ratingsByUserId.get(i)){
    			ratingMatrix[r.getUserId()-1][r.getItemId()-1] = r.getRating();
    		}
    	}
    	
    	for(int i=0;i<nUser;i++){
    		for(int j=0;j<nItem;j++){
    			System.out.print(ratingMatrix[i][j]+" ");
    		}
    		System.out.println();
    	}
    }
    

	@Override
	public int getRatingsCount() {
		return allRatings.size();
	}

	@Override
	public int getUserCount() {
		return allUsers.size();
	}

	@Override
	public int getItemCount() {
		return allItems.size();
	}

	@Override
	public Collection<User> getUsers() {
		return allUsers.values();
	}

	@Override
	public Collection<Item> getItems() {
		return allItems.values();
	}

	@Override
	public User getUser(Integer userId) {
		return allUsers.get(userId);
	}

	@Override
	public Item getItem(Integer itemId) {
		return allItems.get(itemId);
	}

	@Override
	public Collection<Rating> getRatings() {
		return allRatings;
	}

	@Override
	public double getAverageItemRating(int itemId) {
		Item item = allItems.get(itemId);
		Collection<Rating> ratingList = item.getAllRatings();
		int ratingSum = 0;
		for(Rating r : ratingList){
			ratingSum += r.getRating();
		}
		return (double)ratingSum/allItems.size();
	}

	@Override
	public double getAverageUserRating(int userId) {
		List<Rating> ratingList = ratingsByUserId.get(userId);
		int ratingSum = 0;
		for(Rating r : ratingList){
			ratingSum += r.getRating();
		}
		return (double)ratingSum/allUsers.size();
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isIdMappingRequired() {
		return false;
	}

	@Override
	public String[] getAllTerms() {
		return null;
	}


	public Map<Integer, List<Rating>> getRatingsByUserId() {
		return ratingsByUserId;
	}

}
