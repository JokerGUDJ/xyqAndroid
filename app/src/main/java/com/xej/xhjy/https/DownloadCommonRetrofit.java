package com.xej.xhjy.https;


import com.xej.xhjy.common.http.download.DownloadApi;
import com.xej.xhjy.common.http.http.RetrofitClient;
import com.xej.xhjy.common.http.interceptor.Transformer;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @class DownloadRetrofit
 * @author dazhi
 * @Createtime 2018/8/31 09:25
 * @description describe 单纯的下载接口，无参数和校验传递
 * @Revisetime
 * @Modifier
 */
public class DownloadCommonRetrofit {

    private static DownloadCommonRetrofit instance;
    private Retrofit mRetrofit;

    private static String baseUrl = "https://api.github.com/";


    public DownloadCommonRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public static DownloadCommonRetrofit getInstance() {

        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new DownloadCommonRetrofit();
                }
            }

        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static Observable<ResponseBody> downloadFile(String fileUrl) {
        return DownloadCommonRetrofit
                .getInstance()
                .getRetrofit()
                .create(DownloadApi.class)
                .downloadFile(fileUrl)
                .compose(Transformer.<ResponseBody>switchSchedulers());
    }
}
