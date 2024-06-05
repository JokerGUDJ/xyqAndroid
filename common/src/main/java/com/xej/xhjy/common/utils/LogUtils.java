package com.xej.xhjy.common.utils;

import android.util.Log;

/**
 * @author dazhi
 * @class LogUtils 所有Log打印类可在外部控制开关
 * @Createtime 2018/5/25 16:37
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class LogUtils {

    /**
     * 日志开关
     */
    public static boolean isShowLog = true;

    /**
     * 网络请求日志 tag为http
     * @param msg 日志内容
     */
    public static void netLog(String msg) {
        if (!isShowLog) {
            return;
        }
        Log.d("http", msg);
    }

    /**
     * 个人定位日志
     * @param msg 日志内容
     */
    public static void dazhiLog(String msg) {
        if (!isShowLog) {
            return;
        }
        Log.e("dazhi", msg);
    }

    /**
     * 自定义tag日志
     * @param tag 日志标记
     * @param msg 日志内容
     */
    public static void customLog(String tag, String msg) {
        if (!isShowLog) {
            return;
        }
        Log.e(tag, msg);
    }
}
