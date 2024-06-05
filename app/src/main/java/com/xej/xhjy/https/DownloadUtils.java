package com.xej.xhjy.https;

import com.xej.xhjy.common.http.RxHttpUtils;
import com.xej.xhjy.common.http.download.DownloadObserver;
import com.xej.xhjy.common.http.http.RxHttpManager;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author dazhi
 * @class DownloadUtils
 * @Createtime 2018/6/13 18:13
 * @description 下载工具类
 * @Revisetime
 * @Modifier
 */
public class DownloadUtils {

    /**
     * @param url      下载地址
     * @param fileName 文件名
     * @param callback 回调
     */
    public static void downLoadFile(String url, String fileName,final String tag, Map<String, String> maps, final DownLoadCallback callback) {
        RxHttpUtils
                .downloadFile(url,maps)
                .subscribe(new DownloadObserver(fileName) {
                    @Override
                    protected void getDisposable(Disposable d) {
                        RxHttpManager.addDisposable(tag,d);
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        callback.onError();
                    }
                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        callback.onProgress(progress);
                        if (done) {
                            callback.onSuccess(filePath);
                        }
                    }
                });
    }

    /**
     * @param url      下载地址
     * @param fileName 文件名
     * @param callback 回调
     */
    public static void downLoadFile(String url, String fileName, final String tag, final DownLoadCallback callback) {
        RxHttpUtils
                .downloadFile(url)
                .subscribe(new DownloadObserver(fileName) {
                    @Override
                    protected void getDisposable(Disposable d) {
                        RxHttpManager.addDisposable(tag,d);
                    }

                    @Override
                    protected void onError(String errorMsg) {
                        callback.onError();
                    }

                    @Override
                    protected void onSuccess(long bytesRead, long contentLength, float progress, boolean done, String filePath) {
                        callback.onProgress(progress);
                        if (done) {
                            callback.onSuccess(filePath);
                        }
                    }
                });
    }
}
