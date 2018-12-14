package com.chq.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.chq.o2o.dao.ProductDao;
import com.chq.o2o.dao.ProductImgDao;
import com.chq.o2o.dto.ImageHolder;
import com.chq.o2o.dto.ProductExecution;
import com.chq.o2o.entity.Product;
import com.chq.o2o.entity.ProductImg;
import com.chq.o2o.enums.ProductStateEnum;
import com.chq.o2o.exception.ProductOperationException;
import com.chq.o2o.service.ProductService;
import com.chq.o2o.util.FileUtil;
import com.chq.o2o.util.ImageUtil;
import com.chq.o2o.util.PageCalculator;
import com.chq.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;

	@Override
	public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<Product> productList = productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setCount(count);
		pe.setProductList(productList);
		return pe;
	}

	@Override
	public Product getProductById(long productId) {
		// TODO Auto-generated method stub
		return productDao.queryProductByProductId(productId);
	}

	@Override
	@Transactional
	/*
	 * 1. 处理缩略图，获取缩略图相对路径并赋值给product 2. 往tb_product写入商品信息，获取productId 3.
	 * 结合productId批量处理商品详情图片 4. 将商品详情图列表批量插入tb_product_img中
	 */
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList) {
		// TODO Auto-generated method stub
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			// 默认是上架状态
			product.setEnableStatus(1);
			if (thumbnail != null) {
				// 这里是添加商品的缩略图
				addThumbnail(product, thumbnail);
			}
			try {
				int effectedNum = productDao.insertProduct(product);
				if (effectedNum <= 0) {
					throw new ProductOperationException("添加商品失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("添加商品失败:" + e.getMessage());
			}
			// 若商品详情图不为空则添加
			if (productImgHolderList != null && productImgHolderList.size() > 0) {
				addProductImgList(product, productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS, product);

		} else {
			//传入参数为空，则直接报错
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	@Override
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgs) throws RuntimeException {
		// TODO Auto-generated method stub
		if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
			product.setLastEditTime(new Date());
			if (thumbnail != null) {
				Product tempProduct = productDao.queryProductByProductId(product.getProductId());
				if (tempProduct.getImgAddr() != null) {
					FileUtil.deleteFile(tempProduct.getImgAddr());
				}
				addThumbnail(product, thumbnail);
			}
			if (productImgs != null && productImgs.size() > 0) {
				deleteProductImgs(product.getProductId());
				addProductImgList(product, productImgs);
			}
			try {
				int effectedNum = productDao.updateProduct(product);
				if (effectedNum <= 0) {
					throw new RuntimeException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS, product);
			} catch (Exception e) {
				throw new RuntimeException("更新商品信息失败:" + e.toString());
			}
		} else {
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	private void deleteProductImgs(Long productId) {
		// TODO Auto-generated method stub
		
	}

	private void addThumbnail(Product product, ImageHolder thumbnail){
		//获取该商品所属店铺的图片文件夹地址，这里的地址是相对路径
		String dest = PathUtil.getShopImgPath(product.getShop().getShopId());
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumbnailAddr);
	}
	
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList){
		String dest = PathUtil.getShopImgPath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<>();
		//遍历上传的图片，在每次遍历中都生成一个ProductImg对象，并将其放入productImgList中
		for(ImageHolder imageHolder : productImgHolderList){
			String imgAddr = ImageUtil.generateNormalImg(imageHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		//这里是批量像数据库中添加商品详情图片的信息 
		if(productImgList.size()>0){
			try{
				int effectedRows = productImgDao.batchInsertProductImg(productImgList);
				if(effectedRows<=0){
					throw new ProductOperationException("创建商品详情图片失败");
				}
			}catch(Exception e){
				throw new ProductOperationException("创建商品详情图片失败:"+e.getMessage());
			}
		}
	}

}
