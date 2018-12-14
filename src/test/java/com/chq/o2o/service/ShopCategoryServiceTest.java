package com.chq.o2o.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chq.o2o.BaseTest;
import com.chq.o2o.entity.ShopCategory;

public class ShopCategoryServiceTest extends BaseTest{
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Test
	public void getShopCategoryListTest(){
		List<ShopCategory> test = null;
		test = shopCategoryService.getShopCategoryList(new ShopCategory());
		assertEquals(2,test.size());
	}
}
