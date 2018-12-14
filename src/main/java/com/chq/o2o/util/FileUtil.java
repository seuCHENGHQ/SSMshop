package com.chq.o2o.util;

import java.io.File;

public class FileUtil {

	public static void deleteFile(String imgAddr) {
		// TODO Auto-generated method stub
		String basePath = PathUtil.getImgBasePath();
		File imageFile = new File(basePath+imgAddr);
		if (imageFile.exists()) {
			//如果输入的是一个文件夹，则递归删除
			if (imageFile.isDirectory()) {
				File files[] = imageFile.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
			imageFile.delete();
		}
	}

}
