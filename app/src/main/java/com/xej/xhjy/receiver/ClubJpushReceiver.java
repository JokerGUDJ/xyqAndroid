package com.xej.xhjy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.main.ClubMainActivty;
import com.xej.xhjy.ui.main.RecivedJpusheEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class ClubJpushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
//            LogUtils.dazhiLog("[MyReceiver] 接收Registration Id : " + bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
//            LogUtils.dazhiLog("action---" + intent.getAction());
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//                LogUtils.dazhiLog("action---MyReceiver-----" + regId);

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//                LogUtils.dazhiLog("[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                if (AppConstants.IS_LOGIN) {
                    EventBus.getDefault().post(new RecivedJpusheEvent(true));
                }
//                LogUtils.dazhiLog("通知的ID-------" + notifactionId);
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                LogUtils.dazhiLog("点开额can数");
                Intent intent1 = new Intent(context, ClubMainActivty.class);
                intent1.putExtra(ClubMainActivty.JPUSH_CICK, AppConstants.JpushClick.GO_MESSAGE);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//                LogUtils.dazhiLog("ACTION_RICHPUSH_CALLBACK点开额can数");

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
//                LogUtils.dazhiLog("ACTION_CONNECTION_CHANGE点开额can数");
//				boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            } else {
//                LogUtils.dazhiLog("else点开额can数");
            }
        } catch (Exception e) {

        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
