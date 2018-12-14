package com.chq.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestUtil {

	/**
	 * 将HttpServletRequest中的键对应的值转换为int
	 * @param request
	 * @param key
	 * @return 转换后的int值，转换失败的话返回-1
	 */
	public static int getInt(HttpServletRequest request, String key) {
		try{
			return Integer.decode(request.getParameter(key));
		}catch(Exception e){
			return -1;
		}
	}
	
	/**
	 * 将HttpServletRequest中的键对应的值转换为long
	 * @param request
	 * @param key
	 * @return 转换后的long值，转换失败的话返回-1
	 */
	public static long getLong(HttpServletRequest request, String key) {
		try{
			return Long.decode(request.getParameter(key));
		}catch(Exception e){
			//当key属性的值为null或key不存在的时候，这里就会走到catch里，从而返回-1
			return -1;
		}
	}
	
	/**
	 * 将HttpServletRequest中的键对应的值转换为double
	 * @param request
	 * @param key 
	 * @return 转换后的double值，转换失败的话返回-1
	 */
	public static double getDouble(HttpServletRequest request, String key) {
		try{
			return Double.valueOf(request.getParameter(key));
		}catch(Exception e){
			return -1d;
		}
	}
	
	/**
	 * 将HttpServletRequest中的键对应的值转换为boolean
	 * @param request
	 * @param key
	 * @return 转换后的boolean值，转换失败的话返回false
	 */
	public static boolean getBoolean(HttpServletRequest request, String key) {
		try{
			return Boolean.valueOf(request.getParameter(key));
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 将HttpServletRequest中的键对应的值转换为String
	 * @param request
	 * @param key
	 * @return 转换后的String值，转换失败的话返回null
	 */
	public static String getString(HttpServletRequest request,String key){
		try{
			String result = request.getParameter(key);
			if(result!=null){
				result=result.trim();
			}
			if("".equals(result)){
				result=null;
			}
			return result;
		}catch(Exception e){
			return null;
		}
	}
}
