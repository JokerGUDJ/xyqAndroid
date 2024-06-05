package com.xej.xhjy.common.base;

/**
 * @class WebLoadingCallback
 * @author dazhi
 * @Createtime 2018/6/7 15:38
 * @description web页面加载回调
 * @Revisetime
 * @Modifier
 */
public interface WebLoadingCallback {
    //开始加载
    void start();
    //加载完成
    void finish();
    //加载进度
    void onProgressChanged(int progress);
}
