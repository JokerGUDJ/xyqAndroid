package com.xej.xhjy.common.http.http;

import okhttp3.OkHttpClient;

/**
 * @class HttpClient
 * @author dazhi
 * @Createtime 2018/5/29 18:01
 * @description okHttp client
 * @Revisetime
 * @Modifier
 */

public class HttpClient {

    private static HttpClient instance;

    private OkHttpClient.Builder builder;

    public HttpClient() {
        builder = new OkHttpClient.Builder();
    }

    public static HttpClient getInstance() {

        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                }
            }

        }
        return instance;
    }


    public OkHttpClient.Builder getBuilder() {
        return builder;
    }

}
