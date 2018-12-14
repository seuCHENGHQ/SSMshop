package com.chq.o2o.service;

import java.util.List;

import com.chq.o2o.dto.ProductCategoryExecution;
import com.chq.o2o.entity.ProductCategory;
import com.chq.o2o.exception.ProductCategoryOperationException;

public interface ProductCategoryService {

	/**
	 * 获取某个shopId下的所有商品类别
	 * 
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> getProductCategoryList(Long shopId);

	/**
	 * 批量插入ProductCategory
	 * 
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException;

	/**
	 * 在删除某个商品类别时，可能碰到该商品类别下面还挂载有商品的情况，此时应该先将挂在在该商品类别下的商品的商品类别ID置为null，然后再删除该商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException;
}
