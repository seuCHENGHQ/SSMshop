package com.chq.o2o.web.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.chq.o2o.dto.ImageHolder;
import com.chq.o2o.dto.ShopExecution;
import com.chq.o2o.entity.Area;
import com.chq.o2o.entity.PersonInfo;
import com.chq.o2o.entity.Shop;
import com.chq.o2o.entity.ShopCategory;
import com.chq.o2o.enums.ShopStateEnum;
import com.chq.o2o.service.AreaService;
import com.chq.o2o.service.ShopCategoryService;
import com.chq.o2o.service.ShopService;
import com.chq.o2o.util.CodeUtil;
import com.chq.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ManagementController负责和js进行数据交互
 * 这里负责与html页面交互 html页面获取数据和提交数据都是通过这里的路由来进行的
 * 
 * @author CHQ
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private AreaService areaService;

	@RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopInitInfo() {
		Map<String, Object> modelMap = new HashMap<>();
		List<ShopCategory> shopCategoryList = new ArrayList<>();
		List<Area> areaList = new ArrayList<>();
		try {
			// 这里获取所有的店铺种类信息
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("seccess", false);
			modelMap.put("errMsg", "验证码输入错误!");
		}

		// 1.接收并转化相应的参数，包括店铺信息以及图片信息
		// 这里的的"shopStr"是和前端约定好的一个键，否则肯定不知道要获取什么键值对，调用util中的哪个方法
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		// 这是pom.xml文件中导入的jackson-databin的jar包中的对象，用于pojo对象和json数据的相互转换
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		// 这里来处理用户上传的店铺照片文件
		CommonsMultipartFile shopImg = null;
		// 创建一个多部分解析器，从http请求的会话中获取上下文对象，然后以此创建出对应的multipart解析器
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断request中是否有文件上传，即是否有多部分请求
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传文件不能为空");
			return modelMap;
		}

		// 2.注册店铺
		if (shop != null && shopImg != null) {
			// 因为只有当登陆之后才能进行店铺的注册操作，一旦注册之后，就会将user信息写入到session的"user"键值对中去，因此这里可以直接读到
			PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
			shop.setOwner(owner);
			// 之前addShop方法是要传入File对象的，然是在编写controller层的时候，发现将multipartFile转换为File非常不便，并且增加了系统出错的可能性，将上传的文件保存在本地一份，也增加了系统垃圾
			// 因此，需要对ShopService进行重构，将File改为InputStream
			ShopExecution se;
			try {
				ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
				// 既然inputstream和filename都是基于CommonsMultipartFile来的，为什么不直接传一个CommonsMultipartFile对象到方法中去呢？
				// 这是因为CommonsMultipartFile对象比较难造出来，如果我们的方法是接收CommonsMultipartFile对象的，那么单元测试就会很难做，这都是权衡的结果
				se = shopService.addShop(shop, imageHolder);
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
					// 该用户可以操作的店铺列表，这也是在用户登陆后，就直接加载到session中的信息，"shopList"也是约定好的键值
					// 如果用户没有可以操作的店铺，但是此时创建了一个，就将其添加到shopList中
					List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
					if (shopList == null) {
						shopList = new ArrayList<>();
					}
					shopList.add(shop);
					request.getSession().setAttribute("shopList", shopList);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}
		// 3.返回结果
	}

	@RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopById(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		if (shopId > -1) {
			try {
				Shop shop = shopService.getByShopId(shopId);
				List<Area> areaList = areaService.getAreaList();
				modelMap.put("shop", shop);
				// 这里获取areaList有什么用？
				modelMap.put("areaList", areaList);
				modelMap.put("success", true);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty shopId");
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("seccess", false);
			modelMap.put("errMsg", "验证码输入错误!");
		}

		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		ObjectMapper mapper = new ObjectMapper();
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 如果用户在修改店铺信息的时候没有上传新的图片，那么就沿用老的店铺图片
		if (commonsMultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		}

		// 2.修改店铺信息
		if (shop != null && shop.getShopId() != null) {
			ShopExecution se;
			try {
				if (shopImg == null) {
					ImageHolder imageHolder = new ImageHolder(null, null);
					se = shopService.modifyShop(shop, imageHolder);
				} else {
					ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(), shopImg.getInputStream());
					se = shopService.modifyShop(shop, imageHolder);
				}
				if (se.getState() == ShopStateEnum.CHECK.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", se.getStateInfo());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺id");
			return modelMap;
		}
		// 3.返回结果
	}

	@RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getShopList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 这里设置user只是暂时的,因为用户登陆还没写,因此无法从session中获取到登陆用户的信息,因此才只能先自己设置一个user
		PersonInfo user = new PersonInfo();
		user.setUserId(1L);
		user.setName("test");
		request.getSession().setAttribute("user", user);
		user = (PersonInfo) request.getSession().getAttribute("user");
		try {
			Shop shopCondition = new Shop();
			shopCondition.setOwner(user);
			ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
			modelMap.put("success", true);
			modelMap.put("user", user);
			modelMap.put("shopList", se.getShopList());
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
		}
		return modelMap;
	}

	@RequestMapping(value = "/getshopmanagementinfo", method = RequestMethod.GET)
	@ResponseBody
	// 这个方法使用来控制违规操作的,如果用户没有登陆就直接访问该页面,则将其重定向到getshoplist页面上去
	private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<>();
		// 只有从shoplist页面点击进店铺之后,session中才会有shopId属性?
		long shopId = HttpServletRequestUtil.getLong(request, "shopId");
		// 一旦传入了shopId，程序就认为你有权限修改店铺信息，其实是否真的有权限，还是拦截器说了算
		if (shopId <= 0) {
			// 这里存储了当前正在被操作的店铺对象
			Object currentShopObj = request.getSession().getAttribute("currentShop");
			/*
			 * 这里的逻辑是：如果即没有从session中得到shopId，也没有currentShop，就说明客户是直接通过/shopadmin
			 * /shopmanagement.html来进行访问的 因此该用户没有权限进行店铺管理，要被送回shoplist界面去
			 */
			if (currentShopObj == null) {
				modelMap.put("redirect", true);
				modelMap.put("url", "/o2o/shopadmin/shoplist");
			} else {
				// 通过shopadmin/shopmanagement这个url直接访问，如果session中有currentShop对象，则说明用户之前通过正确的方式访问过店铺管理页面，因此也有权限进行店铺信息修改
				Shop currentShop = (Shop) currentShopObj;
				modelMap.put("redirect", false);
				modelMap.put("shopId", currentShop.getShopId());
			}
		} else {
			// 如果用户通过带ShopId的url访问，那就会在session中留下currentShop对象，以后就可以通过不带shopId的url直接访问店铺管理页面了
			Shop currentShop = new Shop();
			currentShop.setShopId(shopId);
			request.getSession().setAttribute("currentShop", currentShop);
			modelMap.put("redirect", false);
		}
		return modelMap;
	}

}
