package com.xej.xhjy.tools;

import android.util.Log;

import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.https.HttpCallBack;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.https.RxHttpClient;

import java.util.HashMap;
import java.util.Map;

public class EventTrackingUtil {
    /**
     *
     * @param mActivity
     * @param pageName 页面名称
     * @param pageParams 页面参数
     * @param eventName 事件名称
     * @param eventParams 事件参数
     */
    public static void EventTrackSubmit(BaseActivity mActivity, String pageName, String pageParams, String eventName, String eventParams){
        String tag = "event_tracking";
        mActivity.addTag(tag);
        Map<String, String> paras = new HashMap<>();
        paras.put("pageName", pageName);
        paras.put("pageParams", pageParams);
        paras.put("eventName", eventName);
        paras.put("eventParams", eventParams);
        RxHttpClient.doPostStringWithUrl(mActivity, NetConstants.EVENT_TRACKING, tag, paras, new HttpCallBack() {
            @Override
            public void onSucess(String jsonString) {
                Log.d("event_tracking", pageName+"_"+pageParams+"_"+eventName+"_"+eventParams);
            }

            @Override
            public void onError(String errorMsg) {
            }
        });
    }
}
