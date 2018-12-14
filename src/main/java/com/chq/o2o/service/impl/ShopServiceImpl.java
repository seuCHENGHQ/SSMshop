package com.chq.o2o.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chq.o2o.dao.ShopDao;
import com.chq.o2o.dto.ImageHolder;
import com.chq.o2o.dto.ShopExecution;
import com.chq.o2o.entity.Shop;
import com.chq.o2o.enums.ShopStateEnum;
import com.chq.o2o.exception.ShopOperationException;
import com.chq.o2o.service.ShopService;
import com.chq.o2o.util.ImageUtil;
import com.chq.o2o.util.PageCalculator;
import com.chq.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopDao shopDao;

	@Override
	@Transactional
	public ShopExecution addShop(Shop shop, ImageHolder imageHolder) {
		// TODO Auto-generated method stub
		// 第一步，做空值判断，shop中的成员变量也要做空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		} else if (shop.getOwner().getUserId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_OWNER);
		} else if (shop.getShopCategory().getShopCategoryId() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOPCATEGORY);
		} else if (shop.getShopName() == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOPNAME);
		}
		try {
			// 第二步，给店铺赋初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// 第三步，添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			System.out.println("after insert:" + shop.getShopId());
			if (effectedNum <= 0) {
				// 只有抛出ShopOperationException或者其子类，事务失败时才会回滚，Exception是不会回滚的
				throw new ShopOperationException("店铺创建失败");
			} else {
				if (imageHolder.getImage() != null) {
					try {
						addShopImg(shop, imageHolder);
					} catch (Exception e) {
						// 这里是当添加商店的图片发生异常时，抛出runtimeExceptino以回滚事务
						// 为什么catchException呢？这是因为我们不知道addShopImg中会抛出什么样的异常
						throw new ShopOperationException(("addShopImg error:") + e.getMessage());
					}
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		// 所有操作都正常完成，这里返回一个CHECK状态的shopExecution对象，同时把传入数据库中的店铺也一并返回
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	private void addShopImg(Shop shop, ImageHolder imageHolder) {
		// TODO Auto-generated method stub
		String dest = PathUtil.getShopImgPath(shop.getShopId());
		// System.out.println(dest);
		String shopImgAddr = ImageUtil.generateThumbnail(imageHolder, dest);
		// System.out.println(shopImgAddr);
		shop.setShopImg(shopImgAddr);
	}

	@Override
	public Shop getByShopId(Long shopId) {
		// TODO Auto-generated method stub
		return shopDao.queryByShopId(shopId);
	}

	@Override
	/**
	 * 用于店铺信息的修改 fileName是用来获取文件扩展名的
	 */
	public ShopExecution modifyShop(Shop shop, ImageHolder imageHolder) {
		// TODO Auto-generated method stub
		try {
			if (shop == null || shop.getShopId() == null) {
				return new ShopExecution(ShopStateEnum.NULL_SHOP);
			} else {
				// 1.判断是否需要处理图片
				// 当输入流不为空，fileName不为空，也不为空字符串时，说明有新的店铺照片被上传了，因此要先将老的存放缩略图的文件夹删除掉，再把新的添加进去
				if (imageHolder.getImage() != null && imageHolder.getImageName() != null && !"".equals(imageHolder.getImageName())) {
					Shop tempShop = shopDao.queryByShopId(shop.getShopId());
					if (tempShop.getShopImg() != null) {
						ImageUtil.deleteFileOrPath(tempShop.getShopImg());
					}
					// 删除老的缩略图，并生成新的并添加进去
					addShopImg(shop, imageHolder);
				}
				// 2.更新店铺信息
				shop.setLastEditTime(new Date());
				int effectedNum = shopDao.updateShop(shop);
				if (effectedNum <= 0) {
					// 若店铺更新未成功，就返回一个执行结果，说明发生了内部错误
					return new ShopExecution(ShopStateEnum.INNER_ERROR);
				} else {
					// 店铺更新成功，返回执行结果是更新成功，然后将更新后的店铺信息一并返回
					shop = shopDao.queryByShopId(shop.getShopId());
					return new ShopExecution(ShopStateEnum.CHECK, shop);
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("modifyShop error:" + e.getMessage());
		}

	}

	@Override
	public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
		int shopListSize = shopDao.queryShopCount(shopCondition);
		ShopExecution se = new ShopExecution();
		if (shopList != null) {
			se.setCount(shopListSize);
			se.setShopList(shopList);
		}else{
			se.setState(ShopStateEnum.INNER_ERROR.getState());
		}
		return se;
	}

}
