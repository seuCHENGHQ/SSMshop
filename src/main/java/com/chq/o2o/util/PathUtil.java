package com.chq.o2o.util;

import java.io.File;

public class PathUtil {
	// 获取当前系统的文件系统的路径分隔符，win是“\”，linux和mac是“/”
//	private static String seperator = System.getProperty("file.seperator");
	private static String seperator = File.separator;

	/**
	 * 工具类的工具方法，返回项目中存放图片的绝对路径的根目录
	 * 
	 * @return 返回整个项目存放图片的绝对路径的根目录
	 */
	public static String getImgBasePath() {
		String os = System.getProperty("os.name");
		String basePath = "";
		if (os.toLowerCase().startsWith("win")) {
			basePath = "D:/o2oProjectDev/image/";
		} else {
			basePath = "/home/chq/image/";
		}
		// 这里针对不同的系统做路径分隔符的替换
		basePath = basePath.replace("/", seperator);
		return basePath;
	}

	/**
	 * 这里依据不同的业务需求，返回图片的子路径
	 * 
	 * @param shopId
	 * @return 对应shopId图片所在文件夹的子路径
	 */
	public static String getShopImgPath(Long shopId) {
		// 不同的shop的图片存在不同的文件夹中
		String imagePath = "upload/item/shop/" + shopId + "/";
		imagePath = imagePath.replace("/", seperator);
		return imagePath;
	}
}
