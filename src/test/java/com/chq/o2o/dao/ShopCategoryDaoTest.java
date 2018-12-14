package com.chq.o2o.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chq.o2o.BaseTest;
import com.chq.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest extends BaseTest{
	
	@Autowired
	private ShopCategoryDao shopCategoryDao;
	
	@Test
	public void queryShopCategoryTest(){
		List<ShopCategory> test = null;
//		test = shopCategoryDao.queryShopCategory(new ShopCategory());
//		assertEquals(1,test.size());
		ShopCategory sc1 = new ShopCategory();
		ShopCategory sc2 = new ShopCategory();
		sc1.setShopCategoryId(1L);
		sc2.setParent(sc1);
		test = shopCategoryDao.queryShopCategory(sc2);
		assertEquals(1, test.size());
		System.out.println(test.get(0).getShopCategoryName());
	}
}
