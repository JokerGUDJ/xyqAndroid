package com.xej.xhjy.common.http.upload;


import android.app.Dialog;

import com.xej.xhjy.common.http.http.RetrofitClient;
import com.xej.xhjy.common.http.interceptor.Transformer;
import com.xej.xhjy.common.utils.LogUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author dazhi
 * @class UploadRetrofit
 * @Createtime 2018/5/30 09:08
 * @description 为上传单独建一个retrofit
 * @Revisetime
 * @Modifier
 */

public class UploadRetrofit {

    private static UploadRetrofit instance;
    private Retrofit mRetrofit;
    private OkHttpClient httpClient;
    private static String baseUrl = "https://api.github.com/";


    public UploadRetrofit() {
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
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
                        Request request = builder.method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
//                .addInterceptor(new AddCookiesInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(httpClient)
                .build();
    }

    public static UploadRetrofit getInstance() {

        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new UploadRetrofit();
                }
            }

        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static Observable<ResponseBody> uploadImg(String uploadUrl, String filePath) {
        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        return UploadRetrofit
                .getInstance()
                .getRetrofit()
                .create(UploadFileApi.class)
                .uploadImg(uploadUrl, body)
                .compose(Transformer.<ResponseBody>switchSchedulers());
    }

    public static Observable<ResponseBody> uploadImg(String uploadUrl, String filePath, Dialog loadingDialog) {
        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

        return UploadRetrofit
                .getInstance()
                .getRetrofit()
                .create(UploadFileApi.class)
                .uploadImg(uploadUrl, body);
    }

    /**
     * 多张图片上传，通过list上传，需要添加请求头字段
     *
     * @param uploadUrl
     * @param filePaths
     * @param jsonObject
     * @return
     */

    public static Observable<ResponseBody> uploadImgs(String uploadUrl, List<String> filePaths, JSONObject jsonObject) {
        final String token = jsonObject.optString("token");
        final String signature = jsonObject.optString("signature");
        final String nonce = jsonObject.optString("nonce");
        final String timestamp = jsonObject.optString("timestamp");
        final String cutinfo = jsonObject.optString("cutInfo");
        LogUtils.dazhiLog("token=" + token + " signature=" + signature + " nonce" + nonce + " timestamp" + timestamp);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder()
                                .header("content-type", "application/json;charset=utf-8")
                                .header("Accept", "text/xml,application/json")
                                .header("Accept-Language", "zh-CN,zh;q=0.8")
                                .header("Connection", "Keep-Alive")
                                .header("client-type", "WLJY-ANDROID")
                                .header("Accept-Encoding", "identity")
                                .header("token", token)
                                .header("signature", signature)
                                .header("nonce", nonce)
                                .header("timestamp", timestamp);
                        Request request = builder.method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(httpClient)
                .build();
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            File file = new File(filePaths.get(i));
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file" + i, file.getName(), imageBody);
            parts.add(part);
        }
        LogUtils.dazhiLog("上传图片地址--"+uploadUrl+"?cutinfo="+cutinfo);
        return retrofit
                .create(UploadFileApi.class)
                .uploadImgs(uploadUrl+"?cutinfo="+cutinfo, parts);
    }


    /**
     * 视频上传通过File上传，执行力要求添加请求头字段
     *
     * @param uploadUrl
     * @param jsonObject
     * @return
     */

    public static Observable<ResponseBody> uploadVideo(String uploadUrl, String path, JSONObject jsonObject) {
        final String token = jsonObject.optString("token");
        final String signature = jsonObject.optString("signature");
        final String nonce = jsonObject.optString("nonce");
        final String timestamp = jsonObject.optString("timestamp");
        LogUtils.dazhiLog("token=" + token + " signature=" + signature + " nonce" + nonce + " timestamp" + timestamp);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder()
                                .header("content-type", "application/json;charset=utf-8")
                                .header("Accept", "text/xml,application/json")
                                .header("Accept-Language", "zh-CN,zh;q=0.8")
                                .header("Connection", "Keep-Alive")
                                .header("client-type", "WLJY-ANDROID")
                                .header("Accept-Encoding", "identity")
                                .header("token", token)
                                .header("signature", signature)
                                .header("nonce", nonce)
                                .header("timestamp", timestamp);
                        Request request = builder.method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(httpClient)
                .build();
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_video_file", file.getName(), requestFile);
        LogUtils.dazhiLog("上传视频地址--"+uploadUrl);
        return retrofit
                .create(UploadFileApi.class)
                .uploadImg(uploadUrl, body);
    }
}
