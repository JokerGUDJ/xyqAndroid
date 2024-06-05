package com.xej.xhjy.common.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * @author dazhi
 * @class SharedPreUtil
 * @Createtime 2018/8/31 17:38
 * @description describe 加解密存储
 * @Revisetime
 * @Modifier
 */
public class CommonPerference {

	/**
	 * sharedpreferences 存储文件名
	 */
	public static final String SPConfig = "app_config";
	public SharedPreferences preferences;

	public CommonPerference(Context context) {
		preferences = context.getSharedPreferences(SPConfig, Context.MODE_PRIVATE);
	}

	/**
	 * 获取存储的boolean值（如果取不到则返回false）
	 *
	 * @param key
	 * @return boolean
	 */
	public boolean getState(String key) {
		return preferences.getBoolean(key, false);
	}

	/**
	 * 存储Boolean到本地
	 *
	 * @param key
	 * @param state
	 */
	public void setState(String key, boolean state) {
		preferences.edit().putBoolean(key, state).commit();
	}

	/**
	 * 获取存储的String值（如果取不到则返回""字符串）
	 *
	 * @param key
	 * @return String
	 */
	public String getString(String key, String def) {
		//对获取的信息解密
		String info = preferences.getString(key, def);
		if (!"".equals(info)) {
			info = DESUtils.decrypt(info);
		}
		return info;
	}

	/**
	 * 存储String到本地
	 *
	 * @param key
	 * @param value
	 */
	public void putString(String key, String value) {
		//对xml文件存储的信息加密
		if (value != null && !"".equals(value)) {
			value = DESUtils.encrypt(value);
		}
		preferences.edit().putString(key, value).commit();
	}

	/**
	 * 获取存储的int值（如果取不到则返回-1）
	 *
	 * @param key
	 * @return int
	 */
	public int getInt(String key, int def) {
		return preferences.getInt(key, def);
	}

	/**
	 * 存储int到本地
	 *
	 * @param key
	 * @param value
	 */
	public void putInt(String key, int value) {
		preferences.edit().putInt(key, value).commit();
	}

	/**
	 * boolean（如果取不到则返回-1）
	 *
	 * @param key
	 * @return boolean
	 */
	public boolean getBoolean(String key, boolean def) {
		return preferences.getBoolean(key, def);
	}

	/**
	 * 存储boolean到本地
	 *
	 * @param key
	 * @param value
	 */
	public void putBoolean(String key, boolean value) {
		preferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * 获取Editor对象
	 *
	 * @return Editor
	 */
	public Editor getEdit() {
		return preferences.edit();
	}


}
