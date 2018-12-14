package com.chq.o2o.entity;

import java.util.Date;

public class ProductCategory {
	private Long productCategoryId;
	/*
	 * 该商品类别是某个店铺下的类别，
	 * 想想也十分合理，
	 * 某个商店会主营某一类商品，这时需要定义这个商店所属的类别，
	 * 商店的老板会给自己上架的商品添加商品所属的店铺内的类别
	 * 
	 * 表明该商品类别是由哪个商店创建出来的
	 */
	private Long shopId;
	private String productCategoryName;
	// 商品权重，当我们选择某商品时，权重越大，显示的越靠前
	private Integer priority;
	private Date createTime;

	public Long getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
