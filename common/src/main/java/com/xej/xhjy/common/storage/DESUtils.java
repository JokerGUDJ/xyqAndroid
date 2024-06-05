package com.xej.xhjy.common.storage;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @class DESUtils
 * @author dazhi
 * @Createtime 2018/8/31 17:39
 * @description describe 字符串DES加解密
 * @Revisetime
 * @Modifier
 */
public class DESUtils {

	private static final String CHARSET = "UTF-8";
	private static final String DES = "DES";
	private static final String KEY = "CSII-DES";
	private static SecretKey secretkey = null;

	private static Key getKey() {
		if (secretkey == null) {
			byte[] bb = null;
			try {
				bb = KEY.getBytes(CHARSET);
				secretkey = new SecretKeySpec(bb, DES);
			} catch (Exception e) {
				
			}
		}
		return secretkey;
	}

	/**
	 * 加密
	 */
	public static String encrypt(String source) {
		if (source==null||"".equals(source)) {
			return "";
		}
		String s = null;
		byte[] target = null;
		try {
			byte[] center = source.getBytes(CHARSET);
			Key key = getKey();
			Cipher cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			target = cipher.doFinal(center);
			s = Base64.encodeToString(target, Base64.DEFAULT);
		} catch (Exception e) {
			return "";
		}
		return s;
	}

	/**
	 * 解密
	 */
	public static String decrypt(String source) {
		if (source==null||"".equals(source)) {
			return "";
		}
		String s = null;
		byte[] dissect = null;
		try {
			byte[] center = Base64.decode(source.getBytes(CHARSET), Base64.DEFAULT);
			Key key = getKey();
			Cipher cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.DECRYPT_MODE, key);
			dissect = cipher.doFinal(center);
			s = new String(dissect, CHARSET);
		} catch (Exception e) {
			return "";
		}
		return s;
	}
}
