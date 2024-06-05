package com.xej.xhjy.https;

/**
 * @class DownLoadCallback
 * @author dazhi
 * @Createtime 2018/6/13 18:13
 * @description 下载回调
 * @Revisetime
 * @Modifier
 */
public interface DownLoadCallback {
    //进度
    void onProgress(float progress);
    //成功回调文件地址
    void onSuccess(String path);
    //失败回调
    void onError();
}
