package com.xej.xhjy.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.tools
 * @ClassName: Utils
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/6/11 下午4:30
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/11 下午4:30
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Utils {


    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    @SuppressLint("MissingPermission")
    public static void callPhone(Activity activity, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        activity.startActivity(intent);
    }

    /**
     * 将字符串转为utf-8
     *
     * @param str
     * @return
     */
    public static String StringtoUtf8(String str) {
        String result = "";
        try {
            result = URLEncoder.encode(str, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
        }

        return result;
    }

    /**
     * 将utf-8 转为字符串
     *
     * @param str
     * @return
     */

    public static String Utf8toString(String str) {
        String result = "";
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return result;
    }

    /**
     * 把中文字符串转换为十六进制Unicode编码字符串
     */
    public static String stringToUnicode(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            if (ch > 255)
                str += "\\u" + Integer.toHexString(ch);
            else
                str += String.valueOf(s.charAt(i));
        }
        return str;
    }

    /**
     * 把十六进制Unicode编码字符串转换为中文字符串
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{2,4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("\\\\u");

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

}
