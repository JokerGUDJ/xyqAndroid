package com.xej.xhjy.common.utils;

import android.content.Context;
import android.widget.Toast;
/**
 * @class ToastUtils
 * @author dazhi
 * @Createtime 2018/6/6 17:12
 * @description Toast工具类
 * @Revisetime
 * @Modifier
 */
public class ToastUtils {

    /**
     * 时间短的Toast
     * @param context 上下文
     * @param msg String文本
     */
    public static void shortToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 时间短的Toast
     * @param context 上下文
     * @param msgID  文本ID
     */
    public static void shortToast(Context context,int msgID){
        Toast.makeText(context,msgID,Toast.LENGTH_SHORT).show();
    }
    /**
     * 时间长的Toast
     * @param context 上下文
     * @param msg String文本
     */
    public static void longToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
    /**
     * 时间长的Toast
     * @param context 上下文
     * @param msgID  文本ID
     */
    public static void longToast(Context context,int msgID){
        Toast.makeText(context,msgID,Toast.LENGTH_LONG).show();
    }
}
