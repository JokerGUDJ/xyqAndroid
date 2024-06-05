package com.xej.xhjy.common.http.download;


import com.xej.xhjy.common.http.http.RetrofitClient;
import com.xej.xhjy.common.http.interceptor.Transformer;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author dazhi
 * @class DownloadRetrofit
 * @Createtime 2018/5/29 17:59
 * @description 为下载单独建一个retrofit
 * @Revisetime
 * @Modifier
 */

public class DownloadRetrofit {

    private static DownloadRetrofit instance;
    private Retrofit mRetrofit;
    private OkHttpClient httpClient;
    private static String baseUrl = "https://api.github.com/";


    public DownloadRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.customLog("download", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new DownloadHeaderInterceptor())
                .addInterceptor(loggingInterceptor)
//                .addInterceptor(new AddCookiesInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(baseUrl)
                .build();
    }

    public static DownloadRetrofit getInstance() {

        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new DownloadRetrofit();
                }
            }

        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static Observable<ResponseBody> downloadFile(String fileUrl, Map<String, String> maps) {
        return DownloadRetrofit
                .getInstance()
                .getRetrofit()
                .create(DownloadApi.class)
                .downloadFile(fileUrl, maps)
                .compose(Transformer.<ResponseBody>switchSchedulers());
    }

    public static Observable<ResponseBody> downloadFile(String fileUrl) {
        return DownloadRetrofit
                .getInstance()
                .getRetrofit()
                .create(DownloadApi.class)
                .downloadFile(fileUrl)
                .compose(Transformer.<ResponseBody>switchSchedulers());
    }
}
