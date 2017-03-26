package com.itheima.heimamall.util;

import java.security.MessageDigest;

public class MD5Util {
	/**
	 * 将msg加密为32位的密文
	 */
	public static String md5(String msg){
		StringBuffer sBuffer = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] digestBytes = digest.digest(msg.getBytes());
			for (int i = 0; i < digestBytes.length; i++)   
            {
				/*
				public static String toHexString(int i)
				以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式。
				如果参数为负，那么无符号整数值为参数加上 232；否则等于该参数。
				将该值转换为十六进制（基数 16）的无前导 0 的 ASCII 数字字符串。
				如果无符号数的大小值为零，则用一个零字符 '0' (’\u0030’) 表示它；
				否则，无符号数大小的表示形式中的第一个字符将不是零字符。
				用以下字符作为十六进制数字：
				 0123456789abcdef
				 这些字符的范围是从 '\u0030' 到 '\u0039' 和从 '\u0061' 到 '\u0066'。

				 如果希望得到大写字母，可以在结果上调用 String.toUpperCase() 方法：
				 Integer.toHexString(n).toUpperCase()

				参数：
				i - 要转换成字符串的整数。
				返回：
				参数的十六进制（基数 16）无符号整数值的字符串表示形式。
				从以下版本开始：
				JDK1.0.2
				 */
				String string = Integer.toHexString(digestBytes[i] & 0xFF);
                if (string.length() == 1)  {
                	sBuffer.append("0");
                }
                sBuffer.append(string);  
            }  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sBuffer.toString();
	}
}
