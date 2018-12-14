package com.chq.o2o.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.chq.o2o.BaseTest;
import com.chq.o2o.entity.ProductCategory;

//FixMethodOrder来控制测试方法的执行顺序
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {
	
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	public void BqueryProductCategoryListTest(){
		long shopId = 1;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		assertEquals(4,productCategoryList.size());
	}
	
	@Test
	public void AbatchInsertProductCategoryTest(){
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryName("商品名称1");
		productCategory.setPriority(1);
		productCategory.setCreateTime(new Date());
		productCategory.setShopId(1L);
		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("商品名称2");
		productCategory2.setPriority(2);
		productCategory2.setCreateTime(new Date());
		productCategory2.setShopId(1L);
		List<ProductCategory> productCategoryList = new ArrayList<>();
		productCategoryList.add(productCategory);
		productCategoryList.add(productCategory2);
		int effectRows = productCategoryDao.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectRows);
	}
	
	@Test
	public void CdeleteProductCategoryTest(){
		long shopId = 1;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		for(ProductCategory item : productCategoryList){
			if("商品名称1".equals(item.getProductCategoryName())||"商品名称2".equals(item.getProductCategoryName())){
				int effectedRows = productCategoryDao.deleteProductCategory(item.getProductCategoryId(), shopId);
				assertEquals(1, effectedRows);
			}
		}
	}
}
