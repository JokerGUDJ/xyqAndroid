package com.xej.xhjy.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StatusBarUtils {

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 不包含虚拟导航栏高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth2(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight2(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 包含虚拟导航栏高度
     * @param context
     * @return
     */
    public static int getScreenWidth3(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        defaultDisplay.getRealSize(outPoint);
        return outPoint.x;
    }

    public static int getScreenHeight3(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        defaultDisplay.getRealSize(outPoint);
        return outPoint.y;
    }

    /**
     *  注意：这个方法的值必须等到页面创建完成后才能获取到。推荐写在onWindowFocusChanged（）方法中
     * @param activity
     * @return
     */
    public int getStatusBarHeight2(Activity activity) {
        Rect rectangle = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    /**
     *  注意：这个方法的值必须等到页面创建完成后才能获取到。推荐写在onWindowFocusChanged（）方法中
     * @param activity
     * @return
     */
    public static int getContentViewHeight(Activity activity) {
        Rect rectangle= new Rect();
        activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rectangle);
        return rectangle.height();
    }

    /**
     *  可以利用getScreenHeight() 与 getScreenHeight3() 返回值的差异来判断是否存在虚拟导航栏
     * @param context
     * @return
     */
    public static boolean hasNavigationBar(Activity context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return false;
        }
        return getContentViewHeight(context) != getScreenHeight3(context);
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        return getSystemComponentDimen(context, "navigation_bar_height");
    }

    public static int getSystemComponentDimen(Context context, String dimenName) {
        // 反射手机运行的类：android.R.dimen.status_bar_height.
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            String heightStr = clazz.getField(dimenName).get(object).toString();
            int height = Integer.parseInt(heightStr);
            //dp->px
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
