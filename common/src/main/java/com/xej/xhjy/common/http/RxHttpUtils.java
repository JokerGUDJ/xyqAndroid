package com.xej.xhjy.common.http;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.xej.xhjy.common.http.download.DownloadRetrofit;
import com.xej.xhjy.common.http.http.GlobalRxHttp;
import com.xej.xhjy.common.http.http.SingleRxHttp;
import com.xej.xhjy.common.http.upload.UploadRetrofit;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * @author dazhi
 * @class RxHttpUtils
 * @Createtime 2018/5/30 09:18
 * @description 网络请求单例
 * @Revisetime
 * @Modifier
 */

public class RxHttpUtils {

    @SuppressLint("StaticFieldLeak")
    private static RxHttpUtils instance;
    @SuppressLint("StaticFieldLeak")
    private static Application context;

    public static RxHttpUtils getInstance() {
        checkInitialize();
        if (instance == null) {
            synchronized (RxHttpUtils.class) {
                if (instance == null) {
                    instance = new RxHttpUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     * @param app Application
     */
    public static void init(Application app) {
        context = app;
    }

    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        checkInitialize();
        return context;
    }

    /**
     * 检测是否调用初始化方法
     */
    private static void checkInitialize() {
        if (context == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 RxHttpUtils.init() 初始化！");
        }
    }


    public GlobalRxHttp config() {
        return GlobalRxHttp.getInstance();
    }


    /**
     * 使用全局参数创建请求
     * @param cls Class
     * @param <K> K
     * @return 返回
     */
    public static <K> K createApi(Class<K> cls) {
        return GlobalRxHttp.createGApi(cls);
    }

    /**
     * 获取单个请求配置实例
     *
     * @return SingleRxHttp
     */
    public static SingleRxHttp getSInstance() {
        return SingleRxHttp.getInstance();
    }

    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    public static Observable<ResponseBody> downloadFile(String fileUrl, Map<String,String> maps) {
        return DownloadRetrofit.downloadFile(fileUrl,maps);
    }

    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    public static Observable<ResponseBody> downloadFile(String fileUrl) {
        return DownloadRetrofit.downloadFile(fileUrl);
    }

    /**
     * 上传单张图片
     *
     * @param uploadUrl 地址
     * @param filePath  文件路径
     * @return ResponseBody
     */
    public static Observable<ResponseBody> uploadImg(String uploadUrl, String filePath) {
        return UploadRetrofit.uploadImg(uploadUrl, filePath);
    }

    /**
     * 上传多张图片
     *
     * @param uploadUrl 地址
     * @param filePaths 文件路径
     * @return ResponseBody
     */
    public static Observable<ResponseBody> uploadImgs(String uploadUrl, List<String> filePaths,JSONObject jsonObject) {
        return UploadRetrofit.uploadImgs(uploadUrl, filePaths,jsonObject);
    }

    /**
     * 获取Cookie
     *
     * @return HashSet
     */
//    public static HashSet<String> getCookie() {
//        HashSet<String> preferences = (HashSet<String>) PerferenceUtils.get(SPKeys.COOKIE, new HashSet<String>());
//        return preferences;
//    }
}
