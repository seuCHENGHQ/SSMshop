package com.chq.o2o;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(System.getProperty("file.seperator"));
		// System.out.println(File.separator);
		// Integer i = Integer.decode("0110");
		// System.out.println(i);
		// i = Integer.decode("10");
		// System.out.println(i);
		// i = Integer.decode("0x10");
		// System.out.println(i);
		// i=Integer.parseInt("10", 2);
		// System.out.println(i);
		
		String regex = "(^|&)" + "shopId" + "=([^&]*)(&|$)";
		Pattern pattern = Pattern.compile(regex);
		String url = "shopId=1&shopId=2";
		Matcher matcher = pattern.matcher(url);
		while(matcher.find()){
			System.out.println(matcher.group());
		}
	}

}
