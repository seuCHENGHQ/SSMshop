package com.chq.o2o.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chq.o2o.BaseTest;
import com.chq.o2o.dto.ImageHolder;
import com.chq.o2o.dto.ProductExecution;
import com.chq.o2o.entity.Product;
import com.chq.o2o.entity.ProductCategory;
import com.chq.o2o.entity.Shop;
import com.chq.o2o.enums.ProductStateEnum;

public class ProductServiceTest extends BaseTest {
	
	@Autowired
	private ProductService productService;
	
	@Test
	public void addProductTest() throws FileNotFoundException{
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(1L);
		product.setShop(shop);
		product.setProductId(1L);
		product.setProductCategory(pc);
		product.setCreateTime(new Date());
		product.setPriority(20);
		product.setProductDesc("测试商品1");
		product.setProductName("测试商品");
		//设置为上架状态
		product.setEnableStatus(1);
		//创建缩略图文件流
		File thumbnailFile = new File("C:\\Users\\CHQ\\Desktop\\testPicture.png");
		//创建商品详情图片
		File normalImageFile1 = new File("C:\\Users\\CHQ\\Desktop\\TIM图片20180808161523.png");
		File normalImageFile2 = new File("C:\\Users\\CHQ\\Desktop\\微信图片_20180808163756.jpg");
		
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder("testPicture.png", is);
		List<ImageHolder> productImgList = new ArrayList<>();
		InputStream is1 = new FileInputStream(normalImageFile1);
		productImgList.add(new ImageHolder("TIM图片20180808161523.png", is1));
		InputStream is2 = new FileInputStream(normalImageFile2);
		productImgList.add(new ImageHolder("微信图片_20180808163756.jpg", is2));
		
		
		ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
	}
}
