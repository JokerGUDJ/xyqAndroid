package com.xej.xhjy.common.safe;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;

public class SecureManager {

    /**
     * 说明：建议在BaseActivity的oncreate()方法里执行
     * @param activity
     */
    public static void preventScreenShot(Activity activity) {
        if (activity != null) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    /**
     * 判断客户端运行环境是否是模拟器</br>
     * 说明：在客户端启动时优先执行，禁止客户端在模拟器环境下运行，如果返回true，可以选择给用户提示后，退出客户端。
     *
     * @param context
     * @return boolean
     */
    public static boolean isEmulator(Context context) {
        return AntiEmulator.isEmulator(context);
    }

    /**
     * 检测应用程序状态，是否进入后台，防止activity劫持，应用程序进后台及时给用户提示！
     *
     * @param context   上下文对象
     * @param allowHint 是否开启进入后台提示信息
     * @param listener  进入后台监听回调
     */
    public static void monitorRunningTask(Context context, boolean allowHint, ActivityPrevent.TaskStatusListener listener) {
        ActivityPrevent.monitorRunningTask(context, allowHint, listener);
    }
}
