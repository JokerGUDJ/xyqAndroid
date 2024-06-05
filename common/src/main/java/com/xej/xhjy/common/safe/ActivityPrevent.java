package com.xej.xhjy.common.safe;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.util.List;

/**
 * @class ActivityPrevent 防劫持提示
 * @author dazhi
 * @Createtime 2018/6/13 17:01
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class ActivityPrevent {
	private static final String TAG = "ActivityPrevent";

	/**
	 * 检测应用程序状态，是否进入后台，防止activity劫持，应用程序进后台及时给用户提示！
	 * @param context 上下午对象
	 * @param allowHint 是否允许toast提示信息
	 * @param listener 状态监听接口
	 */
	public static void monitorRunningTask(Context context,boolean allowHint,TaskStatusListener listener){
		if (!isAppOnForeground(context)) {
			if (allowHint) {
				Toast.makeText(context.getApplicationContext(), "您的应用程序已经进入后台运行！", Toast.LENGTH_LONG).show();
			}
			if (listener!=null) {
				listener.taskStatusChanged(true);
			}
		}else {
			if (listener!=null) {
				listener.taskStatusChanged(false);
			}
		}
	}
	private static boolean isBackground(Context context) {

	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	    for (RunningAppProcessInfo appProcess : appProcesses) {
	         if (appProcess.processName.equals(context.getPackageName())) {
	                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
	                          Log.i("后台", appProcess.processName);
	                          return true;
	                }else{
	                          Log.i("前台", appProcess.processName);
	                          return false;
	                }
	           }
	    }
	    return false;
	}
    /**
	 * 判断APP是否是在前台运行
	 * 
	 * @param ctx
	 * @return boolean
	 */
	private static boolean isAppOnForeground(Context ctx) {
		final ActivityManager activityManager = (ActivityManager) ctx
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		final String packageName = ctx.getApplicationContext().getPackageName();

		final List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		}
		for (final RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}
    public interface TaskStatusListener{
    	public void taskStatusChanged(boolean isBackground);
    }
}
