package com.chq.o2o.entity;

import java.util.Date;

public class ProductImg {
	private Long productImgId;
	// 图片地址
	private String ImgAddr;
	// 图片描述
	private String ImgDesc;
	// 图片的显示权重，越大显示的越靠前
	private Integer priority;
	private Date createTime;
	// 指明是哪个商品的详情图片，一个商品会对应多个图片，多对一的关系
	private Long productId;

	public Long getProductImgId() {
		return productImgId;
	}

	public void setProductImgId(Long productImgId) {
		this.productImgId = productImgId;
	}

	public String getImgAddr() {
		return ImgAddr;
	}

	public void setImgAddr(String imgAddr) {
		ImgAddr = imgAddr;
	}

	public String getImgDesc() {
		return ImgDesc;
	}

	public void setImgDesc(String imgDesc) {
		ImgDesc = imgDesc;
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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
