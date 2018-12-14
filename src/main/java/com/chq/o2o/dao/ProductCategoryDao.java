package com.chq.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.chq.o2o.entity.ProductCategory;

public interface ProductCategoryDao {

	/**
	 * 通过shop id查询该店铺下的设置的产品分类信息
	 * 
	 * @param shopId
	 * @return 返回该店铺设置的产品类别信息
	 */
	List<ProductCategory> queryProductCategoryList(@Param("shopId") Long shopId);

	/**
	 * 批量添加商品类别
	 * 
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(@Param("productCategoryList") List<ProductCategory> productCategoryList);
	
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
}
