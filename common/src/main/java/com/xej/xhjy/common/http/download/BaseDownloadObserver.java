package com.xej.xhjy.common.http.download;

import com.xej.xhjy.common.http.exception.ApiException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import okhttp3.ResponseBody;

/**
 * @class BaseDownloadObserver
 * @author dazhi
 * @Createtime 2018/5/29 17:58
 * @description describe
 * @Revisetime
 * @Modifier
 */

public abstract class BaseDownloadObserver implements Observer<ResponseBody> {

    /**
     * 失败回调
     *
     * @param errorMsg 错误信息
     */
    protected abstract void doOnError(String errorMsg);


    @Override
    public void onError(@NonNull Throwable e) {
        String error = ApiException.handleException(e).getMessage();
        setError(error);
    }

    private void setError(String errorMsg) {
        doOnError(errorMsg);
    }

}
