package com.xej.xhjy.common.http.http;

import androidx.collection.ArrayMap;

import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * @author dazhi
 * @class RxHttpManager
 * @Createtime 2018/5/30 23:14
 * @description 网络请求管理类，可单独取消，可批量取消，使用请求的Tag作为Key
 * @Revisetime
 * @Modifier
 */
public class RxHttpManager {

    private static ArrayMap<String, Disposable> mHttpMap = new ArrayMap<>();
    /**
     * 加入某个请求
     * @param tag 网络请求的页面标识
     * @param d   Disposable请求体
     */
    public static void addDisposable(String tag, Disposable d) {
        mHttpMap.put(tag, d);
    }

    /**
     * 移除某个请求
     * @param tag 网络请求的标识
     */
    public static void removeDisposable(String tag) {
        if (!mHttpMap.isEmpty() && mHttpMap.containsKey(tag)) {
            Disposable disposable  = mHttpMap.get(tag);
            if (!disposable.isDisposed()){
                disposable.dispose();
            }
            mHttpMap.remove(tag);
        }
    }

    /**
     * 取消单个请求
     * @param tag 页面参数
     */
    public static void cancelHttp(String tag) {
        if (!mHttpMap.isEmpty() && mHttpMap.containsKey(tag)) {
            Disposable disposable  = mHttpMap.get(tag);
            if (!disposable.isDisposed()){
                disposable.dispose();
            }
            mHttpMap.remove(tag);
        }
    }

    /**
     * 取消所有请求
     */
    public static void cancelAll(){
        if (!mHttpMap.isEmpty()) {
            Set<String> keys = mHttpMap.keySet();
            for (String apiKey : keys) {
                Disposable disposable  = mHttpMap.get(apiKey);
                if (!disposable.isDisposed()){
                    disposable.dispose();
                }
            }
        }
        mHttpMap.clear();
    }
}
