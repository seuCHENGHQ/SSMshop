package com.chq.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * AdminController负责对外界的url访问进行路由，指向指定的页面 关于店铺及店铺内管理的所有路由都放在这里
 * 我们不想让外界直接访问html页面，因此才需要这个路由
 * 
 * @author CHQ
 *
 */
@Controller
@RequestMapping(value = "/shopadmin", method = RequestMethod.GET)
public class ShopAdminController {

	// 我们不希望外界直接访问到商店的注册页面，因此这里写一个controller做一下转发
	@RequestMapping(value = "/shopoperation")
	public String shopOperation() {
		// 这里直接找到了/WEB-INF/shop/shopoperation.html文件，并用它做视图解析器进行显示
		return "shop/shopoperation";
	}

	@RequestMapping(value = "/shoplist")
	public String shopList() {
		return "shop/shoplist";
	}

	@RequestMapping(value = "/shopmanagement")
	public String shopManage() {
		return "shop/shopmanagement";
	}

	@RequestMapping(value = "/productcategorymanagement")
	public String productCategoryManage() {
		return "shop/productcategorymanage";
	}
	
	@RequestMapping(value="/productoperation")
	public String productOperation() {
		return "shop/productoperation";
	}
	
	@RequestMapping(value="/productmanage")
	public String productManage(){
		return "shop/shopmanage";
	}
}
