package com.xej.xhjy.common.http.download;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @class DownloadApi
 * @author dazhi
 * @Createtime 2018/5/30 14:49
 * @description 文件下载Api
 * @Revisetime
 * @Modifier
 */

public interface DownloadApi {


    /**
     * 大文件官方建议用 @Streaming 来进行注解，不然会出现IO异常，小文件可以忽略不注入
     *
     * @param fileUrl 地址
     * @return ResponseBody
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl, @QueryMap Map<String, String> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
