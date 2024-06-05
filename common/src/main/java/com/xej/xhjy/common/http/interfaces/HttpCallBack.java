package com.xej.xhjy.common.http.interfaces;

/**
 * @author dazhi
 * @class HttpCallBack
 * @Createtime 2018/5/30 22:10
 * @description 自定义网络请求的回调
 * @Revisetime
 * @Modifier
 */
public interface HttpCallBack {

    /**
     * 成功回调
     */
    void onSucess(String jsonString);

    /**
     * 失败回调
     */
    void onError(String errorMsg);
}
