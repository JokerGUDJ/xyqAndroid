package com.xej.xhjy.common.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import com.xej.xhjy.common.base.BaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

	protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	protected static MessageDigest messagedigest = null;

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	public static String getFileMD5String(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
				file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}
	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}

	/**
	 * MD5加密
	 * @param byteStr 需要加密的内容
	 * @return 返回 byteStr的md5值
	 */
	public static String encryptionMD5(byte[] byteStr) {
		MessageDigest messageDigest = null;
		StringBuffer md5StrBuff = new StringBuffer();
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(byteStr);
			byte[] byteArray = messageDigest.digest();
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5StrBuff.toString();
	}
	/**
	 * 获取app签名md5值
	 */
	public static String getSignMd5Str(BaseActivity mActivity) {
		try {
			PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			String signStr = encryptionMD5(sign.toByteArray());
			return signStr;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}