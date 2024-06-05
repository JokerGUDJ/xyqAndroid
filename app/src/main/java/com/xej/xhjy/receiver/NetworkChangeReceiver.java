package com.xej.xhjy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.utils.AppConstants;

import org.greenrobot.eventbus.EventBus;

/**
 * @class NetworkChangeReceiver
 * @author dazhi
 * @Createtime 2018/9/3 16:25
 * @description android 7.0之后收不到静态注册的网络状态广播，故这里直接动态注册以兼容所有版本
 * @Revisetime
 * @Modifier
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (GenalralUtils.isNetWorkConnected(context)){
            if (!AppConstants.IS_NETWORK){
                AppConstants.IS_NETWORK = true;
                EventBus.getDefault().post(new NetWorkStatusEvent(""));
            } else {
                AppConstants.IS_NETWORK = false;
            }
        }
    }
}
