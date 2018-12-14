package com.chq.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.chq.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

	// 因为我们的水印图片是保存在src/main/resources文件夹下的，这是当前项目的一个classpath，因此我们用获取到的当前线程的绝对路径，来定位classpath
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random r = new Random();

	/**
	 * 这里是生成缩略图的工具方法，商店的门面照和商品的缩略图都用该方法进行处理
	 * 因为用户上传图片直接用的是Spring自带的文件流，因此直接使用CommonsMultipartFile对象进行处理
	 * 
	 * @param thumbnail
	 *            用户上传的图片的CommonsMultipartFile对象
	 * @param targetAddr
	 *            缩略图要被存放到的目标文件夹
	 * @return 返回生成的缩略图的相对路径
	 */
	public static String generateThumbnail(CommonsMultipartFile thumbnail, String targetAddr) {
		// 因为用户上传的图片名字都是自己起的，因此很有可能重名，我们不用文件原来的名字，让系统重新设置一个名字
		String realFileName = getRandomFileName();
		// 获取图片的扩展名，.jpg .png等
		String extension = getFileExtension(thumbnail);
		// targetAddr这个路径可能不存在，因此先使用该方法把这个目录创建出来
		makeDirPath(targetAddr);
		// 通过上边的操作，将要存储在服务器上的文件的相对地址拼接出来
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			/*
			 * 创建缩略图，of中输入图片的输入流，size指定缩略图大小，watermark(缩略图所在位置，加入水印图片的图片文件，
			 * 水印图片的透明度) 压缩一下图片(输出大小是输入的0.8)，指定输出文件的位置
			 * 
			 * 为什么basePath+"/watermark.jpg"可以找到水印图片的位置呢？
			 * 水印图片和java代码明明不在一个classpath中 这是因为在项目的Java builder
			 * path中我们设定src/main/java和src/main/resources都输出到o2o/target/
			 * classes文件夹中， 因此在代码运行时，会找到classes文件夹的绝对路径，因而可以找到水印图片的位置
			 * 
			 * 若单元测试时，报错找不到水印图片，这是因为src/test/java和src/test/resources文件夹的输出目录是o2o
			 * /target/test-classes， 从而没法找到classes文件夹中的水印图片
			 * 解决办法是将水印图片复制到src/test/resources文件夹中一份
			 */
			Thumbnails.of(thumbnail.getInputStream()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
					.outputQuality(0.8).toFile(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(basePath);
		return relativeAddr;
	}

	/**
	 * 创建目标路径所涉及到的目录，如/home/work/CHQ/xxx.jpg 那么home work CHQ这三个目录都要被自动创建出来
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		// TODO Auto-generated method stub
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			// 若目标路径不存在，则递归的创建目录
			dirPath.mkdirs();
		}
	}

	/**
	 * 获取输入文件流的扩展名
	 * 
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(CommonsMultipartFile thumbnail) {
		// TODO Auto-generated method stub
		String originalFileName = thumbnail.getOriginalFilename();
		// lastIndexOf方法获取到文件扩展名所在的那个“.”的索引，然后用substring把扩展名取出来，例如“.jpg”，点也在获取到的扩展名内
		return originalFileName.substring(originalFileName.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前年月日小时分钟秒钟+5位随机数
	 * 
	 * @return
	 */
	private static String getRandomFileName() {
		// 获取随机的5位数，保证是在10000~99999之间的一个随机数
		int randomNumber = r.nextInt(89999) + 10000;
		String nowTimeStr = simpleDateFormat.format(new Date());
		// TODO Auto-generated method stub
		return nowTimeStr + randomNumber;
	}

	public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f)
					.outputQuality(0.8).toFile(dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(basePath);
		return relativeAddr;
	}

	private static String getFileExtension(String fileName) {
		// TODO Auto-generated method stub
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 删除传入的文件，如果传入的是路径，就将该路径的文件夹和其下面的文件全部删除
	 * 
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
		if (fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				for (File file : fileOrPath.listFiles()) {
					file.delete();
				}
			}
			// 如果传入的是文件夹而非文件，在上边删除完内部的文件后，将这个文件夹也删除掉
			// 如果传入的是文件，则直接在这里将文件删除掉
			fileOrPath.delete();
		}
	}

	/**
	 * 产生商品的详情图片,并返回图片所在的相对路径
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 */
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		String realFileName = getRandomFileName();
		String extension = getFileExtension(thumbnail.getImageName());
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		// logger.debug("current complete addr
		// is:"+PathUtil.getImgBasePath()+relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.png")), 0.25f).outputQuality(0.9f)
					.toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建详情图失败:" + e.getMessage());
		}
		return relativeAddr;

	}

	// public static void main(String[] args) throws IOException{
	// File shopImg = new File("C:\\Users\\CHQ\\Desktop\\testPicture.png");
	// String dest = "D:\\o2oProjectDev\\image\\test.png";
	// Thumbnails.of(new FileInputStream(shopImg)).size(200, 200)
	// .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new
	// File(basePath+"/watermark.png")),0.25f)
	// .outputQuality(0.8).toFile(dest);
	// }
}
