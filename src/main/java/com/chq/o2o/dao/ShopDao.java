package com.chq.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.chq.o2o.entity.Shop;

public interface ShopDao {
	/**
	 * 插入商店
	 * @param shop
	 * @return 数据库中被影响的条数
	 */
	int insertShop(Shop shop);
	
	/**
	 * 更改店铺信息
	 * @param shop
	 * @return 数据库中被影响的数据的行数
	 */
	int updateShop(Shop shop);
	
	/**
	 * 通过shopId查询店铺信息
	 * @param shopId
	 * @return
	 */
	Shop queryByShopId(Long shopId);
	
	/**
	 * 分页查询商店,可输入的条件有:店铺名(模糊),店铺状态,店铺类别,区域,owner
	 * @param shopCondition 其中装载了上边写的5个输入条件
	 * @param rowIndex 从第几行开始取
	 * @param pageSize 取多少行
	 * @return
	 */
	List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 
	 * @return 返回queryShopList总数
	 */
	int queryShopCount(@Param("shopCondition") Shop shopCondition);
}
