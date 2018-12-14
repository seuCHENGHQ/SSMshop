package com.chq.o2o.service;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.chq.o2o.dto.ImageHolder;
import com.chq.o2o.dto.ProductExecution;
import com.chq.o2o.entity.Product;

public interface ProductService {

	/**
	 * 通过查询条件进行模糊查询
	 * 
	 * @param productCondition 其中封装了查询条件
	 * @param pageIndex	从第几页开始
	 * @param pageSize	每页有多少条数据
	 * @return
	 */
	ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);

	/**
	 * 
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);

//	/**
//	 * 添加商品信息及对应的图片
//	 * 通过观察我们发现，5个参数其实有点太多了
//	 * InputStream thumbnail, String fileName和List<InputStream> productImgs,List<String> fileNames
//	 * 其实都可以封装为ImageHolder,List<ImageHolder>对象
//	 * 从而减少接口中传入参数的数量，增加清晰度
//	 * 
//	 * @param product
//	 * @param thumbnail
//	 * @param productImgs
//	 * @return
//	 * @throws RuntimeException
//	 */
//	 ProductExecution addProduct(Product product, InputStream thumbnail,
//	 String fileName, List<InputStream> productImgs,
//	 List<String> fileNames) throws RuntimeException;
	
	/**
	 * 
	 * @param product
	 * @param thumbnail
	 * @param thumbnals
	 * @return
	 */
	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> thumbnals);

	/**
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 * @throws RuntimeException
	 */
	ProductExecution modifyProduct(Product product, ImageHolder thumbnail,
			List<ImageHolder> productImgs) throws RuntimeException;
	
	
}
