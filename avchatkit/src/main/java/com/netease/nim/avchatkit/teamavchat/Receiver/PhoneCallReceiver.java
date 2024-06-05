package com.netease.nim.avchatkit.teamavchat.Receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.xej.xhjy.common.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

public class PhoneCallReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneCallReceiver ";

    public PhoneCallReceiver() {

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        int state = tm.getCallState();
        LogUtils.dazhiLog("---------------未进入");
        if (state == TelephonyManager.CALL_STATE_RINGING) {
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        }

    }


    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    AnswerThePhone();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //输出来电号码
                    break;

            }

        }
    };


    public interface IPhoneReceiverStates {
        /**
         * 接听电话
         */
        void onAnswerThePhone();
    }

    protected static Map<String, IPhoneReceiverStates> mMapNotifys = new HashMap<String, IPhoneReceiverStates>();

    public static void registerNotify(String notifyKey, IPhoneReceiverStates mIPhoneReceiverStates) {
        if (!mMapNotifys.containsKey(notifyKey)) {
            mMapNotifys.remove(notifyKey);
            mMapNotifys.put(notifyKey, mIPhoneReceiverStates);
        }
    }

    public static void removeNotify(String notifyKey) {
        if (mMapNotifys.containsKey(notifyKey)) {
            mMapNotifys.remove(notifyKey);
        }
    }

    //接听电话
    public void AnswerThePhone() {
        try {
            for (IPhoneReceiverStates notify : mMapNotifys.values()) {
                notify.onAnswerThePhone();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
