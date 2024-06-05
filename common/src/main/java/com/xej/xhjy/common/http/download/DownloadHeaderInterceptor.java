package com.xej.xhjy.common.http.download;

import android.text.TextUtils;

import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.LogUtils;

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
public class DownloadHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("content-type", "application/json;charset=utf-8")
                .header("Accept", "text/xml,application/json")
                .header("Accept-Language", "zh-CN,zh;q=0.8")
                .header("Connection", "Keep-Alive")
                .header("client-type", "WLJY-ANDROID")
                .header("Accept-Encoding", "identity");

        //把存储的cookie key加入header里
        String cookieKey = PerferenceUtils.get("cookie_key", "");
        if (!TextUtils.isEmpty(cookieKey)){
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
        Request request = builder.method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
