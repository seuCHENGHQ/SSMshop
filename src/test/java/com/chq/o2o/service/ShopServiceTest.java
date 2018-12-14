package com.chq.o2o.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chq.o2o.BaseTest;
import com.chq.o2o.dto.ImageHolder;
import com.chq.o2o.dto.ShopExecution;
import com.chq.o2o.entity.Area;
import com.chq.o2o.entity.PersonInfo;
import com.chq.o2o.entity.Shop;
import com.chq.o2o.entity.ShopCategory;
import com.chq.o2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest {
	@Autowired
	private ShopService shopService;
	
	@Test
	@Ignore
	public void testAddShop(){
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺2");
		shop.setShopDesc("test2");
		shop.setShopAddr("test2");
		shop.setPhone("test2");
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg = new File("C:\\Users\\CHQ\\Desktop\\testPicture.png");
		InputStream shopImgInputStream;
		try {
			shopImgInputStream = new FileInputStream(shopImg);
			ImageHolder imageHolder = new ImageHolder(shopImg.getName(), shopImgInputStream);
			ShopExecution se = shopService.addShop(shop, imageHolder);
			assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	@Ignore
	public void modifyShopTest() throws FileNotFoundException{
		Shop shop = shopService.getByShopId(1L);
		shop.setShopName("修改后的店铺名称");
		File file = new File("C:\\Users\\CHQ\\Desktop\\testPicture2.png");
		InputStream is = new FileInputStream(file);
		ImageHolder imageHolder = new ImageHolder(file.getName(), is);
		ShopExecution se = shopService.modifyShop(shop, imageHolder);
		System.out.println(se.getShop().getShopName());
		System.out.println(se.getState());
	}
	
	@Test
	public void getShopListTest(){
		Shop shopCondition = new Shop();
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		shopCondition.setOwner(owner);
		ShopExecution se = shopService.getShopList(shopCondition, 3, 2);
		System.out.println("显示出来的店铺列表的大小:"+se.getShopList().size());
	}
}
