package com.xej.xhjy.common.http.upload;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * @class UploadFileApi
 * @author dazhi
 * @Createtime 2018/5/30 09:08
 * @description 文件上传APi
 * @Revisetime
 * @Modifier
 */

public interface UploadFileApi {

    /**
     * 上传
     *
     * @param uploadUrl 地址
     * @param file      文件
     * @return ResponseBody
     */
    @Multipart
    @POST
    Observable<ResponseBody> uploadImg(@Url String uploadUrl,
                                       @Part MultipartBody.Part file);


    /**
     * 上传多个文件
     *
     * @param uploadUrl 地址
     * @param files      文件
     * @return ResponseBody
     */
    @Multipart
    @POST
    Observable<ResponseBody> uploadImgs(@Url String uploadUrl,
                                        @Part List<MultipartBody.Part> files);



}
