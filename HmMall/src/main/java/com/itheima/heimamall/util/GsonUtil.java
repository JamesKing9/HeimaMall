package com.itheima.heimamall.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 解析Json的封装
 * @author Administrator
 *
 */
public class GsonUtil {


	/**
	 * 把一个json字符串变成对象
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T parseJsonToBean(String json, Class<T> cls) {
		Gson gson = new Gson();
		T t = null;
		try {
			t = gson.fromJson(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}


	/**
	 * 把json字符串变成集合
	 * params: new TypeToken<List<yourbean>>(){}.getType(),
	 * 
	 * @param json
	 * @param type  new TypeToken<List<yourbean>>(){}.getType()
	 * @return
	 */
	public static List<?> parseJsonToList(String json, Type type) {
		Gson gson = new Gson();
		List<?> list = gson.fromJson(json, type);
		return list;
	}

	public static List<?> parseJsonToList(InputStream is, Type type) {
		Gson gson = new Gson();
		List<?> list = gson.fromJson(new InputStreamReader(is), type);
		return list;
	}

	public static String toJson(Object obj){
		if(obj==null)return "";
		return new Gson().toJson(obj);
	}

}
