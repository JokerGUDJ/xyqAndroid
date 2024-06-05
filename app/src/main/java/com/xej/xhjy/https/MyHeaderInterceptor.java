package com.xej.xhjy.https;

import android.text.TextUtils;

import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.LogUtils;
import com.xej.xhjy.common.utils.MD5Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author dazhi
 * @class HeaderInterceptor
 * @Createtime 2018/6/6 10:48
 * @description 动态添加请求头
 * @Revisetime
 * @Modifier
 */
public class MyHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        //把存储的cookie key加入header里
        String cookieKey = PerferenceUtils.get(NetConstants.COOKIE_KEY, "");
        if (!TextUtils.isEmpty(cookieKey)) {
            StringBuffer sb = new StringBuffer();
            String[] keys = cookieKey.split(",");
            for (String key : keys) {
                if (key.contains("g_token")) {
                    builder.header("g-token", PerferenceUtils.get(key, ""));
                } else {
                    builder.header(key, PerferenceUtils.get(key, ""));
                }
                sb.append(key).append("=").append(PerferenceUtils.get(key, "")).append(";");
            }
            builder.header("Cookie", sb.toString());
        }
        builder.header("content-type", "application/json;charset=utf-8");
        builder.header("Accept", "text/xml,application/json");
        builder.header("Accept-Language", "zh-CN,zh;q=0.8");
        builder.header("Connection", "Keep-Alive");
        builder.header("client-type", "WLJY-ANDROID");
        int random = (int)((Math.random()*9+1)*1000);
        LogUtils.dazhiLog("随机数------->"+random);
        String temp = System.currentTimeMillis()+String.valueOf(random);
        String tem = temp+"abc123";
        String  str =temp+"."+MD5Utils.encryptionMD5(tem.getBytes());
        builder.header("randomToken", str);
        Request request = builder.method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
