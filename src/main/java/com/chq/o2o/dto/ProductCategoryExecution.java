package com.chq.o2o.dto;

import java.util.List;

import com.chq.o2o.entity.ProductCategory;
import com.chq.o2o.enums.ProductCategoryStateEnum;

public class ProductCategoryExecution {
	
	//操作的执行状态
	private int state;
	
	//操作的状态信息
	private String stateInfo;
	
	//操作成功后，
	private List<ProductCategory> productCategoryList;
	
	public ProductCategoryExecution(){
		
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

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}

	//操作失败时的构造方法
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum) {
		this.state = stateEnum.getStateCode();
		this.stateInfo = stateEnum.getState();
	}
	
	//操作成功时的构造方法
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum, List<ProductCategory> productCategoryList){
		this.state = stateEnum.getStateCode();
		this.stateInfo = stateEnum.getState();
		this.productCategoryList = productCategoryList;
	}
}
