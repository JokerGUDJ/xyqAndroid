package com.xej.xhjy.common.http.interfaces;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author dazhi
 * @class ApiService
 * @Createtime 2018/5/30 22:38
 * @description 基于Retrofit2+Rxjava2封装的ApiService，支持返回String，支持返回Bean，
 * 支持通用的GET/POST传入Url地址请求，同时也可自定义接口，自定义返回bean
 * @Revisetime
 * @Modifier
 */

public interface ApiService {

    /**
     * @param url  url后缀
     * @param maps get的后缀参数
     * @return 返回String
     */
    @GET()
    Observable<String> executeGet(
            @Url String url,
            @QueryMap Map<String, String> maps);

    /**
     * 无参数get请求
     *
     * @param url url后缀
     * @return 返回String
     */
    @GET()
    Observable<String> executeGet(
            @Url String url);


    /**
     * @param url   url后缀
     * @param route Json提交
     * @return 返回String
     */
    @POST()
    Observable<String> executePost(
            @Url String url,
            @Body RequestBody route);

    /**
     * 大文件官方建议用 @Streaming 来进行注解，不然会出现IO异常，小文件可以忽略不注入
     *
     * @param fileUrl 地址
     * @return ResponseBody
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl, @Body RequestBody route);

    @GET("getContentList.do?channelURL=XHJT&size=2&siteID=1003")
    Observable<String> getOtherString();

}
