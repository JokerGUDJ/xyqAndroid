package com.xej.xhjy.common.http.download;

/**
 * @class ProgressListener
 * @author dazhi
 * @Createtime 2018/5/30 14:50
 * @description 下载监听
 * @Revisetime
 * @Modifier
 */

public interface ProgressListener {

    /**
     * 载进度监听
     *
     * @param bytesRead     已经下载文件的大小
     * @param contentLength 文件的大小
     * @param progress      当前进度
     * @param done          是否下载完成
     * @param filePath      文件路径
     */
    void onResponseProgress(long bytesRead, long contentLength, int progress, boolean done, String filePath);


}
