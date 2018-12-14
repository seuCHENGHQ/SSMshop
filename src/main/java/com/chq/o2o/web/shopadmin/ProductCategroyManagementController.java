package com.chq.o2o.web.shopadmin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import com.chq.o2o.dto.ProductCategoryExecution;
import com.chq.o2o.entity.ProductCategory;
import com.chq.o2o.entity.Shop;
import com.chq.o2o.enums.ProductCategoryStateEnum;
import com.chq.o2o.exception.ProductCategoryOperationException;
import com.chq.o2o.service.ProductCategoryService;

/**
 * 负责与商品类别相关的页面进行数据交互
 * 
 * @author CHQ
 *
 */
@Controller
@RequestMapping("/shopadmin")
// shopadmin表明这是店家管理系统下面的controller
public class ProductCategroyManagementController {
	@Autowired
	private ProductCategoryService productCategroyService;

	@RequestMapping(value = "/getproductcategorylist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductCategoryList(HttpServletRequest request) {
		// 这里把shopId=1的shop对象写入currentShop中，是为了测试，等到系统上线的时候，会在shopmanagementcontroller中将current写入session
		// Shop shop = new Shop();
		// shop.setShopId(1L);
		// request.getSession().setAttribute("currentShop", shop);

		Map<String, Object> modelMap = new HashMap<>();

		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		if (currentShop != null && currentShop.getShopId() > 0) {
			List<ProductCategory> productCategoryList = productCategroyService
					.getProductCategoryList(currentShop.getShopId());
			modelMap.put("success", true);
			modelMap.put("productCategoryList", productCategoryList);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", ProductCategoryStateEnum.NULL_CURRENT_SHOP.getState());
		}
		return modelMap;
	}

	@RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	// ReponseBody是返回JSON字符串，RequestBody是接收JSON字符串
	private Map<String, Object> addProductCategorys(@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
		for (ProductCategory item : productCategoryList) {
			item.setShopId(currentShop.getShopId());
			item.setCreateTime(new Date());
		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution pce = productCategroyService.batchInsertProductCategory(productCategoryList);
				if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getStateCode()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pce.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少放入一个商品类别");
		}
		return modelMap;
	}

	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		if (productCategoryId != null && productCategoryId > 0) {
			try {
				Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
				ProductCategoryExecution pce = productCategroyService.deleteProductCategory(productCategoryId,
						currentShop.getShopId());
				if (pce.getState() == ProductCategoryStateEnum.SUCCESS.getStateCode()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pce.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入的productCategoryId不能为空");
		}
		return modelMap;
	}

}
