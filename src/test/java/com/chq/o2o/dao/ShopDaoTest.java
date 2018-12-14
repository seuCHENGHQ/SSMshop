package com.chq.o2o.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chq.o2o.BaseTest;
import com.chq.o2o.entity.Area;
import com.chq.o2o.entity.PersonInfo;
import com.chq.o2o.entity.Shop;
import com.chq.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest {
	@Autowired
	private ShopDao shopDao;
	
	@Test
	//加了ignore标签之后，junit就不会再测试该方法了
	@Ignore
	public void testInsertShop(){
		Shop shop=new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(1);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试店铺");
		shop.setShopDesc("test");
		shop.setShopAddr("test");
		shop.setPhone("test");
		shop.setPriority(1);
		shop.setShopImg("test");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(1);
		shop.setAdvice("审核中");
		int effectedNum=shopDao.insertShop(shop);
		assertEquals(1,effectedNum);
	}
	
	@Test
	@Ignore
	public void testUpdateShop(){
		Shop shop=new Shop();
		shop.setShopId(1L);
		shop.setShopDesc("测试描述");
		shop.setShopAddr("测试地址");
		shop.setAdvice("审核中");
		shop.setLastEditTime(new Date());
		int effectedNum=shopDao.updateShop(shop);
		assertEquals(1,effectedNum);
	}
	
	@Test
	@Ignore
	public void queryByShopIdTest(){
		long shopId = 1;
		Shop shop = shopDao.queryByShopId(shopId);
		System.out.println(shop.getArea().getAreaId());
		System.out.println(shop.getArea().getAreaName());
	}
	
	@Test
	public void queryShopListTest(){
		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 2);
		System.out.println("显示出来的店铺列表的大小:"+shopList.size());
	}
	
	@Test
	public void queryShopCountTest(){
		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		ShopCategory sc = new ShopCategory();
		sc.setShopCategoryId(2L);
		shopCondition.setOwner(owner);
		shopCondition.setShopCategory(sc);
		int shopListSize = shopDao.queryShopCount(shopCondition);
		assertEquals(1, shopListSize);
	}
}
