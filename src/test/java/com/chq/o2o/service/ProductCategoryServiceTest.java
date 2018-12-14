package com.chq.o2o.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chq.o2o.BaseTest;
import com.chq.o2o.entity.ProductCategory;

public class ProductCategoryServiceTest extends BaseTest {
	@Autowired
	private ProductCategoryService productCategroyService;
	
	@Test
	public void getProductCategroyListTest(){
		long shopId = 1;
		List<ProductCategory> productCategoryList = productCategroyService.getProductCategoryList(shopId);
		assertEquals(2,productCategoryList.size());
	}
}
