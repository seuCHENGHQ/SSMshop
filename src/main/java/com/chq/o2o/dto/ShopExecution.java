package com.chq.o2o.dto;

import java.util.List;

import com.chq.o2o.entity.Shop;
import com.chq.o2o.enums.ShopStateEnum;

/**
 * 我们在对店铺进行操作的时候，还需要知道这次的操作是否成功
 * 如果失败，失败的状态码是什么，失败的状态标识是什么
 * @author CHQ
 *
 */
public class ShopExecution {
	//结果状态
	private int state;
	
	//状态标识
	private String stateInfo;
	
	//店铺数量
	private int count;
	
	//操作的shop(增删改店铺的时候用到)
	private Shop shop;
	
	//shop列表(查询店铺列表的时候用到)
	private List<Shop> shopList;
	
	public ShopExecution(){
		
	}
	
	/**
	 * 店铺操作失败时使用的构造器
	 * 这个构造方法主要是针对操作失败的时候，
	 * 返回状态码和状态表示，
	 * 并不返回shop对象
	 * @param stateEnum
	 */
	public ShopExecution(ShopStateEnum stateEnum){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	
	/**
	 * 店铺操作成功的时候使用的构造器
	 * @param stateEnum
	 * @param shop
	 */
	public ShopExecution(ShopStateEnum stateEnum, Shop shop){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.shop = shop;
	}
	
	/**
	 * 店铺操作成功时使用的构造器
	 * 当操作成功时，使用该构造器来构造shopExecution对象
	 * @param stateEnum
	 * @param shopList
	 */
	public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList){
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.shopList = shopList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
	
	
}
