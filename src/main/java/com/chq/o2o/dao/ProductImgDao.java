package com.chq.o2o.dao;

import java.util.List;

import com.chq.o2o.entity.ProductImg;

public interface ProductImgDao {

	/**
	 * 查询某商品的全部图片
	 * @param productId
	 * @return
	 */
	List<ProductImg> queryProductImgList(long productId);

	/**
	 * 批量插入图片
	 * @param productImgList
	 * @return
	 */
	int batchInsertProductImg(List<ProductImg> productImgList);

	/**
	 * 批量删除图片，删除某商品下挂载的所有图片
	 * @param productId
	 * @return
	 */
	int deleteProductImgByProductId(long productId);
}
