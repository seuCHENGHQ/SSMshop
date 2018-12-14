package com.chq.o2o.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chq.o2o.dao.ProductCategoryDao;
import com.chq.o2o.dto.ProductCategoryExecution;
import com.chq.o2o.entity.ProductCategory;
import com.chq.o2o.enums.ProductCategoryStateEnum;
import com.chq.o2o.exception.ProductCategoryOperationException;
import com.chq.o2o.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

	@Autowired
	private ProductCategoryDao productCategoryDao;

	@Override
	public List<ProductCategory> getProductCategoryList(Long shopId) {
		// TODO Auto-generated method stub
		return productCategoryDao.queryProductCategoryList(shopId);
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchInsertProductCategory(List<ProductCategory> productCategoryList)
			throws ProductCategoryOperationException {
		// TODO Auto-generated method stub
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedRows = productCategoryDao.batchInsertProductCategory(productCategoryList);
				if (effectedRows > 0) {
					return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS, productCategoryList);
				} else {
					throw new ProductCategoryOperationException("店铺类别创建失败");
				}
			} catch (Exception e) {
				throw new ProductCategoryOperationException("batchInsertProductCategory error:" + e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
		}
	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId)
			throws ProductCategoryOperationException {
		// TODO 将挂在在该商品类别下的商品的productCategoryId置为null，因为product部分的代码还没有编写，因此这里还未完成
		try {
			int effectedRows = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
			if (effectedRows < 0) {
				throw new ProductCategoryOperationException("商品类别删除失败");
			} else {
				return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
			}
		} catch (Exception e) {
			throw new ProductCategoryOperationException("deleteProductCategoryError: " + e.getMessage());
		}
	}

}
