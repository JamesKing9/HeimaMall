package com.itheima.heimamall.sms;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Random;

/**
 * sms的请求header生成
 * @author lxj
 *
 */
public class SMSHeaderFactory {
	private static int nonce_length = 20;
	private static char[] arr = {'0','1','2','3','4','5','6','7','8','9','a','b','c'
		,'d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v'
		,'w','x','y','z'};
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
        '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/**
	 * 开发者平台分配的appkey,appSecret,在网易云信管理中心应用的App Key管理中
	 * @param appKey
	 * @param appSecret
	 * @return
	 */
	//appKey和appSecret需要替换为自己账号的，需要去网易云信后台申请一个账号
	public static String appKey = "";
	public static String appSecret = "";

	public static HashMap<String, String> genSmsHeaders(){
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("AppKey", appKey);
		String nonce = genNonce();
		headers.put("Nonce", nonce);//随机数（最大长度128个字符）
		String curTime = String.valueOf(System.currentTimeMillis()/1000);
		headers.put("CurTime",curTime );
		headers.put("CheckSum", getCheckSum(appSecret, nonce, curTime));
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		return headers;
	}
	
	private static String genNonce() {
		Random random = new Random();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < nonce_length; i++) {
			int index = random.nextInt(arr.length);
			builder.append(arr[index]);
		}
		return builder.toString();
	}
	 // 计算并获取CheckSum
	private static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    private static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
}
