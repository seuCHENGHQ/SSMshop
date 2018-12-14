package com.chq.o2o.service;

import java.io.InputStream;

import com.chq.o2o.dto.ImageHolder;
import com.chq.o2o.dto.ShopExecution;
import com.chq.o2o.entity.Shop;

public interface ShopService {
	/**
	 * 添加店铺
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 */
	//为什么需要fileName呢，因为在CommonsMultipartFile对象中时可以获取到名字的，而inputStream中获取不到名字，而Thumbnail需要文件名来获取图片的扩展名
	ShopExecution addShop(Shop shop, ImageHolder imageHolder);
	
	/**
	 * 通过shopId从数据库中查找对应的shop对象
	 * @param shopId
	 * @return
	 */
	Shop getByShopId(Long shopId);
	
	/**
	 * 修改店铺信息
	 * @param shop
	 * @param shopImgInputStream
	 * @param fileName
	 * @return
	 */
	ShopExecution modifyShop(Shop shop, ImageHolder imageHolder);
	
	/**
	 * 根据shopCondition查询返回相应的列表数据
	 * @param shopCondition
	 * @param pageIndex 注意这里是页的索引值,不是rowIndex
	 * @param pageSize
	 * @return
	 */
	//为什么DAO层要输入行数,service层要输入页数呢?  因为前端显示的时候都是以页为单位的,而在数据库查询的时候,是以行为单位.  所以还需要一个进行pageIndex和rowIndex进行转换的工具类
	ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);
}
